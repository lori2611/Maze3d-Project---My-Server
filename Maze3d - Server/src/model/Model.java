package model;

import java.io.IOException;

public interface Model {

	/**
	 * Open connection to server
	 * @throws IOException 
	 */
	public void open() throws IOException;
	
	/**
	 * Close connection from server
	 * @throws Exception 
	 */
	public void close() throws Exception;
	
}
