package fr.etu.ea.model.operators.crossover;

import fr.etu.ea.model.Individu;

public class NoCrossover implements ICrossover {

	@Override
	public Individu[] crossover(Individu parent1, Individu parent2) {
		Individu child = new Individu(parent1);
		Individu child2 = new Individu(parent2);
		child = parent1;
		child2 = parent2;
		Individu[] children = {child, child2};
		return children;
	}

	public String toString() {
		return this.getClass().getSimpleName();
	}
}
