package opt_ga_tsp006;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Arrays;

public class genetics {

	tsp_problem tsp;

	population_entity[] PE;

	int population_size;

	// uniform distribution
	java.util.Random rnd = new java.util.Random(12345);
	java.util.Random rnd2 = new java.util.Random(12345);

	public genetics(tsp_problem t, int p_size) {

		tsp = t;
		population_size = p_size;
		PE = new population_entity[population_size * 2];

	}

	public void init() {
		java.util.Random r = new java.util.Random(12345);
		for (int i = 0; i < population_size * 2; i++) {
			PE[i] = new population_entity();
		}

		for (int i = 0; i < population_size; i++) {

			PE[i].tour = createGRandTour(r.nextInt(tsp.customers.length));
			PE[i].score = score(PE[i].tour);
			System.out.println(PE[i].score);
		}

		Arrays.sort(PE);

		/*
		 * System.out.println("hihi");
		 * 
		 * for(int i = 0; i<population_size; i++) {
		 * 
		 * System.out.println(PE[i].score);
		 * 
		 * 
		 * }
		 */

		System.out.println("initial population created.");
	}

	private double score(int[] tour) {
		int len = tour.length;
		double dist = 0;
		for (int i = 0; i < len; i++)
			dist += tsp.matrix[tour[i]][i + 1 >= len ? tour[0] : tour[i + 1]];

		// return (1/dist) * 100000;
		return dist;

	}

	// random greedy tour
	private int[] createGRandTour(int rnd) {

		int[] tour;
		int[] avail = new int[tsp.customers.length];
		tour = new int[tsp.customers.length];
		for (int i = 0; i < avail.length; i++)
			avail[i] = 1;
		avail[rnd] = -1;
		tour[0] = rnd;
		for (int i = 1; i < tour.length; i++) {
			int closest = -1;
			double dist = Double.MAX_VALUE;
			for (int j = 0; j < avail.length; j++)
				if ((norm(tsp.customers, tour[i - 1], j) < dist)
						&& (avail[j] >= 0)) {
					dist = norm(tsp.customers, tour[i - 1], j);
					closest = j;
				} // end if: new nearest neighbor
			tour[i] = closest;
			avail[closest] = -1;
		} // end for

		return tour;
	}

	// absolutely random tour
	private int[] createRandTour() {

		List<Integer> t = new ArrayList<Integer>();

		for (int i = 1; i < tsp.customers.length; i++) {
			t.add(i);
		}
		Collections.shuffle(t);

		int[] tour = new int[tsp.customers.length];
		for (int i = 0; i < t.size(); i++) {
			tour[i] = t.get(i);
		}

		return tour;
	}

	// greedy approach
	private int[] createTour() {

		int[] tour;
		int[] avail = new int[tsp.customers.length];
		tour = new int[tsp.customers.length];
		for (int i = 0; i < avail.length; i++)
			avail[i] = i;
		for (int i = 1; i < tour.length; i++) {
			int closest = -1;
			double dist = Double.MAX_VALUE;
			for (int j = 1; j < avail.length; j++)
				if ((norm(tsp.customers, tour[i - 1], j) < dist)
						&& (avail[j] >= 0)) {
					dist = norm(tsp.customers, tour[i - 1], j);
					closest = j;
				} // end if: new nearest neighbor
			tour[i] = closest;
			avail[closest] = -1;
		} // end for

		return tour;
	}

	private double norm(double[][] matr, int a, int b) {
		double xDiff = matr[b][0] - matr[a][0];
		double yDiff = matr[b][1] - matr[a][1];
		return Math.sqrt(xDiff * xDiff + yDiff * yDiff);
	} // end norm

	private void cross_over() {
		int flag[] = new int[population_size];
		for (int i = 0; i < population_size; i++) {
			flag[i] = 1;
		}

		java.util.Random r = new java.util.Random(12345);
		int finished = population_size;
		// double the population by cross over
		for (int i = 0; i < population_size; i++) {
			if (flag[i] == 0)
				continue;

			flag[i] = 0;
			finished--;
			int k = 0, j = 0;
			do {
				j = r.nextInt(population_size);

			} while (flag[j] == 0 && finished > 0);

			flag[j] = 0;
			finished--;
			// System.out.println(i + "  +   " + j);

			PE[i + population_size].tour = cross(i, j);
			PE[i + population_size].score = score(PE[i + population_size].tour);
			PE[j + population_size].tour = cross(j, i);
			PE[j + population_size].score = score(PE[j + population_size].tour);
			// System.out.println(PE[i+population_size].score);
			// System.out.println(PE[j+population_size].score);
		}

	}

