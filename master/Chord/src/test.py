#! /usr/bin/python

from SimpleXMLRPCServer import SimpleXMLRPCServer
import sys
import time
import xmlrpclib


nr = int(sys.argv[1]) + 9000
server = SimpleXMLRPCServer(('localhost', nr), logRequests=True, allow_none=True)
def ping():
	if nr != 9000:
		proxy = xmlrpclib.ServerProxy('http://localhost:' + str(nr - 1))
		print "return smthing else"
		return proxy.ping()
	else:
		print "return hello"
		return "hello!"

server.register_function(ping)
server.serve_forever()


time.sleep(7)
print ping()
