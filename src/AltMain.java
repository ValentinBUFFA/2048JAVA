import modele.Direction;
import modele.Jeu;
import modele.Case;

public class AltMain {
    public static void main(String[] args) {
        Jeu jeu = new Jeu(4);
  
        Case v = jeu.getVoisinAv(0, 1, Direction.droite);
        System.out.println(v);
    }
}
