#!/usr/bin/python

import memcache
import sys

memcache_client = memcache.Client(["127.0.0.1:11211"])
memcache_client.set("1234", "ceva")
print memcache_client.get("1234")

