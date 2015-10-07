/**
    DUMITRESCU EVELINA Tema3 SPRC
 */

import java.io.*;
import java.net.Socket;
import java.security.*;
import java.security.cert.*;
import java.util.*;
import java.util.logging.Logger;
import javax.crypto.*;
import sun.misc.*;
import javax.net.ssl.*;
import java.security.cert.Certificate;

/*
 class used for handling communication between the client and thed epartment server
 */
class CommunicationHandler implements Runnable {
	private static Logger logger = Logger.getLogger(CommunicationHandler.class
			.getName());
	final int CHUNK_SIZE = 1000;
	private Server server;
	private Socket clientSocket;
	private String clientName;
	private String department;
	BufferedReader reader = null;
	BufferedWriter writer = null;

	private boolean hasToRun = true;

	public CommunicationHandler(Server server, Socket clientSocket,
			String clientName, String department) {
		this.server = server;
		this.clientSocket = clientSocket;
		this.department = department;
		this.clientName = clientName;
		(new Thread(this)).start();
	}

	/*
	 * list files uploaded on server
	 */
	private void listFiles() {
		try {
			if (new File("uploadedfiles").exists()) {

				/*
				 * decrypt the file with the entries
				 */
				this.server.decryptFile("uploadedfiles", "uploadedfiles.temp");

				/*
				 * read the data
				 */
				BufferedReader br = new BufferedReader(new FileReader(
						"uploadedfiles.temp"));
				StringBuilder stringBuilder = new StringBuilder();
				String line;
				int ind;
				while ((line = br.readLine()) != null) {
					ind = line.indexOf('|');
					if (ind > 0) {
						stringBuilder.append(line.substring(0, ind));
						stringBuilder.append("\t");
					}
				}
				br.close();
				/*
				 * encrypt the file and remove the temporary decrypted one
				 */
				this.server.encryptFile("uploadedfiles.temp", "uploadedfiles");
				(new File("uploadedfiles.temp")).delete();
				String listedFiles = stringBuilder.toString();
				this.writer.write(listedFiles);
				this.writer.newLine();
				this.writer.flush();

			} else {
				this.writer.write("No files are uploaded at the moment.");
				this.writer.newLine();
				this.writer.flush();

			}
		} catch (Exception exc) {
			exc.printStackTrace();
			logger.severe(exc.toString());
		}
	}

	/*
	 * upload one file
	 */
	private void uploadFile(String fileName) {
		try {
			char[] buffer = new char[CHUNK_SIZE + 1];
			FileWriter fw = new FileWriter("uploads/" + fileName);
			BufferedWriter bw = new BufferedWriter(fw);
			/*
			 * first get the size of the file, the the content
			 */
			long size = Integer.parseInt(reader.readLine());

			long seek = 0;
			int rd = 0;
			while (seek < size) {
				rd = reader.read(buffer, 0, CHUNK_SIZE);

				buffer[rd] = '\0';
				bw.write(buffer, 0, rd);
				bw.flush();
				seek += rd;
			}

			bw.close();
			logger.info("Uploaded the file " + fileName);
		} catch (Exception exc) {
			exc.printStackTrace();
			logger.severe(exc.toString());
		}

	}

