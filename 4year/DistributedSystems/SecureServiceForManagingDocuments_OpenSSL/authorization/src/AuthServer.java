/*
    DUMITRESCU EVELINA Tema3 SPRC
 */

import java.io.*;
import java.security.*;
import java.security.cert.*;
import java.util.Date;
import java.util.StringTokenizer;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TimerTask;
import java.util.Timer;
import java.util.logging.Logger;
import javax.crypto.*;
import sun.misc.*;
import javax.net.ssl.*;
import java.security.cert.Certificate;

/*
 Class used to perform client bann tasks
 */
class BannedTimerTask extends TimerTask {
	private AuthServer server = null;
	private String clientName = null;

	public BannedTimerTask(AuthServer server, String clientName) {
		this.server = server;
		this.clientName = clientName;
	}

	public void run() {

		this.server.logger.info("Removed the ban for " + this.clientName);
		try {
			/*
			 * update the file with the banned clients
			 */
			if (new File("banned_encrypted").exists() == false)
				(new File("banned_encrypted")).createNewFile();
			/*
			 * decrypt file
			 */
			this.server.decryptFile("banned_encrypted", "banned_decrypted");
			BufferedReader br = new BufferedReader(new FileReader(
					"banned_decrypted"));
			String strLine;
			BufferedWriter bw = new BufferedWriter(new FileWriter(
					"banned_decrypted2", true));
			/*
			 * rewrite the file without the specified client
			 */
			while ((strLine = br.readLine()) != null) {
				if (strLine.indexOf(clientName) < 0) {
					bw.write(strLine);
					bw.newLine();
					bw.flush();
				}
			}
			bw.close();
			this.server.encryptFile("banned_decrypted2", "banned_encrypted");

		} catch (Exception e) {
			this.server.logger
					.warning("An exception was caught while trying to remove '"
							+ clientName + "' from the banned list");
			e.printStackTrace();
			return;
		}
		/*
		 * remove decrypted file
		 */
		File temp = new File("banned_decrypted");
		temp.delete();
		temp = new File("banned_decrypted2");
		temp.delete();
		return;
	}
}

public class AuthServer implements Runnable {
	static Logger logger = Logger.getLogger(AuthServer.class.getName());
	/*
	 * map with known departments and priorities
	 */
	private LinkedHashMap<String, Integer> priorities;
	/*
	 * server keystore
	 */
	private KeyStore serverKeyStore = null;
	private SSLContext sslContext = null;
	private SSLServerSocket serverSocket = null;
	/*
	 * decryption/encryption ciphers
	 */
	private Cipher cipherDecrypt;
	private Cipher cipherEncrypt;
	/*
	 * server key
	 */
	private Key key;
	final int CHUNK_SIZE = 1000;

	public AuthServer() {
		/*
		 * include known departments and priorities
		 */
		this.priorities = new LinkedHashMap<String, Integer>();
		this.priorities.put("HR", 1);
		this.priorities.put("ACCOUNTING", 1);
		this.priorities.put("IT", 2);
		this.priorities.put("MANAGEMENT", 3);

	}

	/*
	 * encrypt the banned clients file in base 64 format
	 */
	public void encryptFile(String fileIn, String fileOut) {
		try {
			byte[] stringBytes;
			byte[] raw;
			BufferedReader br = new BufferedReader(new FileReader(fileIn));
			BufferedWriter bw = new BufferedWriter(new FileWriter(fileOut));
			String strLine;
			/*
			 * Read File Line By Line
			 */
			while ((strLine = br.readLine()) != null) {
				stringBytes = strLine.getBytes("UTF8");
				raw = cipherEncrypt.doFinal(stringBytes);
				BASE64Encoder encoder = new BASE64Encoder();
				String base64 = encoder.encode(raw);
				bw.write(base64);
				bw.newLine();
			}
			/*
			 * Close the input stream
			 */
			br.close();
			bw.close();
		} catch (Exception exc) {
			exc.printStackTrace();
		}
	}

	/*
	 * decrypt file
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
			 * Read File Line By Line
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
			System.err.println("Failed to decrypt the information");
			e.printStackTrace();

		}
	}

	/*
	 * generate Key , encrption and decryption ciphers
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
	 * create server key store
	 */
	private void createKeyStore() {
		logger.info("Obtaining a reference to the client's keystore");
		String keyStoreName = "security/authorization_server.ks";
		String password = "authorization_server_password";
		try {
			this.serverKeyStore = KeyStore.getInstance("JKS");
			FileInputStream is = new FileInputStream(keyStoreName);
			this.serverKeyStore.load(is, password.toCharArray());
			logger.info("Succeeded.");
		} catch (Exception e) {
			logger.severe("Failed");
			e.printStackTrace();
		}

	}

