package fr.etu.ea.apprentissage;

import java.util.ArrayList;
import java.util.List;

import fr.etu.ea.model.Population;
import fr.etu.ea.model.operators.IOperators;

public class SelectionUCB extends SelectionOperators {
		
	/**
	 * Le nombre de fois qu'un opérateur à été choisi
	 */
	private int countOperators;

	/**
	 * Scaling factor for MAB based on UCB
	 */
	private float scalingFactor;
	
	/**
	 * Seuil de tolerance lors du calcul de la differnce entre la récompense actuel et la moyenne des récompenses actuelles pour le Dynamic MAB
	 */
	private float tolerance;
	
	private List<float[]> accumulationDiff;

	private List<float[]> difference;

	/**
	 * Seuil au dela duquel le test Page-Hinkley reinitialise les valeurs
	 */
	private float threshold;
	
	/**
	 * Construit une selection dite "MAB" (ajout du scalingFactor) basé on UCB héritant du constructeur de {@Selection} 
	 * {@inheritDoc Selection}
	 * @param operators
	 * @param balanceCoefficient
	 * @param pMin
	 * @param windowSize
	 */
	public SelectionUCB(IOperators[] operators, float balanceCoefficient, float pMin, int windowSize, float scalingFactor, float tolerance, float threshold) {
		super(operators.length, balanceCoefficient, pMin, windowSize);
		int size = operators.length;
		this.pMin = pMin;
		this.probabilities = new float[size];
		this.difference = new ArrayList<>(size);
		this.accumulationDiff = new ArrayList<>(size);
		for(int i = 0; i < size; i++) {
			this.probabilities[i] = (float)1/size;
			this.difference.add(new float[windowSize]);
			this.accumulationDiff.add(new float[windowSize]);
			for(int j = 0; j < windowSize; j++) {
				this.difference.get(i)[j] = 0;
				this.accumulationDiff.get(i)[j] = 0;
			}
		}
		this.scalingFactor = scalingFactor;
		this.countOperators = size;
		this.tolerance = tolerance;
		this.threshold = threshold;
	}
	
	private void computeProbability(int operateurIndex) {
		//System.out.println("Utilities: " + this.getUtilities()[operateurIndex]);
		this.probabilities[operateurIndex] = this.getUtilities()[operateurIndex] + this.scalingFactor * (float)Math.sqrt((2.0 * Math.log((float)this.countOperators)/(float)this.choosen[operateurIndex]));
		//System.out.println("count: " + this.countOperators);
		//System.out.println("prob per choosen: " + this.scalingFactor * (float)Math.sqrt((2.0 * Math.log((float)this.countOperators)/(float)this.choosen[operateurIndex])));
		//System.out.println("utilities: " + this.getUtilities()[operateurIndex]);
		//System.out.println("prob: " + this.probabilities[operateurIndex]);
	}
	
	/**
	 * Choisi l'operateur en fonction de la probabilité calculé pour tous les opérateurs
	 * @param population qui permettra de calculer le nouvel alpha (distance de Hamming) pour l'utilité
	 * @return le tableau de booleen avec tous les operateurs et un boolean a TRUE indicant l'operateur choisi
	 */
	@Override
	public int getChoosenOperator(Population population) {
		float higherProb = (float)-1.;
		int higherProbIndex = -1;
		for(int i = 0; i < this.probabilities.length; i++) {
			//System.out.println(i + "------------------------");
			if(this.getIteration() != 0)
				computeProbability(i);
			if(this.getProbabilities()[i] > higherProb) {
				higherProb = this.getProbabilities()[i];
				higherProbIndex = i;
			}
			//System.out.println("Probabilities: " + this.getProbabilities()[i]);
		}
	    this.newIteration(population);
	    //System.out.println("Lenght: " + this.probabilities.length + " index: " + higherProbIndex);
	    	++this.choosen[higherProbIndex];
	    //System.out.println("index choosen: " + index);
	    ++this.countOperators;
	    return higherProbIndex;
	}
	
	/**
	 * Permet la mise en place du DMAB (Dynamic MAB Algorithm)
	 */
	public void checkDynamic() {
		float[] averageGains = new float[this.probabilities.length];
		float[] lastAverageGains = new float[this.probabilities.length];
		for(int i = 0; i < this.probabilities.length; i++) {
			for(int j = 0; j < this.gains.get(i).length; j++) {
				averageGains[i] += this.gains.get(i)[j];
			}
			averageGains[i] /= this.gains.get(i).length;
			lastAverageGains[i] += this.gains.get(i)[this.choosen[i]%this.gains.get(i).length];
		
			this.difference.get(i)[this.choosen[i]%this.difference.get(i).length] = lastAverageGains[i] - averageGains[i] + this.tolerance;
			float accuDiff = 0;
			int indexMaxAccuDiff = 0;
			float valMaxAccuDiff = 0;
			for(int j = 0; j < this.difference.get(i).length; j++) {
				accuDiff += this.difference.get(i)[j];
				if(this.accumulationDiff.get(i)[indexMaxAccuDiff] < Math.abs(this.accumulationDiff.get(i)[j]))
					indexMaxAccuDiff = j;
				valMaxAccuDiff = Math.abs(this.accumulationDiff.get(i)[j]);
			}
			this.accumulationDiff.get(i)[this.countOperators%this.difference.get(i).length] = accuDiff;
			//System.out.println("accuDiff: " + accuDiff);
			//System.out.println("valMaxAccuDiff: " + valMaxAccuDiff);
			//System.out.println("-------------iteration: " + this.getIteration() + "--------------\n(valMaxAccuDiff - accuDiff): " + (valMaxAccuDiff - accuDiff));
			if((valMaxAccuDiff - accuDiff) > this.threshold) {
				System.out.println("\n\n----------- PH test for iteration: " + this.getIteration() + "--------------");
				this.countOperators -= this.choosen[i]-1;
				this.utility[i] = 0;
				this.choosen[i] = 1;
				for(int j = 0; j < this.gains.get(i).length; j++) {
					this.gains.get(i)[j] = 0;
					this.difference.get(i)[j] = 0;
					this.accumulationDiff.get(i)[j] = 0;
				}
			}
		}
	}
}
