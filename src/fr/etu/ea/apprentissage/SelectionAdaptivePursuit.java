package fr.etu.ea.apprentissage;

import java.util.Random;

import fr.etu.ea.model.Population;
import fr.etu.ea.model.operators.IOperators;

public class SelectionAdaptivePursuit extends SelectionOperators {

private Random random = new Random();
	
	/**
	 * L'index du meilleur opérateur
	 */
	private int bestIndexUtility;

	private float beta;
	
	/**
	 * Construit une selection dite "Adaptative Pursuit" héritant du constructeur de {@Selection} avec une valeur pMin permettant 
	 * de toujours avoir une probabilité supérieur à 0 pour tous les opérateurs
	 * {@inheritDoc Selection}
	 * @param operators
	 * @param balanceCoefficient
	 * @param pMin
	 * @param beta 
	 * @param windowSize
	 */
	public SelectionAdaptivePursuit(IOperators[] operators, float balanceCoefficient, float pMin, float beta, int windowSize) {
		super(operators.length, balanceCoefficient, pMin, windowSize);
		int size = operators.length;
		this.beta = beta;
		this.probabilities = new float[size];
		for(int i = 0; i < size; i++) {
			this.probabilities[i] = (float)1/size;
		}
	}
	
	private void computeProbability(int operateurIndex) {
		if(operateurIndex == this.bestIndexUtility)
			this.probabilities[operateurIndex] = this.probabilities[operateurIndex] + this.beta*((float)(1-(float)(this.probabilities.length-1)*this.pMin) - this.probabilities[operateurIndex]);
		else 
			this.probabilities[operateurIndex] = this.probabilities[operateurIndex] + this.beta*(this.pMin - this.probabilities[operateurIndex]);
		//System.out.println(this.probabilities[operateurIndex]);
	}
	
	
	
	/**
	 * Choisi l'operateur en fonction de la probabilité calculé pour tous les opérateurs
	 * @param population qui permettra de calculer le nouvel alpha (distance de Hamming) pour l'utilité
	 * @return le tableau de booleen avec tous les operateurs et un boolean a TRUE indicant l'operateur choisi
	 */
	@Override
	public int getChoosenOperator(Population population) {
		int sumProbability = 0;
		float bestUtility = -1;
		for(int i = 0; i < this.getUtilities().length; i++) {
			if(this.getUtilities()[i] > bestUtility) {
			    this.bestIndexUtility = i;
			    bestUtility = this.getUtilities()[i];
			}
		}
		for(int i = 0; i < this.probabilities.length; i++) {
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
