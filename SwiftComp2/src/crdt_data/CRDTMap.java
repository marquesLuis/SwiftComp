package crdt_data;

import java.util.HashMap;

public interface CRDTMap {

	public boolean addReading(int timeStamp, String wayID, double speed);
	
	public double[] getAverageSpeed(String wayID, int currentTime);
	
	public HashMap<String, CRDTAverage> merge(CRDTMap other);

	public void mergeHallow(String wayID, String hallowID,
			Agglomerator agglomerator);
}
