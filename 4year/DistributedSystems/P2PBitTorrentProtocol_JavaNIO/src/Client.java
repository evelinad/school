/*
    DUMITRESCU EVELINA 341C3 Tema4 SPRC
 */
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.StandardSocketOptions;
import java.nio.*;
import java.nio.channels.*;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.io.*;
import java.util.*;
import java.util.logging.Logger;

/*Server class used to handle I/O connections from other peers*/
class ServerPeer implements Runnable {

	final int BYTE_BUFFER_SIZE = 4096;
	final int NR_BYTES_SIZE = 4;
	private ByteBuffer buffer = ByteBuffer.allocate(BYTE_BUFFER_SIZE);
	private Map<SocketChannel, List<byte[]>> keepDataTrack = new HashMap<>();
	int port;
	Charset charset;
	CharsetDecoder decoder;

	public ServerPeer(int port) {
		this.port = port;
		this.charset = Charset.defaultCharset();
		this.decoder = charset.newDecoder();
		(new Thread(this)).start();

	}
	public void run() {

		/* open Selector and ServerSocketChannel */
		try {
			Selector selector = Selector.open();
			ServerSocketChannel serverSocketChannel = ServerSocketChannel
					.open();
			/* check if they were successfully opened */
			if ((serverSocketChannel.isOpen()) && (selector.isOpen())) {
				/* non-blocking mode */
				serverSocketChannel.configureBlocking(false);
				/* bind the server socket channel to port */
				serverSocketChannel.bind(new InetSocketAddress(port));
				/* register the current channel with the given selector */
				serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
				Client.logger.info("Waiting for connections ...");
				while (true) {
					/* waiting for incomming events */
					selector.select();

					/* select a key for a processing an event */
					Iterator keys = selector.selectedKeys().iterator();

					while (keys.hasNext()) {
						SelectionKey key = (SelectionKey) keys.next();

						/* remove key such that the same key won't come up again */
						keys.remove();

						if (!key.isValid()) {
							continue;
						}
						/* new connection/reading event/writing event */
						if (key.isAcceptable()) {
							acceptOP(key, selector);
						} else if (key.isReadable()) {
							this.readOP(key);
						} else if (key.isWritable()) {
							this.writeOP(key);
						}
					}
				}

			} else {
				Client.logger
						.severe("The server socket channel or selector cannot be opened!");
			}
		} catch (IOException ex) {
			Client.logger
					.severe("An error occurred while communicating with other hosts");
		}
	}

	/* new connecton */
	private void acceptOP(SelectionKey key, Selector selector)
			throws IOException {

		ServerSocketChannel serverChannel = (ServerSocketChannel) key.channel();
		SocketChannel socketChannel = serverChannel.accept();
		socketChannel.configureBlocking(false);
		Client.logger.info("Incoming connection from: "
				+ socketChannel.getRemoteAddress());
		/* register channel with selector for exchanging further messages */
		keepDataTrack.put(socketChannel, new ArrayList<byte[]>());
		socketChannel.register(selector, SelectionKey.OP_READ);
	}

	/* reading event */
	private void readOP(SelectionKey key) {
		try {
			SocketChannel socketChannel = (SocketChannel) key.channel();

			buffer.clear();

			int numRead = -1;
			try {
				numRead = socketChannel.read(buffer);
			} catch (IOException e) {
				Client.logger.severe("Cannot read from socket");
			}

			if (numRead == -1) {
				this.keepDataTrack.remove(socketChannel);
				Client.logger.severe("Connection closed by: "
						+ socketChannel.getRemoteAddress());
				socketChannel.close();
				key.cancel();
				return;
			}

			byte[] data = new byte[numRead];
			System.arraycopy(buffer.array(), 0, data, 0, numRead);

			/* respond to client */
			doEchoJob(key, data);
		} catch (Exception ex) {
			System.err.println(ex);
		}
	}

