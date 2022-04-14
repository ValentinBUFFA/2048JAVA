import modele.Direction;
import modele.Jeu;
import modele.Case;
import java.util.HashMap;

import javax.swing.text.Position;

import java.awt.Point;

public class AltMain {
    public static void main(String[] args) {
        /*HashMap<Case, Point> hm = new HashMap<Case, Point>();
        Case c = new Case(1);
        hm.put(c, new Point(2,2));
        hm.put(new Case(2), new Point(3,2));

        HashMap<Case, Point> hm_copy = new HashMap<Case, Point>();
        hm_copy.putAll(hm);
        /*for(int i = 0; i<hm.size(); i++){
            hm_copy.put()
        }
        hm.remove(c);
        hm.put(new Case(1), new Point(4,4));
        System.out.println(hm.toString());
        System.out.println(hm_copy.toString());*/

        Case[][] tab = new Case[][]{
            {new Case(1),new Case(2)},
            {new Case(3),new Case(4)}
        };
        Case[][] newTab = new Case[tab.length][];
        for(int i = 0; i<tab.length; i++){
            newTab[i] = tab[i].clone();
        }

        for(int i = 0; i<tab.length; i++){
            System.out.println(tab[i].toString());
        }
        System.out.println();
        for(int i = 0; i<newTab.length; i++){
            System.out.println(newTab[i].toString());
        }
    }
}
