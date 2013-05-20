package crdt_data;

import java.util.HashMap;

public interface CRDTAverage {

	/**
	 * Returns true if the information is successful added to the system.
	 * @param timeStamp
	 * @param speed
	 */
	public boolean addReading(int timeStamp, double speed);
	
	/**
	 * Returns the array with the sum in the first position and the counter in the second one.
	 * @param currentTime
	 */
	public double[] getAverage(int currentTime);
	
	/**
	 * Merges two CRDTAverages and stores the result in the one that calls the method.
	 * @param other
	 * @return
	 */
	public HashMap<String, Agglomerator> merge(CRDTAverage other);

	/**
	 * Merges the agglomerator into the system.
	 * @param hallowID
	 * @param agglomerator
	 */
	public void mergeHallow(String hallowID, Agglomerator agglomerator);

}
