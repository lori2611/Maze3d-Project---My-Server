package model;

import java.beans.XMLDecoder;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import presenter.CommunicationProperties;

public class MyModel extends CommonModel{

	private int numOfClients; // Given by XML file
	private int port; // Given by XML file
	private boolean stop;
	private ExecutorService threadpool;
	private ServerSocket server;
	private Thread mainServerThread;
	private MyClientHandler clientHandler;
	private XMLDecoder decoder;
	private CommunicationProperties cp;
	private int clientsHandled;
	
	public MyModel() {
		setXML();
		this.clientsHandled = 0;
		this.clientHandler = new MyClientHandler();
		threadpool=Executors.newFixedThreadPool(cp.getNumOfClients());
		this.stop = false;
	}
	
	/**
	 * Initialize configuration from the XML
	 */
	public void setXML() {
		
		// Read properties from XML
		try {
			this.decoder = new XMLDecoder(new FileInputStream("./communicationProperties.xml"));
			this.cp = (CommunicationProperties) decoder.readObject();
			decoder.close();

			// Load configuration
			this.port = cp.getPort();
			this.numOfClients = cp.getNumOfClients();
		} catch (FileNotFoundException e) {
			setChanged();
			notifyObservers(e);
		}
	}
	
	@Override
	public void open() throws IOException {
			server=new ServerSocket(this.cp.getPort());
			server.setSoTimeout(50*1000);
			
			mainServerThread=new Thread(new Runnable() {			
				@Override
				public void run() {
					while(!stop){
						try {
							final Socket someClient=server.accept();
							if(someClient!=null){
								threadpool.execute(new Runnable() {									
									@Override
									public void run() {
										try{										
											clientsHandled++;
											setChanged();
											notifyObservers("handling client "+clientsHandled);
											clientHandler.handleClient(someClient.getInputStream(), someClient.getOutputStream());
											setChanged();
											notifyObservers("done handling client "+clientsHandled);
											someClient.close();
										}catch(IOException e){
											setChanged();
											notifyObservers(e);
										}									
									}
								});								
							}
						}
						catch (SocketTimeoutException e){
							setChanged();
							notifyObservers("no client connected...");
						} 
						catch (IOException e) {
							setChanged();
							notifyObservers(e);
						}
					}
					setChanged();
					notifyObservers("done accepting new clients.");
				} // end of the mainServerThread task
			});
			
			mainServerThread.start();
	}

	@Override
	public void close() throws Exception{
		this.stop=true;	
		this.clientHandler.saveToZip();
		// do not execute jobs in queue, continue to execute running threads
		setChanged();
		notifyObservers("shutting down");
		threadpool.shutdown();
		// wait 10 seconds over and over again until all running jobs have finished
		boolean allTasksCompleted=false;
		while(!(allTasksCompleted=threadpool.awaitTermination(10, TimeUnit.SECONDS)));
		setChanged();
		notifyObservers("all the tasks have finished");
		mainServerThread.join();		
		setChanged();
		notifyObservers("main server thread is done");
		server.close();
		setChanged();
		notifyObservers("server is safely closed");
	}

}
