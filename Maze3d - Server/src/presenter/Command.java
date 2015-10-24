package presenter;

public interface Command {

	/**
	 * Do command.
	 * @param params
	 * @throws Exception 
	 */
	public void doCommand(String[] params);
}