package fr.etu.ea.model.operators.mutation;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import fr.etu.ea.model.Individu;
import fr.etu.ea.model.graphe.Noeud;

public class MutationRechercheLocale implements IMutation {

	@Override
	public void mutate(Individu individu) {
		System.out.println("Recherche Locale");

		individu.getGraph().couleurs = new int[individu.getGraph().noeuds.length];
		int maxColor = 0;
		for(int i=0; i<individu.getGraph().couleurs.length; i++) {
			individu.getGraph().couleurs[i] = individu.getGraph().noeuds[i].couleur;
			if(maxColor < individu.getGraph().couleurs[i])
				maxColor = individu.getGraph().couleurs[i];
		}
		
		for(int i=0; i<individu.getGraph().noeuds.length; i++) {
			Noeud node = individu.getGraph().noeuds[i];
			if(node.couleur == individu.getGraph().kColor) {
				int c = ((int)(Math.random()*(double)maxColor)) + 1;
				node.coloreNoeud(c);
//				System.out.println("Nouvelle couleur: " + c);
			}   
		}

		for(int n=0; n<1000; n++) {
			List<Noeud> conflicts = new ArrayList<Noeud>();
			for(int i=0; i<individu.getGraph().noeuds.length; i++) {
				Noeud node = individu.getGraph().noeuds[i];
				if(!node.estCouleurValide(individu.getGraph(), node.couleur)) {
					conflicts.add(node);
				}
			}
			//System.out.println(conflicts.size());

			if(conflicts.size() == 0) {
				maxColor = 0;
				for(int i=0; i<individu.getGraph().couleurs.length; i++) {
					individu.getGraph().couleurs[i] = individu.getGraph().noeuds[i].couleur;
					if(maxColor < individu.getGraph().couleurs[i])
						maxColor = individu.getGraph().couleurs[i];
				}
				System.out.println("Found without conflicts Coloring - " + (maxColor));
				individu.getGraph().kColor = maxColor;
				break;
			} else {
				changeColorsRandomly(conflicts, maxColor);
				conflicts = new ArrayList<Noeud>();
				for(int i=0; i<individu.getGraph().noeuds.length;i++)
				{
					Noeud node = individu.getGraph().noeuds[i];
					if(!node.estCouleurValide(individu.getGraph(), node.couleur))
					{
						conflicts.add(node);
					}
				}
			}

			for(int i=0; i<1000; i++)  {
				if(conflicts.size() == 0) {
					break;
				}

				int rand = (int)(Math.random() * conflicts.size());
				Noeud node = conflicts.get(rand);
				int bestcolor = -1;
				int bestconflicts = -1;
				for(int c=1; c<=maxColor; c++) {
					node.couleur = c;
					List<Noeud> con = node.trouverNoeudsEnConflit(individu.getGraph());

					if(con.size() == 0) {
						bestcolor = c;
						conflicts.remove(node);
						break;
					}

					if(bestcolor == -1) {
						bestcolor = c;
						bestconflicts = con.size();
					} else {
						if(bestconflicts > con.size()) {
							bestconflicts = con.size();
							bestcolor = c;
						}
					}
				}
				node.couleur = bestcolor;
			}
		}
	}

	public static void changeColorsRandomly(List<Noeud> conflicts, int k) {
		Iterator<Noeud> it = conflicts.iterator();
		while(it.hasNext()) {
			Noeud node = (Noeud)it.next();
			int color = ((int)(Math.random()*k)) + 1;
			node.couleur = color;
		}
	}
}
