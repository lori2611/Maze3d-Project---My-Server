package view;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

public class CLI extends CommonView {

	private BufferedReader in;
	private PrintWriter out;
	
	public CLI(BufferedReader in,PrintWriter out) {
		this.in = in;
		this.out = out;
	}
	
	@Override
	public void start() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				String input;
				out.println("----- Server - Command Line -----");
				out.println("Hello and welcome to the server side. \ntype \"open the server\" to open it\ntype \"stop the server\" to stop it");
				out.flush();
				try {
					input = in.readLine();

					// Run while the client doesn't insert "exit"
					while (!input.equals("close the server")) {
						// Pass the input to the view according to the MVP pattern
						setChanged();
						notifyObservers(input);
						out.flush();
						input = in.readLine();
					}
					setChanged();
					notifyObservers(input);
					out.println("--- EXIT ---");
				} catch (IOException e) {

					// Send error to the view.
					setChanged();
					notifyObservers("Invalid command");
				}
				out.close();
			}
		}).start();
	}

	@Override
	public void displayMessage(String msg) {
		out.println(msg);
		out.flush();
	}

}
