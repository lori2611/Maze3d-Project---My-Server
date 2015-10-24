package model;

import java.io.IOException;
import java.util.Observable;

public abstract class CommonModel extends Observable implements Model
{

	@Override
	public abstract void open() throws IOException;

	@Override
	public abstract void close() throws Exception;

}
