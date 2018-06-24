package fr.etu.ea.model.graphe;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;

import fr.etu.ea.Constants;

public class MaxClique {    
	
    public static LinkedHashSet<Integer> computeClique(Graphe graph) throws Exception {
        System.out.println("Calcul de la Clique...");
        LinkedHashSet<Integer> meilleurClique = new LinkedHashSet<>();
        
        graph.trieNoeuds();
        Noeud [] noeudsTries = graph.getNoeudsTries();
        Clique clique = new Clique(noeudsTries[0].valeur, graph);
        List<NoeudDeListeTriee> sList = clique.calculListeTriee();
        Iterator<NoeudDeListeTriee> ittt = sList.iterator();
        while(clique.ajoutsPossibles.size() > 0) {
            NoeudDeListeTriee node = (NoeudDeListeTriee)ittt.next();
            if(clique.ajoutsPossibles.contains(node.noeud)) {
                clique.ajouteSommet(node.noeud, graph.matrice);
            }
        }

        meilleurClique.addAll(clique.clique);
        for(int i=0; i<Constants.ANNEALING_ITERATIONS; i++) {
            /* Choisis deux sommets aléatoirement et les supprime de la clique */
            int rand1 = (int)(Math.random() * (double)clique.clique.size());
            int vertex1 = ((Integer)clique.cliqueListe.get(rand1)).intValue();
            int rand2 = (int)(Math.random() * (double)clique.clique.size());
            int vertex2 = ((Integer)clique.cliqueListe.get(rand2)).intValue();
            while(rand1 == rand2) {
                rand2 = (int)(Math.random() * (double)clique.clique.size());
                vertex2 = ((Integer)clique.cliqueListe.get(rand2)).intValue();
            }
            clique.supprimeSommet(vertex1, graph.matrice);
            clique.supprimeSommet(vertex2, graph.matrice);

            /* Ajoute des sommets à la clique basé sur sur le TreeSet des ajouts possible triées */
            if(clique.ajoutsPossibles.size() > 0) {
                List<NoeudDeListeTriee> sortedList = clique.calculListeTriee();
                Iterator<NoeudDeListeTriee> itt = sortedList.iterator();
                while(clique.ajoutsPossibles.size() > 0) {
                    NoeudDeListeTriee node = itt.next();
                    if(clique.ajoutsPossibles.contains(new Integer(node.noeud))) {
                        clique.ajouteSommet(node.noeud, graph.matrice);
                    }
                }
            }
            
            if(clique.clique.size() > meilleurClique.size()) {
                meilleurClique = new LinkedHashSet<Integer>();
                meilleurClique.addAll(clique.clique);
            }
        }

        /* 
         * Finalement, essaye d'améliorer la solution en 
         * considérant tous les sommets 1 par 1 en utilisant 1-OPT
         */
        int maxClique = meilleurClique.size();
        clique.clique = meilleurClique;
        clique.cliqueListe = new ArrayList<>();
        clique.cliqueListe.addAll(clique.clique);
        while(true) {
            boolean flag = false;
            for(int i=0; i<clique.clique.size(); i++) {
                int vertex = ((Integer)clique.cliqueListe.get(i)).intValue();
                clique.supprimeSommet(vertex, graph.matrice);
                List<NoeudDeListeTriee> sortedList = clique.calculListeTriee();
                Iterator<NoeudDeListeTriee> it = sortedList.iterator();
                while(clique.ajoutsPossibles.size() > 0) {
                    NoeudDeListeTriee node = (NoeudDeListeTriee)it.next();
                    if(clique.ajoutsPossibles.contains(new Integer(node.noeud))) {
                        clique.ajouteSommet(node.noeud, graph.matrice);
                    }
                }
                
                if(clique.clique.size() > maxClique) {
                    maxClique = clique.clique.size();
                    flag = true;
                    break;
                }
            }
            if(!flag) {
                break;
            }
        }
        
        meilleurClique = new LinkedHashSet<Integer>();
        meilleurClique.addAll(clique.clique);
        
        System.out.println("Taille de la plus grande clique: " + meilleurClique.size());
       
        return meilleurClique;
    }
}
