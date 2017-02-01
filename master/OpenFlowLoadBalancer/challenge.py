#!/usr/bin/python

from mininet.net import Mininet
from mininet.node import Controller, RemoteController, OVSController
from mininet.cli import CLI
from mininet.log import setLogLevel, info
from mininet.link import Intf

def ch2Net():
	# Am folosit IP-ul de pe interfata vmnet1 de pe masina fizica
	CONTROLLER_IP = '192.168.164.1' # <=== TODO: CONTROLLER IP HERE!
	CTRL_IF = 'eth1' # <=== TODO: CONTROL INTERFACE HERE!
	
	net = Mininet(topo=None, build=False, )
	
	c1 = net.addController('c1', controller=RemoteController, ip=CONTROLLER_IP, port=6653)
	c2 = net.addController(name='c2', controller=OVSController)

	h1 = net.addHost('h1', ip='10.1.0.1/24')
	h2 = net.addHost('h2', ip='10.1.0.2/24', mac='00:de:ad:be:ef:00')
	h3 = net.addHost('h3', ip='10.1.0.2/24', mac='00:de:ad:be:ef:00')

	s1 = net.addSwitch('s1')
	s2 = net.addSwitch('s2')

	
	net.addLink(h1, s1)
	net.addLink(h2, s1)
	net.addLink(h3, s1)
	
	net.addLink(h2, s2)
	net.addLink(h3, s2)
	Intf(CTRL_IF, node=s2)

	net.build()

	c2.start()
	
	s1.start([c1])
	s2.start([c2])
	
	#h2.cmdPrint('dhclient h2-eth1')
	#h3.cmdPrint('dhclient h3-eth1')
        h2.cmdPrint('ifconfig h2-eth1 192.168.56.3 netmask 255.255.255.0 broadcast 192.168.56.255')
	h3.cmdPrint('ifconfig h3-eth1 192.168.56.4 netmask 255.255.255.0 broadcast 192.168.56.255')

	h2.cmdPrint('/usr/sbin/sshd')
	h3.cmdPrint('/usr/sbin/sshd')
	
	h2.cmdPrint('/etc/init.d/apache2-h2 start')
	h3.cmdPrint('/etc/init.d/apache2-h3 start')

	CLI( net )
	net.stop()

if __name__ == '__main__':
	setLogLevel( 'info' )
	ch2Net()
