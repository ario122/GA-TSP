package opt_ga_tsp006;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

import opt_ga_tsp006.GlobalData;

public class tsp_problem {

	public int numCustomers = 0;
	public int[][] customersPoints;
	public double[][] customers;
	public double[][] matrix;

	private void init() {

		matrix = createMatrix(customers);

	}

	private double[][] createMatrix(double[][] customers) {
		int len = customers.length;
		double[][] matrix = new double[len][len];

		for (int i = 0; i < len; i++)
			for (int j = i + 1; j < len; j++)
				matrix[i][j] = matrix[j][i] = norm(customers[i][0],
						customers[i][1], customers[j][0], customers[j][1]);
		return matrix;
	} // end createMatrix

	/** Calculate distance between two points. */
	private double norm(double x1, double y1, double x2, double y2) {
		double xDiff = x2 - x1;
		double yDiff = y2 - y1;
		return Math.round(Math.sqrt(xDiff * xDiff + yDiff * yDiff));
	} // end norm

	public void readcostumer(String myFile) {
		customers = new double[numCustomers][2];
		try {
			File input = new File(myFile);
			BufferedReader br = new BufferedReader(new InputStreamReader(
					new FileInputStream(input)));
			String line;
			String token;
			line = br.readLine();
			line = br.readLine();
			line = br.readLine();
			line = br.readLine();
			StringTokenizer st = new StringTokenizer(line);
			token = st.nextToken();
			token = st.nextToken();
			numCustomers = Integer.parseInt(token);
			customers = new double[numCustomers][2];
			line = br.readLine();
			line = br.readLine();
			for (int i = 0; i < numCustomers; i++) {
				line = br.readLine();
				st = new StringTokenizer(line);
				token = st.nextToken();
				token = st.nextToken();
				customers[i][0] = Double.parseDouble(token);
				token = st.nextToken();
				customers[i][1] = Double.parseDouble(token);
			}
			line = br.readLine();
			if (!line.toUpperCase().contains("EOF")) {
				throw new IllegalArgumentException(
						"Error while reading the input file: EOF Section");
			}

		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Error " + e.getMessage());
			System.exit(-1);
		}
		GlobalData.numCustomers = numCustomers;
		init();
	}

	public tsp_problem() {
		System.out.println("hello");
	}

	public enum ParamFile {
		NONE, PARAMS, INSTANCES
	}

	public static void readParams(String args[]) {
		ParamFile fileStatus = ParamFile.NONE;
		String myFileParam = "C:/Users/ARIO/workspace/OPT_GA_TSP_001/Data/TSP/param.txt";
		if (args.length > 0) {
			myFileParam = args[0];
		}
		try {
			File input = new File(myFileParam);
			BufferedReader br = new BufferedReader(new InputStreamReader(
					new FileInputStream(input)));
			String line;
			String token;
			StringTokenizer st;
			while ((line = br.readLine()) != null) {
				switch (fileStatus) {
				case NONE:
					st = new StringTokenizer(line);
					token = st.nextToken();

					if (token.toUpperCase().equals("PARAMS")) {
						fileStatus = ParamFile.PARAMS;
						System.out.println("Reading the parameters");
					} else if (token.toUpperCase().equals("INSTANCES")) {
						fileStatus = ParamFile.INSTANCES;
						System.out.println("Reading the instances");
					} else if (token.toUpperCase().equals("EOF")) {
						System.out.println("Close the file");
						br.close();
						return;
					}
					break;
				case PARAMS:
					if (line.toUpperCase().contains("ENDPARAMS")) {
						fileStatus = ParamFile.NONE;
						System.out.println("End of parameters");
					} else {
						// Read the parameters
					}
					break;
				case INSTANCES:
					if (line.toUpperCase().contains("ENDINSTANCES")) {
						System.out.println("End of instances");
						fileStatus = ParamFile.NONE;
					} else {
						// Read the input files
					}
					break;
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Error " + e.getMessage());
			System.exit(-1);
		}
	}

}
