import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.List;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import libmemcached.constants.memcached_return;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;


public class Client {
	private static ZooKeeper zk;
	private static Watcher memcachedNodesWatcher;
	private ConcurrentHashMap<String, MemcachedNode> memcachedNodes = new ConcurrentHashMap<String, MemcachedNode>();
	private HeartBeat heartbeat;
	
	private void readCommands(String filename) {
		MemcachedRequestHandler memcachedRequestHandler = new MemcachedRequestHandler(memcachedNodes);
		try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
		    String line;
		    while ((line = br.readLine()) != null) {
		      memcachedRequestHandler.processCommand(line);
		    }
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public Client(String zookeeperHostname) {
			try {
			zk = new ZooKeeper(zookeeperHostname, MemcachedService.SESSION_TIMEOUT, memcachedNodesWatcher);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		memcachedNodesWatcher = new Watcher(){
	        @Override
	        public void process(WatchedEvent watchEvent) {
	        	switch (watchEvent.getType()) {
	        	//The Memcached service state changed
	        	case NodeDataChanged: {
	        		String path = watchEvent.getPath();
	        		String nodeName = path.replace(MemcachedService.SERVICE_PATH + "/", "");
	        		MemcachedNode memcachedNode = memcachedNodes.get(nodeName);
	        		if (memcachedNode != null) {
						byte state[];
						try {
							state = zk.getData(path, memcachedNodesWatcher,
									null);
			        		System.out.println("Service state for " + nodeName + " changed. New state: " + new String(state));
							memcachedNode.setState(new String(state));
							memcachedNodes.put(nodeName, memcachedNode);
						} catch (Exception e) {
							e.printStackTrace();
						}
	        		}
	        		break;
	        	}
	        	// The Watchdog died.
	        	case NodeDeleted: {
	        		String path = watchEvent.getPath();
	        		String nodeName = path.replace(MemcachedService.SERVICE_PATH + "/", "");
	        		System.out.println("Watchdog died for node " + nodeName);

	        		MemcachedNode memcachedNode = memcachedNodes.get(nodeName);
	        		if (memcachedNode != null) {
						try {
							memcachedNode.setState(new String(
									MemcachedService.WATCHDOG_DIED));
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
	        		} 
	        		break;
	        	}
	        	// A Watchdog (re)started or died.
	           	case NodeChildrenChanged: {
	          		String path = watchEvent.getPath();
	    			getMemcachedNodes();
	        		System.out.println("Children changed on path: " + path + ". Known memcached nodes: ");
	        		for (Entry<String, MemcachedNode> entry : memcachedNodes.entrySet()) {
	        			String nodeName = entry.getKey();
	        			String nodeState = entry.getValue().getState();
	        			System.out.println("node name: " + nodeName + " state: " + nodeState);
	        		}
	        		
	        		break;
	        	}

	        	}
	        }
		};
		getMemcachedNodes();
		heartbeat = new HeartBeat();
		heartbeat.start();
	}
	
	private void getMemcachedNodes() {
		try {
			List<String> nodes = zk.getChildren(MemcachedService.SERVICE_PATH, memcachedNodesWatcher);
			for (String node : nodes) {
				MessageDigest sha1 = MessageDigest.getInstance("SHA-1");
				sha1.reset();
				sha1.update(node.getBytes("UTF-8"));
				String sha1_hash = new String(
						new BigInteger(1, sha1.digest()).toString(16));

				byte state[] = zk.getData(MemcachedService.SERVICE_PATH + "/"
						+ node, memcachedNodesWatcher, null);

				memcachedNodes.put(node, new MemcachedNode(node, sha1_hash, new String(state)));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	class HeartBeat extends Thread {
		public void run() {
			try {
				while (true) {
					for (Entry<String, MemcachedNode> entry : memcachedNodes
							.entrySet()) {
						String nodeKey = entry.getKey();
						MemcachedNode node = memcachedNodes.get(nodeKey);

						Process p = Runtime.getRuntime().exec(
								"ping -c 3 " + nodeKey
										+ " | grep errors | wc -l");
						BufferedReader input = new BufferedReader(
								new InputStreamReader(p.getInputStream()));
						String line = input.readLine();

						input.close();
						if (!line.equals("0")) {
							node.setState(MemcachedService.UNREACHABLE);
						} else if (node.getState() == MemcachedService.UNREACHABLE) {
							if (zk != null) {
								try {
									byte state[] = zk.getData(
											MemcachedService.SERVICE_PATH + "/"
													+ node,
											memcachedNodesWatcher, null);
									node.setState(new String(state));
								} catch (KeeperException.NoNodeException keeperException) {
									// There is no path for the node.
									// The Watchdog died => set WATCHDOG_DIED state
									node.setState(MemcachedService.WATCHDOG_DIED);
								}
							}
						}

						memcachedNodes.put(nodeKey, node);
					}

				}
			} catch (Exception exc) {

			}
		}
		
	}
	
	public static void main(String[] args) {
		Client client = new Client(args[0]);
		client.readCommands("/home/evelinad/workspace/tema4/src/INPUT");
	}
	
}
