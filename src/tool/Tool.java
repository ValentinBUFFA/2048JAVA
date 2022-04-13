package tool;
import java.lang.Math;
import java.awt.Point;
import java.util.HashMap;

import modele.Case;

public class Tool {
    public static int log2(int N){
        return (int)(Math.log(N)/Math.log(2));
    }

    public static Case[][] copy2Darray(Case[][] tab){
        Case[][] newTab = new Case[tab.length][];
        for(int i = 0; i<tab.length; i++){
            newTab[i] = tab[i].clone();
        }
        return newTab;
    }

    public static HashMap<Case, Point> copyHashMap(HashMap<Case, Point> hm){
        HashMap<Case, Point> hm_copy = new HashMap<Case, Point>();
        
    }
}
