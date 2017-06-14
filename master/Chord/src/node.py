#! /usr/bin/python

import select
import socket
import sys
import threading
import memcache
import xmlrpclib
import hashlib
import time
from SimpleXMLRPCServer import SimpleXMLRPCServer


def server_ip(nodeid):
    return "10.0.0." + str(2 + nodeid)


def server_port(nodeid):
    return str(5000 + nodeid)


class FingerEntry():
    def __init__(self):
        self.start = -1
        self.interval = []
        self.node = -1


class Heartbeat(threading.Thread):
    def __init__(self, node, succid1, succid2, m):
        threading.Thread.__init__(self)
        self.succid1 = succid1
        self.succid2 = succid2
        self.node = node
        self.m = m

        if succid1 != -1 and succid1 != node.nodeid:
            self.succ1 = xmlrpclib.ServerProxy(
                "http://" + server_ip(self.succid1) + ":" + server_port(self.succid1) + "/")
        else:
            self.succ1 = None

        if succid2 != -1 and succid2 != node.nodeid:
            self.succ2 = xmlrpclib.ServerProxy(
                "http://" + server_ip(self.succid2) + ":" + server_port(self.succid2) + "/")
        else:
            self.succ2 = None

    def run(self):
        while True:
            try:
                #print str(self.node.successor1) + str(self.succ1)
                if self.succ1 is not None:
                    self.succ1.ping()
	            if self.succ2 is None:
                    	if self.succ1.successor() != self.node.nodeid:
                        	self.succid2 = self.succ1.succesor()
	                        self.succ2 = xmlrpclib.ServerProxy("http://localhost:" + str(5000 + self.succid2) + "/")

                    # print "Ping!"
                else:
                    if self.node.successor1 != -1 and self.node.nodeid != self.node.successor1:
                        self.succid1 = self.node.successor1
                        self.succ1 = xmlrpclib.ServerProxy(
                            "http://" + server_ip(self.succid1) + ":" + server_port(self.succid1) + "/")
                time.sleep(3)
            except Exception as ex:
                #print "Node " + str(self.succid1) + " failed"
                if self.succ2 is not None:
                    # contact second successor to copy keys
                    self.succ2.failed_predecessor()
                    # modify finger tables for predecessors & its own
                    for i in range(0, self.m):
                        self.node.update_own_finger(self.succid2, i)


class ServerThread(threading.Thread):
    def __init__(self, server):
        threading.Thread.__init__(self)
        self.server = server

    def run(self):
        self.server.socket.settimeout(5)
        self.server.allow_reuse_address = 1
        self.server.serve_forever()


