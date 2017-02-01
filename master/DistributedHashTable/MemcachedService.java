
public interface MemcachedService {
	public final String RUNNING = "RUNNING";
	public final String STOPPED = "STOPPED";
	public final String UNREACHABLE = "UNREACHABLE";
	public final String SERVICE_PATH = "/memcached";
	public final String WATCHDOG_DIED = "WATCHDOG_DIED";
	public final int SESSION_TIMEOUT = 1000;
	public final int PORT = 11211;
}