	/*
	 * update the file that keeps all the entries for the uploaded files
	 */
	private boolean updateUploadedFilesFile(String fileName) {

		try {
			boolean acceptTransfer = true;
			if (new File("uploadedfiles").exists()) {
				/*
				 * decrypt the file with the entries
				 */
				this.server.decryptFile("uploadedfiles", "uploadedfiles.temp");
				BufferedReader br = new BufferedReader(new FileReader(
						"uploadedfiles.temp"));
				boolean newFile = true;
				String strLine;
				/*
				 * read all entries
				 */
				BufferedWriter bw = new BufferedWriter(new FileWriter(
						"uploadedfiles.temp2", true));
				while ((strLine = br.readLine()) != null) {

					if (strLine.indexOf("|") > 0) {
						StringTokenizer st = new StringTokenizer(strLine, "|");
						String fileNameEntry = st.nextToken();
						String owner = st.nextToken();
						String department = st.nextToken();
						/*
						 * if the file exists on server, then it can be updated
						 * by clients that belong to the same department as the
						 * owner
						 */
						if ((fileNameEntry.equals(fileName))) {
							newFile = false;
							if (this.department.equals(department) == false) {
								sendMessage("DENIED: File already exists and belongs to a different department. You do not have permission to modify it.");
								acceptTransfer = false;
								logger.info("File"
										+ fileName
										+ "already exists and belongs to a different department. You do not have permission to modify it.");
								bw.write(fileNameEntry + "|" + owner + "|"
										+ department);
								bw.newLine();
								bw.flush();
								break;

							} else {
								sendMessage("ALLOWED: File already exists and belongs to a person from the same department. The file will be replaced. New file owner: "
										+ owner + ".");
								bw.write(fileName + "|" + this.clientName + "|"
										+ this.department);
								bw.newLine();
								bw.flush();
								logger.info("File "
										+ fileName
										+ " already exists and will be replaced.");
							}
							break;
						} else {
							bw.write(fileNameEntry + "|" + owner + "|"
									+ department);
							bw.newLine();
							bw.flush();
						}

					}
				}
				bw.close();
				br.close();
				/*
				 * if the file is new, then a new entry is added
				 */
				if (newFile) {
					sendMessage("ALLOWED: New file has been added to local storage.");
					BufferedWriter bw2 = new BufferedWriter(new FileWriter(
							"uploadedfiles.temp2", true));
					bw2.append(fileName + "|" + this.clientName + "|"
							+ this.department + "\n");
					bw2.close();
					logger.info("The file" + fileName
							+ "has been added to local storage. Current owner "
							+ this.clientName);
				}

				(new File("uploadedfiles.temp")).delete();

			} else {
				(new File("uploadedfiles.temp2")).createNewFile();
				BufferedWriter bw2 = new BufferedWriter(new FileWriter(
						"uploadedfiles.temp2", true));
				bw2.append(fileName + "|" + this.clientName + "|"
						+ this.department + "\n");
				bw2.close();
				sendMessage("ALLOWED: New file has been added to local storage.");
				logger.info("The file" + fileName
						+ "has been added to local storage. Current owner "
						+ this.clientName);
			}
			/*
			 * remove temporary decrypted file
			 */
			this.server.encryptFile("uploadedfiles.temp2", "uploadedfiles");
			(new File("uploadedfiles.temp2")).delete();
			return acceptTransfer;
		}

		catch (Exception exc) {
			exc.printStackTrace();
			logger.severe(exc.toString());
			return false;
		}
	}

	/*
	 * get the department of the owner for a specific file
	 */
	String getDepartment(String fileName) {
		String fileDept = null;
		try {
			this.server.decryptFile("uploadedfiles", "uploadedfiles.temp");
			BufferedReader br = new BufferedReader(new FileReader(
					"uploadedfiles.temp"));
			String strLine;
			while ((strLine = br.readLine()) != null) {

				if (strLine.indexOf("|") > 0) {
					StringTokenizer st = new StringTokenizer(strLine, "|");
					String fileNameEntry = st.nextToken();
					String owner = st.nextToken();
					String department = st.nextToken();
					if ((fileNameEntry.equals(fileName))) {
						fileDept = department;
						break;
					}

				}
			}
			br.close();

			(new File("uploadedfiles.temp")).delete();
			return fileDept;
		} catch (Exception exc) {
			exc.printStackTrace();
			logger.severe(exc.toString());
			return null;
		}
	}

	/*
	 * start downloading a file
	 */
	private void downloadFile(String fileName) {
		try {
			File file = new File("uploads/" + fileName);
			long size = file.length();
			long seek = 0;
			BufferedReader br = new BufferedReader(new FileReader("uploads/"
					+ fileName));
			OutputStream sockOs = this.clientSocket.getOutputStream();
			PrintWriter pw = new PrintWriter(sockOs, true);
			pw.println(size);
			pw.flush();
			char[] buffer = new char[CHUNK_SIZE + 1];
			int rd = 0;
			while ((rd = br.read(buffer, 0, CHUNK_SIZE)) != -1) {

				if (rd > 0) {
					buffer[rd] = '\0';
					pw.write(buffer, 0, rd);
					pw.flush();
					seek += rd;
				}
			}

		} catch (Exception exc) {
			exc.printStackTrace();
			logger.severe(exc.toString());
		}
		logger.info("Downloaded the file " + fileName);
	}

