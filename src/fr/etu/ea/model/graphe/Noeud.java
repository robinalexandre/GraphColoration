package fr.etu.ea.model.graphe;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;

import fr.etu.ea.Constants;

/**
 * A node in the graph.
 *
 * @author Shalin Shah
 * Email: shah.shalin@gmail.com
 */
public class Noeud implements Comparable<Noeud>
{    
    public int valeur;
    public int degre = 0;
    public LinkedHashSet<Integer> voisins = null;
    public Noeud suivant = null;
    public Noeud precedent = null;
    public int couleur = Constants.NONCOLORE;
    public int degreSaturation = 0;
    public List<Integer> couleursPossibles;
    public int nbCouleurs = 0;
    
    public Noeud(int v) {
        valeur = v;
        voisins = new LinkedHashSet<>();
        couleursPossibles = new ArrayList<>();
    }
    
    public Noeud(Noeud noeud) {
    		if(noeud != null) {
    			valeur = new Integer(noeud.valeur);
	        voisins = new LinkedHashSet<>();
	        if(noeud.voisins != null)
	        		voisins = new LinkedHashSet<>(noeud.voisins);
	        if(noeud.couleursPossibles != null)
	        		couleursPossibles = new ArrayList<>(noeud.couleursPossibles);
	        	degre = new Integer(noeud.degre);
	        suivant = noeud.suivant;
	        precedent = noeud.precedent;
	        couleur = new Integer(noeud.couleur);
	        degreSaturation = new Integer(noeud.degreSaturation);
	        nbCouleurs = new Integer(noeud.nbCouleurs);
    		}
    }
    

    public int compareTo(Noeud o) {
        Noeud obj = o;
        
        if(this.valeur == obj.valeur) {
            return 0;
        }
        
        if(this.degre < obj.degre)
            return 1;
        else if(this.degre > obj.degre)
            return -1;
        else
            return 0;
    }
    
    public boolean equals(Object obj) {
        Noeud o = (Noeud)obj;
        if(o.valeur == this.valeur)
            return true;
        
        return false;
    }
    
    public void ajouteArrete(int ev) {
        voisins.add(new Integer(ev));
    }
    
    public String toString() {
        return "Value=" + valeur + " Degree=" + degre;
    }
    
    public void coloreNoeud(int col) {
        couleur = col;
    }
    
    public boolean estCouleurValide(Graphe graph, int color) {
        Iterator<Integer> it = voisins.iterator();
        while(it.hasNext()) {
            int vertex = ((Integer)it.next()).intValue();
            Noeud node = graph.noeuds[vertex];
            if(node.couleur == color) {   
                return false;
            }
        }
        
        return true;
    }
    
    public Noeud next() {
        return suivant;
    }
    
    public Noeud previous() {
        return precedent;
    }
    
    public void computeDegreeSat(Graphe graph) {
        degreSaturation = 0;
        Iterator<Integer> it = voisins.iterator();
        while(it.hasNext()) {
            int vertex = ((Integer)it.next()).intValue();
            Noeud node = graph.noeuds[vertex];
            if(node.couleur != Constants.NONCOLORE) {
                degreSaturation++;
            }
        }
    }
 
    public int nextColor() {
        if(nbCouleurs == couleursPossibles.size()) {
            resetColorCount();
            couleur = Constants.NONCOLORE;
            return Constants.NONCOLORE;
        }
        
        int col = ((Integer)couleursPossibles.get(nbCouleurs)).intValue();
        nbCouleurs++;
        return col;
    }
    
    public void resetColorCount() {
        nbCouleurs = 0;
        couleur = Constants.NONCOLORE;
    }
    
    public void computePossibleColors(Graphe graph, int k) {
        couleursPossibles = new ArrayList<>();
        for(int i=1; i<=k; i++) {
            Iterator<Integer> it = voisins.iterator();
            boolean flag = true;
            while(it.hasNext()) {
                int vertex = ((Integer)it.next()).intValue();
                Noeud node = graph.noeuds[vertex];
                if(node.couleur == i) {
                    flag = false;
                    break;
                }
            }
            
            if(flag) {
                couleursPossibles.add(new Integer(i));
            }
        }
    }
    
    public List<Noeud> trouverNoeudsEnConflit(Graphe graph) {
        List<Noeud> conflicts = new ArrayList<>();
        Iterator<Integer> it = voisins.iterator();
        while(it.hasNext()) {
            int vertex = ((Integer)it.next()).intValue();
            Noeud node = graph.noeuds[vertex];
            if(node.couleur == this.couleur) {
                conflicts.add(node);
            }
        }
        
        return conflicts;
    }
}