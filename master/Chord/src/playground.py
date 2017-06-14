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

        self.addLink(client, sw0, bw=10, delay='5ms', loss=0, max_queue_size=1000, use_htb=True)
        self.addLink(sw0, sw1, bw=10, delay='5ms', loss=0, max_queue_size=1000, use_htb=True)
        self.addLink(sw0, sw2, bw=10, delay='5ms', loss=0, max_queue_size=1000, use_htb=True)

        for i in range(0, n):
            hosts[i] = self.addHost('h%s' % i)

        for i in range(0, n / 2):
            self.addLink(sw1, hosts[i], bw=10, delay='5ms', loss=0, max_queue_size=1000, use_htb=True)
            self.addLink(sw2, hosts[i + n / 2], bw=10, delay='5ms', loss=0, max_queue_size=1000, use_htb=True)


def run(net, n):
    net.pingAll()
    '''
    for i in range(1, n):
      for j in range(1, n):
        h1 = net.getNodeByName('h%s' % i)
        h2 = net.getNodeByName('h%s' % j)
        h_out = h1.cmd('ping  -c 3 ' + h2.IP())
        print h_out
    '''

    memcache_servers = []
    for i in range(0, n):
        h = net.getNodeByName('h%s' % i)
        print h.IP()
        memcache_server = h.popen('memcached -m 64 -l ' + h.IP() + ' -p 11211 -u mininet -d')
        h.sendCmd("ps -ef | grep memcache")
        h_out = h.waitOutput()
        print h_out
        sleep(2)
        memcache_server.terminate()
    return
    sleep(2)

    for i in range(0, n):
        h = net.getNodeByName('h%s' % i)
        h.sendCmd("ps -ef | grep memcache")
        h_out = h.waitOutput()
        print h_out
        h.sendCmd("./memcacheclient.py")
        h_out = h.waitOutput()
        print h_out
        # memcache_servers[i].terminate()


def main():
    net = Mininet(topo=ChordTopo(int(sys.argv[1])), link=TCLink)
    net.start()
    run(net, int(sys.argv[1]))
    net.stop()


if __name__ == '__main__':
    main()
