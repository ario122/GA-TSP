package opt_ga_tsp006;

public class population_entity implements Comparable<population_entity> {

	public int[] tour;
	public double score;

	public population_entity() {
		score = Double.MAX_VALUE;
	}

	public int compareTo(population_entity compareEntity) {

		double compareQuantity = ((population_entity) compareEntity).score;

		// ascending order
		return (int) (-1 * (compareQuantity - this.score));
		// return (int)

		// descending order
		// return compareQuantity - this.quantity;

	}

}
