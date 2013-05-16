package crdt_data;

import java.util.HashMap;

public interface CRDTAverage {

	public boolean addReading(int timeStamp, double speed);
	
	public double[] getAverage(int currentTime);
	
	public HashMap<String, Agglomerator> merge(CRDTAverage other);

	public void mergeHallow(String hallowID, Agglomerator agglomerator);

}
