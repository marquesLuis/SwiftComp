package crdt_data;

import java.util.HashMap;

public interface CRDTMap {

	/**
	 * Returns true if the info is successful introduced into the system. 
	 * @param timeStamp
	 * @param wayID
	 * @param speed
	 */
	public boolean addReading(int timeStamp, String wayID, double speed);
	
	/**
	 * Returns an array with two positions. The first one is the sum and the second one is the count of the valid readings.
	 * @param wayID
	 * @param currentTime
	 */
	public double[] getAverageSpeed(String wayID, int currentTime);
	
	/**
	 * Merges two CRDTMaps and stores the result in the one that calls the method.
	 * @param other
	 */
	public HashMap<String, CRDTAverage> merge(CRDTMap other);

	/**
	 * Merges an Hallow Replica into the current information on the system.
	 * @param wayID
	 * @param hallowID
	 * @param agglomerator
	 */
	public void mergeHallow(String wayID, String hallowID,
			Agglomerator agglomerator);
}
