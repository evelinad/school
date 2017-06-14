#!/usr/bin/python

from mininet.net import Mininet
from mininet.topo import Topo
from mininet.link import Link, TCLink
import sys
from time import sleep
import memcache


class ChordTopo(Topo):
    def __init__(self, n):
        # Initialize topology
        Topo.__init__(self)
        hosts = [None] * n
        client = self.addHost('client')

        sw0 = self.addSwitch('s0')
        sw1 = self.addSwitch('s1')
        sw2 = self.addSwitch('s2')

        self.addLink(client, sw0)  # , bw=10, delay='5ms',  max_queue_size=1000)
        self.addLink(sw0, sw1)  # , bw=10, delay='5ms', max_queue_size=1000)
        self.addLink(sw0, sw2)  # , bw=10, delay='5ms', max_queue_size=1000)

        for i in range(0, n):
            hosts[i] = self.addHost('h%s' % i)

        for i in range(0, n / 2):
            self.addLink(sw1, hosts[i])  # , bw=10, delay='5ms', max_queue_size=1000)
            self.addLink(sw2, hosts[i + n / 2])  # , bw=10, delay='5ms', max_queue_size=1000)


topos = {'chord': (lambda: ChordTopo(2))}