	/* writing event */
	private void writeOP(SelectionKey key) throws IOException {

		SocketChannel socketChannel = (SocketChannel) key.channel();
		List<byte[]> channelData = keepDataTrack.get(socketChannel);
		Iterator<byte[]> its = channelData.iterator();
		RandomAccessFile f;
		long positionToJump;
		StringTokenizer st;
		String task;
		String fileName;
		int fragment;
		/* process all requests from channel queue */
		while (its.hasNext()) {
			byte[] it = its.next();
			its.remove();
			task = new String(it);
			/* remove trailing grabage characters from message */
			task = task.replaceAll("[^A-Za-z0-9. ]", "");
			st = new StringTokenizer(task, " ");
			/* extract filename, fragment no and send back the file fragment */
			fileName = st.nextToken();
			fragment = Integer.parseInt(st.nextToken());
			byte[] bufferRead = new byte[BYTE_BUFFER_SIZE];
			f = new RandomAccessFile(fileName, "r");
			positionToJump = (BYTE_BUFFER_SIZE - NR_BYTES_SIZE) * fragment;
			f.seek(positionToJump);
			int rd = f.read(bufferRead, 4, BYTE_BUFFER_SIZE - NR_BYTES_SIZE);
			for (int i = 0; i < 4; i++) {
				bufferRead[i] = (byte) (rd >> (i * 8));

			}
			f.close();
			Client.logger
					.info("Sending fragment " + fragment + " from " + fileName
							+ " to peer " + socketChannel.getRemoteAddress());
			socketChannel.write(ByteBuffer.wrap(bufferRead));

		}

		key.interestOps(SelectionKey.OP_READ);
	}

	/* store in a queue the requests for further processing */
	private void doEchoJob(SelectionKey key, byte[] data) {
		SocketChannel socketChannel = (SocketChannel) key.channel();
		List<byte[]> channelData = keepDataTrack.get(socketChannel);
		channelData.add(data);

		key.interestOps(SelectionKey.OP_WRITE);
	}

}
/* client class used to send I/O requests to other peers */
class ClientPeer implements Runnable {
	ByteBuffer receivingBufferPeer;
	final int BYTE_BUFFER_SIZE = 4096;
	final int NR_BYTES_SIZE = 4;
	String remoteHost;
	int remotePort;
	LinkedList tasks;
	String downloadFile;
	Client client;
	public ClientPeer(String remoteHost, int remotePort, String downloadFile,
			LinkedList tasks, Client client) {
		this.remoteHost = remoteHost;
		this.client = client;
		this.remotePort = remotePort;
		this.downloadFile = downloadFile;
		this.tasks = tasks;
		this.receivingBufferPeer = ByteBuffer.allocate(BYTE_BUFFER_SIZE);
		(new Thread(this)).start();
	}

	public void run() {
		try {

			/* open Selector and ServerSocketChannel */
			Selector selector = Selector.open();
			SocketChannel socketChannel = SocketChannel.open();
			Client.logger
					.info("Opening socket channel and trying to connect to remote peer");
			// check if both of them are opened
			if ((socketChannel.isOpen()) && (selector.isOpen())) {

				/* non-blocking mode */
				socketChannel.configureBlocking(false);

				/* register the channel */
				socketChannel.register(selector, SelectionKey.OP_CONNECT);

				/* connect to remote host */
				socketChannel.connect(new java.net.InetSocketAddress(
						remoteHost, remotePort));

				while (selector.select(1000) > 0) {

					/* get current selected keys */
					Set keys = selector.selectedKeys();
					Iterator its = keys.iterator();

					/* process each one */
					while (its.hasNext()) {
						SelectionKey key = (SelectionKey) its.next();

						/* remove the selected one */
						its.remove();

						/* get the socket channel for current key */
						try (SocketChannel keySocketChannel = (SocketChannel) key
								.channel()) {

							/* try to connect */
							if (key.isConnectable()) {

								Client.logger.info("Connection succeded");

								/* close pendent connections */
								if (keySocketChannel.isConnectionPending()) {
									keySocketChannel.finishConnect();
								}

								RandomAccessFile f = new RandomAccessFile(
										this.downloadFile + "2", "rw");
								for (Object fragment : tasks) {
									byte[] message = new byte[BYTE_BUFFER_SIZE];
									byte[] messageBytes = (this.downloadFile
											+ " " + (int) fragment).getBytes();
									System.arraycopy(messageBytes, 0, message,
											0, messageBytes.length);

									Client.logger
											.info("Sending request to remote peer "
													+ socketChannel
															.getRemoteAddress());
									/* send the request to the remote peer */
									ByteBuffer sendingBuffer = ByteBuffer
											.wrap(message);
									keySocketChannel.write(sendingBuffer);
									int numRead = 0;
									int numBytes = 0;
									int positionToJump = (BYTE_BUFFER_SIZE - NR_BYTES_SIZE)
											* ((int) fragment);
									f.seek(positionToJump);
									receivingBufferPeer.clear();
									Client.logger
											.info("Receving response from remote peer "
													+ socketChannel
															.getRemoteAddress());
									/* get the response and write the fragment */
									while ((numRead += keySocketChannel
											.read(receivingBufferPeer)) < BYTE_BUFFER_SIZE);
									receivingBufferPeer.flip();
									byte[] data = new byte[numRead];
									System.arraycopy(
											receivingBufferPeer.array(), 0,
											data, 0, numRead);
									/*
									 * first 4 bytes represent the size of the
									 * file fragment
									 */
									numBytes = ((0xFF & data[3]) << 24)
											+ ((0xFF & data[2]) << 16)
											+ ((0xFF & data[1]) << 8)
											+ (0xFF & data[0]);

									f.write(data, 4, numBytes);
									Client.logger.info("Writing fragment "
											+ (int) fragment + " from file "
											+ this.downloadFile + " of "
											+ numBytes + " bytes");
									this.client.sendMessageToCentralNode("own "
											+ this.downloadFile + " "
											+ (int) fragment + " "
											+ this.client.ownServerHost + " "
											+ this.client.ownServerPort);
									Client.logger
											.info("Sending own message to central node");
									if (receivingBufferPeer.hasRemaining()) {
										receivingBufferPeer.compact();
									} else {
										receivingBufferPeer.clear();
									}

								}
								f.close();
							}
						} catch (IOException ex) {
							Client.logger
									.severe("An error occurred when trying to get the socket channel for a key");

						}
					}
				}
			} else {
				Client.logger
						.severe("The socket channel or selector cannot be opened!");
			}

		} catch (Exception exc) {
			Client.logger
					.severe("An error occurred when communicating with remote peer");
		}
	}
}
public class Client extends AbstractClient {

