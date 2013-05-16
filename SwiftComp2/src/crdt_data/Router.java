package crdt_data;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Router{

	private int num_tables;
	private CRDTMap[] maps;
	private String routerID;
	private List<Hallow> hallows;
	private double multiplier;

	public Router(int tables, File file, String routerID, double multiplier) {
		this.num_tables = tables;
		this.multiplier = multiplier;

		this.routerID = routerID;

		maps = new CRDTMap[num_tables];
		for (int i = 0; i < num_tables; i++)
			maps[i] = new CRDTHashMap(this.routerID);

		if(file != null){
			Thread t = new MyReader(file);
			t.setDaemon(true);
			t.start();
		}
	}

	public synchronized boolean addReading(int time, String wayID, double speed) {
		return addReading(mapIndex(wayID), time, wayID, speed);
	}

	private synchronized boolean addReading(int mapIndex, int time,
			String wayID, double speed) {
		return maps[mapIndex].addReading(time, wayID, speed);
	}

	private int mapIndex(String wayID) {
		return Integer.parseInt(wayID) % num_tables;
	}

	public synchronized double[] getAverageSpeed(String wayID, int currentTime) {
		return maps[mapIndex(wayID)].getAverageSpeed(wayID, currentTime);
	}

	public synchronized void mergeMap(int index, Router other) {
		maps[index].merge(other.maps[index]);
	}

	public synchronized void mergeRouter(Router r) {
		for (int i = 0; i < num_tables; i++)
			maps[i].merge(r.maps[i]);
	}

	public int addNewHallow(File file) {
		int id = hallows.size();
		hallows.add(new Hallow(file, routerID + "_hallow_" + id, multiplier));
		return id;
	}

	public synchronized void mergeHallow(int index) {
		Hallow hallow = hallows.get(index);
		if (hallow.needsUpdate()) {
			HashMap<String, Agglomerator> hallowMap = hallow.resetHallow();
			String hallowID = hallow.getID();
			for (String wayID : hallowMap.keySet()) {
				Agglomerator aux = hallowMap.get(wayID);
				if (aux.getNumReadings() != 0)
					maps[mapIndex(wayID)].mergeHallow(wayID, hallowID, aux);
			}
		}
	}

	public synchronized void mergeHallows(){
		for(int i = 0; i<hallows.size(); i++)
			mergeHallow(i);
	}

	private class MyReader extends Thread implements Runnable {

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
			while (in.hasNextLine()) {
				String[] tokens = in.nextLine().split(" ");
				addReading(tokens);
			}
		}

		public void addReading(String[] tokens) {
			int time = (int) Float.parseFloat(tokens[0]);
			String wayID = tokens[3];
			double speed = multiplier * Double.parseDouble(tokens[6]);

			Router.this.addReading(mapIndex(wayID), time, wayID, speed);
		}

	}

}
