package modele;

import java.awt.Point;

public class Case {
    private int valeur;
    private Jeu jeu;
    private boolean state;

    public Case(int _valeur, Jeu _jeu) {
        valeur = _valeur;
        jeu = _jeu;
        state = true;
    }
    public Case(int _valeur) {
        valeur = _valeur;
    }

    public int getValeur() {
        return valeur;
    }
    public Jeu getJeu(){
        return jeu;
    }
    
    public boolean deplacer(Direction d){ // Retourne true s'il y a eu changement, false sinon.
        Point pt = this.jeu.hm.get(this); 
        boolean hasChanged = true;
        int i = pt.x;// les coordonnées du point sont inversées
        int j = pt.y;// pour correspondre au point de vue "matrice"
        Case voisin;
        int k = -1;
        //On trouve le premier voisin non null en se déplaçant dans la direction donnée
        do{
            k++;  
            switch (d) {
                case gauche:
                    voisin = jeu.getVoisinAv(i, j-k, d);
                    break;
                case droite:
                    voisin = jeu.getVoisinAv(i, j+k, d);
                    break;
                case bas:
                    voisin = jeu.getVoisinAv(i+k, j, d);
                    break;
                case haut:
                    voisin = jeu.getVoisinAv(i-k, j, d);
                    break;
                default:
                    voisin = new Case(-1);
                    break;
            }
        }while (voisin == null);
        //k correspond au nombre de pas effectués dans la direction d pour arriver à la case avant le premier voisin non null
        
        //Si fusion possible: on fusionne
        if (voisin.getValeur() == this.getValeur() && voisin.state){
            voisin.doubler();
            jeu.supprimerCase(this, i, j);
            //System.out.println("d"+i+" "+j);
        }else{//sinon, on relocalise la case à l'endroit adapté 
            
            switch (d) {
                case gauche:
                    hasChanged = jeu.mouvementCase(this, i, j-k);
                    jeu.supprimerCase(this, i, j);
                    jeu.ajouterCase(this, i, j-k);
                    break;
                case droite:
                    hasChanged = jeu.mouvementCase(this, i, j+k);
                    jeu.supprimerCase(this, i, j);  
                    jeu.ajouterCase(this, i, j+k);
                    break;
                case bas:
                    hasChanged = jeu.mouvementCase(this, i+k, j);
                    jeu.supprimerCase(this, i, j);
                    jeu.ajouterCase(this, i+k, j);
                    break;
                case haut:
                    hasChanged = jeu.mouvementCase(this, i-k, j);
                    jeu.supprimerCase(this, i, j);
                    jeu.ajouterCase(this, i-k, j);
                    break;
                default:
                    break;
            }
        }
        return hasChanged;
    }

    public void doubler(){
        this.valeur*=2;
        this.state = false;//Empeche de fusionner 2 fois dans le meme deplacement
        jeu.ajouterScore(this.valeur);
    }
    public void resetState(){
        this.state = true;
    }
}
