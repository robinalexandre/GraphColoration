package fr.etu.ea.model.operators.mutation;

import fr.etu.ea.model.Individu;
import fr.etu.ea.model.Population;
import fr.etu.ea.model.operators.IOperators;
import fr.etu.ea.util.Utilities;

public interface IMutation extends IOperators {
		
	public void mutate(Individu individu);
	
	public default void mutationAll(Population population, int probability) {
		for(int i = 0; i < population.getIndividus().length; i++) {
			if(Utilities.probabilite(probability)) {
				Individu previous = new Individu(population.getIndividus()[i]);
				int previousFitness = previous.fitness();
				this.mutate(population.getIndividus()[i]);
				
				if(population.getIndividus()[i].fitness() > previousFitness)
					population.getIndividus()[i] = previous;
			}
		}
	}

}
