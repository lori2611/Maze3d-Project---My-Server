package model;

import java.io.IOException;
import java.util.Observable;

public abstract class CommonModel extends Observable implements Model
{

	@Override
	/**
	 * Open the server
	 */
	public abstract void open() throws IOException;

	@Override
	/**
	 * Close the server
	 */
	public abstract void close() throws Exception;

}
