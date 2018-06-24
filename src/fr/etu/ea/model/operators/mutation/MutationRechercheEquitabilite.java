package fr.etu.ea.model.operators.mutation;

import java.util.Arrays;

import fr.etu.ea.model.Individu;
import fr.etu.ea.model.graphe.Noeud;
import fr.etu.ea.model.operators.mutation.MutationGreedy.ColorClass;
import fr.etu.ea.model.operators.mutation.MutationGreedy.ComparateurBaisse;

public class MutationRechercheEquitabilite implements IMutation {

	private ColorClass[] classes;

	@Override
	public void mutate(Individu individu) {
		System.out.println("Recherche Equitable");
		//		System.out.println("Nb noeuds total: " + individu.getGraph().noeuds.length);
		//		int tot = 0;
		//		for(ColorClass c : classes)
		//			if(c != null && c.noeuds.size() != 0)
		//				tot += c.noeuds.size();
		//		System.out.println("Nb noeuds total classes: " + tot);
		if(!equilibre(individu)) {
			System.out.println("Pas d'améliorations");
			int indexMinDegre = 0;
			int l = 0;
			for(Noeud noeud: classes[0].noeuds) {
				indexMinDegre = noeud.degre < classes[0].noeuds.get(indexMinDegre).degre ? indexMinDegre = l : indexMinDegre;
				++l;
			}
			int k = -1;
			for(int i=0; i<classes.length; i++) {
				if(k == -1) {
					k = classes[i].couleur;
				} else if(k < classes[i].couleur) {
					k = classes[i].couleur;
				}
			}

			int indexNoeud = 0;
			Noeud noeud = classes[0].noeuds.get(indexMinDegre);
			for(Noeud nd: individu.getGraph().noeuds) {
				if(nd.valeur == noeud.valeur)
					break;
				++indexNoeud;
			}
			classes[0].noeuds.remove(noeud);
			individu.getGraph().kColor++;
			noeud.coloreNoeud(individu.getGraph().kColor);
			individu.getGraph().noeuds[indexNoeud] = noeud;
		}
		for(int i = 0; i < individu.getGraph().noeuds.length; ++i)
			equilibre(individu);
	}

	private boolean equilibre(Individu individu) {
		classes = Individu.calculeClassesCouleurs(individu.getGraph().noeuds);
		int moyenne = 0;

		for(ColorClass classe : classes) {
//			System.out.println(classe);
			moyenne += classe.noeuds.size();
		}
		moyenne /= classes.length;
		boolean meilleur = false;
		Arrays.sort(classes, new ComparateurBaisse());
		for(ColorClass classe : classes) {
			int indexMinDegre = 0;
			int i = 0;
			if(classe.noeuds.size() < moyenne+1)
				break;
			for(Noeud noeud: classe.noeuds) {
				indexMinDegre = noeud.degre < classe.noeuds.get(indexMinDegre).degre ? indexMinDegre = i : indexMinDegre;
				++i;
			}
			Noeud noeud = classe.noeuds.get(indexMinDegre);
			boolean flag = true;
			int indexNoeud = 0;
			for(Noeud nd: individu.getGraph().noeuds) {
				if(nd.valeur == noeud.valeur)
					break;
				++indexNoeud;
			}
			int j = classes.length-1;
//			System.out.println("Classes: " + Arrays.toString(classes));
//			System.out.println("Moyenne: " + moyenne);
			while(flag && j >= 0) {	
				if(classes[j].noeuds.size() > moyenne-1) {
//					System.out.println("Break");
					break;
				}
				if(noeud.estCouleurValide(individu.getGraph(), classes[j].couleur) && classes[j].couleur != noeud.couleur) {
//					System.out.println("Meilleur equitabilité trouvée de la couleur " + noeud.couleur + " vers " + classes[j].couleur);
					classe.noeuds.remove(noeud);
					noeud.coloreNoeud(classes[j].couleur);
					classes[j].noeuds.add(noeud);
					individu.getGraph().noeuds[indexNoeud] = noeud;
					flag = false;
					meilleur = true;
				}
				--j;
			}
//			System.out.println("j: " + j);
			Arrays.sort(classes, new ComparateurBaisse());
		}
		return meilleur;
	}

}