	String ownServerHost;
	int ownServerPort;
	final int BYTE_BUFFER_SIZE = 4096;
	final int NR_BYTES_SIZE = 4;
	ByteBuffer outgoingBuffer;
	ByteBuffer receivingBuffer;
	ByteBuffer outgoingBufferPeer;
	ByteBuffer receivingBufferPeer;
	SocketChannel socketChannel;
	CharBuffer charBuffer;
	Charset charset;
	int centralNodePort = 9001;
	String centralNodeHost = "127.0.0.1";
	CharsetDecoder decoder;
	static Logger logger = Logger.getLogger(Client.class.getName());
	public Client(String ownServerHost, int ownServerPort,
			String centralNodeHost, int centralNodePort) {
		this.ownServerPort = ownServerPort;
		this.ownServerHost = ownServerHost;
		this.centralNodeHost = centralNodeHost;
		this.centralNodePort = centralNodePort;
		this.outgoingBuffer = ByteBuffer.allocateDirect(BYTE_BUFFER_SIZE);
		this.receivingBuffer = ByteBuffer.allocateDirect(BYTE_BUFFER_SIZE);
		this.receivingBufferPeer = ByteBuffer.allocate(BYTE_BUFFER_SIZE);
		this.charset = Charset.defaultCharset();
		this.decoder = charset.newDecoder();
		startClient();
		new Thread(new ServerPeer(ownServerPort));

	}
	/* send messages to the central node */
	void sendMessageToCentralNode(String message) {
		try {
			ByteBuffer sendingBuffer = ByteBuffer.wrap(message.getBytes());
			socketChannel.write(sendingBuffer);
		} catch (Exception exc) {
			exc.printStackTrace();
		}

	}
	/* upload one file */
	public void publishFile(File file) {
		if (file.exists() == false) {
			logger.severe("File does not exist.");

		} else {
			/*
			 * publishing message format: upload fielName fileSize fragmentSize
			 * ownServerHost ownServerPort
			 */
			long fileSize;
			int fragmentSize;
			StringBuilder newCommandBuilder;
			String fileName = file.getName();
			fileSize = (new File(fileName)).length();
			fragmentSize = BYTE_BUFFER_SIZE - NR_BYTES_SIZE;
			newCommandBuilder = new StringBuilder();
			newCommandBuilder.append("upload ");
			newCommandBuilder.append(fileName + " ");
			newCommandBuilder.append(fileSize + " ");
			newCommandBuilder.append(fragmentSize + " ");
			newCommandBuilder.append(this.ownServerHost + " ");
			newCommandBuilder.append(this.ownServerPort + " ");
			sendMessageToCentralNode(newCommandBuilder.toString());
			logger.info("Published the file " + fileName);
		}

	}
	/* download one file */
	public File retrieveFile(String fileName) {
		long fileSize;
		int fragmentSize;
		StringBuilder newCommandBuilder;
		newCommandBuilder = new StringBuilder();
		newCommandBuilder.append("download ");
		newCommandBuilder.append(fileName);
		sendMessageToCentralNode(newCommandBuilder.toString());
		getResponse(fileName);
		logger.info("Retrived the file " + fileName);
		return new File(fileName);
	}

