package model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import algorithms.mazeGenerators.Maze3d;
import algorithms.mazeGenerators.Maze3dSearchable;
import algorithms.mazeGenerators.Position;
import algorithms.search.AStar;
import algorithms.search.BFS;
import algorithms.search.Heuristic;
import algorithms.search.MazeAirDistance;
import algorithms.search.MazeManhattanDistance;
import algorithms.search.Searchable;
import algorithms.search.Searcher;
import algorithms.search.Solution;

public class MyClientHandler implements ClientHandler {

	private HashMap<Maze3d, Solution<Position>> mazeSolution;
	private ExecutorService threadpool;
	private ObjectOutputStream messageToClient;
	private ObjectInputStream messageFromClient;
	
	/**
	 * C'tor
	 */
	public MyClientHandler() {
		try {
			loadFromZip();
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(this.mazeSolution==null)
		this.mazeSolution = new HashMap<Maze3d, Solution<Position>>();
		this.threadpool = Executors.newFixedThreadPool(3);
	}

	@Override
	/**
	 * This method will check if the client's problem can be solved by the server
	 * If it can - it will activate the right method
	 */
	public void handleClient(InputStream inFromClient, OutputStream outToClient) {
			// Get clients problem which contain title(String),and needed params.
			try {
				messageFromClient = new ObjectInputStream(inFromClient);
				ArrayList<Object> problem = (ArrayList<Object>)messageFromClient.readObject();
				messageToClient = new ObjectOutputStream(outToClient);
				
				// Check if the server knows the problem
				if (problem.get(0).equals("solve")) {
					handleSolution(problem);
				}	
				else
				{
					System.out.println("Invalid problem");
				}
			} catch (Exception e) {
				e.printStackTrace();
			}	
	}
	
	/**
	 * This method will create solution to the maze and return it to the client
	 * @param list
	 */
	private void handleSolution(ArrayList<Object> list) {
		
		// Declare variables
		String algorithm = (String) list.get(1);
		Maze3d maze = (Maze3d) list.get(2);
		
		// Create solution for the maze in different thread
		Future<Solution<Position>> sol = threadpool.submit(new Callable<Solution<Position>>() {

			public Solution<Position> call() {

				Searcher<Position> algo = null;
				Searchable<Position> sm = new Maze3dSearchable(maze);
				
				// Check for the right algorithm
				if (algorithm.equals("bfs")) {
					algo = new BFS<Position>();
				} else if (algorithm.equals("manhattan")) {
					Heuristic<Position> ManhattanDistance = new MazeManhattanDistance();
					algo = new AStar<Position>(ManhattanDistance);
				} else if (algorithm.equals("air")) {
					Heuristic<Position> AirDistance = new MazeAirDistance();
					algo = new AStar<Position>(AirDistance);
				}

				Solution<Position> solution = algo.search(sm);
				return solution;
			}
		});

		// Add the solution to the relevant HashMaps
		Solution<Position> solution;
		try {
			solution = sol.get();
			mazeSolution.put(maze, solution);
			
			// Send solution to client
			messageToClient.writeObject(solution);
			messageToClient.flush();
			
			// Close streams
			messageFromClient.close();
			messageToClient.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Load the saved solutions from the zip file
	 * @throws Exception
	 */
	private void loadFromZip() throws Exception {
		
		ObjectInputStream in;
		File file = new File("./mazeSolution.gz");
		
		// If the file doesn't exist - create it
		if(!file.createNewFile())
		{
			try {
				in = new ObjectInputStream(new GZIPInputStream(new FileInputStream(file)));
				this.mazeSolution = (HashMap<Maze3d, Solution<Position>>) in.readObject();
				in.close();
			} catch (IOException e) {
				System.out.println("Maze solution file is empty");
			}
		}
	}
	
	/**
	 * Save out solutions in zip file
	 * @throws IOException
	 */
	public void saveToZip() throws IOException {

		// Write all the data from the HashMap to the specified file
		FileOutputStream fos = null;
		GZIPOutputStream gzip = null;
		ObjectOutputStream out = null;
		
		try {
			
			fos = new FileOutputStream("./mazeSolution.gz");
			gzip = new GZIPOutputStream(fos);
			out = new ObjectOutputStream(gzip);
			out.writeObject(mazeSolution);
			out.flush();
			out.close();
			
		} catch (FileNotFoundException e) {
			System.out.println("File doesn't found");
		} catch (IOException e) {
			System.out.println("IO exception");
		} finally {
			if(gzip != null)
			{
				gzip.close();
			}
		}
	}
}
