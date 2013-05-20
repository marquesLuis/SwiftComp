import java.io.*;
import java.util.*;

public class Tester {

	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		Scanner in = new Scanner(System.in);
		Scanner fileIn = new Scanner(new File("lisbon_trace.gps"));
		
		int routers = in.nextInt();
		PrintStream[] out = new PrintStream[routers];
		for(int i = 0; i<routers; i++)
			out[i]=new PrintStream(new File("Router"+i+".txt"));
		while(fileIn.hasNext()){
			int i = new Random().nextInt(routers);
			out[i].println(fileIn.nextLine());
		}

	}

}
