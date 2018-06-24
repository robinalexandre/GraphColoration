package fr.etu.ea.model.operators;

import java.util.List;

import fr.etu.ea.model.Individu;
import fr.etu.ea.model.Population;

public class ComputeAllFitness {
	
	private Integer bestFitness = null;
	private Integer bestIndex = null;
	private Integer worstFitness = null;
	private Integer worstIndex = null;
	private Integer averageFitness = null;
	private Individu[] individus;
	private List<Integer> indexWorst;
	private List<Integer> indexBest;
	private int BestKCouleurs;
	
	public ComputeAllFitness(Population population, List<Integer> bestIndexAldreadyUse, List<Integer> worstIndexAldreadyUse){
		this.individus = population.getIndividus();
		this.indexWorst = worstIndexAldreadyUse;
		this.indexBest = bestIndexAldreadyUse;
		this.averageFitness = 0;
		
		for(int i = 0; i < individus.length; i++){
			this.computeFitness(i);
		}
		setAverageFitness(getAverageFitness()/this.individus.length);
	}
	
	public synchronized Integer getBestIndex(){
		return this.bestIndex;
	}
	
	public synchronized Integer getBestFitness(){
		return this.bestFitness;
	}
	
	public synchronized void setBestFitness(Integer bestFitness){
		this.bestFitness = bestFitness;
	}
	
	public synchronized void setBestIndex(Integer bestIndex){
		this.bestIndex = bestIndex;
	}
	
	public synchronized Integer getWorstIndex(){
		return this.worstIndex;
	}
	
	public synchronized Integer getWorstFitness(){
		return this.worstFitness;
	}
	
	public synchronized void setWorstFitness(Integer worstFitness){
		this.worstFitness = worstFitness;
	}
	
	public synchronized void setWorstIndex(Integer worstIndex){
		this.worstIndex = worstIndex;
	}
	
	/**
	 * @return the averageFitness
	 */
	public int getAverageFitness() {
		return this.averageFitness;
	}

	/**
	 * @param averageFitness the averageFitness to set
	 */
	public void setAverageFitness(int averageFitness) {
		this.averageFitness = averageFitness;
	}

	public int getkCouleurs() {
		return BestKCouleurs;
	}

	private void computeFitness(final Integer index){
		int fitness = individus[index].fitness();	
		if((getBestFitness() == null || fitness < getBestFitness()) && (indexBest == null || !indexBest.contains(index))){
			setBestFitness(fitness);
			setBestIndex(index);
			this.BestKCouleurs = individus[index].getGraph().kColor;
		}
		
		if((getWorstFitness() == null || fitness > getWorstFitness()) && (indexWorst == null || !indexWorst.contains(index))){
			setWorstFitness(fitness);
			setWorstIndex(index);
		}
		setAverageFitness(getAverageFitness() + fitness);
	}

}
