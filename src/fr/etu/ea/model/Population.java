package fr.etu.ea.model;

import java.util.LinkedHashSet;

import fr.etu.ea.model.graphe.Graphe;
import fr.etu.ea.model.graphe.MaxClique;

public class Population {
	
	Individu[] individus;

	public Population(Integer populationSize, Graphe graph) {
		this.individus = new Individu[populationSize];
		
		LinkedHashSet<Integer> clique = null;
		/* Calcul Clique (Sommets adjacents)  */
		try {
			clique = MaxClique.computeClique(graph);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		graph.clique = clique;
		for(int i = 0; i < populationSize; ++i) {
			Graphe graph2 = new Graphe(graph, true);
			graph2.initColors();
			this.individus[i] = new Individu(graph2);
		}
	}
	
	public Population(Integer populationSize) {
		this.individus = new Individu[populationSize];
	}
	
	public Population(Population population) {
		this.individus = new Individu[population.getTaille()];
		for(int i = 0; i < population.getTaille(); ++i) {
			this.individus[i] = new Individu(population.getIndividus()[i]);
			this.individus[i].getGraph().clique = population.getIndividus()[i].getGraph().shuffle(population.getIndividus()[i].getGraph().clique);
		}
	}

	public Individu[] getIndividus() {
		return this.individus;
	}
	
	public void setIndividus(Individu[] population) {
		this.individus = population;
	}
	
	public int getTaille() {
		return this.individus.length;
	}
	
	public String toString() {
		String pop = "";
		for(int i = 0; i < this.getTaille(); i++) {
			pop += "Individu: " + i + "\n" + individus[i].toString() + "\n";
		}
		return pop;
	}
}
