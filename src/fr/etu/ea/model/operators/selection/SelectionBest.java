package fr.etu.ea.model.operators.selection;

import java.util.ArrayList;
import java.util.List;

import fr.etu.ea.model.Individu;
import fr.etu.ea.model.Population;
import fr.etu.ea.model.operators.ComputeAllFitness;

public class SelectionBest implements ISelection {

	@Override
	public Population selection(Population population, int nbSelections) {
		Population best = new Population(nbSelections);
		List<Integer> indexAlreadyUse = new ArrayList<>();
		int index;
		int i = 0;
		
		while(i < nbSelections) {
			ComputeAllFitness fitness = new ComputeAllFitness(population, indexAlreadyUse, null);
			index = fitness.getBestIndex();
			best.getIndividus()[i] = new Individu(population.getIndividus()[index]);
			indexAlreadyUse.add(index);
			i++;
		}
		return best;
	}

	public String toString() {
		return this.getClass().getSimpleName();
	}
}
