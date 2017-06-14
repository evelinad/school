#! /usr/bin/python

from SimpleXMLRPCServer import SimpleXMLRPCServer
import xmlrpclib
import sys
import time


nr = int(sys.argv[1]) + 9000
#server = SimpleXMLRPCServer(('localhost', nr), logRequests=True)
def ping():
	proxy = xmlrpclib.ServerProxy('http://localhost:' + str(nr - 1))
	return proxy.ping()

#server.register_function(ping)
#server.serve_forever()


time.sleep(7)
print ping()
