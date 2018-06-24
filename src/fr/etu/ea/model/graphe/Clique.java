package fr.etu.ea.model.graphe;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;

import fr.etu.ea.Constants;

public class Clique {    
	public LinkedHashSet<Integer> ajoutsPossibles;
	public LinkedHashSet<Integer> clique;
	/*TreeMap des ajouts possibles classées selon leurs degrées */
	public TreeMap<Noeud, Noeud> ajoutsPossiblesTriees;
	public Graphe graph;
	public List<Integer> cliqueListe;

	public Clique(int firstVertex, Graphe graph) {
		int [][] matrice = graph.matrice;
		ajoutsPossibles = new LinkedHashSet<>();
		clique = new LinkedHashSet<>();
		ajoutsPossiblesTriees = new TreeMap<>();
		cliqueListe = new ArrayList<>();

		clique.add(new Integer(firstVertex));
		cliqueListe.add(new Integer(firstVertex));
		this.graph = graph;
		for(int i=0; i<Constants.NUMBER_NODES; i++) {
			if(i == firstVertex) {
				continue;
			}
			if(matrice[i][firstVertex] == 1) {
				ajoutsPossibles.add(new Integer(i));
				ajoutsPossiblesTriees.put(graph.noeuds[i], graph.noeuds[i]);
			}
		}   
	}

	public void ajouteSommet(int sommet, int [][] matrice) {
		clique.add(new Integer(sommet));
		cliqueListe.add(new Integer(sommet));
		supprimeajoutsPossiblesTriees(graph.noeuds[sommet]);
		ajoutsPossibles.remove(new Integer(sommet));
		for(Iterator<Integer> it = ajoutsPossibles.iterator(); it.hasNext();) {
			int pavertex = ((Integer)it.next()).intValue();
			if(matrice[pavertex][sommet] == 0) {
				it.remove();
				supprimeajoutsPossiblesTriees(graph.noeuds[pavertex]);
			}
		}
	}

	public void supprimeSommet(int sommet, int [][] matrice) {
		if(!clique.contains(new Integer(sommet)))
			return;

		clique.remove(new Integer(sommet));
		cliqueListe.remove(new Integer(sommet));
		for(int i=0; i<Constants.NUMBER_NODES; i++) {
			if(clique.contains(new Integer(i))) {
				continue;
			}
			else {
				Iterator<Integer> it = clique.iterator();
				boolean flag = true;
				while(it.hasNext()) {
					int ver = ((Integer)it.next()).intValue();
					if(matrice[i][ver] == 0) {
						flag = false;
						break;
					}
				}
				if(flag) {
					ajoutsPossibles.add(new Integer(i));
					ajoutsPossiblesTriees.put(graph.noeuds[i], graph.noeuds[i]);
				}
			}
		}
	}

	private void supprimeajoutsPossiblesTriees(Noeud node) {
		Set<Noeud> set = ajoutsPossiblesTriees.keySet();
		Iterator<Noeud> it = set.iterator();
		while(it.hasNext()) {
			if(it.next().equals(node)) {
				it.remove();
				break;
			}
		}
	}

	/* 
	 * Calcul une liste de noeuds basé sur les degrées du sous-graphe induit des ajouts possibles
	 */
	public List<NoeudDeListeTriee> calculListeTriee() {
		List<NoeudDeListeTriee> sortedList = new ArrayList<>();
		Iterator<Integer> it = ajoutsPossibles.iterator();
		while(it.hasNext()) {
			int node1 = ((Integer)it.next()).intValue();
			int reach = 0;
			Iterator<Integer> itt = ajoutsPossibles.iterator();
			while(itt.hasNext()) {
				int node2 = ((Integer)itt.next()).intValue();
				if(graph.matrice[node1][node2] == 1) {
					reach++;
				}
			}
			NoeudDeListeTriee n = new NoeudDeListeTriee();
			n.atteint = reach;
			n.noeud = node1;
			sortedList.add(n);
		}
		Collections.sort(sortedList);
		return sortedList;
	}
}