	/* if previous command was upload then wait for peer response */
	public void getResponse(String fileName) {
		try {

			String msgReceived = "";
			receivingBuffer.clear();
			/*
			 * the expected message format is a linkedlist each entry
			 * corresponds to a fragment and contains a list of remote peer
			 * addresses
			 */
			while (msgReceived.endsWith("]]") == false) {
				socketChannel.read(receivingBuffer);
				receivingBuffer.flip();
				msgReceived += decoder.decode(receivingBuffer).toString();
				msgReceived = msgReceived.replaceAll("[^A-Za-z0-9.,:\\[\\] ]",
						"");
				receivingBuffer.clear();
			}
			logger.info("Received response from server "
					+ socketChannel.getRemoteAddress() + " for " + fileName);
			/* do some parsing */
			StringTokenizer st = new StringTokenizer(msgReceived, "]");
			LinkedList<LinkedList<String>> fragments = new LinkedList<LinkedList<String>>();
			int nrFragments = 0;
			while (st.hasMoreTokens()) {
				LinkedList peers = new LinkedList<String>();
				String token = st.nextToken();
				token = token.replaceAll(" ", "").replaceAll("\\[", "");
				StringTokenizer st2 = new StringTokenizer(token, ",");
				while (st2.hasMoreTokens()) {
					peers.add(st2.nextToken());

				}
				fragments.add(peers);

			}
			/* equally divide the download requests to each peer */
			LinkedHashMap tasks = new LinkedHashMap();
			int min = Integer.MAX_VALUE;
			int peerNrTasks;
			String chosenPeer = "";
			for (int i = 0; i < fragments.size(); i++) {
				LinkedList<String> fragment = fragments.get(i);
				min = Integer.MAX_VALUE;
				for (String peer : fragment) {
					if (tasks.get(peer) == null) {
						chosenPeer = peer;
						min = 1;
					} else {
						peerNrTasks = ((LinkedList) tasks.get(peer)).size();
						if (min > peerNrTasks) {
							min = peerNrTasks + 1;
							chosenPeer = peer;
						}
					}
				}
				if (tasks.get(chosenPeer) == null) {
					tasks.put(chosenPeer, new LinkedList());
				}
				((LinkedList) tasks.get(chosenPeer)).add(i);

			}
			/* for each peer create a sepparte download thread */
			LinkedList<Thread> threads = new LinkedList<Thread>();
			int i = 0;
			logger.info("Downloading " + fragments.size() + " fragments from "
					+ tasks.size() + " available peers");
			for (Object key : tasks.keySet()) {
				LinkedList peerTasks = (LinkedList) tasks.get(key);
				StringTokenizer st3 = new StringTokenizer((String) key, ":");
				String peerHost = st3.nextToken();
				int peerPort = Integer.parseInt(st3.nextToken());
				threads.add(new Thread(new ClientPeer(peerHost, peerPort,
						fileName, peerTasks, this)));

			}
			for (i = 0; i < tasks.size(); i++) {
				try {
					threads.get(i).join();
				} catch (Exception exc) {
					logger.severe("An exception occured when trying to join threads");
				}
			}

		} catch (Exception exc) {
			logger.severe("An exception occured when trying to process the response from server");
		}
	}

	public void startClient() {

		/* create a new socket channel */
		try {

			socketChannel = SocketChannel.open();
			if (socketChannel.isOpen()) {
				/* for communicating with the central node set tblocking mode on */
				socketChannel.configureBlocking(true);
				/* set send receive socket buffer */
				socketChannel.setOption(StandardSocketOptions.SO_SNDBUF,
						BYTE_BUFFER_SIZE);
				socketChannel.setOption(StandardSocketOptions.SO_RCVBUF,
						BYTE_BUFFER_SIZE);
				socketChannel.setOption(StandardSocketOptions.SO_LINGER, 10);

				/* establish channel connection */
				socketChannel.connect(new InetSocketAddress(centralNodeHost,
						centralNodePort));
				logger.info("Establish channel connection");
			} else {
				logger.severe("The socket channel cannot be opened");
			}
		} catch (Exception ex) {
			logger.severe("An error occured " + ex.toString());
		}

	}
}

