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

/* client class used to send I/O requests to other peers */
class ClientPeer implements Runnable {
	ByteBuffer receivingBufferPeer;

	final int BYTE_BUFFER_SIZE = 4096;
	final int NR_BYTES_SIZE = 4;
	int remotePort;
	String downloadFile;
	long startFragment;
	public ClientPeer(int remotePort, String downloadFile, long fragment) {
		this.remotePort = remotePort;
		this.downloadFile = downloadFile;
		this.receivingBufferPeer = ByteBuffer.allocate(BYTE_BUFFER_SIZE);
        this.startFragment = fragment;
		(new Thread(this)).start();
	}

	public void run() {
		try {

			/* open Selector and ServerSocketChannel */
			Selector selector = Selector.open();
			SocketChannel socketChannel = SocketChannel.open();
		
			// check if both of them are opened
			if ((socketChannel.isOpen()) && (selector.isOpen())) {

				/* non-blocking mode */
				socketChannel.configureBlocking(false);

				/* register the channel */
				socketChannel.register(selector, SelectionKey.OP_CONNECT);

				/* connect to remote host */
				socketChannel.connect(new java.net.InetSocketAddress(
						"localhost", remotePort));

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

								/* close pendent connections */
								if (keySocketChannel.isConnectionPending()) {
									keySocketChannel.finishConnect();
								}

                                    RandomAccessFile f = new RandomAccessFile(
										this.downloadFile + "2", "rw");
								
									byte[] message = new byte[BYTE_BUFFER_SIZE];
									byte[] messageBytes = ("size "+this.downloadFile ).getBytes();
									System.arraycopy(messageBytes, 0, message,
											0, messageBytes.length);

									/* send the request to the remote peer */
									ByteBuffer sendingBuffer = ByteBuffer
											.wrap(message);
									keySocketChannel.write(sendingBuffer);
									int numRead = 0;
									int numBytes = 0;
									long fileSize = 0;
									long numBytesReceived = 0;
									receivingBufferPeer.clear();

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

                                    fileSize = ((0xFF & data[11]) << 54)
                                            + ((0xFF & data[10]) << 48) 
                                            + ((0xFF & data[9]) << 40)
                                            + ((0xFF & data[8]) << 32)
                                            + ((0xFF & data[7]) << 24)
											+ ((0xFF & data[6]) << 16)
											+ ((0xFF & data[5]) << 8)
											+ (0xFF & data[4]);
									System.out.println("Am primit "+numBytes+" "+ fileSize);																				
    								
    							long fragmentNo = fileSize/(long)4092;
    							if(fileSize % 4092 != 0) fragmentNo++;

	                           for (;startFragment< fragmentNo; startFragment++) {
									message = new byte[BYTE_BUFFER_SIZE];
									messageBytes = ("fragment "+this.downloadFile
											+ " " +  startFragment).getBytes();
									System.arraycopy(messageBytes, 0, message,
											0, messageBytes.length);

									ByteBuffer sendingBuffer2 = ByteBuffer
											.wrap(message);
									keySocketChannel.write(sendingBuffer2);
									
									long positionToJump = (BYTE_BUFFER_SIZE - NR_BYTES_SIZE)
											* ((long)startFragment);
									f.seek(positionToJump);
									receivingBufferPeer.clear();
								
                                    numRead = 0;
									while ((numRead += keySocketChannel
											.read(receivingBufferPeer)) < BYTE_BUFFER_SIZE);
									receivingBufferPeer.flip();
									data = new byte[numRead];
									System.arraycopy(
											receivingBufferPeer.array(), 0,
											data, 0, numRead);
								
									numBytes = ((0xFF & data[3]) << 24)
											+ ((0xFF & data[2]) << 16)
											+ ((0xFF & data[1]) << 8)
											+ (0xFF & data[0]);

									f.write(data, 4, numBytes);
								
									
									
									if (receivingBufferPeer.hasRemaining()) {
										receivingBufferPeer.compact();
									} else {
										receivingBufferPeer.clear();
									}
									System.out.println("ma cac pe IDP"); 
								}
								f.close();   								
								}

							
						} catch (IOException ex) {
							ex.printStackTrace();

						}
					}
				}
			 

		}
		} catch (Exception exc) {
			exc.printStackTrace();
		}
	}
}


