/**
 * 
 */
package crdt_data;

import java.util.Collection;
import java.util.HashMap;

/**
 * @author lfmarques
 * 
 */
public class CRDTHashAverage implements CRDTAverage {

	protected HashMap<String, Agglomerator> map;
	private String processID;
	private Agglomerator myAgglomerator;

	public CRDTHashAverage(String processID) {
		map = new HashMap<String, Agglomerator>();
		this.processID = processID;
		myAgglomerator = new Agglomerator();
		map.put(processID, myAgglomerator);
	}

	/**
	 * 
	 * @param timeStamp
	 * @param speed
	 * @return
	 */
	public boolean addReading(int timeStamp, double speed) {
		return myAgglomerator.addReading(timeStamp, speed);
	}

	public double[] getAverage(int currentTime) {
		Collection<Agglomerator> avgs = map.values();
		double[] result = new double[2];
		for(Agglomerator g : avgs){
			double[] aux = g.getAverage(currentTime);
			result[0]+=aux[0];
			result[1]+=aux[1];
		}
		return result;
	}

	public HashMap<String, Agglomerator> merge(CRDTAverage other) {
		HashMap<String, Agglomerator> otherMap = ((CRDTHashAverage) other).map;

		for (String otherPID : otherMap.keySet()) {
			if (otherPID.equals(processID))
				continue;
			Agglomerator myOtherAgg = map.get(otherPID);
			Agglomerator otherAgg = otherMap.get(otherPID);
			if (myOtherAgg != null) {
				if (myOtherAgg.getNumReadings() < otherAgg.getNumReadings()) { // UPDATE
																				// IF
																				// MORE
																				// RECENT
					map.remove(otherPID);
					map.put(otherPID, new Agglomerator(otherAgg));
				}
			} else
				map.put(otherPID, new Agglomerator(otherAgg));
		}
		return map;
	}

	@Override
	public void mergeHallow(String hallowID, Agglomerator agglomerator) {
		Agglomerator agg = map.get(hallowID);
		if(agg == null)
			map.put(hallowID, new Agglomerator(agglomerator));
		else
			agg.merge(agglomerator);
	}
}
