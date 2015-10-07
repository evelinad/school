/**
    DUMITRESCU EVELINA Tema3 SPRC
 */

import java.io.*;
import java.security.KeyStore;
import java.security.cert.Certificate;
import java.security.cert.*;
import java.util.logging.Logger;
import java.util.*;
import javax.net.ssl.*;

public class Client implements Runnable {
	private boolean hasToRun = true;
	private SSLSocket socket = null;
	private BufferedReader reader;
	private KeyStore clientKeyStore = null;
	static Logger logger = Logger.getLogger(Client.class.getName());
	final int CHUNK_SIZE = 1000;
	private String name;
	private String department;
	private String hostname;
	private int port;

	public Client(String name, String department, String hostname, int port) {
		this.name = name;
		this.port = port;
		this.hostname = hostname;
		this.department = department;

	}

	/*
	 * create key store
	 */
	private void createKeyStore() {
		logger.info("Obtaining a reference to the client's keystore");
		String keyStoreName = "security/" + this.name + "/" + this.name + ".ks";
		;
		String password = this.name + "_password";
		try {
			this.clientKeyStore = KeyStore.getInstance("JKS");
			FileInputStream is = new FileInputStream(keyStoreName);
			this.clientKeyStore.load(is, password.toCharArray());
			logger.info("Succeeded.");
		} catch (Exception e) {
			logger.severe("Failed");
			e.printStackTrace();
		}

	}

	/*
	 * crate key manager factory
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
			String password = this.name + "_password";
			// initialize the key manager factory with a source of key material
			keyManagerFactory.init(this.clientKeyStore, password.toCharArray());
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
			trustManagerFactory.init(this.clientKeyStore);
			logger.info("Succeeded.");
		} catch (Exception e) {
			logger.severe("Failed.");
			e.printStackTrace();

		}
		return trustManagerFactory;
	}

	/*
	 * initialize SSL context
	 */
	private SSLContext createSSLContext() {
		logger.info("Initializing SSLContext ...");
		SSLContext sslContext = null;
		try {
			TrustManagerFactory trustManagerFactory = createTrustKeyManagerFactory();
			KeyManagerFactory keyManagerFactory = createKeyManagerFactory();
			sslContext = SSLContext.getInstance("TLS");
			sslContext.init(keyManagerFactory.getKeyManagers(),
					trustManagerFactory.getTrustManagers(), null);
			logger.info("Succeeded.");
		} catch (Exception e) {
			logger.severe("Failed.");
			e.printStackTrace();

		}

		return sslContext;
	}

	/*
	 * send one message to server
	 */
	void sendDataToServer(String message) {
		try {
			OutputStream sockOs = this.socket.getOutputStream();
			PrintWriter pw = new PrintWriter(sockOs, true);
			pw.println(message);
			pw.flush();
		} catch (Exception exc) {
			exc.printStackTrace();
			logger.severe(exc.toString());
		}
	}

	/*
	 * receive message from server
	 */
	String receiveDataFromServer() {
		try {
			InputStream sockIs = this.socket.getInputStream();
			BufferedReader br = new BufferedReader(
					new InputStreamReader(sockIs));
			String msg = br.readLine();
			return msg;

		} catch (Exception exc) {
			exc.printStackTrace();
			logger.severe(exc.toString());
			return null;
		}
	}

	/*
	 * send list command
	 */
	void listFiles(String newCommand) {
		sendDataToServer(newCommand);
		String response = receiveDataFromServer();
		logger.info("[SERVER] " + response);
		if (response.indexOf("DENIED") < 0) {
			response = receiveDataFromServer();
			System.out.println("Files stored on server: " + response);
		}
	}

