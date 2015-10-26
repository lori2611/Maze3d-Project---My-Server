package view;

import java.util.HashMap;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.MessageBox;

public class GUI extends CommonView{

	private HashMap<String,Listener> listeners;
	private ServerWindow serverWindow;
	
	public GUI() {
		this.listeners = new HashMap<String,Listener>();
		initListeners();
		this.serverWindow = new ServerWindow(100, 100, "Welcome to the Server", listeners);
	}
	
	private void initListeners() {
		listeners.put("exit", new Listener() {
			
			@Override
			public void handleEvent(Event e) {

				// Close the main window
				MessageBox messageBox = new MessageBox(serverWindow.getShell(), SWT.ICON_QUESTION | SWT.YES | SWT.NO);
				messageBox.setMessage("Do you really want to exit?");
				messageBox.setText("Exiting Application");

				if(messageBox.open() == SWT.YES)
				{
					setChanged();
					notifyObservers("close the server");
					serverWindow.close();
					e.doit = true;
				}
				else
				{
					e.doit = false;
				}
			}
		});
		
		listeners.put("open", new Listener() {
			
			@Override
			public void handleEvent(Event e) {
				setChanged();
				notifyObservers("open the server");
				serverWindow.setButtons(true);
			}
		});
		
		listeners.put("close", new Listener() {
			
			@Override
			public void handleEvent(Event e) {
				setChanged();
				notifyObservers("close the server");
			}
		});
	}

	@Override
	public void start() {
		serverWindow.start();
	}

	@Override
	public void displayMessage(String msg) {
		serverWindow.displayMessage(msg);
	}
	
	public void displayException(Exception e) {
		e.printStackTrace();
	}	
}
