/*
    DUMITRESCU EVELINA 341C3 Tema4 SPRC
 */
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.StandardSocketOptions;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.util.*;
import java.util.logging.Logger;

/*class used to handle I/O message exchaging*/
class CommunicationHandler implements Runnable {

	private CentralNode server;
	private SocketChannel socketChannel;
	final int BYTE_BUFFER_SIZE = 4096;

	public CommunicationHandler(CentralNode server, int index) {
		this.server = server;
		this.socketChannel = (SocketChannel) this.server.openedConnections
				.get(index);
		(new Thread(this)).start();
	}
	/*process peer incoming request*/
	public String processRequest(String request) {
		String response = "";
		try {
            /*upload message type*/
			if (request.startsWith("upload")) {

				StringTokenizer st = new StringTokenizer(request, " ");
				st.nextToken();
				String fileName = st.nextToken();
				long fileSize = Long.parseLong(st.nextToken());
				long fragmentSize = Long.parseLong(st.nextToken());
				long nrFragments = fileSize / fragmentSize
						+ (((fileSize % fragmentSize) == 0) ? 0 : 1);
				/*add peer for the uploaded file entry*/		
				String peerServer = st.nextToken();
				String portServer = st.nextToken();
				String entry = peerServer + ":" + portServer;
				LinkedList peers = (LinkedList) this.server.uploadedFiles
						.get(fileName);
				if (peers == null) {
					peers = new LinkedList();
					for (int i = 0; i < nrFragments; i++) {
						peers.add(i, new LinkedList());
					}
					this.server.uploadedFiles.put(fileName, peers);
				}

				for (int i = 0; i < nrFragments; i++) {

					if (((LinkedList) peers.get(i)).indexOf(entry) == -1)
						((LinkedList) peers.get(i)).add(entry);

				}
				CentralNode.logger.info("Received upload request for "
						+ fileName + " from "
						+ socketChannel.getRemoteAddress());
            /*download message type*/
			} else if (request.startsWith("download")) {
				StringTokenizer st = new StringTokenizer(request, " ");
				st.nextToken();
				String fileName = st.nextToken();
				/*return a list for each file fragment with the peers that have the file*/
				LinkedList peers = (LinkedList) this.server.uploadedFiles
						.get(fileName);
				response = peers.toString();
				CentralNode.logger.info("Received download request for "
						+ fileName + " from "
						+ socketChannel.getRemoteAddress());
			/*own message type*/
			} else if (request.startsWith("own")) {
                /*a client just finished downloading the fragment*/
				StringTokenizer st = new StringTokenizer(request, " ");
				st.nextToken();
				String fileName = st.nextToken();
				int fragmentNr = Integer.parseInt(st.nextToken());
				String peerServer = st.nextToken();
				String portServer = st.nextToken();
				String entry = peerServer + ":" + portServer;
				LinkedList fragmentPeers = (LinkedList) ((LinkedList) this.server.uploadedFiles
						.get(fileName)).get(fragmentNr);
				if (fragmentPeers.indexOf(fileName) == -1)
					fragmentPeers.add(entry);
				CentralNode.logger.info("Received \"own\" request for "
						+ fileName + " fragment number " + fragmentNr
						+ " from " + socketChannel.getRemoteAddress());
			}

			else {
				CentralNode.logger.warning("Received unknown request from "
						+ socketChannel.getRemoteAddress());
			}
		} catch (Exception ex) {
			CentralNode.logger
					.severe("An error occurred while processing the request.");
		}
		return response;
	}
	public void run() {
		try {

			ByteBuffer incomingBuffer = ByteBuffer
					.allocateDirect(BYTE_BUFFER_SIZE);
			ByteBuffer outgoingBuffer;
			Charset charset = Charset.defaultCharset();
			CharsetDecoder decoder = charset.newDecoder();
			/*read a message*/
			while (socketChannel.read(incomingBuffer) != -1) {
				incomingBuffer.flip();
				String msgReceived = decoder.decode(incomingBuffer).toString();
				if (incomingBuffer.hasRemaining()) {
					incomingBuffer.compact();
				} else {
					incomingBuffer.clear();
				}
				/*process it*/
				String response = processRequest(msgReceived);
				/*if the message is download type*/
				if (response.equals("") == false) {
					byte[] data = response.getBytes();
					byte[] chunk;
					int limit;
					/*send the list of peers*/
					for (int i = 0; i < data.length; i += BYTE_BUFFER_SIZE) {
						if ((i + BYTE_BUFFER_SIZE) >= data.length)
							limit = data.length % BYTE_BUFFER_SIZE;
						else {
							limit = BYTE_BUFFER_SIZE;
						}
						chunk = new byte[BYTE_BUFFER_SIZE];
						System.arraycopy(data, i, chunk, 0, limit);
						outgoingBuffer = ByteBuffer.wrap(chunk);
						socketChannel.write(outgoingBuffer);
						CentralNode.logger
								.info("Send back response for download request to "
										+ socketChannel.getRemoteAddress());
						outgoingBuffer.flip();
					}
				}
			}
		} catch (Exception exc) {
			exc.printStackTrace();
		}

	}
}
public class CentralNode implements Runnable {

	int port;
	String ip;
	SocketChannel socketChannel;
	LinkedList openedConnections;
	int index = 0;
	final int BYTE_BUFFER_SIZE = 4096;
	LinkedHashMap uploadedFiles;
	static Logger logger = Logger.getLogger(CentralNode.class.getName());
	public CentralNode(int port) {
		this.port = port;
		this.ip = "127.0.0.1";
		openedConnections = new LinkedList();
		uploadedFiles = new LinkedHashMap<String, LinkedList>();
	}
	public void run() {
		try  {
            /*open socket chanel and wait for incoming connections*/        
            ServerSocketChannel serverSocketChannel = ServerSocketChannel
				.open();		
			if (serverSocketChannel.isOpen()) {
				serverSocketChannel.configureBlocking(true);

				/* set socket buffer receive option */
				serverSocketChannel.setOption(StandardSocketOptions.SO_RCVBUF,
						BYTE_BUFFER_SIZE);
				serverSocketChannel.setOption(
						StandardSocketOptions.SO_REUSEADDR, true);

				/* bind the server socket channel to the address specified */
				serverSocketChannel.bind(new InetSocketAddress(this.ip,
						this.port));
				CentralNode.logger.info("Listening on " + this.ip + " "
						+ this.port);
				while (true) {
					try {
						socketChannel = serverSocketChannel.accept();
						openedConnections.add(socketChannel);
						/*for each connection create a new thread */
						new CommunicationHandler(this, index);
						index++;
						CentralNode.logger.info("Accepted new connection from "
								+ socketChannel.getRemoteAddress());

					} catch (IOException ex) {
						ex.printStackTrace();
					}
				}
			} else {
				CentralNode.logger
						.severe("The server socket channel cannot be opened");
			}
		} catch (IOException ex) {
			CentralNode.logger
					.severe("An error occurred when trying to open the server socket channel");
		}

	}
	public static void main(String[] args) {
		(new Thread(new CentralNode(Integer.parseInt(args[0])))).start();

	}
}

