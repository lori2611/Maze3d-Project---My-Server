package presenter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;

import model.CommonModel;
import model.Model;
import view.CommonView;
import view.View;

public class Presenter implements Observer{

	private View v;
	private Model m;
	private HashMap<String, Command> commands;
	
	public static final String COMMAND_ERR = "Invalid Command ";

	public Presenter(Model m, View v) {
		this.m = m;
		this.v = v;
		this.commands = new HashMap<String, Command>();
		initCommands();
	}

	public View getV() {
		return v;
	}

	public void setV(View v) {
		this.v = v;
	}

	public Model getM() {
		return m;
	}

	public void setM(Model m) {
		this.m = m;
	}

	@Override
	public void update(Observable o, Object arg) {
		if(o==this.m)
		{
			analyzeModelOutput(arg);
		}	
		if(o==this.v)
		{
			analyzeCommand((String) arg);
		}
	}
	
	private void analyzeModelOutput(Object obj) {
		if(obj instanceof Exception)
		{
			v.displayException((Exception)obj);
			return;
		}
		if(obj instanceof String)
		{
			v.displayMessage((String)obj);
			return;
		}
	}

	private void analyzeCommand(String input) {
		try {
			ArrayList<String> paramsList = new ArrayList<String>();
			
			// Check if the input is a command in the HashMap
			while(!commands.containsKey(input))
			{
				// Delete the last word of the input and save it in the ArrayList
				paramsList.add(input.substring(input.lastIndexOf(" ") + 1));
				input = input.substring(0, input.lastIndexOf(" "));
			}
			if(input.equals(""))
			{
				// Print message to the client - invalid command
				v.displayMessage(COMMAND_ERR);
			}
			else
			{
				// Reverse the ArrayList and convert to String array
				Collections.reverse(paramsList);
				String[] params = new String[paramsList.size()];
				paramsList.toArray(params);
				
				// Activate command
				commands.get(input).doCommand(params);
			}
			} catch (Exception e) {
				v.displayMessage(e.toString());
			}
	}

	private void initCommands() {
		
		commands.put("open the server", new Command() {
			
			@Override
			public void doCommand(String[] params) {
				try {
					m.open();
				} catch (IOException e) {
					System.out.println("Presenter->initCommands->Open->exception");
					e.printStackTrace();
				}
			}
		});	
		
		commands.put("close the server", new Command() {
			
			@Override
			public void doCommand(String[] params) {
				try {
					m.close();
				} catch (Exception e) {
					System.out.println("Presenter->initCommands->Close->exception");
					e.printStackTrace();
				}
			}
		});		
	}
}
