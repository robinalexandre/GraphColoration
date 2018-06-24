package fr.etu.ea.model.operators.selection;

import fr.etu.ea.model.Population;

public class SelectionTournoi implements ISelection {

	@Override
	public Population selection(Population population, int nbSelections) {
		Population parents = new Population(nbSelections);
		Population populationAleatoire = new SelectionRandom().selection(population, 2*nbSelections);
		for(int i = 0; i < populationAleatoire.getIndividus().length; i+=2) {
			if(populationAleatoire.getIndividus()[i].fitness() < populationAleatoire.getIndividus()[i+1].fitness())
				parents.getIndividus()[i/2] = populationAleatoire.getIndividus()[i];
			else 
				parents.getIndividus()[i/2] = populationAleatoire.getIndividus()[i+1];
		}
		return parents;
	}

	public String toString() {
		return this.getClass().getSimpleName();
	}
}
