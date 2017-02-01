import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import com.sun.jna.ptr.IntByReference;
import com.sun.jna.ptr.LongByReference;

import libmemcached.LibMemcached;
import libmemcached.compat.size_t;
import libmemcached.compat.time_t;
import libmemcached.constants.memcached_return;
import libmemcached.memcached.memcached_st;
import libmemcached.exception.LibMemcachedRuntimeException;


public class MemcachedRequestHandler {
	private static final String GET_COMMAND = "GET";
	private static final String PUT_COMMAND = "PUT";
	private static final String SLEEP_COMMAND = "SLEEP";
	private static ConcurrentHashMap<String, MemcachedNode> memcachedNodes;
	
	public MemcachedRequestHandler(ConcurrentHashMap<String, MemcachedNode> memcachedNodes) {
		this.memcachedNodes = memcachedNodes;
	}
	
	private static String getStorageNode(String keyHash) {
		// The storage node is the smallest node with the hash greater than the hash of the object.
		String nodeMinimumGreaterKey = null;
		String nodeMinimumGreaterHash = null;
		
		// If the object hash is greater than the hashes of all the existing nodes, pick the node with the smallest hash.
		String nodeMinimumKey = null;
		String nodeMinimumHash = null;
	
		for (Entry<String, MemcachedNode> entry : memcachedNodes.entrySet()) {
			String nodeKey = entry.getKey();
			MemcachedNode node = entry.getValue();
			// Ignore the memcached nodes that have the service stopped, the watchdog died
			// or are temporary unreachable due to network failures.
			// Consider only the ones that have the service running.
			if (node.getState() == MemcachedService.STOPPED ||
					node.getState() == MemcachedService.WATCHDOG_DIED ||
					node.getState() == MemcachedService.UNREACHABLE) {
				continue;
			}

			String nodeHash = node.getSha1();
			if (keyHash.compareTo(nodeHash) < 0 && nodeHash.compareTo(nodeMinimumGreaterHash) < 0) {
				nodeMinimumGreaterHash = nodeHash;
				nodeMinimumGreaterKey = nodeKey;
			}
			if (nodeMinimumHash == null) {
				nodeMinimumHash = nodeHash;
				nodeMinimumKey = nodeKey;
			} else if (nodeHash.compareTo(nodeMinimumHash) < 0) {
				nodeMinimumHash = nodeHash;
				nodeMinimumKey = nodeKey;
			}				
		}
		if (nodeMinimumGreaterKey == null) {
			return nodeMinimumKey;
		} else {
			return nodeMinimumGreaterKey;
		}
	}
	
	public static int processPUTCommand(String node, memcached_st mmc, String key, String keyHash, String value) {
		time_t expiration = new time_t(0);
		size_t keyLength = new size_t(key.length());
		size_t valueLength = new size_t(value.length());
		
		int rc = LibMemcached.memcached.memcached_set(mmc, key,
				keyLength, value, valueLength, expiration, 0);
        System.out.println("PUT result for key = " + key + " and value = " +  value + ". Error returned: " + rc);

		return rc;
	}
	

	public void processCommand(String action) {
		String[] tokens = action.split(" ");
		final String command = tokens[0];
		switch (command) {
		case GET_COMMAND: {
			final String key = tokens[1];		

			try {
				MessageDigest sha1 = MessageDigest.getInstance("SHA-1");

				sha1.reset();
				sha1.update(key.getBytes("UTF-8"));
				
				String keyHash = new String(
						new BigInteger(1, sha1.digest()).toString(16));
				String node = getStorageNode(keyHash);
				LongByReference valueLength =  new LongByReference();
			    IntByReference flags = new IntByReference();
			    IntByReference error = new IntByReference();
			    size_t keyLength = new size_t(key.length());
			    memcached_st mmc = memcachedNodes.get(node).mmc;
			    String value = LibMemcached.memcached.memcached_get(mmc, key, keyLength, valueLength, flags, error);
			        
			    System.out.println("GET result for key = " + key + " and value = " +  value + ". Error returned: " + error.getValue());
			} catch (Exception e) {
				e.printStackTrace();
			}
			break;
		}
		case PUT_COMMAND: {
			try {
				final String key = tokens[1];
				final String value = tokens[2];
				MessageDigest sha1;

				sha1 = MessageDigest.getInstance("SHA-1");
				sha1.reset();
				sha1.update(key.getBytes("UTF-8"));
				String keyHash = new String(
						new BigInteger(1, sha1.digest()).toString(16));

				String node = getStorageNode(keyHash);

				memcached_st mmc =  memcachedNodes.get(node).mmc;
				time_t expiration = new time_t(0);
				size_t keyLength = new size_t(key.length());
				size_t valueLength = new size_t(value.length());
			
				int rc = LibMemcached.memcached.memcached_set(mmc, key,
						keyLength, value, valueLength, expiration, 0);
		        System.out.println("PUT result for key = " + key + " and value = " +  value + ". Error returned: " + rc);
			} catch (Exception e) {
				e.printStackTrace();
			}
			      
			break;
		}
		case SLEEP_COMMAND: {
			try {
				int duration = Integer.parseInt(tokens[1]);
				Thread.sleep(duration);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			break;
		}
		}
	}

}
