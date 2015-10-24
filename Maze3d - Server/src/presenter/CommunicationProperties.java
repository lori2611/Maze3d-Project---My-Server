package presenter;

public class CommunicationProperties {

	private int port;
	private int numOfClients;

	public CommunicationProperties() {}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}
	
	public int getNumOfClients() {
		return numOfClients;
	}
	
	public void setNumOfClients(int numOfClients) {
		this.numOfClients = numOfClients;
	}
}
