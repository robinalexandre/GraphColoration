package fr.etu.ea.model.operators.insertion;

import fr.etu.ea.model.Population;
import fr.etu.ea.model.operators.IOperators;

public interface IInsertion extends IOperators {
	
	public void insert(Population population, Population enfants);
}