	/*
	 * client sent a close command
	 */
	void close() {
		BufferedWriter writer = null;
		try {
			OutputStream sockOs = this.clientSocket.getOutputStream();
			PrintWriter pw = new PrintWriter(sockOs, true);
			pw.println("bye");
			pw.flush();

			this.clientSocket.close();
			this.server.deleteConnection(this);
		} catch (Exception e) {
			logger.severe("Failed to say goodbye to a client");
			e.printStackTrace();

		}
	}

	/*
	 * process client command
	 */
	private void processRequest(String request) {
		try {
			/*
			 * if the client sent a message with a forbidden word, then it is
			 * banned
			 */
			if ((request.indexOf("bomba") != -1)
					|| (request.indexOf("greva") != -1)
					|| (request.indexOf("virus") != -1)) {

				logger.info("Client '" + this.clientName
						+ "' will be banned for 10 s");
				String message = "BAN" + " " + this.clientName;
				String authResp = this.server.queryAuth(message);
				sendMessage(authResp);
			} else {
				/*
				 * list command
				 */
				if (request.equals("list")) {
					logger.info("Received list command.");
					String authReq = "LIST " + this.clientName;
					String authResp = this.server.queryAuth(authReq);
					sendMessage(authResp);
					if (authResp.indexOf("DENIED") < 0) {
						logger.info("Client is allowed to perform this operation.");
						listFiles();
					} else
						logger.info("Client is not allowed to perform this operation.");

				}
				/*
				 * download command
				 */
				if (request.startsWith("download")) {
					logger.info("Received download command.");
					StringTokenizer st = new StringTokenizer(request, " ");
					st.nextToken();
					String checkFile;
					while (st.hasMoreTokens()) {
						String newFile = st.nextToken();
						String department = getDepartment(newFile);
						String authReq, authResp;
						logger.info("Quering the authorization server ...");
						if ((new File("uploads/" + newFile)).exists() == false) {
							authResp = "DENIED: File " + newFile
									+ " does not exist on server.";
							logger.warning("File " + newFile
									+ " does not exist on server.");
							sendMessage(authResp);
						} else {
							authReq = "DOWNLOAD " + this.clientName + " "
									+ this.department + " " + department;
							authResp = this.server.queryAuth(authReq);

							sendMessage(authResp);

							if (authResp.indexOf("DENIED") < 0) {
								logger.info("Client is allowed to perform this operation.\n"
										+ authResp);
								downloadFile(newFile);

							} else {

								logger.info("Client is not alowed to perform this operation.\n"
										+ authResp);
							}
						}

					}
				}

				/*
				 * upload command
				 */
				if (request.startsWith("upload")) {
					logger.info("Received upload command.");
					StringTokenizer st = new StringTokenizer(request, " ");
					st.nextToken();
					while (st.hasMoreTokens()) {
						String newFile = st.nextToken();
						String authReq = "UPLOAD " + this.clientName;
						String authResp = this.server.queryAuth(authReq);
						sendMessage(authResp);
						if (authResp.indexOf("DENIED") < 0) {
							logger.info("Client is allowed to perform this operation.");
							if (updateUploadedFilesFile(newFile))
								uploadFile(newFile);
						} else {

							logger.info("Client is not alowed to perform this operation.");
						}
					}

				}
				/*
				 * close command
				 */
				if (request.startsWith("close")) {
					close();
					hasToRun = false;
				}

			}
		} catch (Exception exc) {
			exc.printStackTrace();
			logger.severe(exc.toString());
		}

	}

	/*
	 * send a message back to the client
	 */
	private void sendMessage(String line) {
		try {
			this.writer.write(line);
			this.writer.newLine();
			this.writer.flush();
			logger.info("Successfully sent the message <" + line
					+ "> to the associated client");
		} catch (IOException e) {
			logger.severe("Failed to send the message <" + line
					+ "> to the associated client");
			return;
		}

	}

	public void run() {
		logger.info("Creating a writer and a reader for the client socket ...");
		try {
			/*
			 * get reader and writer to communicate over the socket
			 */
			reader = new BufferedReader(new InputStreamReader(
					clientSocket.getInputStream()));
			writer = new BufferedWriter(new OutputStreamWriter(
					clientSocket.getOutputStream()));
		} catch (IOException e) {
			logger.severe("Failed.");
			e.printStackTrace();
			return;
		}
		logger.info("Succeded.");
		String line = null;
		while (hasToRun) {
			try {
				/*
				 * read one line and process it
				 */
				line = reader.readLine();
				processRequest(line);
			} catch (IOException e) {
				logger.info("An exception occured when trying to read a new message from socket.");
				e.printStackTrace();
				break;
			}
			/*
			 * end of input has been reached
			 */
			if (line == null) {
				try {
					this.clientSocket.close();
				} catch (IOException e) {
					logger.info("Failed to close the client socket");
					e.printStackTrace();
				}
				logger.severe("End of the input stream.");
				break;
			}

		}
		this.server.deleteConnection(this);
	}

}

