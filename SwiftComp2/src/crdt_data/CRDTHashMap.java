package crdt_data;

import java.util.*;

public class CRDTHashMap implements CRDTMap {

	private HashMap<String, CRDTAverage> map;
	private String processID;

	public CRDTHashMap(String PID) {
		map = new HashMap<String, CRDTAverage>();
		processID = PID;
	}

	public boolean addReading(int timeStamp, String wayID, double speed) {
		CRDTAverage wayAvg = map.get(wayID);
		if (wayAvg == null){
			wayAvg = new CRDTHashAverage(processID);
			map.put(wayID, wayAvg);
		}
		return wayAvg.addReading(timeStamp, speed);
	}
	
	public double[] getAverageSpeed(String wayID, int currentTime){
		CRDTAverage wayAvg = map.get(wayID);
		if(wayAvg==null)
			return null;
		return wayAvg.getAverage(currentTime);
	}

	public HashMap<String, CRDTAverage> merge(CRDTMap other) {
		HashMap<String, CRDTAverage> otherMap = ((CRDTHashMap)other).map;
		for (String wayID : otherMap.keySet()) {
			CRDTAverage myOtherAvg = map.get(wayID);
			if (myOtherAvg==null){
				myOtherAvg = new CRDTHashAverage(processID);
				map.put(wayID, myOtherAvg);
			}
			myOtherAvg.merge(otherMap.get(wayID));
		}
		return map;
	}

	@Override
	public void mergeHallow(String wayID, String hallowID,
			Agglomerator agglomerator) {
		CRDTAverage avg = map.get(wayID);
		if(avg==null){
			avg=new CRDTHashAverage(processID);
			map.put(processID, avg);
		}
		avg.mergeHallow(hallowID,agglomerator);
	}
}
