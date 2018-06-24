package fr.etu.ea.apprentissage;

import java.util.Random;

import fr.etu.ea.model.Population;
import fr.etu.ea.model.operators.IOperators;

/**
 * Permet de faire une selection dite "Adaptative Roulette Wheel" d'un opérateur entre différents opérateurs du meme type (mutation, croisement...)
 * @author Alex
 *
 */
public class SelectionAdaptiveRouletteWheel extends SelectionOperators {
	
	private Random random = new Random();

	/**
	 * Construit une selection dite "Adaptative Roulette Wheel" héritant du constructeur de {@Selection} avec une valeur pMin permettant 
	 * de toujours avoir une probabilité supérieur à 0 pour tous les opérateurs
	 * {@inheritDoc Selection}
	 * @param operateurs
	 * @param balanceCoefficient
	 * @param pMin
	 * @param windowSize
	 */
	public SelectionAdaptiveRouletteWheel(IOperators[] operators, float balanceCoefficient, float pMin, int windowSize) {
		super(operators.length, balanceCoefficient, pMin, windowSize);
		this.probabilities = new float[operators.length];
		for(int i = 0; i < this.getProbabilities().length; i++) {
			this.probabilities[i] = (float)1/this.getProbabilities().length;
		}
	}
	
	private void computeProbability(int operateurIndex) {
		float term = 0;
		for(int i = 0; i < this.getProbabilities().length; i++) {
			term += this.getUtilities()[i];
		}
		if(term != 0.0) {
			this.probabilities[operateurIndex] = this.pMin + ((1 - this.getProbabilities().length*pMin) * (this.getUtilities()[operateurIndex] / term));
		}
		//System.out.println("prob: " + this.probabilities[operateurIndex]);
	}
	
	/**
	 * Choisi l'operateur en fonction de la probabilité calculé pour tous les opérateurs
	 * @param population qui permettra de calculer le nouvel alpha (distance de Hamming) pour l'utilité
	 * @return le tableau de booleen avec tous les operateurs et un boolean a TRUE indicant l'operateur choisi
	 */
	@Override
	public int getChoosenOperator(Population population) {
		int sumProbability = 0;
		for(int i = 0; i < this.getProbabilities().length; i++) {
			if(this.getIteration() != 0)
				computeProbability(i);
			sumProbability += Math.round(this.probabilities[i]*100000000);
			//System.out.println(this.probabilities[i]);
			//System.out.println("Proba i:" + i + "= " + this.probabilities[i]);
		}
		//System.out.println(sumProbability);
		int random = this.random.nextInt(sumProbability);
		//System.out.println(random);
		sumProbability = -1;
		int index = -1;
		while(sumProbability < random) {
			if(sumProbability == -1)
				sumProbability++;
			index ++;
			//System.out.println("SumProbability: " + sumProbability);
			sumProbability += Math.round(this.probabilities[index]*100000000);
	    }
		//System.out.println("Index:" + index);
		//System.out.println("longueur tableau proba:" + this.probabilities.length);
		//System.out.println("longueur tableau opérateurs:" + this.getOperateurs().length);
	    this.newIteration(population);
	    ++this.choosen[index];
	    return index;
	}
}
