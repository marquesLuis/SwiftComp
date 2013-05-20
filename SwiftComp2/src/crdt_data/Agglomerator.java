package crdt_data;

public class Agglomerator {

	private static final int DEFAULT_NUM_UNITS = 10;
	private static final int DEFAULT_UNIT_SIZE = 60;
	private double[] sums;
	private int[] counters;
	private long[] timestamps;
	private int num_units;
	private int unit_size;
	private int maxTime;
	private double num_readings;

	public Agglomerator(int num_units, int unit_size) {
		sums = new double[num_units];
		counters = new int[num_units];
		timestamps = new long[num_units];
		this.num_units = num_units;
		maxTime = -1;
		this.unit_size = unit_size;
		num_readings = 0;
	}

	public Agglomerator() {
		this(DEFAULT_NUM_UNITS, DEFAULT_UNIT_SIZE);
	}

	/**
	 * This constructor builds a independent clone of other.
	 * 
	 * @param other
	 */
	public Agglomerator(Agglomerator other) {
		this(other.num_units, other.unit_size);
		for (int i = 0; i < other.num_units; i++) {
			sums[i] = other.sums[i];
			counters[i] = other.counters[i];
			timestamps[i] = other.timestamps[i];
		}
		this.maxTime = other.maxTime;
		this.num_readings = other.num_readings;
	}

	/**
	 * Adds a reading to the agglomerator. Returns true if the timestamp is
	 * valid(within the time frame).
	 */
	public boolean addReading(int timestamp, double speed) {
		timestamp /= unit_size;

		if (timestamp < lastValidTime(maxTime))
			return false;

		if (maxTime < timestamp)
			maxTime = timestamp;

		int pos = timestamp % num_units;

		if (timestamp == timestamps[pos]) {
			sums[pos] += speed;
			counters[pos]++;
		} else if (timestamp > timestamps[pos]) {
			sums[pos] = speed;
			counters[pos] = 1;
			timestamps[pos] = timestamp;
		} else
			return false;

		num_readings++;
		return true;
	}

	/**
	 * Returns the number of readings already done
	 * 
	 * @return
	 */
	public double getNumReadings() {
		return num_readings;
	}

	private int lastValidTime(int upperLimit) {
		return upperLimit - num_units + 1;
	}

	/**
	 * Returns an array with two positions. In the first is the sum and in the
	 * second the count
	 * 
	 * @param currentTime
	 * @return an array with two positions. In the first is the sum and in the
	 *         second the count.
	 */
	public double[] getAverage(int currentTime) {
		currentTime /= unit_size;
		int lastValidTime = lastValidTime(currentTime);
		double[] avg = new double[2];
		for (int i = 0; i < num_units; i++)
			if (timestamps[i] >= lastValidTime) {
				avg[0] += sums[i];
				avg[1] += counters[i];
			}
		return avg;
	}

	/**
	 * This method merges two Agglomerators, but it should only be used by
	 * Hallow Replicas because it does an incremental merge.
	 * 
	 * @param agg
	 * @return
	 */
	public boolean merge(Agglomerator agg) {
		if (num_units != agg.num_units || unit_size != agg.unit_size)
			return false;
		for (int i = 0; i < num_units; i++) {
			if (timestamps[i] == agg.timestamps[i]) {
				sums[i] += agg.sums[i];
				counters[i] += agg.counters[i];
			} else if (timestamps[i] < agg.timestamps[i]) {
				sums[i] = agg.sums[i];
				counters[i] = agg.counters[i];
				timestamps[i] = agg.timestamps[i];
			}
		}
		num_readings += agg.num_readings;
		maxTime = Math.max(maxTime, agg.maxTime);
		return true;
	}

}