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
import fr.etu.ea.util.Utilities;
import fr.etu.ea.util.Writer;

public class RunFixedRoulette implements Runnable {


	private Integer populationSize;
	private Integer probabilityMutation;
	private Integer probabilityCroisement;
	private int nbSelection;
	ISelection[] selection;
	ICrossover[] crossover;
	IMutation[] mutation;
	IInsertion[] insertion;
	private int mutationSelected;
	private int crossoverSelected;
	private Graphe graph;
	private long duration;

	public RunFixedRoulette(Integer populationSize, Integer probabilityMutation, Integer probabilityCroisement, int nbSelection, ISelection[] selection, ICrossover[] crossover, IMutation[] mutation, IInsertion[] insertion, Graphe graph, long duration) {
		super();
		this.populationSize = populationSize;
		this.probabilityMutation = probabilityMutation;
		this.probabilityCroisement = probabilityCroisement;
		this.nbSelection = nbSelection;		
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
		Chart graph = new Chart("Fixed Roulette");
//		Chart graphChoosen = new Chart();

		Population population = new Population(populationSize, this.graph);
		int iteration = 0;
		ComputeAllFitness fitness = new ComputeAllFitness(population, null, null);
 
		graph.addBestXY(iteration, fitness.getBestFitness());
		graph.addMoyenneXY(iteration, fitness.getAverageFitness());
		graph.addMinXY(iteration, fitness.getWorstFitness());

		long time = 0;
		long start = Calendar.getInstance().getTimeInMillis();
		while(time < start + this.duration) {
			System.out.println("\n\n-------------------------Iteration "+ (iteration) +"------------");

			time = Calendar.getInstance().getTimeInMillis();
//			System.out.println("------------ Iteration" + iteration + "--------------");

			crossoverSelected = Utilities.random.nextInt(this.crossover.length);
			mutationSelected = Utilities.random.nextInt(this.mutation.length);

			Population parents = this.selection[0].selection(population, nbSelection);
			Population enfants = this.crossover[crossoverSelected].crossoverAll(parents, probabilityCroisement);
			this.mutation[mutationSelected].mutationAll(enfants, probabilityMutation);
			this.insertion[0].insert(population, enfants);
			
			fitness = new ComputeAllFitness(population, null, null);

			graph.addBestXY(iteration, fitness.getBestFitness());
			graph.addMinXY(iteration, fitness.getWorstFitness());
			graph.addMoyenneXY(iteration, fitness.getAverageFitness());
			
//			graphChoosen.addSeriesScramble5(iteration, selectionsOperatorsMutation.getChoosen()[0]);
//			graphChoosen.addSeriesSwap5(iteration, selectionsOperatorsMutation.getChoosen()[1]);
//			graphChoosen.addSeriesInversion5(iteration, selectionsOperatorsMutation.getChoosen()[2]);
//			graphChoosen.addSeriesMoveAfter5(iteration, selectionsOperatorsMutation.getChoosen()[3]);
//
//			graphChoosen.addSeriesScramble3(iteration, selectionsOperatorsMutation.getChoosen()[4]);
//			graphChoosen.addSeriesSwap3(iteration, selectionsOperatorsMutation.getChoosen()[5]);
//			graphChoosen.addSeriesInversion3(iteration, selectionsOperatorsMutation.getChoosen()[6]);
//			graphChoosen.addSeriesMoveAfter3(iteration, selectionsOperatorsMutation.getChoosen()[7]);
//
//			graphChoosen.addSeriesScramble1(iteration, selectionsOperatorsMutation.getChoosen()[8]);
//			graphChoosen.addSeriesSwap1(iteration, selectionsOperatorsMutation.getChoosen()[9]);
//			graphChoosen.addSeriesInversion1(iteration, selectionsOperatorsMutation.getChoosen()[10]);
//			graphChoosen.addSeriesMoveAfter1(iteration, selectionsOperatorsMutation.getChoosen()[11]);

			iteration++;
			System.out.println("Fixed Roulette ------------ Best individu: "+ fitness.getBestIndex() +", Fitness: "+ fitness.getBestFitness() +"------------ , kCouleurs: "+ fitness.getkCouleurs() +"------------Nb de problèmes: " + population.getIndividus()[fitness.getBestIndex()].getGraph().isWellFormed() + "\n");
			
			//Villes: \" + population.getIndividus()[fitness.getBestIndex()].toString()
		}
		System.out.println("\n\nFixed Roulette------------Nombre total d'iterations "+ (iteration-1) +"------------");
		System.out.print("------------Best individu: "+ fitness.getBestIndex() +", Fitness: "+ fitness.getBestFitness() +"------------\n");	//Villes: \" + population.getIndividus()[fitness.getBestIndex()].toString()
		Writer.writeOutFile(population.getIndividus()[fitness.getBestIndex()].getGraph());
	}
}
