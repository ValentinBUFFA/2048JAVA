package tool;
import java.lang.Math;
import java.awt.Point;
import java.io.File;
import java.util.HashMap;

import java.awt.Color;

import modele.Case;
import modele.Jeu;

public class Tool {
    //Utile pour calculer la couleur de fond des tuiles
    public static int log2(int N){
        return (int)(Math.log(N)/Math.log(2));
    }

    //Copie simple, en gardant les adresses
    public static Case[][] copy2Darray(Case[][] tab){
        Case[][] newTab = new Case[tab.length][];
        for(int i = 0; i<tab.length; i++){
            newTab[i] = tab[i].clone();
        }
        return newTab;
    }

    //! Pas utilisé
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
    //! Pas utilisé
    public static HashMap<Case, Point> copyHashMap(HashMap<Case, Point> hm){
        HashMap<Case, Point> hm_copy = new HashMap<Case, Point>();
        hm_copy.putAll(hm);
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

    // pr entre 0 et 1000
    public static Color fadeTo(Color origin, Color dest, int pr){
        int or = origin.getRed(), og = origin.getGreen(), ob = origin.getBlue(), dr = dest.getRed(), dg = dest.getGreen(), db = dest.getBlue();
        return new Color((int)((or*(1000-pr)+dr*pr)/1000), (int)((og*(1000-pr)+dg*pr)/1000), (int)((ob*(1000-pr)+db*pr)/1000));
    }

    public static String[] saveSearch() {
        File f = new File("saves");    
        String[] s = f.list();
        return s;
    }

    public static boolean saveNameCompare(String filename) {
        File f = new File("saves");    
        String[] list = f.list();
        if (list.length > 0) {
            for (int i = 0; i < list.length ; i++) {
                if (filename.equals(list[i])) {
                    return true;
                }
            }
        }
        return false;
    }

    public static void checkSaveDir() {
        String dir = "saves";
        File file = new File(dir);
        if (!file.exists()) {
             // true if the directory was created, false otherwise
            if (file.mkdirs()) {
                System.out.println("Directory is created!");
            } else {
                System.out.println("Failed to create directory!");
            }
        }
    }
}
