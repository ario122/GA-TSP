package opt_ga_tsp006;

import java.sql.Date;
import java.sql.Time;

import javax.print.attribute.standard.DateTimeAtCompleted;

public class main {

	public static void main(String[] args) {

		String myFile = new String(
				"C:/Users/ARIO/Desktop/OPT/data/TSP/rat195.tsp");

		tsp_problem tsp = new tsp_problem();
		tsp.readParams(args);
		tsp.readcostumer(myFile);

		genetics ga = new genetics(tsp, 10000);

		long time2;
		long time1;
		time1 = System.currentTimeMillis();
		ga.init();

		ga.start(150);
		time2 = System.currentTimeMillis();

		System.out.println(time2 - time1);
		// java.util.Random r = new java.util.Random( 12345 );

	}

}