/*
 * class for a department server
 */
public class Server implements Runnable {

	private static Logger logger = Logger.getLogger(Server.class.getName());
	/*
	 * port used to connect to the authorization server
	 */
	private static int authorizationServerPort;
	/*
	 * name of the department server
	 */
	private String name;
	/*
	 * port of the department server
	 */
	private int port;
	/*
	 * authorization server hostname
	 */
	private String authHostname;
	/*
	 * department server keystore
	 */
	private String keyStoreName;
	/*
	 * keystore password
	 */
	private String password;

	private KeyStore serverKeyStore = null;
	private SSLContext sslContext = null;
	private SSLServerSocket serverSocket = null;
	/*
	 * encryption and decryption ciphers
	 */
	private Cipher cipherDecrypt;
	private Cipher cipherEncrypt;
	private LinkedList<CommunicationHandler> connections = null;
	private Key key;
	final int CHUNK_SIZE = 1000;

	public Server(String name, int port, String hostname, int authPort) {
		this.name = name;
		this.port = port;
		this.authHostname = hostname;
		this.authorizationServerPort = authPort;
		this.keyStoreName = "security/" + name + "/" + name + ".ks";
		this.password = name + "_password";
		this.connections = new LinkedList<CommunicationHandler>();
	}

	/*
	 * encrypt a file using base 64
	 */
	public void encryptFile(String fileIn, String fileOut) {
		try {
			byte[] stringBytes;
			byte[] raw;
			BufferedReader br = new BufferedReader(new FileReader(fileIn));
			BufferedWriter bw = new BufferedWriter(new FileWriter(fileOut));
			String strLine;
			/*
			 * read file line by line encrypt each line in base 64 format
			 */
			while ((strLine = br.readLine()) != null) {
				stringBytes = strLine.getBytes("UTF8");
				raw = cipherEncrypt.doFinal(stringBytes);
				BASE64Encoder encoder = new BASE64Encoder();
				String base64 = encoder.encode(raw);
				bw.write(base64);
				bw.newLine();
			}

			br.close();
			bw.close();
		} catch (Exception exc) {
			exc.printStackTrace();
			logger.severe(exc.toString());
		}
	}

	/*
	 * decrypt a file
	 */
	public void decryptFile(String fileIn, String fileOut) {

		try {
			BufferedReader br = new BufferedReader(new FileReader(fileIn));
			BufferedWriter bw = new BufferedWriter(new FileWriter(fileOut));
			BASE64Decoder decoder = new BASE64Decoder();
			byte[] raw;
			byte[] stringBytes;
			String strLine;
			String result;
			/*
			 * read the file line by line decrypt the specific line and write it
			 * back in the file with decrypted content
			 */
			while ((strLine = br.readLine()) != null) {

				raw = decoder.decodeBuffer(strLine);
				stringBytes = cipherDecrypt.doFinal(raw);
				result = new String(stringBytes, "UTF8");
				bw.write(result);
				bw.newLine();

			}
			br.close();
			bw.close();
		} catch (Exception e) {
			e.printStackTrace();
			logger.severe(e.toString());

		}
	}

	/*
	 * generate DES key, cipher for encryption/decryption
	 */
	private void generateKey() {
		try {
			if ((new File("SecretKey.ser")).exists()) {
				ObjectInputStream in = new ObjectInputStream(
						new FileInputStream("SecretKey.ser"));
				key = (Key) in.readObject();
				in.close();
			} else {
				KeyGenerator generator = KeyGenerator.getInstance("DES");
				generator.init(new SecureRandom());
				key = generator.generateKey();
				ObjectOutputStream out = new ObjectOutputStream(
						new FileOutputStream("SecretKey.ser"));
				out.writeObject(key);
				out.close();
			}
			cipherEncrypt = Cipher.getInstance("DES/ECB/PKCS5Padding");
			cipherDecrypt = Cipher.getInstance("DES/ECB/PKCS5Padding");
			cipherEncrypt.init(Cipher.ENCRYPT_MODE, key);
			cipherDecrypt.init(Cipher.DECRYPT_MODE, key);
		} catch (Exception exc) {
			exc.printStackTrace();
			logger.severe(exc.toString());
		}
	}

