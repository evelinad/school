import libmemcached.LibMemcached;
import libmemcached.memcached.memcached_st;

public class MemcachedNode {
		private String ip;
		private String sha1;
		// private String md5;
		private String state;
	    memcached_st mmc = LibMemcached.memcached.memcached_create(null);
		
		public MemcachedNode(String ip, String sha1, String state) {
			this.ip = ip;
			this.setSha1(sha1);
			//this.md5 = md5;
			this.state = state;
			LibMemcached.memcached.memcached_server_add(mmc, ip, MemcachedService.PORT);
		}

		public String getState() {
			return state;
		}

		public void setState(String state) {
			this.state = state;
		}

		public String getSha1() {
			return sha1;
		}

		public void setSha1(String sha1) {
			this.sha1 = sha1;
		}
	
	}
