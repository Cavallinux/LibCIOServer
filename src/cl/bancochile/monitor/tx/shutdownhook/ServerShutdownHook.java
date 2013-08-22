package cl.bancochile.monitor.tx.shutdownhook;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cl.bancochile.monitor.tx.Server;

public class ServerShutdownHook extends Thread {
	private static Logger logger = LoggerFactory.getLogger(ServerShutdownHook.class);

	@Override
	public void run() {
		logger.debug("Signal trapped, exiting application...");
		Server.setServerExecuting(false);
	}
}