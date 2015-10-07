package net;

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

	int port;
	final int BYTE_BUFFER_SIZE = 4096;
	final int NR_BYTES_SIZE = 4;
	private ByteBuffer buffer = ByteBuffer.allocate(BYTE_BUFFER_SIZE);
	private Map<SocketChannel, List<byte[]>> keepDataTrack = new HashMap<>();
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
                System.out.println("Wait for connections ..");
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
			
			}
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}
	
	/* new connecton */
	private void acceptOP(SelectionKey key, Selector selector)
			throws IOException {

		ServerSocketChannel serverChannel = (ServerSocketChannel) key.channel();
		SocketChannel socketChannel = serverChannel.accept();
		socketChannel.configureBlocking(false);
		System.out.println("Incoming connection from: "+ socketChannel.getRemoteAddress());
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
				System.out.println("Cannot read from socket");
			}

			if (numRead == -1) {
				this.keepDataTrack.remove(socketChannel);
				System.out.println("Connection closed by: "
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
	private void writeOP(SelectionKey key)  {
	    try
	    {
		SocketChannel socketChannel = (SocketChannel) key.channel();
		List<byte[]> channelData = keepDataTrack.get(socketChannel);
		Iterator<byte[]> its = channelData.iterator();
		RandomAccessFile f;

		StringTokenizer st;
		String fileName;
		int fragment;
		String task;
		/* process all requests from channel queue */
		while (its.hasNext()) {
			byte[] it = its.next();
			its.remove();
			/* extract filename, fragment no and send back the file fragment */
			task = new String(it);
			/* remove trailing grabage characters from message */
			task = task.replaceAll("[^A-Za-z0-9._\\ ]", "");
			String[] tokens = task.split(" ");
			fileName = tokens[1];
			if(tokens[0].equals("size") )
			{
			byte[] bufferRead = new byte[BYTE_BUFFER_SIZE];

	    	long size = new File(fileName).length();
	    	System.out.println("file size "+ size+fileName);	    	
	    	for(int i=4;i<12;i++) {
	    	    bufferRead[i] = (byte)(size >> (i *8));
             bufferRead[0] = 8;


	    	}
 	          	socketChannel.write(ByteBuffer.wrap(bufferRead));	 	    	    
	    	}
	    	else
	    	{
	    	    fragment = Integer.parseInt(tokens[2]);
	    	   fileName = tokens[1];
       			f = new RandomAccessFile(fileName, "r");
    			long positionToJump = (BYTE_BUFFER_SIZE - NR_BYTES_SIZE) * fragment;
	    		f.seek(positionToJump);
	    		//System.out.println("file fragment "+fragment + " "+fileName);
    			byte[] bufferRead = new byte[BYTE_BUFFER_SIZE];
	    		int rd = f.read(bufferRead, 4, BYTE_BUFFER_SIZE - NR_BYTES_SIZE);
	    		for (int i = 0; i < 4; i++) {
	    			bufferRead[i] = (byte) (rd >> (i * 8));

	    		}
	    		socketChannel.write(ByteBuffer.wrap(bufferRead));       			
    			f.close();       			
            }
	    	  		


			
		}

		key.interestOps(SelectionKey.OP_READ);
		}
		catch(Exception exc)
		{
		    exc.printStackTrace();
        }
	
	}
	/* store in a queue the requests for further processing */
	private void doEchoJob(SelectionKey key, byte[] data) {
		SocketChannel socketChannel = (SocketChannel) key.channel();
		List<byte[]> channelData = keepDataTrack.get(socketChannel);
		channelData.add(data);
		key.interestOps(SelectionKey.OP_WRITE);
	}	

}
