package fr.etu.ea.model.operators.mutation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import fr.etu.ea.Constants;
import fr.etu.ea.model.Individu;
import fr.etu.ea.model.graphe.Noeud;

public class MutationGreedy implements IMutation {

	@Override
	public void mutate(Individu individu) {
		System.out.println("Greedy");
		individu.getGraph().couleurs = new int[individu.getGraph().noeuds.length];
		for(int i=0; i<individu.getGraph().noeuds.length; i++) {
			individu.getGraph().couleurs[i] = individu.getGraph().noeuds[i].couleur;
		}

		for(int i=0; i<Constants.ITERATIONS; i++) {
			double rand = Math.random();
			Noeud [] ordre = null;
			double left = 0;
			double right = 0;
			right+=Constants.ALEATOIRE;
			boolean choisi = false;
			if(rand < right && rand > left && !choisi) {
				ordre = ordreAleatoire(individu.getGraph().noeuds);
				choisi = true;
			}

			left = right;
			right+=Constants.DECROISSANCE;
			if(rand < right && rand > left && !choisi) {
				ordre = ordreDecroissant(individu.getGraph().noeuds);
				choisi = true;
			}

			left = right;
			right+=Constants.CROISSANCE;
			if(rand < right && rand > left && !choisi) {
				ordre = ordreCroissant(individu.getGraph().noeuds);
				choisi = true;
			}

			left = right;
			right+=Constants.INVERSE;
			if(rand < right && rand > left && !choisi) {
				ordre = ordreInverse(individu.getGraph().noeuds);
				choisi = true;
			}

			int maxCouleur = -1;
			for(int n=0; n<ordre.length; n++) {
				ordre[n].couleur = Constants.NONCOLORE;
			}

			for(int n=0; n<ordre.length; n++) {
				for(int c=1;;c++) {
					if(ordre[n].estCouleurValide(individu.getGraph(), c)) {
						ordre[n].coloreNoeud(c);
						if(maxCouleur == -1) {
							maxCouleur = c;
						} else if(maxCouleur < c) {
							maxCouleur = c;
						}
						break;
					}
				}
			}

			if(maxCouleur < individu.getGraph().kColor) {
				System.out.println("Found Better Coloring - " + maxCouleur);
				individu.getGraph().kColor = maxCouleur;
			}
			for(int j=0; j<individu.getGraph().couleurs.length; j++) {
				individu.getGraph().couleurs[j] = individu.getGraph().noeuds[j].couleur;
			}
		}
	}


	public static Noeud [] ordreAleatoire(Noeud [] order) {
		List<Noeud> ns = new ArrayList<Noeud>(Arrays.asList(order));
		Collections.shuffle(ns);
		Noeud [] nodes = new Noeud[order.length];
		for(int i=0; i<order.length; i++) {
			nodes[i] = (Noeud)ns.get(i);
		}
		return nodes;
	}

	public static Noeud [] ordreDecroissant(Noeud [] order)
	{
		ColorClass[] classes = Individu.calculeClassesCouleurs(order);
		for(int i = 0; i < classes.length; ++i) {
			if(classes[i] == null)
				classes[i] = new ColorClass();
		}

		Arrays.sort(classes, new ComparateurBaisse());
		for(int i=0; i<classes.length; i++) {
			Collections.reverse(classes[i].noeuds);
		}

		Noeud [] noeuds = new Noeud[order.length];
		int compteur = 0;
		for(int i=0; i<classes.length; i++) {
			ColorClass cls = classes[i];
			List<Noeud> nds = cls.noeuds;
			for(Iterator<Noeud> it = nds.iterator(); it.hasNext();) {
				noeuds[compteur] = (Noeud)it.next();
				compteur++;
			}
		}
		return noeuds;
	}

	public static Noeud [] ordreCroissant(Noeud [] order) {
		ColorClass[] classes = Individu.calculeClassesCouleurs(order);
		for(int i = 0; i < classes.length; ++i)
			if(classes[i] == null)
				classes[i] = new ColorClass();
		
		Arrays.sort(classes, new ComparateurAugmentation());
		for(int i=0; i<classes.length; i++) {
			Collections.reverse(classes[i].noeuds);
		}

		Noeud [] nodes = new Noeud[order.length];
		int count = 0;
		for(int i=0; i<classes.length; i++) {
			ColorClass cls = classes[i];
			List<Noeud> nds = cls.noeuds;
			for(Iterator<Noeud> it = nds.iterator(); it.hasNext();) {
				nodes[count] = (Noeud)it.next();
				count++;
			}
		}
		return nodes;
	}

	public static Noeud [] ordreInverse(Noeud [] order) {
		List<Noeud> ns = new ArrayList<Noeud>(Arrays.asList(order));
		Collections.reverse(ns);
		Noeud [] nodes = new Noeud[order.length];
		for(int i=0; i<nodes.length; i++) {
			nodes[i] = (Noeud)ns.get(i);
		}

		return nodes;
	}

	public static class ColorClass {
		public List<Noeud> noeuds;
		public int couleur;
		public ColorClass() {
			noeuds = new ArrayList<Noeud>();
			couleur = -1;
		}		
		
		public String toString() {
			return "Nb de noeuds: " + noeuds.size() + " couleur: " + couleur;
		}
	}

	public static class ComparateurBaisse implements Comparator<ColorClass> {
		public int compare(ColorClass obj1, ColorClass obj2) {
			if(obj1.noeuds.size() > obj2.noeuds.size()) {
				return -1;
			} else if(obj1.noeuds.size() < obj2.noeuds.size()) {
				return 1;
			} else {
				return 0;
			}
		}
	}

	public static class ComparateurAugmentation implements Comparator<ColorClass> {
		public int compare(ColorClass obj1, ColorClass obj2) {
			if(obj1.noeuds.size() > obj2.noeuds.size()) {
				return 1;
			} else if(obj1.noeuds.size() < obj2.noeuds.size()) {
				return -1;
			} else {
				return 0;
			}
		}
	}

	public String toString() {
		return this.getClass().getSimpleName();
	}
}
