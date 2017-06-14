#! /usr/bin/python

import xmlrpclib
import sys
from node import compute_key_id


def server_ip(nodeid):
    return "10.0.0." + str(2 + nodeid)


def server_port(nodeid):
    return str(5000 + nodeid)



proxy = xmlrpclib.ServerProxy("http://" + server_ip(int(sys.argv[1])) + ":" + server_port(int(sys.argv[1])) + "/")
prompt = "Client {} > ".format(sys.argv[1])
m = int(proxy.get_size())
print m


def read_commands():
    global m
    while True:
        print prompt,
        try:
            line = sys.stdin.readline()
            tokens = line.split()
            tokens = [token.lower() for token in tokens]
            if tokens[0] == "put":
                proxy.put_key(compute_key_id(tokens[1], m), tokens[2])
            elif tokens[0] == "del":
                proxy.delete_key(compute_key_id(tokens[1], m))
            elif tokens[0] == "get":
                print proxy.get_value(compute_key_id(tokens[1], m))
            elif tokens[0] == "join":
                proxy.join_ring()
            elif tokens[0] == "print_finger":
                proxy.print_finger()
            elif tokens[0] == "print_info":
                proxy.print_info()

        except KeyboardInterrupt:
            break

        if not line:
            break

        print line


read_commands()
