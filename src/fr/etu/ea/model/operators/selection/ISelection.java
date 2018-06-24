package fr.etu.ea.model.operators.selection;

import fr.etu.ea.model.Population;
import fr.etu.ea.model.operators.IOperators;

public interface ISelection extends IOperators {
	
	public Population selection(Population population, int nbSelections);

}
