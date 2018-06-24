package fr.etu.ea.model.operators.insertion;

import java.util.ArrayList;
import java.util.List;

import fr.etu.ea.model.Individu;
import fr.etu.ea.model.Population;
import fr.etu.ea.model.operators.ComputeAllFitness;

public class InsertionFitness implements IInsertion {

	@Override
	public void insert(Population population, Population enfants) {
		List<Integer> worseFitnessIndex = new ArrayList<>();
		int index;
		for(int i = 0; i < enfants.getIndividus().length; i++) {
			ComputeAllFitness fitness = new ComputeAllFitness(population, null, worseFitnessIndex);
			index = fitness.getWorstIndex();
			population.getIndividus()[index] = new Individu(enfants.getIndividus()[i]);
			worseFitnessIndex.add(index);
		}
	}

	public String toString() {
		return this.getClass().getSimpleName();
	}
}
