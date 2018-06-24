package fr.etu.ea;

import java.util.Calendar;

import fr.etu.chart.Chart;
import fr.etu.ea.model.Population;
import fr.etu.ea.model.graphe.Graphe;
import fr.etu.ea.model.operators.ComputeAllFitness;
import fr.etu.ea.model.operators.crossover.ICrossover;
import fr.etu.ea.model.operators.insertion.IInsertion;
import fr.etu.ea.model.operators.mutation.IMutation;
import fr.etu.ea.model.operators.selection.ISelection;

public class RunGeneticAlgorithm implements Runnable {
	
	private Integer populationSize;
	private int probabilityMutation;
	private int probabilityCroisement;
	private int nbSelection;
	private ISelection selection;
	private ICrossover crossover;
	private IMutation mutation;
	private IInsertion insertion;
	private Graphe graph;
	private long duration;

	public RunGeneticAlgorithm(Integer populationSize, int probabilityMutation,
			int nbSelection, int probabilityCroisement, ISelection selection,
			IMutation mutation, ICrossover crossover, IInsertion insertion, Graphe graph, long duration) {
		super();
		this.populationSize = populationSize;
		this.probabilityMutation = probabilityMutation;
		this.probabilityCroisement = probabilityCroisement;
		this.nbSelection = nbSelection;
		this.selection = selection;
		this.mutation = mutation;
		this.crossover = crossover;
		this.insertion = insertion;
		this.graph = graph;
		this.duration = duration;
	}

	@Override
	public void run() {
		//Création du graphe
		Chart graph = new Chart("Genetic Algorithm");
				
		Population population = new Population(populationSize, this.graph);
		int iteration = 0;
		ComputeAllFitness fitness = new ComputeAllFitness(population, null, null);
		
		graph.addBestXY(iteration, fitness.getBestFitness());
		graph.addMoyenneXY(iteration, fitness.getAverageFitness());
		graph.addMinXY(iteration, fitness.getWorstFitness());
		
		long time = 0;
		long start = Calendar.getInstance().getTimeInMillis();
		while(time < start + this.duration) {	
			time = Calendar.getInstance().getTimeInMillis();
//			System.out.println("------------ Iteration" + iteration + "--------------");

			Population parents = this.selection.selection(population, nbSelection);
			Population enfants = this.crossover.crossoverAll(parents, probabilityCroisement);
			this.mutation.mutationAll(enfants, probabilityMutation);
			this.insertion.insert(population, enfants);

			fitness = new ComputeAllFitness(population, null, null);
			graph.addBestXY(iteration, fitness.getBestFitness());
			graph.addMoyenneXY(iteration, fitness.getAverageFitness());
			graph.addMinXY(iteration, fitness.getWorstFitness());
			
			iteration++;
		}
		System.out.println("\n\nGA ------------Iteration "+ (iteration-1) +"------------");
		System.out.print("------------Best individu: "+ fitness.getBestIndex() +", Fitness: "+ fitness.getBestFitness() +"------------\nVilles: " + population.getIndividus()[fitness.getBestIndex()].toString());
	}
}
