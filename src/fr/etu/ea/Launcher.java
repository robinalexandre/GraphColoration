package fr.etu.ea;

import fr.etu.ea.model.graphe.Graphe;
import fr.etu.ea.model.operators.crossover.ICrossover;
import fr.etu.ea.model.operators.crossover.NoCrossover;
import fr.etu.ea.model.operators.insertion.IInsertion;
import fr.etu.ea.model.operators.insertion.InsertionFitness;
import fr.etu.ea.model.operators.mutation.IMutation;
import fr.etu.ea.model.operators.mutation.MutationGreedy;
import fr.etu.ea.model.operators.mutation.MutationRechercheEquitabilite;
import fr.etu.ea.model.operators.mutation.MutationRechercheLocale;
import fr.etu.ea.model.operators.selection.ISelection;
import fr.etu.ea.model.operators.selection.SelectionTournoi;
import fr.etu.ea.util.Reader;

public class Launcher {

	public static void main(String[] args) {
		///Initialisation des valeurs
		Integer populationSize = 10;
				
		int probabilityMutation = 100; //Probability in %
		int probabilityCroisement = 0;
		
		int nbSelection = 2; //Nombre pair d'individu selectionnés
		
//		int windowSize = 20;
//		float pmin = (float) 0.1;
//		float alpha = (float) 0.35;
//		float beta = (float) 0.3;
//		float scalingFactor = (float) 0.6;
//		float tolerance = (float) 0.15;
//		float threshold = (float) 5;
		
		long duration = 45 * 1000;
		
		Graphe distance = Reader.AskGraph();
		
		ISelection[] selection = {new SelectionTournoi()};
		ICrossover[] crossover = {new NoCrossover()};
		IMutation[] mutation = {new MutationGreedy(), new MutationRechercheLocale(), new MutationRechercheEquitabilite()}; //, new MutationTransposition()
		IInsertion[] insertion = {new InsertionFitness()};
				
//		Thread thread = new Thread(new RunGeneticAlgorithm(populationSize, probabilityMutation, nbSelection, probabilityCroisement, (ISelection)new SelectionTournoi(), (IMutation)new Mutation5Scramble(), (ICrossover)new CrossoverOX(), (IInsertion) new InsertionFitness(), distance, duration));
//		thread.start();
		
//		Thread thread2 = new Thread(new RunAdaptiveRouletteWheel(populationSize, probabilityMutation, probabilityCroisement, nbSelection, pmin, alpha, windowSize, selection, crossover, mutation, insertion, distance, duration));
//		thread2.start();

//		Thread thread3 = new Thread(new RunAdaptivePursuit(populationSize, probabilityMutation, probabilityCroisement, nbSelection, pmin, beta, alpha, windowSize, selection, crossover, mutation, insertion, distance, duration));
//		thread3.start();
		
//		Thread thread4 = new Thread(new RunUCB(populationSize, probabilityMutation, probabilityCroisement, nbSelection, pmin, alpha, windowSize, scalingFactor, tolerance, threshold, selection, crossover, mutation, insertion, distance, duration));
//		thread4.start();
		
		Thread thread5 = new Thread(new RunFixedRoulette(populationSize, probabilityMutation, probabilityCroisement, nbSelection,  selection, crossover, mutation, insertion, distance, duration));
		thread5.start();
	}
}
