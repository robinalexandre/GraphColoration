package fr.etu.ea;

import java.util.Calendar;

import fr.etu.chart.Chart;
import fr.etu.ea.apprentissage.SelectionAdaptiveRouletteWheel;
import fr.etu.ea.apprentissage.SelectionOperators;
import fr.etu.ea.model.Population;
import fr.etu.ea.model.graphe.Graphe;
import fr.etu.ea.model.operators.ComputeAllFitness;
import fr.etu.ea.model.operators.IOperators;
import fr.etu.ea.model.operators.crossover.ICrossover;
import fr.etu.ea.model.operators.insertion.IInsertion;
import fr.etu.ea.model.operators.mutation.IMutation;
import fr.etu.ea.model.operators.selection.ISelection;

public class RunAdaptiveRouletteWheel implements Runnable {

	private Integer populationSize;
	private Integer probabilityMutation;
	private Integer probabilityCroisement;
	private int nbSelection;
	private float pmin;
	private float alpha;
	private int windowSize;
	ISelection[] selection;
	ICrossover[] crossover;
	IMutation[] mutation;
	IInsertion[] insertion;
	private int mutationSelected;
	private int crossoverSelected;
	private Graphe graph;
	private long duration;
	
	public RunAdaptiveRouletteWheel(Integer populationSize, Integer probabilityMutation, Integer probabilityCroisement, int nbSelection, float pmin, float alpha, int windowSize, ISelection[] selection, ICrossover[] crossover, IMutation[] mutation, IInsertion[] insertion, Graphe graph, long duration) {
		super();
		this.populationSize = populationSize;
		this.probabilityMutation = probabilityMutation;
		this.probabilityCroisement = probabilityCroisement;
		this.nbSelection = nbSelection;
		this.pmin = pmin;
		this.alpha = alpha;
		this.windowSize = windowSize;
		this.selection = selection;
		this.crossover = crossover;
		this.mutation = mutation;
		this.insertion = insertion;
		this.graph = graph;
		this.duration = duration;
	}
	
	@Override
	public void run() {
		//Création du graphe
		Chart graph = new Chart("Adaptive Roulette Wheel");
		//Chart graphOperateurs = new Chart();
				
		Population population = new Population(populationSize, this.graph);
		int iteration = 0;
		ComputeAllFitness fitness = new ComputeAllFitness(population, null, null);
		
		SelectionOperators selectionsOperatorsCrossover = new SelectionAdaptiveRouletteWheel((IOperators[])this.crossover, this.alpha, this.pmin, this.windowSize);
		SelectionOperators selectionsOperatorsMutation = new SelectionAdaptiveRouletteWheel((IOperators[])this.mutation, this.alpha, this.pmin, this.windowSize);
		
		graph.addBestXY(iteration, fitness.getBestFitness());
		graph.addMoyenneXY(iteration, fitness.getAverageFitness());
		graph.addMinXY(iteration, fitness.getWorstFitness());
		
		long time = 0;
		long start = Calendar.getInstance().getTimeInMillis();
		while(time < start + this.duration) {
			time = Calendar.getInstance().getTimeInMillis();
			//System.out.println("------------ Iteration" + iteration + "--------------");
			crossoverSelected = selectionsOperatorsCrossover.getChoosenOperator(population);
			mutationSelected = selectionsOperatorsMutation.getChoosenOperator(population);
			if(iteration > 30)
				mutationSelected = this.mutation.length-1;
			Population parents = this.selection[0].selection(population, nbSelection);
			fitness = new ComputeAllFitness(parents, null, null);
			selectionsOperatorsCrossover.setBestFitness(fitness.getBestFitness());
			
			Population enfants = this.crossover[crossoverSelected].crossoverAll(parents, probabilityCroisement);
			
			fitness = new ComputeAllFitness(enfants, null, null);
			selectionsOperatorsCrossover.addLastPerformance(fitness.getBestFitness(), crossoverSelected);
			selectionsOperatorsMutation.setBestFitness(fitness.getBestFitness());
			
			this.mutation[mutationSelected].mutationAll(enfants, probabilityMutation);
			
			fitness = new ComputeAllFitness(enfants, null, null);
			selectionsOperatorsMutation.addLastPerformance(fitness.getBestFitness(), mutationSelected);
			
			this.insertion[0].insert(population, enfants);
						
			fitness = new ComputeAllFitness(population, null, null);
			
			graph.addBestXY(iteration, fitness.getBestFitness());
			graph.addMinXY(iteration, fitness.getWorstFitness());
			graph.addMoyenneXY(iteration, fitness.getAverageFitness());
			
			/*graphOperateurs.addKFlip1(iteration, mutations.getUtilities()[0]);
			graphOperateurs.addKFlip3(iteration, mutations.getUtilities()[1]);
			graphOperateurs.addKFlip5(iteration, mutations.getUtilities()[2]);
			graphOperateurs.addBitFlips(iteration, mutations.getUtilities()[3]);
			
			graphOperateurs.addUniforme(iteration, croisements.getUtilities()[0]);
			graphOperateurs.addOneCut(iteration, croisements.getUtilities()[1]);
			graphOperateurs.addTwoCuts(iteration, croisements.getUtilities()[2]);

			graphOperateurs.addAleatoire(iteration, selections.getUtilities()[0]);
			graphOperateurs.addBest(iteration, selections.getUtilities()[1]);
			graphOperateurs.addTournoi(iteration, selections.getUtilities()[2]);*/
			
			//graph.busyWait(0.005);
			
			iteration++;
			System.out.println("\n\nAdaptive Roulette------------Iteration "+ (iteration-1) +"------------");
			System.out.print("------------Best individu: "+ fitness.getBestIndex() +", Fitness: "+ fitness.getBestFitness() +"------------, kCouleurs: "+ fitness.getkCouleurs() +"------------\n");	 //Villes: \" + population.getIndividus()[fitness.getBestIndex()].toString()
		
		}
		System.out.println("\n\nAdaptive Roulette Wheel------------Iteration "+ (iteration-1) +"------------");
		System.out.print("------------Best individu: "+ fitness.getBestIndex() +", Fitness: "+ fitness.getBestFitness() +"------------\nVilles: " + population.getIndividus()[fitness.getBestIndex()].toString());			
	}

}
