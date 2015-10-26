package view;

public interface View {

	/**
	 * Start the view
	 */
	public void start();
	
	/**
	 * Display message to the client throw the interface
	 * @param msg - the message 
	 */
	public void displayMessage(String msg);
	
	/**
	 *  Display exceptions to the client throw the interface
	 * @param e - the exception
	 */
	public void displayException(Exception e);
	
}