	/*
	 * make a new key store
	 */
	private void createKeyStore() {
		logger.info("Obtaining a reference to the client's keystore");
		String keyStoreName = "security/" + this.name + "/" + this.name + ".ks";
		;
		String password = this.name + "_password";
		try {
			this.serverKeyStore = KeyStore.getInstance("JKS");
			FileInputStream is = new FileInputStream(keyStoreName);
			this.serverKeyStore.load(is, password.toCharArray());
		} catch (Exception e) {
			logger.severe("Failed");
			e.printStackTrace();
		}
		logger.info("Succeeded.");

	}

	/*
	 * crate key manager factory instance
	 */
	/*
	 * crate key manager factory instance
	 */
	private KeyManagerFactory createKeyManagerFactory() {
		logger.info("Obtainig a KeyManagerFactory ...");
		KeyManagerFactory keyManagerFactory = null;
		try {
			if (System.getProperty("java.vm.vendor").toLowerCase()
					.indexOf("ibm") != -1) {
				keyManagerFactory = KeyManagerFactory.getInstance("IBMX509",
						"IBMJSSE");
			} else {
				keyManagerFactory = KeyManagerFactory.getInstance("SunX509");
			}
		} catch (Exception e) {
			logger.severe("Failed.");
			e.printStackTrace();
		}
		logger.info("Succeeded.");
		logger.info("Initializing the KeyManagerFactory ...");
		try {
			String password = this.name + "_password";
			keyManagerFactory.init(this.serverKeyStore, password.toCharArray());
		} catch (Exception e) {
			logger.severe("Failed.");
			e.printStackTrace();
		}
		logger.info("Succeeded.");
		return keyManagerFactory;
	}

	/*
	 * get trust manager factory instance
	 */
	private TrustManagerFactory createTrustManagerFactory() {
		logger.info("Obtainig a TrustManagerFactory ...");
		TrustManagerFactory trustManagerFactory = null;
		try {
			if (System.getProperty("java.vm.vendor").toLowerCase()
					.indexOf("ibm") != -1) {
				trustManagerFactory = TrustManagerFactory.getInstance(
						"IBMX509", "IBMJSSE");
			} else {
				trustManagerFactory = TrustManagerFactory
						.getInstance("SunX509");
			}
			logger.info("Succeeded.");
		} catch (Exception e) {
			logger.severe("Failed.");
			e.printStackTrace();
		}
		logger.info("Initializing the TrustManagerFactory ...");
		try {
			trustManagerFactory.init(this.serverKeyStore);
			logger.info("Succeeded.");
		} catch (Exception e) {
			logger.severe("Failed.");
			e.printStackTrace();
		}
		return trustManagerFactory;
	}

	/*
	 * create new ssl context
	 */
	private void createSSLContext() {
		logger.info("Initializing SSLContext ...");
		KeyManagerFactory keyManagerFactory = null;
		TrustManagerFactory trustManagerFactory = null;
		try {
			trustManagerFactory = createTrustManagerFactory();
			keyManagerFactory = createKeyManagerFactory();
			this.sslContext = SSLContext.getInstance("TLS");
			this.sslContext.init(keyManagerFactory.getKeyManagers(),
					trustManagerFactory.getTrustManagers(), null);
			logger.info("Succeeded.");
		} catch (Exception e) {
			logger.severe("Failed.");
			e.printStackTrace();
		}
	}

