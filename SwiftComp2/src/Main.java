import java.io.File;
import java.util.*;

import crdt_data.*;

public class Main {

	private static final String ERROR_INIT = "Error initializing:\nargs[0] = number of tables: args[1] = speed multiplier (m/s = 1, km/h = 3.6)";
	private static final String EXIT = "exit";
	private static final String NEW_ROUTER = "newRouter";
	private static final String GET_SPEED = "getSpeed";
	private static final int SPEED_NUM_ARGS_ALL = 2;
	private static final int SPEED_NUM_ARGS_SINGLE = 3;
	private static final String ADD_READING = "addReading";
	private static final int READING_ADD_UNDEF = 3;
	private static final int READING_ADD_DEF = 4;
	private static final String MERGE_MAP = "mergeMap";
	private static final String MERGE_ROUTER = "mergeRouter";
	private static final String NEW_HALLOW = "newHallow";
	private static final int HALLOW_ROUTER_DEF = 2;
	private static final int HALLOW_ROUTER_UNDEF = 1;
	private static final String MERGE_HALLOW = "mergeHallow";
	private static final String MERGE_ALL_HALLOWS = "mergeAllHallows";
	

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		if (args.length != 2) {
			System.err.println(ERROR_INIT);
			System.exit(1);
		}

		// args[0] - number of tables per router
		// args[1] - speed multiplier (KM/H) = 3.6
		int num_tables = 0;
		double multiplier = 0;
		try {
			num_tables = Integer.parseInt(args[0]);
			multiplier = Double.parseDouble(args[1]);
		} catch (Exception e) {
			System.err.println(ERROR_INIT);
			System.exit(1);
		}

		Scanner in = new Scanner(System.in);
		String input = "";

		RouterManager manager = new RouterManager(num_tables, multiplier);
		System.out.print(">");
		while (!(input = in.nextLine()).equals(EXIT)) {
			if (input.startsWith(NEW_ROUTER)) {
				System.out.print("FilePath: ");
				System.out.println("Router_ID = "
						+ manager.addNewRouter(new File(in.nextLine())));
			} else if (input.startsWith(GET_SPEED)) {
				System.out.print(">WayID Time RouterID(Opcional): ");
				String[] tokens = in.nextLine().split(" ");
				if (tokens.length == SPEED_NUM_ARGS_ALL) {
					double[] res = manager.getAverageSpeed(tokens[0],
							Integer.parseInt(tokens[1]));
					System.out.println(res != null ? res[0] / res[1] : -1);
				} else if (tokens.length == SPEED_NUM_ARGS_SINGLE) {
					double[] res = manager.getAverageSpeed(
							Integer.parseInt(tokens[0]), tokens[1],
							Integer.parseInt(tokens[2]));
					System.out.println(res != null ? res[0] / res[1] : -1);
				}
			}else if (input.startsWith(ADD_READING)){
				System.out.print(">RouterID(Opcional) Time WayID Speed: ");
				String[] tokens = in.nextLine().split(" ");
				if(tokens.length == READING_ADD_DEF){
					System.out.println(manager.addReading(Integer.parseInt(tokens[0]), Integer.parseInt(tokens[1]), tokens[2], Double.parseDouble(tokens[3])));
				}else if(tokens.length == READING_ADD_UNDEF){
					for(String s :tokens)
						System.out.println(s);
					System.out.println(manager.addReading(Integer.parseInt(tokens[0]), tokens[1], Double.parseDouble(tokens[2])));
				}
			}else if (input.startsWith(MERGE_MAP)){
				System.out.print(">RouterID1 RouterID2 Map: ");
				String[] tokens = in.nextLine().split(" ");
				manager.mergeMap(Integer.parseInt(tokens[0]), Integer.parseInt(tokens[1]), Integer.parseInt(tokens[2]));
			}else if (input.startsWith(MERGE_ROUTER)){
				System.out.print(">RouterID1 RouterID2: ");
				String[] tokens = in.nextLine().split(" ");
				manager.mergeRouter(Integer.parseInt(tokens[0]), Integer.parseInt(tokens[1]));
			}else if (input.startsWith(NEW_HALLOW)){
				System.out.print(">RouterID(Opcional) FilePath: ");
				String[] tokens = in.nextLine().split(" ");
				int id = 0;
				if(tokens.length == HALLOW_ROUTER_DEF){
					id = manager.addNewHallow(Integer.parseInt(tokens[0]), new File(tokens[1]));
				}else if(tokens.length == HALLOW_ROUTER_UNDEF){
					id = manager.addNewHallow(new File(tokens[0]));
				}
				System.out.println("Hallow_ID : "+id);
			}else if (input.startsWith(MERGE_HALLOW)){
				System.out.print(">RouterID HallowID: ");
				String[] tokens = in.nextLine().split(" ");
				manager.mergeHallow(Integer.parseInt(tokens[0]), Integer.parseInt(tokens[1]));
			}else if (input.startsWith(MERGE_ALL_HALLOWS)){
				System.out.print(">RouterID: ");
				String[] tokens = in.nextLine().split(" ");
				manager.mergeHallows(Integer.parseInt(tokens[0]));
			}

			System.out.print(">");
		}
	}

}
