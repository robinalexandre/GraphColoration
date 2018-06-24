package fr.etu.ea.model.operators.selection;

import java.util.ArrayList;
import java.util.List;

import fr.etu.ea.model.Population;
import fr.etu.ea.util.Utilities;

public class SelectionRandom implements ISelection {

	@Override
	public Population selection(Population population, int nbSelections) {
		Population selections = new Population(nbSelections);
		List<Integer> previousSelections = new ArrayList<>();
		int rand;
		for(int i = 0; i < nbSelections; i++) {
			rand = Utilities.random.nextInt(population.getIndividus().length);
			while(previousSelections.contains(rand))
				rand = Utilities.random.nextInt(population.getIndividus().length);
			previousSelections.add(rand);
			selections.getIndividus()[i] = population.getIndividus()[rand];
		}
		return selections;
	}

	public String toString() {
		return this.getClass().getSimpleName();
	}
}
