/*
 * 
 */
package presenter;

public class CommunicationProperties {

	/** The port. */
	private int port;
	
	/** The num of clients. */
	private int numOfClients;

	/**
	 * Instantiates a new communication properties.
	 */
	public CommunicationProperties() {}

	/**
	 * Get the port.
	 *
	 * @return the port
	 */
	public int getPort() {
		return port;
	}

	/**
	 * Sets the port.
	 * @param the new port
	 */
	public void setPort(int port) {
		this.port = port;
	}
	
	/**
	 * Gets the num of clients.
	 * @return the num of clients
	 */
	public int getNumOfClients() {
		return numOfClients;
	}
	
	/**
	 * Sets the num of clients.
	 * @param the new num of clients
	 */
	public void setNumOfClients(int numOfClients) {
		this.numOfClients = numOfClients;
	}
}
