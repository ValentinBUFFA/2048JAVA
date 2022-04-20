package tool;
import java.lang.Math;
import java.awt.Point;
import java.util.HashMap;

import modele.Case;
import modele.Jeu;

public class Tool {
    //Utile pour calculer la couleur de fond des tuiles
    public static int log2(int N){
        return (int)(Math.log(N)/Math.log(2));
    }

    //! Copie profonde nécessaire?
    public static Case[][] copy2Darray(Case[][] tab){
        Case[][] newTab = new Case[tab.length][];
        for(int i = 0; i<tab.length; i++){
            newTab[i] = tab[i].clone();
        }
        return newTab;
    }

    public static Case[][] deepCopy2Darray(Case[][] tab){
        Case[][] newTab = new Case[tab.length][tab.length];
        for(int i = 0; i<tab.length; i++){
            for(int j = 0; j<tab.length; j++){
                newTab[i][j] = new Case(tab[i][j]);
            }
        }
        return newTab;
    }    

    //Copie simple 
    public static HashMap<Case, Point> copyHashMap(HashMap<Case, Point> hm){
        HashMap<Case, Point> hm_copy = new HashMap<Case, Point>();
        hm_copy.putAll(hm);
        /*hm.forEach((c, pt)->{
            hm_copy.put(c, new Point(pt.x,pt.y));
        });*/
        return hm_copy;
    }

    //Copie profonde en recréant chaque Case et Point de la HashMap
    public static HashMap<Case, Point> deepCopyHashMap(HashMap<Case, Point> hm){
        HashMap<Case, Point> hm_copy = new HashMap<Case, Point>();
        hm.forEach((c, pt)->{
            hm_copy.put(new Case(c.getValeur(), c.getJeu()), new Point(pt.x,pt.y));
        });
        return hm_copy;
        
    }

    //Copie profonde en assignant les cases à un nouveau jeu
    public static HashMap<Case, Point> deepCopyHashMap(HashMap<Case, Point> hm, Jeu jeu){
        HashMap<Case, Point> hm_copy = new HashMap<Case, Point>();
        hm.forEach((c, pt)->{
            hm_copy.put(new Case(c.getValeur(), jeu), new Point(pt.x,pt.y));
        });
        return hm_copy;
    }
}
