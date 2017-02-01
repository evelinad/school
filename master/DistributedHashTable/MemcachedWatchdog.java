import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.ZooKeeper;


public class MemcachedWatchdog {
	private MemcachedWatcher watcher;
	private ZooKeeper zk;
	private String memcached_node_path;
	
	public MemcachedWatchdog(String zookeeper_hostname, String memcached_hostname) {
		watcher = new MemcachedWatcher();
	
		try {
			zk = new ZooKeeper(zookeeper_hostname, MemcachedService.SESSION_TIMEOUT,  null);
			if (zk.exists(MemcachedService.SERVICE_PATH, null) == null) {
				zk.create(MemcachedService.SERVICE_PATH, MemcachedService.SERVICE_PATH.getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
			}
			String state = MemcachedService.RUNNING;
			Process p = Runtime.getRuntime().exec("pidof memcached");
			BufferedReader input = new BufferedReader(
					new InputStreamReader(p.getInputStream()));
			String line = input.readLine();
			input.close();

			if (line == null) {
					state = MemcachedService.STOPPED;
			}
			memcached_node_path = zk.create(MemcachedService.SERVICE_PATH + "/" + memcached_hostname, state.getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
			watcher.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	class MemcachedWatcher extends Thread {

		private static final int WATCH_INTERVAL = 1000;
		private String memcached_state;
			
		public MemcachedWatcher() {
			memcached_state = MemcachedService.RUNNING;
		}
		
		public void run() {
			
			try {
				  while (true) {
					Process p = Runtime.getRuntime().exec("pidof memcached");
					BufferedReader input = new BufferedReader(
							new InputStreamReader(p.getInputStream()));
					String line = input.readLine();
					input.close();

					if (line == null) {
						if (memcached_state != MemcachedService.STOPPED) {
							memcached_state = MemcachedService.STOPPED;
							zk.setData(memcached_node_path, memcached_state.getBytes(), -1);
						}
					} else if (memcached_state == MemcachedService.STOPPED) {
						memcached_state = MemcachedService.RUNNING;
						zk.setData(memcached_node_path, memcached_state.getBytes(), -1);
					}		
					//System.out.println("The memcached service is " + memcached_state);
					Thread.sleep(WATCH_INTERVAL);
				  }
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		}
	
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		MemcachedWatchdog watchdog = new MemcachedWatchdog(args[0], args[1]);
		
	}

}
