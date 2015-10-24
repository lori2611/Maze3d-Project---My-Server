package boot;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import model.CommonModel;
import model.MyModel;
import presenter.Presenter;
import view.CLI;
import view.CommonView;

public class Run {

	public static void main(String[] args) {
		// Create model
		CommonModel m = new MyModel();
		
		// Create view
		CommonView v = new CLI(new BufferedReader(new InputStreamReader(System.in)), new PrintWriter(System.out));
		
		// Create presenter
		Presenter p = new Presenter(m, v);
		
		v.addObserver(p);
		m.addObserver(p);
		
		// Start view
		v.start();

	}

}