	private int[] cross(int i, int j) {
		java.util.Random r = new java.util.Random(12345);
		int[] tour = new int[tsp.customers.length];
		int[] avail = new int[tsp.customers.length];
		int point = r.nextInt(tsp.customers.length);

		int pi = 0, pt = 0;

		for (int m = 0; m < tsp.customers.length; m++) {
			avail[m] = 1;
		}

		while (pi < point) {

			tour[pt] = PE[i].tour[pi];
			avail[PE[i].tour[pi]] = 0;
			pt++;
			pi++;
		}

		while (pt < tsp.customers.length) {
			if (avail[PE[j].tour[pi]] != 0) {
				tour[pt] = PE[j].tour[pi];
				avail[PE[j].tour[pi]] = 0;

				pt++;
			}
			pi++;
			if (pi == tsp.customers.length)
				pi = 0;

		}
		return tour;
	}

	public void mutate() {

		for (int i = 0; i < population_size; i++) {
			if (rnd2.nextInt(100) < 70) // / p = 0.7
			{
				int point1 = rnd.nextInt(tsp.customers.length);
				int point2 = rnd.nextInt(tsp.customers.length);

				int tmp = PE[i].tour[point1];
				PE[i].tour[point1] = PE[i].tour[point2];
				PE[i].tour[point2] = tmp;

			}
		}

	}

	private void cross_over_GX() {

		int flag[] = new int[population_size];
		for (int i = 0; i < population_size; i++) {
			flag[i] = 1;
		}

		java.util.Random r = new java.util.Random(12345);
		int finished = population_size;
		// double the population by cross over
		for (int i = 0; i < population_size; i++) {
			if (flag[i] == 0)
				continue;

			flag[i] = 0;
			finished--;
			int k = 0, j = 0;
			do {
				j = r.nextInt(population_size);

			} while (flag[j] == 0 && finished > 0);

			flag[j] = 0;
			finished--;
			// System.out.println(i + "  +   " + j);

			PE[i + population_size].tour = cross_IGX(i, j);
			PE[i + population_size].score = score(PE[i + population_size].tour);
			PE[j + population_size].tour = cross_IGX(j, i);
			PE[j + population_size].score = score(PE[j + population_size].tour);
			// System.out.println(PE[i+population_size].score);
			// System.out.println(PE[j+population_size].score);
		}

	}

	private int[] cross_IGX(int f, int m) {

		int i = 0;
		int child_index = 0;
		int[] child = new int[tsp.customers.length];
		int[] avail = new int[tsp.customers.length];
		int source_parent = rnd.nextInt(tsp.customers.length);
		int anchor = PE[f].tour[source_parent];

		for (i = 0; i < tsp.customers.length; i++)
			avail[i] = 1;

		int next = anchor;

		child[child_index++] = next;
		avail[next] = 0;

		for (; child_index < tsp.customers.length; child_index++) {

			int[] neighbors = new int[4];

			neighbors[0] = source_parent + 1 >= tsp.customers.length ? -1
					: PE[f].tour[source_parent + 1];
			neighbors[1] = source_parent - 1 < 0 ? -1
					: PE[f].tour[source_parent - 1];

			i = 0;
			while (i < tsp.customers.length) {
				if (PE[m].tour[i] == next) {
					neighbors[2] = i + 1 >= tsp.customers.length ? -1
							: PE[m].tour[i + 1];
					neighbors[3] = i - 1 < 0 ? -1 : PE[m].tour[i - 1];
					break;
				}
				i++;
			}

			int closest = -1;
			i = 0;
			do {

				double dist = Double.MAX_VALUE;

				for (i = 0; i < 4; i++) {
					if (neighbors[i] == -1)
						continue;
					if (dist > norm(tsp.customers, neighbors[i], next)) {
						dist = norm(tsp.customers, neighbors[i], next);
						closest = i;
					}
				}
				if (avail[neighbors[closest]] == 0)
					neighbors[closest] = -1;

			} while (i++ < 4);

			next = neighbors[closest];

			if (next == -1) {
				do {
					next = rnd.nextInt(tsp.customers.length);

				} while (avail[next] == 0);
			}

			avail[next] = 0;
			child[child_index] = next;

			for (i = 0; i < PE[f].tour.length; i++) {
				if (PE[f].tour[i] == next) {
					source_parent = i;
					break;
				}
			}
		}

		// System.out.println("h");
		return child;
	}

	public void start(int iterations) {
		int iterate = 0;
		while (iterate++ < iterations) {
			// cross_over();
			// mutate();

			cross_over_GX();

			Arrays.sort(PE);

			// kill the lower 50%

			for (int i = population_size; i < population_size * 2; i++) {
				PE[i].score = Double.MAX_VALUE;
			}

			System.out.println(iterate + " : " + PE[0].score + "\t"
					+ PE[1].score);

		}

	}

}
