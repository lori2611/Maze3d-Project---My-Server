package view;

import java.util.HashMap;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class ServerWindow {

	/** The display. */
	 Display display;
	
	/** The shell. */
	 Shell shell;
	
	/** The listeners. */
	 HashMap<String,Listener> listeners;
	 
	 int numOfClients;
	 
	 Button openButton;
	 Button closeButton;
	 Label message;
	
	 /**
	  * Ctor
	  * @param width
	  * @param length
	  * @param title
	  * @param listeners
	  */
	public ServerWindow(int width, int length,String title,HashMap<String,Listener> listeners) {
		// Check if display already exist
		if(Display.getCurrent() != null)
		{
			this.display = Display.getCurrent();	
		}	
		else
		{
			this.display = new Display();
		}
		shell = new Shell(this.display);
		shell.setSize(width,length);
		shell.setText(title);
		this.listeners = listeners;
		this.numOfClients = 5;
	}
	
	public void initWidgets() {
		shell.setLayout(new GridLayout(1,false));
		shell.addListener(SWT.Close, listeners.get("exit"));
		
		// Create "open the server" button
		openButton=new Button(shell, SWT.PUSH);
		openButton.setText("Open Server");
		openButton.setLayoutData(new GridData(SWT.FILL, SWT.UP, false, false, 1, 1));
		openButton.addListener(SWT.Selection, listeners.get("open"));
		openButton.setEnabled(true);
	    
		// Create "close the server" button
		closeButton=new Button(shell, SWT.PUSH);
		closeButton.setText("Close Server");
		closeButton.setLayoutData(new GridData(SWT.FILL, SWT.UP, false, false, 1, 1));
		closeButton.addListener(SWT.Selection, listeners.get("close"));
		closeButton.setEnabled(false);
		
		// Set appropriate titles
		Label title1 = new Label(shell, SWT.CENTER);
		title1.setText("Hello, welcome to my server!");
		GridData data = new GridData(SWT.FILL, SWT.BEGINNING, true, false);
		data.horizontalSpan = 3;
		title1.setLayoutData(data);

		Label title2 = new Label(shell, SWT.CENTER);
		title2.setText("Message from the server:");
		data = new GridData(SWT.FILL, SWT.BEGINNING, true, false);
		title2.setLayoutData(data);

		// Show messages from the server
		message = new Label(shell,SWT.CENTER);
		data = new GridData(SWT.FILL, SWT.BEGINNING, true, false);
		message.setLayoutData(data);
		message.setText("---");
	}
	
	/**
	 * Display messages from server by changing the label text
	 * @param msg
	 */
	public void displayMessage(String msg) {
		if(!shell.isDisposed())
		display.asyncExec(new Runnable() {
			
			@Override
			public void run() {
				message.setText(msg);
				message.setBackground(new Color(null, 255,255,255));
			}
		});
	}
	
	/**
	 * Get the shell
	 * @return
	 */
	public Shell getShell() {
		return this.shell;
	}
	
	/**
	 * Close the shell
	 */
	public void close() {
		this.shell.dispose();
	}
	
	/**
	 * Open shell and initialize widgets.
	 */
	public void start() {
		
		initWidgets();
		shell.open();
		// main event loop
		 while(!shell.isDisposed()){ // while window isn't closed

		    if(!display.readAndDispatch()){ 	// if the queue is empty
		       display.sleep(); 			// sleep until an event occurs 
		    }

		 } // shell is disposed
		 display.dispose();
	}
	
	/**
	 * Set buttons enable/disable by the server state
	 * @param isOpen
	 */
	public void setButtons(boolean isOpen) {
		display.asyncExec(new Runnable() {
			
			@Override
			public void run() {
				openButton.setEnabled(!isOpen);
				closeButton.setEnabled(isOpen);
			}
		});
	}
}
