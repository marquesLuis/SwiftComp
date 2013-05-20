package crdt_data;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Hallow {
	
	private HashMap<String, Agglomerator> map;
	private String myID;
	private double multiplier;
	private boolean needsUpdate;
	
	public Hallow(File input, String ID, double multiplier){
		map = new HashMap<String, Agglomerator>();
		this.myID = ID;
		this.multiplier = multiplier;
		needsUpdate = false;
		Thread t = new MyReader(input);
		t.setDaemon(true);
		t.start();
	}
	
	/**
	 * @param time
	 * @param wayID
	 * @param speed
	 * @return
	 */
	private synchronized boolean addReading(int time, String wayID, double speed){
		needsUpdate = true;
		Agglomerator agg = map.get(wayID);
		if(agg==null){
			agg = new Agglomerator();
			map.put(wayID, agg);
		}
		return agg.addReading(time, speed);
	}
	
	/**
	 * Returns true if this hallow replica has something new add to the system
	 * @return
	 */
	public boolean needsUpdate(){
		return needsUpdate;
	}
	
	
	private class MyReader extends Thread implements Runnable{

		private Scanner in;
		
		public MyReader(File file) {
			try {
				in = new Scanner(file);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
			}
		}

		@Override
		public void run() {
			while(in.hasNextLine()){
				String[] tokens = in.nextLine().split(" ");
				addReading(tokens);
			}
		}
		
		public void addReading(String[] tokens){
			int time = (int) Float.parseFloat(tokens[0]);
			String wayID = tokens[3];
			double speed = multiplier * Double.parseDouble(tokens[6]);
			
			Hallow.this.addReading(time, wayID, speed);
		}
		
	}
	
	public synchronized HashMap<String, Agglomerator> resetHallow(){
		HashMap<String, Agglomerator> res = map;
		map = new HashMap<String, Agglomerator>();
		needsUpdate = false;
		return res;
	}

	public String getID() {
		return myID;
	}
	

}