	/*
	 * query authorization server and wait for a response
	 */
	String queryAuth(String message) {
		/*
		 * connect to the authorization server
		 */
		SSLSocket auth = null;
		logger.info("Creating a SSL socket to communicate with the authentication server ...");
		try {
			SSLSocketFactory ssf = sslContext.getSocketFactory();
			auth = (SSLSocket) ssf.createSocket(this.authHostname,
					Server.authorizationServerPort);
		} catch (Exception e) {
			logger.severe("Failed.");
			e.printStackTrace();
			return null;
		}
		logger.info("Succeeded.");
		logger.info("Creating reader and writer for suthetication server socket ...");
		BufferedReader authReader = null;
		BufferedWriter authWriter = null;
		/*
		 * create a writer/reader to communicate over the soket
		 */
		try {
			authReader = new BufferedReader(new InputStreamReader(
					auth.getInputStream()));
			authWriter = new BufferedWriter(new OutputStreamWriter(
					auth.getOutputStream()));
		} catch (Exception e) {
			logger.severe("Failed.");
			e.printStackTrace();
			return null;
		}
		logger.info("Succeeded.");
		try {
			/*
			 * send the message about the client to the authentication server
			 */
			authWriter.write(message);
			authWriter.newLine();
			authWriter.flush();
		} catch (IOException e) {
			logger.severe("[Failed.");
			e.printStackTrace();
			return null;
		}
		logger.info("Succeeded.");
		/*
		 * wait for the response
		 */
		String response = null;
		try {
			response = authReader.readLine();
		} catch (IOException e) {
			logger.severe("Failed.");
			e.printStackTrace();
			return null;
		}
		logger.info("Succeeded.");
		return response;
	}

	public void run() {
		generateKey();
		File uploadsDir = new File("uploads");
		if (uploadsDir.exists() == false)
			uploadsDir.mkdir();
		/*
		 * if the uploads directory does not exist, create it
		 */
		if (!uploadsDir.exists()) {
			uploadsDir.mkdir();
		}
		createKeyStore();
		createSSLContext();
		logger.info("Creating a SSL server socket ..");
		try {
			SSLServerSocketFactory ssf = sslContext.getServerSocketFactory();
			this.serverSocket = (SSLServerSocket) ssf
					.createServerSocket(this.port);
		} catch (Exception e) {
			logger.severe("Failed.");
			e.printStackTrace();
			return;
		}
		logger.info("Succeeded.");
		logger.info("Listening for incoming connections ...");
		serverSocket.setNeedClientAuth(true);
		logger.info("Getting CA certificate ...");
		Certificate CACertificate = null;
		/*
		 * get the certificate of this client's certification authority
		 */
		try {

			CACertificate = this.serverKeyStore
					.getCertificate("certification_authority");
			logger.info("Succeeded.");
		} catch (Exception e) {
			logger.severe("Failed.");
			e.printStackTrace();
		}
		while (true) {
			try {
				/*
				 * wait for incomming connections from clients
				 */
				Socket client = serverSocket.accept();
				logger.info("A new client connection has been accepted.");
				/*
				 * do a handshake, check if
				 */
				((SSLSocket) client).startHandshake();
				logger.info("Handshake succeeded.");
				X509Certificate[] peerCertificates;
				/*
				 * see if the CA certificate of the peers match
				 */
				peerCertificates = (X509Certificate[]) (((SSLSocket) client)
						.getSession()).getPeerCertificates();
				if (CACertificate.equals(peerCertificates[1])) {
					logger.info("The  CA certificate of the client matches the CA certificate of the server.");
				} else {
					logger.severe("The  CA certificate of the client does not match the CA certificate of the server.");
				}
				logger.info("Succeeded.");
				/*
				 * extract the client CN and OU from the certificate
				 */
				String clientDN = peerCertificates[0].getSubjectX500Principal()
						.getName();
				StringTokenizer st = new StringTokenizer(clientDN, " \t\r\n,=");
				String clientCN = null;
				String clientOU = null;
				if (st.hasMoreTokens())
					st.nextToken();
				if (st.hasMoreTokens())
					clientCN = st.nextToken();
				if (st.hasMoreTokens())
					st.nextToken();
				if (st.hasMoreTokens())
					clientOU = st.nextToken();
				if (clientCN == null || clientOU == null) {
					logger.severe("clientCN or clientOU are null.");
					continue;
				}
				connections.add(new CommunicationHandler(this, client,
						clientCN, clientOU));
			} catch (Exception e) {
				logger.severe("Something bad happened while handling the communication with the client.");
				e.printStackTrace();
				continue;
			}
		}
	}

	public void deleteConnection(CommunicationHandler CommunicationHandler) {
		connections.remove(CommunicationHandler);
	}

	public static void main(String args[]) {
		if (args.length != 3) {
			System.err
					.println("Usage: java Server department_name port authentication_server_hostname");
			System.exit(1);
		}
		(new Thread(new Server(args[0], Integer.parseInt(args[1]), args[2],
				7000))).start();
	}
}
