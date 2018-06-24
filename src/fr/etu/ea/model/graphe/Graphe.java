package fr.etu.ea.model.graphe;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import fr.etu.ea.Constants;

public class Graphe {

	public int [][] matrice;
	public  Noeud [] noeudsTries;
	public Noeud [] noeuds;
	public int kColor;
	public int[] couleurs;
	public LinkedHashSet<Integer> clique;

	public Graphe() {
		matrice = new int[Constants.NUMBER_NODES][Constants.NUMBER_NODES];
		noeudsTries = new Noeud[Constants.NUMBER_NODES];
		noeuds = new Noeud[Constants.NUMBER_NODES];
		for(int i=0; i<Constants.NUMBER_NODES; i++) {
			noeuds[i] = new Noeud(i);
			noeudsTries[i] = noeuds[i];
		}
		try {
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public Graphe(Graphe graph) {
		matrice = new int[Constants.NUMBER_NODES][Constants.NUMBER_NODES];
		noeudsTries = new Noeud[Constants.NUMBER_NODES];
		if(graph.clique != null) {
			clique = this.shuffle(graph.clique);
		}
		noeuds = new Noeud[Constants.NUMBER_NODES];
		for(int i=0; i<Constants.NUMBER_NODES; i++) {
			noeuds[i] = new Noeud(graph.noeuds[i]);
			noeudsTries[i] = new Noeud(noeuds[i]);
			for(int j=0; j<Constants.NUMBER_NODES; j++) {
				matrice[i][j] = graph.matrice[i][j];
			}
		}
		this.kColor = new Integer(graph.kColor);
		if(graph.couleurs != null) {
			couleurs = new int[graph.couleurs.length];
			for(int i = 0; i<graph.couleurs.length; i++) {
				this.couleurs[i] = new Integer(graph.couleurs[i]);
			}
		}		
	}

	public Graphe(Graphe graph, boolean b) {
		matrice = new int[Constants.NUMBER_NODES][Constants.NUMBER_NODES];
		noeudsTries = new Noeud[Constants.NUMBER_NODES];
		if(graph.clique != null) {
			clique = this.shuffle(graph.clique);
		}
		noeuds = new Noeud[Constants.NUMBER_NODES];
		for(int i=0; i<Constants.NUMBER_NODES; i++) {
			noeuds[i] = new Noeud(graph.noeuds[i]);
			noeuds[i].couleur = Constants.NONCOLORE;
			noeudsTries[i] = new Noeud(noeuds[i]);
			for(int j=0; j<Constants.NUMBER_NODES; j++) {
				matrice[i][j] = graph.matrice[i][j];
			}
		}
		if(graph.couleurs != null) {
			couleurs = new int[graph.couleurs.length];
			for(int i = 0; i<graph.couleurs.length; i++) {
				this.couleurs[i] = new Integer(graph.couleurs[i]);
			}
		}		
	}

	public void trieNoeuds() {
		this.noeudsTries = Arrays.copyOf(this.noeuds, this.noeuds.length);
		Arrays.sort(noeudsTries);
	}

	public Noeud [] getNoeudsTries() {
		return noeudsTries;
	}

	public LinkedHashSet<Integer> shuffle(Set<Integer> set) {
		List<Integer> shuffleMe = new ArrayList<>(set);
		Collections.shuffle(shuffleMe);
		LinkedHashSet<Integer> shuffledSet = new LinkedHashSet<>();
		shuffledSet.addAll(shuffleMe); 
		return shuffledSet;              
	}

	public void ajouteArrete(int i, int j) {
		matrice[i][j] = 1;
		matrice[j][i] = 1;
		Noeud noeud = noeuds[i];
		if(noeud == null) {
			noeud = new Noeud(i);
			noeuds[i] = noeud;
			noeud.ajouteArrete(j);
			noeudsTries[i] = noeud;
			noeud.degre++;
		} else {
			noeud.ajouteArrete(j);
			noeud.degre++;
		}

		noeud = noeuds[j];
		if(noeud == null) {
			noeud = new Noeud(j);
			noeuds[j] = noeud;
			noeud.ajouteArrete(i);
			noeudsTries[j] = noeud;
			noeud.degre++;
		} else {
			noeud.ajouteArrete(i);
			noeud.degre++;
		}
	}

	public void initColors() {
		/* Colore les sommets de la clique avec des couleurs différentes */
		this.kColor = this.clique.size();
		Iterator<Integer> it = this.clique.iterator();
		int col = 1;
		while(it.hasNext()) {
			int vertex = ((Integer)it.next()).intValue();
			this.noeuds[vertex].coloreNoeud(col);
			col++;
		}

		PossibleColorsComparator comparateur = new PossibleColorsComparator();
		TreeSet<Noeud> noeudsNonColores = new TreeSet<>(comparateur);
		List<Noeud> listeNoeud = new ArrayList<>();
		LinkedHashSet<Integer> flags = new LinkedHashSet<>();
		for(int i=0; i<this.noeuds.length; i++) {
			if(this.noeuds[i].couleur == Constants.NONCOLORE) {
				this.noeuds[i].computeDegreeSat(this);
				noeudsNonColores.add(this.noeuds[i]);
				listeNoeud.add(this.noeuds[i]);
				flags.add(new Integer(this.noeuds[i].valeur));
			}
		}
		while(!noeudsNonColores.isEmpty()) {
			Noeud noeud = (Noeud)noeudsNonColores.first();
			noeudsNonColores.remove(noeud);
			flags.remove(new Integer(noeud.valeur));
			//System.out.println(uncoloredNodes.size());
			for(int i=1; ;i++) {
				if(noeud.estCouleurValide(this, i)) {
					noeud.coloreNoeud(i);
					LinkedHashSet<Integer> list = noeud.voisins;
					it = list.iterator();
					while(it.hasNext()) {
						int vertex = ((Integer)it.next()).intValue();
						if(noeudsNonColores.contains(this.noeuds[vertex])) {
							noeudsNonColores.remove(this.noeuds[vertex]);       
							this.noeuds[vertex].computeDegreeSat(this);
							noeudsNonColores.add(this.noeuds[vertex]);
						}
					}

					if(i > this.kColor) {
						this.kColor = i;
					}
					break;
				}
			}
		}
		System.out.println(this.kColor + " coloring found during initialisation using DSatur");
	}
	
	public int isWellFormed(){   
		int grapheBienColorie=0;
		int i=0;
		int j;
		while (i<this.noeuds.length) {
			j=0;
			while (j<this.noeuds.length) {   
				if (((this.matrice[i][j]>0) && (i!=j) && (this.noeuds[i].couleur == this.noeuds[j].couleur)) || this.noeuds[i].couleur == Constants.NONCOLORE) {
					grapheBienColorie++;
				}
				j++;
			}
			i++;
		}		
		return (int) Math.ceil(grapheBienColorie/2);
    }

	public static class PossibleColorsComparator implements Comparator<Noeud> {
		public int compare(Noeud o1, Noeud o2) {
			Noeud node1 = o1;
			Noeud node2 = o2;
			if(node1.valeur == node2.valeur) {
				return 0;
			}

			if(node1.degreSaturation < node2.degreSaturation) {
				return 1;
			} else {
				return -1;
			}
		}
	}
}
