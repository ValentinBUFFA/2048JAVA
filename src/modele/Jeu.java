package modele;

import java.awt.Point;
import java.util.HashMap;
import java.util.Observable;
import java.util.Random;

import tool.Tool;

import javax.swing.text.Position;
@SuppressWarnings( "deprecation" )

public class Jeu extends Observable {

    private Case[][] tabCases;
    private static Random rnd = new Random(4);
    public HashMap<Case, Point> hm;
    public boolean gameover;

    public Jeu(int size) {
        tabCases = new Case[size][size];
        hm = new HashMap<Case, Point>();
        gameover = false;
        ajouterRnd();
        ajouterRnd();
    }

    public void affichageDebug(){
        for (int i = 0; i < tabCases.length; i++) {
            for (int j = 0; j < tabCases.length; j++) {
                if (tabCases[i][j]==null){
                    System.out.print('_');
                }else{
                    System.out.print(tabCases[i][j].getValeur());
                }
            }
            System.out.println();
        }
    }

    public int getSize() {
        return tabCases.length;
    }

    public Case getCase(int i, int j) {
        if ((i<0 || i>=tabCases.length)||(j<0 || j>=tabCases.length)){
            return new Case(-1);
        }
        return tabCases[i][j];
    }


    public void rnd() {
        Jeu pjeu = this;
        new Thread() { // permet de libérer le processus graphique ou de la console
            public void run() {
                int r;

                for (int i = 0; i < tabCases.length; i++) {
                    for (int j = 0; j < tabCases.length; j++) {
                        r = rnd.nextInt(3);

                        switch (r) {
                            case 0:
                                tabCases[i][j] = null;
                                break;
                            case 1:
                                tabCases[i][j] = new Case(2,pjeu);
                                hm.put(tabCases[i][j],new Point(i,j));
                                break;
                            case 2:
                                tabCases[i][j] = new Case(4,pjeu);
                                hm.put(tabCases[i][j],new Point(i,j));
                                break;
                        }
                    }
                }
                setChanged();
                notifyObservers();

            }

        }.start();

    }

    public void void_action(Direction d){
        int ii,jj;
        for(int i = 0; i<tabCases.length; i++){
            ii = i;
            if (d == Direction.bas){
                ii = (tabCases.length-1-i);
            }
            for(int j = 0; j<tabCases.length; j++){
                jj = j;
                if (d == Direction.droite){
                    jj = (tabCases.length-1-j);
                }
                if (tabCases[ii][jj] != null){
                    tabCases[ii][jj].deplacer(d);
                }
            }
            //System.out.println();
        }
        for(int i = 0; i<tabCases.length; i++){
            for(int j = 0; j<tabCases.length; j++){
                if (tabCases[i][j] != null){
                    tabCases[i][j].resetState();
                }
            }
        }
    }

    public void action(Direction d){
        new Thread() { // permet de libérer le processus graphique ou de la console
            public void run() {
                if (gameover){
                    System.out.println("GAME OVER");
                    return;
                }
                void_action(d);
                affichageDebug();
                if(hm.size()<tabCases.length*tabCases.length){
                    ajouterRnd();
                }
                setChanged();
                notifyObservers();
                if(hm.size()==tabCases.length*tabCases.length && testFinPartie()==false){
                    System.out.println("GAME OVER");
                    gameover = true;
                }
            }

        }.start();
    }

    public Case getVoisinAv(int i, int j, Direction d){
        switch (d) {
            case gauche:
                return getCase(i, j-1);
            case droite:
                return getCase(i, j+1);
            case bas:
                return getCase(i+1, j);
            case haut:
                return getCase(i-1, j);
            default:
                return new Case(-1);
        }
    }

    public void supprimerCase(Case c, int i, int j){
        tabCases[i][j] = null;
        //System.out.println("s"+i+" "+j);
        hm.remove(c);
    }

    //Renvoie false si la coordonnée de la grille de jeu n'est pas vide
    public boolean ajouterCase(Case c, int i, int j){
        if (tabCases[i][j]!=null){
            return false;            
        }
        tabCases[i][j] = c;
        hm.put(c, new Point(i,j));
        //System.out.println("a"+i+" "+j);
        return true;
    }

    //Ajoute 1 ou 2 cases de valeurs aléatoires (2 ou 4)
    //! attention si il ne reste pas beaucoup de place dans la grille
    // =>deja géré par ajouterCase??
    //On compte le nombre de "cases" null et on garde leurs coordonnées en mémoire
    public void ajouterRnd(){
        int nbNulls = 0;
        Point[] nulls = new Point[tabCases.length*tabCases.length];
        for (int i = 0; i < tabCases.length; i++) {
            for (int j = 0; j < tabCases.length; j++) {
                if (tabCases[i][j] == null){
                    nulls[nbNulls]=new Point(i,j);
                    nbNulls++;

                }
            }
        }
        //int nbRnd = rnd.nextInt(1)+1;
        int nbRnd = 1;
        int rIndex;
        Point pt;
        for(int k = 0; k<nbRnd; k++){
            rIndex = rnd.nextInt(nbNulls);
            pt = nulls[rIndex];
            ajouterCase(new Case((rnd.nextInt(1)+1)*2, this), pt.x, pt.y);
        }
    }

    //Renvoie false si la partie est finie, ie plus aucun mouvement n'est possible
    //On devrait rentrer dans cette fonction que si la hashmap est pleine(ie toutes les cellules sont occupées)
    public boolean testFinPartie(){
        boolean hasChanged = false;
        //on copie l'etat actuel de la grille de jeu et de la hashmap pour potentiellement le restaurer après
        Case[][] tab_copy = tool.Tool.copy2Darray(this.tabCases);
        HashMap<Case, Point> hm_copy = tool.Tool.copyHashMap(this.hm);
        //ensuite on teste successivement chaque deplacement, en reinitialisant le jeu à son etat initial entre chaque
        //TODO faire du multithreading pour tester les 4 en //
        for(int k = 0; k<4; k++){
            switch (k) {
                case 0:
                    void_action(Direction.gauche);
                    break;
                case 1:
                    void_action(Direction.droite);
                    break;
                case 2:
                    void_action(Direction.haut);
                    break;
                case 3:
                    void_action(Direction.bas);
                    break;
                default:
                    break;
            }
            if (hm.size()<hm_copy.size()){//si il y a eu un mouvement effectif alors c'est forcement une fusion (car grille pleine)
                hasChanged = true;
            }
            //Puis on remet l'etat initial avant le prochain test
            this.tabCases = tool.Tool.copy2Darray(tab_copy);
            this.hm = tool.Tool.copyHashMap(hm_copy);
        }
        return hasChanged;
    }
}