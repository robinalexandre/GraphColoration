package fr.etu.ea.model.operators.crossover;

import java.util.List;

import fr.etu.ea.model.Individu;
import fr.etu.ea.model.Population;
import fr.etu.ea.model.operators.IOperators;
import fr.etu.ea.util.Utilities;

public interface ICrossover extends IOperators {
		
	public Individu[] crossover(Individu parent1, Individu parent2);

	public default Population crossoverAll(Population population, int probability) {
		Population children = new Population(population);
		for(int i = 0; i < population.getIndividus().length; i+=2) {
			if(Utilities.probabilite(probability)) {
				Individu[] coupleChild = new Individu[2];
				coupleChild = this.crossover(population.getIndividus()[i], population.getIndividus()[i+1]);
				children.getIndividus()[i] = coupleChild[0];
				children.getIndividus()[i+1] = coupleChild[1];
			} else {
				children.getIndividus()[i] = population.getIndividus()[i];
				children.getIndividus()[i+1] = population.getIndividus()[i+1];
			}
		}
		return children;
	}
	
	public static void swap(List<Integer> list1, List<Integer> list2, int start, int end) {
		for (int i = start; i < end; ++i) {
			final Integer temp = list1.get(i);
		    list1.set(i, list2.get(i));
		    list2.set(i, temp);
		}
	}
}
