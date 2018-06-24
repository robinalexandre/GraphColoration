package fr.etu.ea.apprentissage;

import java.util.ArrayList;
import java.util.List;

import fr.etu.ea.model.Population;

/**
 * Permet de faire une selection d'un opérateur entre différents opérateurs du meme type (mutation, croisement...)
 * @author Alex
 *
 */
public abstract class SelectionOperators {
	
	/**
	 * Tableau contenant pour chaque operateurs la moyenne de ces ameliorations/gains réalisé sur le meilleur fitness de la population;
	 */
	protected List<float[]> gains;
	
	/**
	 * Tableau contenant l'utilité de chaque opérateur
	 */
	protected float[] utility;
	
	/**
	 * Coefficient alpha d'apprentissage permettant une balance entre l'utilité passé et actuelle
	 */
	protected float alpha;
	
	/**
	 * Meilleur fitness de la population actuelle
	 */
	private float bestFitness;
	
	/**
	 * Nombre d'iterations realisees
	 */
	private int iteration;
	
	/**
	 * Nombre fois choisis
	 */
	protected int[] choosen;
	
	/**
	 * Permet de toujours avoir une probabilité supérieur à 0 pour tous les opérateurs
	 */
	protected float pMin;

	/**
	 * La probabilite d'être choisi pour chaque operateur
	 */
	protected float[] probabilities;

	private float bestGain;
	
	/**
	 * Construit une nouvelle instance de selection par apprentissage des opérateurs
	 * 
	 * @param operateurs un tableau de booleens correspondant aux opérateurs 
	 * @param balanceCoefficient le coefficient alpha d'apprentissage permettant une balance entre l'utilité passé et actuelle
	 * @param windowSize entier indicant la taille de la fenetre de gain à prendre en compte lors du calcul de l'utilité
	 */
	protected SelectionOperators(int size, float balanceCoefficient, float pMin, int windowSize) {
		this.utility = new float[size];
		this.choosen = new int[size];
		this.gains = new ArrayList<>();
		for(int i = 0; i < size; i++) {
			this.utility[i] = 0;
			this.choosen[i] = 1;
			float[] gains = new float[windowSize];
			for(int j = 0; j < gains.length; j++) {
				gains[j] = 0;
			}
			this.gains.add(i, gains);
		}
		this.alpha = balanceCoefficient;
		this.bestFitness = 0;
		this.iteration = 1;
		this.pMin = pMin;
		this.bestGain = 0;
	}



	public float[] getProbabilities() {
		return this.probabilities;
	}
	
	/**
	 * @return the choosen
	 */
	public int[] getChoosen() {
		return this.choosen;
	}



	/**
	 * @param choosen the choosen to set
	 */
	public void setChoosen(int[] choosen) {
		this.choosen = choosen;
	}



	/**
	 * @return les utilités des operateurs
	 */
	public float[] getUtilities() {
		return this.utility;
	}
	
	/**
	 * @return the operateurs
	 */
	protected int getIteration() {
		return this.iteration;
	}

	/**
	 * @return the bestFitness
	 */
	public float getBestFitness() {
		return this.bestFitness;
	}

	/**
	 * @param bestFitness the bestFitness to set
	 */
	public void setBestFitness(float bestFitness) {
		this.bestFitness = bestFitness;
	}

	/**
	 * Ajoute la derniere performance (dernier meilleur fitness - meilleur fitness actuel) pour l'operateur donné et ainsi calculé le gain et la nouvelle utilité
	 * @param bestFitness le nouveau meilleur fitness normalisé;
	 * @param indexOperateur l'index de l'opérateur qui à réalisé ce fitness
	 */
	public void addLastPerformance(float bestFitness, int indexOperateur) { //Normaliser ici
		for(int j = 0; j < this.gains.size(); j++) {
			if(j == indexOperateur) {
				float gain = this.bestFitness - bestFitness;
				if(gain > 0) {
					if(gain > this.bestGain)
						this.bestGain = gain;
//					System.out.println("Gain pour l'operateur" + indexOperateur + ": "+ gain);
					this.gains.get(j)[(this.choosen[indexOperateur]-1)%this.gains.get(j).length] = gain/this.bestGain;
//					System.out.println("Iteration: " + this.iteration + "\nGain:" + gain/this.bestGain + "operateur: " + j);
				} else { 
					this.gains.get(j)[(this.choosen[indexOperateur]-1)%this.gains.get(j).length] = 0;
				}
			}
			if(this.iteration % this.gains.get(0).length*10 == 0)
				this.bestGain = 0;
			float totalGain = 0;
			float[] gainsOperators = this.gains.get(j);
			for(int i = 0; i < gainsOperators.length; i++) {
				totalGain += gainsOperators[i];
			}
//			System.out.println(totalGain);
			this.utility[j] = this.computeUtility(j, totalGain/(this.iteration <= gainsOperators.length ? this.iteration-1 : gainsOperators.length));
		}
	}

	/**
	 * Calcul la nouvelle utilité 
	 * @param indexOperateur l'index de l'operateur dont on veut calculer l'utilité
	 * @return la nouvelle utilité
	 */
	private float computeUtility(int indexOperateur, float gain) {
		float a = (1-this.alpha) * this.utility[indexOperateur];
		float b = this.alpha * gain;
		return a + b;
	}
	
	protected void newIteration(Population population) {
		this.iteration++;
	}
	
	/**
	 * Choisi l'operateur en fonction de la probabilité calculé pour tous les opérateurs
	 * @return le tableau de booleen avec tous les operateurs et un boolean a TRUE indicant l'operateur choisi
	 */
	public abstract int getChoosenOperator(Population population);
}
