package crdt_data;

import java.io.File;
import java.util.*;

public class RouterManager {

	private int num_tables;
	private List<Router> routers;
	private int seq_router;
	private double multiplier;

	public RouterManager(int num_tables, double multiplier) {
		this.num_tables = num_tables;
		this.multiplier = multiplier;
		seq_router = 0;
		routers = new ArrayList<Router>();
	}

	public int addNewRouter(File inputFile) {
		routers.add(new Router(num_tables, inputFile, seq_router++ + "", multiplier));
		return seq_router -1;
	}

	public double[] getAverageSpeed(String wayID, int currentTime) {
		double[] result = new double[2];
		for (Router r : routers) {
			double[] aux = r.getAverageSpeed(wayID, currentTime);
			if (aux != null) {
				result[0] += aux[0];
				result[1] += aux[1];
			}
		}
		return result;
	}

	public double[] getAverageSpeed(int router, String wayID,
			int currentTime) {
		return routers.get(router).getAverageSpeed(wayID, currentTime);
	}
	
	public boolean addReading(int router, int time, String wayID, double speed){
		return routers.get(router).addReading(time, wayID, speed);
	}
	
	public boolean addReading(int time, String wayID, double speed){
		if(routers.size()==0)
			routers.add(new Router(num_tables, null, seq_router++ + "", multiplier));
		return routers.get(0).addReading(time, wayID, speed);
	}
	
	public void mergeMap(int firstRouter, int secondRouter, int map){
		Router first = routers.get(firstRouter);
		Router second = routers.get(secondRouter);
		first.mergeMap(map, second);
	}
	
	public void mergeRouter(int firstRouter, int secondRouter){
		Router first = routers.get(firstRouter);
		Router second = routers.get(secondRouter);
		first.mergeRouter(second);
	}
	
	public int addNewHallow(int router, File file){
		Router r = routers.get(router);
		return r.addNewHallow(file);
	}
	
	public int addNewHallow(File file){
		if(routers.size()==0)
			routers.add(new Router(num_tables, null, seq_router++ + "", multiplier));
		Router r = routers.get(0);
		return r.addNewHallow(file);
	}
	
	public void mergeHallow(int router, int hallow){
		routers.get(router).mergeHallow(hallow);
	}
	
	public void mergeHallows(int router){
		routers.get(router).mergeHallows();
	}
}
