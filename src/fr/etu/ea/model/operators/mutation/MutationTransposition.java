package fr.etu.ea.model.operators.mutation;

import fr.etu.ea.model.Individu;
import fr.etu.ea.util.Utilities;

public class MutationTransposition implements IMutation {

	@Override
	public void mutate(Individu individu) {
		System.out.println("Transposition");
		//System.out.print("individu.getGraph().noeuds[i] [");
//		for(int i = 0; i < individu.getGraph().noeuds.length; ++i)
//			System.out.print(individu.getGraph().noeuds[i].couleur + ", ");
//		System.out.println("]");
//		
//		System.out.println("individu.getGraph().couleurs  " + Arrays.toString(individu.getGraph().couleurs));
		
		int indexNoeud1 = Utilities.random.nextInt(individu.getGraph().noeuds.length);
		int indexNoeud2 = Utilities.random.nextInt(individu.getGraph().noeuds.length);
		boolean coloration = true;
		while(coloration) {
			while(indexNoeud1 == indexNoeud2)
				indexNoeud2 = Utilities.random.nextInt(individu.getGraph().noeuds.length);
			if(individu.getGraph().noeuds[indexNoeud1].estCouleurValide(individu.getGraph() ,individu.getGraph().noeuds[indexNoeud2].couleur) && individu.getGraph().noeuds[indexNoeud2].estCouleurValide(individu.getGraph() ,individu.getGraph().noeuds[indexNoeud1].couleur)) {
				int colorNoeud1 = individu.getGraph().noeuds[indexNoeud1].couleur;
				individu.getGraph().noeuds[indexNoeud1].coloreNoeud(individu.getGraph().noeuds[indexNoeud2].couleur);
				individu.getGraph().noeuds[indexNoeud2].coloreNoeud(colorNoeud1);
				coloration = false;
			}
		}
	}

}
