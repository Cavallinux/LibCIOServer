package cl.bancochile.monitor.tx;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cl.bancochile.monitor.tx.utils.DatosUtils;

public class Processor implements Runnable {
	private Socket socket;
	private String message;
	private boolean rechazarCierre;
	private static Logger logger = LoggerFactory.getLogger(Processor.class);

	public void run() {
    	try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			String message = reader.readLine();
			logger.debug("Request received: {}", message);
			StringBuilder builder = new StringBuilder();
			
			if (StringUtils.contains(message, "iniciatx")) {
				logger.debug("Peticion de inicio de transaccion CIO");
				builder.append(System.currentTimeMillis());
				builder.append(";");
				builder.append("ok");
				builder.append(";");
				builder.append(DatosUtils.randomStr());
			} else if (StringUtils.contains(message, "avisodecierre")) {
				logger.debug("Peticion de cierre de transaccion CIO");
				logger.debug("Se encuentra activo el flag de rechazo automatico? {}", rechazarCierre);
				
				builder.append(System.currentTimeMillis());
				builder.append(";");
				builder.append(!rechazarCierre ? "ok" : "rechazo");
				builder.append(";");
				builder.append(!rechazarCierre ? DatosUtils.randomStr() : "Transaccion CIO rechazada");
			}

			logger.debug("Response to be sent: {}", builder.toString());
			OutputStream outputStream = socket.getOutputStream();
			outputStream.write(builder.toString().getBytes());
			outputStream.flush();
		} catch (IOException e) {
			logger.error("Error en escritura de respuesta", e);
		} finally {
			try {
				socket.close();
			} catch (IOException e) {
				logger.warn("IOException en cierre de socket");
			}
		}
    }

	public Socket getSocket() {
		return socket;
	}

	public void setSocket(Socket socket) {
		this.socket = socket;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public boolean isRechazarCierre() {
		return rechazarCierre;
	}

	public void setRechazarCierre(boolean rechazarCierre) {
		this.rechazarCierre = rechazarCierre;
	}
}