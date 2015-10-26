package model;

import java.io.InputStream;
import java.io.OutputStream;

public interface ClientHandler {
	
	/**
	 * handle client problem
	 * @param inFromClient
	 * @param outToClient
	 */
	void handleClient(InputStream inFromClient, OutputStream outToClient);
	
}
