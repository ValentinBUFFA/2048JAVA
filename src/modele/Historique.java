package modele;

import java.util.HashMap;
import java.awt.Point;
import java.lang.Math;
import tool.Tool;

public class Historique {
    private HashMap<Case, Point>[] hm_hist;
    private int first_index, last_index, index;
    public Historique(int max){
        hm_hist = (HashMap<Case, Point>[])new HashMap[max];
        index = -1; // indice dans le tableau de la hashmap actuelle
        last_index = -1; // last written index
        //! RAJOUTER UN INDICE POUR LE DEBUT, CAR QUAND ON REJOUE CA "RESET" LE COMPTEUR DE DEBUT
    }

    public int ajouterHist(HashMap<Case, Point> hm){

        index++;
        last_index = index;// On enlève la possibilité de retourner 'dans le futur' si on joue un coup, normal
        hm_hist[index%hm_hist.length] = tool.Tool.deepCopyHashMap(hm);
        return index;
    }

    public HashMap<Case, Point> getLastHM(){
        if (index < 1 || index<last_index-hm_hist.length+2){
            System.out.println("pas possible de revenir en arriere");
            return null;
        }
        index--;
        System.out.println(index);
        return hm_hist[index%hm_hist.length];
    }

    public HashMap<Case, Point> getNextHM(){
        if (index == last_index){
            System.out.println("pas possible d'avancer davantage");
            return null;
        }
        index++;
        return hm_hist[index%hm_hist.length];
    }
    
}