	/*
	 * create key manager factory
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
			logger.info("Succeeded.");
		} catch (Exception e) {
			logger.severe("Failed.");
			e.printStackTrace();

		}

		logger.info("Initializing the KeyManagerFactory ...");
		try {
			String password = "authorization_server_password";
			keyManagerFactory.init(this.serverKeyStore, password.toCharArray());
			logger.info("Succeeded.");
		} catch (Exception e) {
			logger.severe("Failed.");
			e.printStackTrace();

		}

		return keyManagerFactory;
	}

	/*
	 * create trust key manager factory
	 */
	private TrustManagerFactory createTrustKeyManagerFactory() {

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
			// initialize the trust manager factory with a source of key
			// material
			trustManagerFactory.init(this.serverKeyStore);
			logger.info("Succeeded.");
		} catch (Exception e) {
			logger.severe("Failed.");
			e.printStackTrace();

		}

		return trustManagerFactory;
	}

	/*
	 * create ssl context
	 */
	private void createSSLContext() {
		logger.info("Initializing SSLContext ...");
		TrustManagerFactory trustManagerFactory = null;
		KeyManagerFactory keyManagerFactory = null;
		try {
			/*
			 * set the SSL context
			 */
			trustManagerFactory = createTrustKeyManagerFactory();
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
	 * used to check if the client is banned
	 */
	private boolean isBanned(String clientName) {
		byte[] buffer = new byte[1024];
		boolean result;
		try {
			if (new File("banned_encrypted").exists() == false)
				(new File("banned_encrypted")).createNewFile();
			/*
			 * decrypt file
			 */
			decryptFile("banned_encrypted", "banned_decrypted");
			FileInputStream in = new FileInputStream("banned_decrypted");
			in.read(buffer);
			in.close();
			String info = new String(buffer);
			if (info.indexOf(clientName) == -1) {
				result = false;
			} else {
				result = true;
			}
		} catch (Exception e) {
			logger.severe("Failed to find out if client '" + clientName
					+ "' is banned or not");
			e.printStackTrace();
			return false;
		}
		/*
		 * delete the decrypted file
		 */
		File temp = new File("banned_decrypted");
		temp.delete();
		return result;
	}

	/*
	 * adds a client into the file with banned clients
	 */
	private void ban(String clientName) {
		try {
			decryptFile("banned_encrypted", "banned_decrypted");
			FileOutputStream out = new FileOutputStream("banned_decrypted",
					true);
			String line = clientName + " " + System.currentTimeMillis() + "\n";
			/* add new entry to file */
			out.write(line.getBytes());
			out.close();
			/*
			 * delete old encrypted file
			 */
			File file = new File("banned_encrypted");
			file.delete();
			/*
			 * encrypt the new file
			 */
			if (new File("banned_encrypted").createNewFile() == false) {
				logger.severe("Failed to recreate the banned_encrypted file when banning the client "
						+ clientName);
				return;
			}
			encryptFile("banned_decrypted", "banned_encrypted");
			/*
			 * temporary ban for 10 s
			 */
			int after = 10000;
			Date timeToRun = new Date(System.currentTimeMillis() + after);
			Timer timer = new Timer();
			timer.schedule(new BannedTimerTask(this, clientName), timeToRun);

		} catch (Exception e) {
			logger.severe("Failed to ban the client '" + clientName + "'");
			e.printStackTrace();
			return;
		}
		/*
		 * delete the decrypted file
		 */
		File temp = new File("banned_decrypted");
		temp.delete();
		return;
	}

	/*
	 * process a request received from a department server
	 */
	private String processRequest(String request) {
		StringTokenizer st = new StringTokenizer(request, " ");
		int count = 0;
		/*
		 * known operatons: BAN <clientName> UPLOAD <clientName> DOWNLOAD
		 * <clientName> <clientDep> <otherDept>
		 */
		String token1 = null, token2 = null, token3 = null, token4 = null;
		if (st.hasMoreTokens())
			token1 = st.nextToken();
		if (st.hasMoreTokens())
			token2 = st.nextToken();
		if (st.hasMoreTokens())
			token3 = st.nextToken();
		if (st.hasMoreTokens())
			token4 = st.nextToken();
		if (token1.compareTo("BAN") == 0) {
			logger.info("A BAN request has been received");
			ban(token2);
			return "DENIED: Client performed an ilegal operation and will be banned.";
		} else if (token1.equals("LIST") || token1.equals("UPLOAD")) {
			if (isBanned(token2))
				return "DENIED: Client is temporarily banned.";
			else
				return "ALLOWED: Authorization succeded.";
		} else if (token1.equals("DOWNLOAD")) {
			Integer otherClientPriority = this.priorities.get(token4);
			Integer clientPriority = this.priorities.get(token3);
			if ((isBanned(token2) == false)) {
				if (otherClientPriority > clientPriority)
					return "DENIED: You are not authorized to download the file.";
				return "ALLOWED: Authorization succeded.";
			} else {
				return "DENIED: Client is temporarily banned.";
			}
		}
		return "ALLOWED";
	}

	/*
	 * receive a new request from the department server
	 */
	private String receiveRequest(BufferedReader reader, SSLSocket socket) {
		logger.severe("Waiting for a new request.");
		String request = null;
		try {
			request = reader.readLine();
		} catch (IOException e) {
			logger.severe("Failed.");
			try {
				socket.close();
			} catch (IOException ioe) {
				logger.severe("Failed.");
				ioe.printStackTrace();
			}

		}
		if (request == null) {
			logger.severe("The end of the input stream has been reached");
			try {
				socket.close();
			} catch (IOException ioe) {
				logger.severe("Failed.");
				ioe.printStackTrace();
			}
		}
		logger.info("Received request: " + request);
		return request;
	}

	/*
	 * send back authorization server response to department server
	 */
	private void sendResponse(BufferedWriter writer, String response,
			SSLSocket socket) {
		try {
			writer.write(response);
			writer.newLine();
			writer.flush();
		} catch (IOException e) {
			logger.severe("Failed.");
			e.printStackTrace();
			try {
				socket.close();
			} catch (IOException ioe) {
				logger.severe("Failed to close the socket");
				ioe.printStackTrace();
			}

		}
		logger.info("Send response: " + response);
	}

	public void run() {
		/*
		 * generate server key
		 */
		generateKey();
		/*
		 * generate key store
		 */
		createKeyStore();
		/*
		 * initialize SSL context
		 */
		createSSLContext();
		logger.info("Creating a SSL server socket ..");
		try {
			SSLServerSocketFactory ssf = sslContext.getServerSocketFactory();
			this.serverSocket = (SSLServerSocket) ssf.createServerSocket(7000);
			logger.info("Succeeded.");
		} catch (Exception e) {
			logger.severe("Failed.");
			e.printStackTrace();
			return;
		}
		logger.info("Listening for incoming connections ...");
		serverSocket.setNeedClientAuth(true);

		while (true) {

			SSLSocket socket = null;
			try {
				/*
				 * waiting for a new connection
				 */
				socket = (SSLSocket) this.serverSocket.accept();
				logger.info("A new client connection has been accepted.");
				/*
				 * do the handshake between peers
				 */
				socket.startHandshake();
				logger.info("Handshake succeeded.");
				logger.info("Getting CA certificate ...");
				Certificate CACertificate = null;
				try {
					/*
					 * get the certificate of this client CA
					 */
					CACertificate = this.serverKeyStore
							.getCertificate("certification_authority");
					logger.info("Succeeded.");
				} catch (Exception e) {
					logger.severe("Failed.");
					e.printStackTrace();

				}

				X509Certificate[] peerCertificates = null;
				peerCertificates = (X509Certificate[]) (socket.getSession())
						.getPeerCertificates();
				if (CACertificate.equals(peerCertificates[1])) {
					logger.info("End point peers have the same CA certificate.");
				} else {
					logger.severe("End point peers do not have the same CA certificate.");
					continue;
				}
			} catch (Exception exc) {
				exc.printStackTrace();
				logger.severe(exc.toString());
			}
			logger.info("Creating a writer and a reader for the client socket ...");
			/*
			 * wait for department server request and send back the response
			 */
			BufferedReader reader = null;
			BufferedWriter writer = null;
			try {
				reader = new BufferedReader(new InputStreamReader(
						socket.getInputStream()));
				writer = new BufferedWriter(new OutputStreamWriter(
						socket.getOutputStream()));
				logger.severe("Succeeded.");
			} catch (Exception e) {
				logger.severe("Failed.");
				e.printStackTrace();
				continue;
			}

			String request = null;
			request = receiveRequest(reader, socket);
			String response = processRequest(request);
			sendResponse(writer, response, socket);
		}
	}

	public static void main(String args[]) {
		(new Thread(new AuthServer())).start();
	}
}
