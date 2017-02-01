from mininet.topo import Topo
from mininet.node import CPULimitedHost
#from mininet.link import TCLink
from mininet.util import irange
from mininet.net import Mininet
from mininet.node import OVSController
from mininet.cli import CLI
import time

NUM_HOSTS = 10

class DHTTopo( Topo ):

	def __init__( self ):
		# Initialize topology
		Topo.__init__( self )
		hosts = [None] * NUM_HOSTS

		client = self.addHost('client')
		zookeeper = self.addHost('zookeeper')
		sw0 = self.addSwitch('s0')
		self.addLink(client, sw0)
		self.addLink(zookeeper, sw0)

		for i in range(0, NUM_HOSTS):
			hosts[i] = self.addHost('h%s' % i)
			self.addLink(sw0, hosts[i])


if __name__ == '__main__':
	topo = DHTTopo()
	net = Mininet(topo=topo, controller = OVSController)
        net.start()
	net.pingAll()
	time.sleep(5)
	net.getNodeByName('zookeeper').cmd('/home/evelinad/zookeeper/bin/zkServer.sh start &')
	ZOOKEEPER_IP = '10.0.0.%s' % (NUM_HOSTS + 2)
	time.sleep(10)
	for i in range(0, NUM_HOSTS):
		hostip = '10.0.0.%s' % (i + 2) 
		hostname = 'h%s' % i
		print ZOOKEEPER_IP
		print hostip
		net.getNodeByName(hostname).cmd('./start_memcached_watchdog.sh %s %s &' % (ZOOKEEPER_IP, hostip))
	CLI(net)
	net.stop()
#topos = { 'dhttopo': ( lambda: DHTTopo() ) }
