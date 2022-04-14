package modele;

import java.util.HashMap;
import java.awt.Point;

public class Historique {
    private HashMap<Case, Point>[] hm_hist;
    public Historique(int max){
        hm_hist = (HashMap<Case, Point>[])new HashMap[max];
    }
    
}
