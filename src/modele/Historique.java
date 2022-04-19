package modele;

import java.util.HashMap;
import java.awt.Point;
import java.lang.Math;
import tool.Tool;

public class Historique {
    private HashMap<Case, Point>[] hm_hist;
    private int[] score_hist;
    private int first_index, last_index, index;

    public Historique(int max){
        hm_hist = (HashMap<Case, Point>[])new HashMap[max];
        score_hist = new int[max];
        first_index = 0;//premier indice plein
        last_index = -1; // last written index
        index = -1; // indice dans le tableau de la hashmap actuelle
        //! RAJOUTER UN INDICE POUR LE DEBUT, CAR QUAND ON REJOUE CA "RESET" LE COMPTEUR DE DEBUT
    }

    public int ajouterHist(HashMap<Case, Point> hm, int score){
        index++;
        last_index = index;// On enlève la possibilité de retourner 'dans le futur' si on joue un coup, normal
        if (first_index == last_index - hm_hist.length){
            first_index++;
            //System.out.println("fi");
        }

        System.out.println("+:"+first_index+" "+index+" "+" "+last_index);

        hm_hist[index%hm_hist.length] = tool.Tool.deepCopyHashMap(hm);
        score_hist[index%hm_hist.length] = score;
        return index;
    }

    public HashMap<Case, Point> getLastHM(){
        if (index < 1 || index == first_index){
            System.out.println("pas possible de revenir en arriere");
            return null;
        }
        index--;

        //System.out.println("get_last:"+first_index+" "+index+" "+" "+last_index);

        return hm_hist[index%hm_hist.length];
    }

    public HashMap<Case, Point> getNextHM(){
        if (index == last_index){
            System.out.println("pas possible d'avancer davantage");
            return null;
        }
        index++;
        
        //System.out.println("get_next:"+first_index+" "+index+" "+" "+last_index);

        return hm_hist[index%hm_hist.length];
    }
    
    public int getCurrentScore(){
        //System.out.println("ca bouge:"+score_hist[index%hm_hist.length]);

        return score_hist[index%hm_hist.length];
    }
}
