package fr.etu.ea.model.graphe;

//Un noeud de la liste de noeuds basé sur les degrées du sous-graphe induit des additions possibles
public class NoeudDeListeTriee implements Comparable<NoeudDeListeTriee> {
    public int noeud = -1;
    public int atteint = 0;
    
    public int compareTo(NoeudDeListeTriee obj) {
        NoeudDeListeTriee n = obj;
        if(n.atteint > this.atteint) {
            return 1;
        } else if(n.atteint < this.atteint) {
            return -1;
        } else {
            return 0;
        }
    }
}