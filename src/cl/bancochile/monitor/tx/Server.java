package cl.bancochile.monitor.tx;

import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Properties;

import org.apache.commons.lang.math.NumberUtils;
import org.apache.log4j.PropertyConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cl.bancochile.monitor.tx.shutdownhook.ServerShutdownHook;

public class Server {
	private static final Logger log = LoggerFactory.getLogger(Server.class);
	private static boolean serverExecuting = true;

	public static void main(String[] args) throws Exception {
		PropertyConfigurator.configure(System.getProperty("log4j.config.file"));
		int port = getPort(args);
		Properties properties = loadConfig();
		ServerSocket server = new ServerSocket(port);
		log.info("CIO Server Socket, connection data");
		log.info("Server IP: {}", server.getLocalSocketAddress().toString());
		log.info("Server port: {}", server.getLocalPort());
		log.info("Waiting for connections...");
		Runtime.getRuntime().addShutdownHook(new ServerShutdownHook());
		while (serverExecuting) {
			Socket sock = server.accept();
			log.info("New connection from {}:{}", sock.getRemoteSocketAddress().toString(), sock.getPort());
			Processor processor = new Processor();
			processor.setSocket(sock);
			processor.setRechazarCierre(Boolean.valueOf(properties.getProperty("cierre.rechazo.activar")));
			Thread serverThread = new Thread(processor, "cio-server-thread");
			serverThread.start();
		}
		System.exit(0);
	}

	private static Properties loadConfig() throws IOException {
		Properties properties = new Properties();
		properties.load(new FileInputStream(System.getProperty("main.config.file")));
		return properties;
	}

	private static int getPort(String[] args) {
		int port = 9999;
		if (args.length == 1 && NumberUtils.isDigits(args[0])) {
			port = new BigInteger(args[0]).intValue();
		}
		return port;
	}

	public static boolean isServerExecuting() {
		return serverExecuting;
	}

	public static void setServerExecuting(boolean serverExecuting) {
		Server.serverExecuting = serverExecuting;
	}
}