	/*
	 * send a file to server/ upload
	 */
	void sendOneFile(String fileName) {
		try {
			File file = new File(fileName);
			long size = file.length();
			long seek = 0;
			BufferedReader br = new BufferedReader(new FileReader(fileName));
			OutputStream sockOs = this.socket.getOutputStream();
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

	}

	/*
	 * receive one file from server/download
	 */
	void receiveOneFile(String fileName) {
		try {
			char[] buffer = new char[CHUNK_SIZE + 1];
			FileWriter fw = new FileWriter("downloads_" + this.name + "/"
					+ fileName);
			BufferedWriter bw = new BufferedWriter(fw);
			InputStream sockIs = this.socket.getInputStream();
			BufferedReader br = new BufferedReader(
					new InputStreamReader(sockIs));
			long size = Integer.parseInt(br.readLine());
			long seek = 0;
			int rd = 0;
			while (seek < size) {
				rd = br.read(buffer, 0, CHUNK_SIZE);
				buffer[rd] = '\0';
				bw.write(buffer, 0, rd);
				bw.flush();
				seek += rd;
			}
			bw.close();
		} catch (Exception exc) {
			exc.printStackTrace();
			logger.severe(exc.toString());
		}

	}

	/*
	 * send upload command for multiple files
	 */
	void uploadFiles(String newCommand) {

		StringTokenizer st = new StringTokenizer(newCommand, " ");
		StringBuilder newCommandBuilder = new StringBuilder();
		newCommandBuilder.append("upload");
		boolean atLeastOne = false;
		st.nextToken();
		while (st.hasMoreTokens()) {
			String newFile = st.nextToken();
			if ((new File(newFile)).exists() == false) {
				logger.warning(newFile
						+ " does not exist. This file will be skipped.");
			} else {
				atLeastOne = true;
				newCommandBuilder.append(" ");
				newCommandBuilder.append(newFile);
			}
		}
		if (atLeastOne) {
			newCommand = newCommandBuilder.toString();
			st = new StringTokenizer(newCommand, " ");
			sendDataToServer(newCommand);
			String response;
			st.nextToken();
			response = receiveDataFromServer();
			logger.info("[SERVER] " + response);
			if (response.indexOf("DENIED") < 0) {
				while (st.hasMoreTokens()) {
					String newFile = st.nextToken();
					response = receiveDataFromServer();
					logger.info("[SERVER] " + response);
					if (response.indexOf("DENIED") < 0) {
						sendOneFile(newFile);
						logger.info("Successfully uploaded " + newFile + ".");
					} else
						logger.info("Failed to upload " + newFile + ".");

				}
			}

		}
	}

	/*
	 * send download command for multiple files
	 */
	void downloadFiles(String newCommand) {

		StringTokenizer st = new StringTokenizer(newCommand, " ");
		LinkedList<String> files = new LinkedList<String>();
		sendDataToServer(newCommand);
		String response;
		st.nextToken();
		while (st.hasMoreTokens()) {
			String newFile = st.nextToken();
			response = receiveDataFromServer();
			if (response.indexOf("DENIED") < 0) {
				receiveOneFile(newFile);
				logger.info("Successfully downloaded " + newFile + ".");
			} else
				logger.info("Failed to download " + newFile + ".");
		}

	}

	/*
	 * send close command to server and close connection
	 */
	void close(String newCommand) {
		try {
			sendDataToServer(newCommand);
			receiveDataFromServer();
			this.socket.close();
		} catch (Exception e) {
		}
	}

	/*
	 * process input command
	 */
	void processCommand(String newCommand) {
		if (newCommand.compareTo("list") == 0) {
			listFiles(newCommand);

		}
		if (newCommand.startsWith("upload ")) {
			uploadFiles(newCommand);
		}
		if (newCommand.startsWith("download ")) {
			downloadFiles(newCommand);
		}
		if (newCommand.startsWith("close")) {
			close(newCommand);
			hasToRun = false;
		}

	}

	/*
	 * create a connection with the department server
	 */
	private void createConnection() {
		logger.info("Creating SSL socket ..");
		try {
			SSLContext sslContext = createSSLContext();
			SSLSocketFactory ssf = sslContext.getSocketFactory();
			this.socket = (SSLSocket) ssf.createSocket(hostname, port);
			logger.info("Succeeded.");
		} catch (Exception e) {
			logger.severe("Failed.");
			e.printStackTrace();
		}

		logger.info("Initiating handshake ...");
		try {
			this.socket.startHandshake();
			logger.info("Succeeded.");
		} catch (IOException e) {
			logger.severe("Failed.");
			e.printStackTrace();
		}

		logger.info("Geting peer certificates ...");
		Certificate CACertificate = null;
		try {
			CACertificate = this.clientKeyStore
					.getCertificate("certification_authority");
			logger.info("Succeeded.");
		} catch (Exception e) {
			logger.severe("Failed.");
			e.printStackTrace();

		}

		X509Certificate[] peerCertificates;

		try {
			peerCertificates = (X509Certificate[]) ((this.socket).getSession())
					.getPeerCertificates();
			if (CACertificate.equals(peerCertificates[1])) {
				logger.info("End point peers have the same CA certificate.");
			} else {
				logger.severe("End point peers do not have the same CA certificate.");
			}
			logger.info("Succeeded.");
		} catch (Exception e) {
			logger.severe("Failed.");
			e.printStackTrace();
		}

		logger.info("Handling the communication with the server ...");
		try {
			reader = new BufferedReader(new InputStreamReader(
					socket.getInputStream()));
			logger.info("Succeeded.");
		} catch (Exception e) {
			logger.severe("Failed");
			e.printStackTrace();
		}

	}

	/*
	 * wait for user input
	 */
	private void waitForCommand() {
		BufferedReader commandReader = new BufferedReader(
				new InputStreamReader(System.in));
		String newCommand = null;

		while (hasToRun) {
			try {
				System.out.println("Enter a new command:\t");
				newCommand = commandReader.readLine();
				processCommand(newCommand);
			} catch (Exception e) {
				logger.info("An exception occured when trying to read a new message from the server.");
				e.printStackTrace();

			}

		}
	}

	public void run() {
		File downloadsDir = new File("downloads_" + this.name);
		if (downloadsDir.exists() == false)
			downloadsDir.mkdir();
		/*
		 * if the downloads directory does not exist, create it
		 */
		if (!downloadsDir.exists()) {
			downloadsDir.mkdir();
		}
		/*
		 * create key store, key manager factory, trust key manager factory,
		 * initialize ssl context and initiate connection with the server
		 */
		createKeyStore();
		createConnection();
		waitForCommand();

	}

	public static void main(String args[]) {
		if (args.length != 4) {
			System.err
					.println("[Usage]: java clientName clientDepartment hostname port");
			return;
		}
		(new Thread(new Client(args[0], args[1], args[2],
				Integer.parseInt(args[3])))).start();
	}
}