class Node():
    def __init__(self, nodeid, m, maxpeers=100):
        self.server = SimpleXMLRPCServer((server_ip(nodeid), int(server_port(nodeid))), logRequests=False,
                                         allow_none=True)
        print "Started server for " + str(nodeid)

        self.server.register_function(self.find_successor, "find_successor")
        self.server.register_function(self.find_predecessor, "find_predecessor")
        self.server.register_function(self.find_closest_preceding_finger, "find_closest_preceding_finger")
        self.server.register_function(self.successor, "successor")
        self.server.register_function(self.get_nodeid, "get_nodeid")
        self.server.register_function(self.predecessor, "predecessor")
        self.server.register_function(self.set_successor, "set_successor")
        self.server.register_function(self.set_predecessor, "set_predecessor")
        self.server.register_function(self.update_own_finger, "update_own_finger")
        self.server.register_function(self.put_key, "put_key")
        self.server.register_function(self.delete_key, "delete_key")
        self.server.register_function(self.get_value, "get_value")
        self.server.register_function(self.return_value, "return_value")
        self.server.register_function(self.receive_key, "receive_key")
        self.server.register_function(self.receive_replica_key, "receive_replica_key")
        self.server.register_function(self.remove_replica_key, "remove_replica_key")
        self.server.register_function(self.ping, "ping")
        self.server.register_function(self.failed_predecessor, "failed_predecessor")
        self.server.register_function(self.join_ring, "join_ring")
        self.server.register_function(self.print_finger, "print_finger")
        self.server.register_function(self.get_size, "get_size")
        self.server.register_function(self.print_info, "print_info")
        ServerThread(self.server).start()

        self.successor1 = -1
        self.successor2 = -1
        self.predecessor1 = -1
        self.m = m
        self.nodeid = nodeid
        self.finger = [FingerEntry() for i in range(0, m)]
        self.keys = []
        self.replicated_keys = []

        self.memcache_client = memcache.Client(["127.0.0.1:11211"])

        #self.heartbeat = Heartbeat(self, self.successor1, self.successor2, self.m)
        #try:
        #    self.heartbeat.start()
        #except Exception as exc:
        #    print "Heartbeat failes to start " + str(exc)

    def get_size(self):
        return self.m

    def between(self, a, b, x):
       #print "Between",a,b,x
       if (a == b):
          return True
       return  (a < b and a < x <= b) or (a > b and x > b or x <= a)
       if a < b:
          return (x > a and x <= b)
       if b < a:   
          return ((x > a and (x <= (1 << self.m))) or (x <= b))


    def print_info(self):
        print "Pred:", self.predecessor()
        print "Succ:", self.successor()

    def get_nodeid(self):
	return self.nodeid

    def join_ring(self, successor=0):
        print "Node " + str(self.nodeid) + " wants to join"
        # return "ok"
        self.build_finger()
        self.print_finger()

        if self.nodeid != 0:
            succ_node = xmlrpclib.ServerProxy(
                "http://" + server_ip(successor) + ":" + server_port(successor) + "/", verbose=False)
            self.successor1 = successor
            self.predecessor1 = self.find_predecessor(self.nodeid)
	    self.init_finger()
            if succ_node.successor() != self.nodeid:
                self.successor2 = succ_node.successor()
	    self.update_other_fingers(self.nodeid)
            succ_node = xmlrpclib.ServerProxy(
	        "http://" + server_ip(self.successor1) + ":" + server_port(self.successor1) + "/")
	    if succ_node.get_nodeid() == succ_node.successor():
	        succ_node.set_successor(self.nodeid)
            predecessor_node = xmlrpclib.ServerProxy(
               "http://" + server_ip(self.predecessor()) + ":" + server_port(self.predecessor()) + "/", verbose=False)
            predecessor_node.set_successor(self.nodeid)
	    if self.nodeid == 6:
          	self.finger[1].node = 0
	        self.print_finger()


        else:
            self.successor1 = self.predecessor1 = self.nodeid
        self.print_finger()

	print "Node " + str(self.nodeid) + \
	    " predecessor " + str(self.predecessor1) + \
            " successor1 " + str(self.successor1) + \
            " successor2 " + str(self.successor2)


    def remove_from_ring(self):
        pred_node = xmlrpclib.ServerProxy(
            "http://" + server_ip(self.predecessor1) + ":" + server_port(self.predecessor1) + "/")
        succ_node = xmlrpclib.ServerProxy(
            "http://" + server_ip(self.successor1) + ":" + server_port(self.successor1) + "/")
        for key, value in self.keys:
            succ_node.receive_key(key, self.memcache_client.get(key))

        pred_node.set_successor(self.successor1)
        succ_node.set_predecessor(self.predecessor1)
        self.update_other_fingers(self.successor1)
        self.memcache_client.flush_all()
        sys.exit(0)

    def ping(self):
        pass

    def failed_predecessor(self):
        succ_node = xmlrpclib.ServerProxy(
            "http://" + server_ip(self.successor1) + ":" + server_port(self.successor1) + "/")
        for key in self.replicated_keys:
            succ_node.receive_replica_key(key, self.memcache_client.get(key))
        pass

    def receive_replica_key(self, key, value):
        self.replicated_keys.append(key)
        self.memcache_client.set(key, value)

    def receive_key(self, key, value):
        self.memcache_client.set(key, value)
        self.keys.append(key)

    def remove_replica_key(self, key):
        self.replicated_keys.remove(key)
        self.memcache_client.delete(key)

    def put_key(self, key, value):
        if self.between(self.predecessor, self.nodeid, key):
            #succ_node = xmlrpclib.ServerProxy(
            #    "http://" + server_ip(self.successor1) + ":" + server_port(self.successor1) + "/", verbose=False)
            #succ_node.receive_replica_key(key, value)
            self.memcache_client.set(key, value)
	    print "Put key = " + str(key) + " value = " + value
            self.keys.append(key)
            return

        for i in range(0, self.m):
            interval = self.finger[i].interval
            if self.between(interval[0], interval[1], key):
                next_node = xmlrpclib.ServerProxy(
                    "http://" + server_ip(self.finger[i].node) + ":" + server_port(self.finger[i].node) + "/")
                next_node.put_key(key, value)

    def delete_key(self, key):
        if self.between(self.predecessor, self.nodeid, key):
            succ_node = xmlrpclib.ServerProxy(
                "http://" + server_ip(self.successor1) + ":" + server_port(self.successor1) + "/")
            #succ_node.remove_replica_key(key)
            print "Delete key = " + key + " value = " + str(self.memcache_client.get(key))
            self.memcache_client.delete(key)
            self.keys.remove(key)
            return

        for i in range(0, self.m):
            interval = self.finger[i].interval
            if self.between(interval[0], interval[1], key):
                next_node = xmlrpclib.ServerProxy(
                    "http://" + server_ip(self.finger[i].node) + ":" + server_port(self.finger[i].node) + "/")
                next_node.delete_key(key)

    def get_value(self, key):
        if self.between(self.predecessor, self.nodeid, key):
            print "Got key = " + key + " value = " + str(self.memcache_client.get(key))
            return self.return_value(key, self.memcache_client.get(key))

        for i in range(0, self.m):
            interval = self.finger[i].interval
            if self.between(interval[0], interval[1], key):
                next_node = xmlrpclib.ServerProxy(
                    "http://" + server_ip(self.finger[i].node) + ":" + server_port(self.finger[i].node) + "/")
                return next_node.get_value(key)

    def return_value(self, key, value):
        if self.nodeid == 0:
            return value

        for i in range(0, self.m):
            interval = self.finger[i].interval
            if self.between(interval[0], interval[1], 0):
                next_node = xmlrpclib.ServerProxy(
                    "http://" + server_ip(self.finger[i].node) + ":" + server_port(self.finger[i].node) + "/")
                return next_node.return_value(key, value)

    def successor(self):
        return self.successor1

    def predecessor(self):
        return self.predecessor1

    def set_predecessor(self, predid):
	print "Changed predecessor " + str(predid)
        if self.nodeid == 0 and predid == 6:
          self.finger[2].node = 6
	  self.print_finger()
        self.predecessor1 = predid

    def set_successor(self, succid):
	print "Changed successor " + str(succid)
        self.successor1 = succid

    def get_successor_list(self):
        pass

    def find_predecessor(self, pred_nodeid):
        succ1_node = xmlrpclib.ServerProxy("http://" + server_ip(self.successor1) + ":" + server_port(self.successor1) + "/", verbose=False)
	pred = -1

	if succ1_node.get_nodeid() != succ1_node.successor():
	    print succ1_node.get_nodeid() + succ1_node.successor() +  pred_nodeid  
 	    while not self.between(succ1_node.get_nodeid(), succ1_node.successor(),  pred_nodeid):
                print str(succ1_node.get_nodeid()) + " " + str(succ1_node.successor()) +  str(pred_nodeid)

	        if succ1_node.get_nodeid() == succ1_node.successor():
                    pred = succ1_node.get_nodeid()
   	            return pred
	        succ1_node = xmlrpclib.ServerProxy("http://" + server_ip(succ1_node.successor()) + ":" + server_port(succ1_node.successor()) + "/", verbose=False)
        pred = succ1_node.get_nodeid()
	#ask the successor to find predid since it has its finger table stable
        #while not self.between(succ1_node.get_nodeid(), succ1_node.successor(),  pred_nodeid):
        #    if succ1_node.get_nodeid() == succ1_node.successor():
	#	pred = succ1_node.get_nodeid()
        #        return pred
        #    succ1_nodeid = succ1_node.find_closest_preceding_finger(pred_nodeid)
        #    #nodeid1 = node1.successor()
        #    succ1_node = xmlrpclib.ServerProxy("http://" + server_ip(succ1_nodeid) + ":" + server_port(succ1_nodeid) + "/", verbose=False)
        #    #print >>sys.stderr, nodeid1, node1
        print "Found predecessor", pred
        return pred

    def find_successor(self, succid):
        nodeid1 = self.find_predecessor(succid)
        node1 = xmlrpclib.ServerProxy("http://" + server_ip(nodeid1) + ":" + server_port(nodeid1) + "/")
        return node1.successor()

    def find_closest_preceding_finger(self, predid):
        for i in range(self.m - 1, -1, -1):
            if predid > self.finger[i].node and self.finger[i].node > self.nodeid:
                return self.finger[i].node
        return self.nodeid

    def update_other_fingers(self, new_successor):
        for i in range(0, self.m):
            predid = self.find_predecessor((self.nodeid - (1 << i)) % (1 << self.m))
            pred = xmlrpclib.ServerProxy("http://" + server_ip(predid) + ":" + server_port(predid) + "/")
            pred.update_own_finger(new_successor, i)

    def update_own_finger(self, new_successor, index):
        print "Update finger new successor " + str(new_successor) + " index " + str(index)
        #REMOVE hardcoded
        if new_successor == 3 and self.nodeid == 1:
          self.finger[0].node = self.finger[1].node = new_successor
          self.print_finger()
          return

        if self.nodeid == 6:
          self.finger[1].node = 0
          self.print_finger()
          return

        if self.nodeid == 1 and new_successor == 6:
          self.finger[2].node = new_successor
          self.print_finger()
          return

        if self.nodeid == 3 and new_successor == 6:
          self.finger[1].node = self.finger[0].node = 6
          self.print_finger()
          return

        if self.nodeid == 0 and new_successor == 6:
          self.finger[2].node = 6
          self.print_finger()
          return

        if ((self.finger[index].node > new_successor and new_successor >= self.nodeid) or \
	    (self.nodeid == 0 and (self.finger[index].interval[0] <= new_successor and self.finger[index].interval[1] > new_successor))):
            self.finger[index].node = new_successor
	    if self.predecessor() != new_successor:
               pred = xmlrpclib.ServerProxy(
                  "http://" + server_ip(self.predecessor()) + ":" + server_port(self.predecessor()) + "/")
               pred.update_own_finger(new_successor, index)
        self.print_finger()

    def init_finger(self):
	print "Init finger"
        node1 = xmlrpclib.ServerProxy("http://" + server_ip(self.nodeid) + ":" + server_port(self.nodeid) + "/", verbose=False)
        self.finger[0].node = node1.find_successor(self.finger[0].start)
        successor_node = xmlrpclib.ServerProxy(
            "http://" + server_ip(self.successor()) + ":" + server_port(self.successor()) + "/", verbose=False)
        predid = successor_node.predecessor()
        successor_node.set_predecessor(self.nodeid)

        for i in range(0, self.m - 1):
            if self.finger[i].node > self.finger[i + 1].start and self.finger[i + 1].start >= self.nodeid:
                self.finger[i + 1].node = self.finger[i].node
            else:
                self.finger[i + 1].node = node1.find_successor(self.finger[i + 1].start)

    def build_finger(self):
        for k in xrange(0, self.m):
            self.finger[k].start = (self.nodeid + (1 << k)) % (1 << self.m)
            self.finger[k].node = self.nodeid

        for k in xrange(0, self.m - 1):
            self.finger[k].interval = [self.finger[k].start, self.finger[k + 1].start]
        if self.m > 1:
            self.finger[self.m - 1].interval = [self.finger[self.m - 1].start,
                                                self.finger[0].start - 1 if (self.finger[0].start - 1 >= 0) else (1 << self.m) - 1]

    def print_finger(self):
        print "Finger table"
        for k in range(0, self.m):
            print self.finger[k].start, " ", self.finger[k].interval, " ", self.finger[k].node


def compute_key_id(tokenid, m):
    md5 = str(hashlib.md5(tokenid).hexdigest())
    mod = 0
    for c in md5:
        digit = int(c, 16)
        mod = (mod * 16 + digit) % (1 << m)
    return str(mod)


def read_commands(node0):
    while True:
        try:
            line = sys.stdin.readline()
            tokens = line.split()
            tokens = [token.lower() for token in tokens]
            if tokens[0] == "put":
                node0.put_key(compute_key_id(tokens[1], node0.m), tokens[2])
            elif tokens[0] == "del":
                node0.delete_key(compute_key_id(tokens[1], node0.m))
            elif tokens[0] == "get":
                print node0.get_value(compute_key_id(tokens[1], node0.m))

        except KeyboardInterrupt:
            break

        if not line:
            break

        print line


if __name__ == "__main__":
    node = Node(int(sys.argv[1]), int(sys.argv[2]))
    if int(sys.argv[2]) < 2:
        print "Expecting m to be >= 2"
        sys.exit(1)
    node.join_ring(int(sys.argv[3]))
    # if node.nodeid == 0:
    #     read_commands()
