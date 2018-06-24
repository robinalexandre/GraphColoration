package fr.etu.ea.model;

import fr.etu.ea.model.graphe.Graphe;
import fr.etu.ea.model.graphe.Noeud;
import fr.etu.ea.model.operators.mutation.MutationGreedy.ColorClass;

public class Individu {

	private Graphe graph;

	public Individu(Graphe graph) {
		this.graph = graph;
	}

	public Individu(Individu individu) {
		this.graph = new Graphe(individu.graph);
	}

	public Graphe getGraph() {
		return graph;
	}

	public void setGraph(Graphe graph) {
		this.graph = graph;
	}

	public int fitness() {
		ColorClass[] classes = calculeClassesCouleurs(this.graph.noeuds);
		int penalite = 0;
		//		System.out.println("----------------------------");
		int moyenne = 0;
		for(ColorClass classe : classes)
			if(classe != null)
				moyenne += classe.noeuds.size();
		moyenne /= classes.length;
		for(ColorClass couleur : classes) {
			if(couleur != null) {
				int diff = Math.abs(moyenne - couleur.noeuds.size());
				if(diff > 1) {
					penalite += diff-1;
				}
			}
		}
		//		System.out.println("moyenne: " + moyenne);
		//		System.out.println("penalite: " + penalite);
		return (int) (this.graph.kColor + Math.ceil(penalite/3));
	}

	public static ColorClass[] calculeClassesCouleurs(Noeud[] order) {
		int k = -1;
		for(int i=0; i<order.length; i++) {
			if(k == -1) {
				k = order[i].couleur;
			} else if(k < order[i].couleur) {
				k = order[i].couleur;
			}
			//			System.out.println("Couleur noeud i("+i+"): " + order[i].couleur);
		}
//		System.out.println("k: " + k);

		ColorClass [] classes = new ColorClass[k];
		for(int i=0; i<order.length; i++) {
			int color = order[i].couleur;
			if(classes[color-1] == null) {
				classes[color-1] = new ColorClass();
			}
			classes[color-1].couleur = color;
			classes[color-1].noeuds.add(order[i]);
		}
//		System.out.println(Arrays.toString(classes));
		return classes;
	}

	public String toString() {
		return "" + this.fitness();
	}
}

