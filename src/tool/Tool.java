package tool;
import java.lang.Math;
import java.awt.Point;
import java.util.HashMap;

import modele.Case;

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

    //Copie simple 
    public static HashMap<Case, Point> copyHashMap(HashMap<Case, Point> hm){
        HashMap<Case, Point> hm_copy = new HashMap<Case, Point>();
        hm_copy.putAll(hm);
        return hm_copy;
    }

    //Copie profonde en recréant chaque Case et POint de la HashMap
    public static HashMap<Case, Point> deepCopyHashMap(HashMap<Case, Point> hm){
        HashMap<Case, Point> hm_copy = new HashMap<Case, Point>();
        hm.forEach((c, pt)->{
            hm_copy.put(new Case(c.getValeur(), c.getJeu()), new Point(pt.x,pt.y));
        });
        return hm_copy;
        
    }
}
