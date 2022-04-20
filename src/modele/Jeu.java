package modele;

import java.awt.Point;
import java.util.HashMap;
import java.util.Observable;
import java.util.Random;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;

@SuppressWarnings( "deprecation" )

public class Jeu extends Observable {

    private Case[][] tabCases;
    private static Random rnd = new Random(4);
    public HashMap<Case, Point> hm;
    public boolean gameover;
    private Historique historique;
    private int score, highscore;
    public boolean mustBlink;


    public Jeu(int size) throws IOException{
        tabCases = new Case[size][size];
        hm = new HashMap<Case, Point>();
        gameover = false;
        ajouterRnd();
        ajouterRnd();
        historique = new Historique(10); // On garde les 10 coups précédents en mémoire
        historique.ajouterHist(hm, score);
        score = 0;
        highscore = loadHighScore();
        mustBlink = false;

    }

    public Jeu(Jeu jeu){
        tabCases = new Case[jeu.tabCases.length][jeu.tabCases.length];
        hm = tool.Tool.deepCopyHashMap(jeu.hm, this);
        construireGrille(hm);
        score = jeu.score;
        gameover = jeu.gameover;
        mustBlink = false;

    }

    public void affichageDebug(){
        for (int i = 0; i < tabCases.length; i++) {
            for (int j = 0; j < tabCases.length; j++) {
                if (tabCases[i][j]==null){
                    System.out.print("_  ");
                }else{
                    System.out.print(tabCases[i][j].getValeur()+"  ");
                }
            }
            System.out.println();
        }
        System.out.println();

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

    public int void_action(Direction d){       // Retourne > 0 s'il y a eu changement, 0 sinon.
        int ii,jj;
        int hasChanged = 0;
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
                    hasChanged += tabCases[ii][jj].deplacer(d);
                }
            }
        }
        for(int i = 0; i<tabCases.length; i++){
            for(int j = 0; j<tabCases.length; j++){
                if (tabCases[i][j] != null){
                    tabCases[i][j].resetState();
                }
            }
        }
        return hasChanged;
    }

    public void action(Direction d){
        new Thread() { // permet de libérer le processus graphique ou de la console
            public void run() {
                if (gameover){
                    System.out.println("Game OVER");
                    affichageDebug();

                    return;
                }

                if(void_action(d) > 0 /*&& hm.size()<tabCases.length*tabCases.length*/){
                    ajouterRnd();
                    historique.ajouterHist(hm, score);
                    affichageDebug();
                } else {
                    
                    System.out.println("blink bitch");
                    mustBlink = true;
                    affichageDebug();
                }
                System.out.println();
                setChanged();
                notifyObservers();
                testFinPartie();
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

    //Renvoie false si la case ne change pas de place
    public void ajouterCase(Case c, int i, int j){

        tabCases[i][j] = c;
        hm.put(c, new Point(i,j));
        //System.out.println("a"+i+" "+j);
    }

    public int mouvementCase(Case c, int i, int j) {    // Retourne false si la case ne fera pas de mouvement
        if(tabCases[i][j] == c) {
            return 0;
        } else {
            return 1;
        }
    }

    //Ajoute 1 ou 2 cases de valeurs aléatoires (2 ou 4)
    // attention s'il ne reste pas beaucoup de place dans la grille => deja géré dans action()
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

    /*public boolean testFinPartie1(){
        if(hm.size() < tabCases.length*tabCases.length){
            return false;
        }

        boolean hasChanged = false;
        //on copie l'etat actuel de la grille de jeu et de la hashmap pour potentiellement le restaurer après
        Case[][] tab_copy = tool.Tool.copy2Darray(this.tabCases);
        HashMap<Case, Point> hm_copy = tool.Tool.copyHashMap(this.hm);
        //ensuite on teste successivement chaque deplacement, en reinitialisant le jeu à son etat initial entre chaque
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
        if (!hasChanged){
            System.out.println("GAME OVER");
            gameover = true;
        }
        return hasChanged;
    }*/

    //Renvoie false si la partie est finie, ie plus aucun mouvement n'est possible
    public boolean testFinPartie(){
        if(hm.size() < tabCases.length*tabCases.length){
            return false;
        }
        //TODO: créer 4 jeux temporaires et tester les directions en parallèle avec du multithreading
        Jeu temp_jeu;
        boolean hasChanged = false;
        for(int k = 0; k<4; k++){
            temp_jeu = new Jeu(this);
            int hmsize = temp_jeu.hm.size();
            switch (k) {
                case 0:
                    temp_jeu.void_action(Direction.gauche);
                    break;
                case 1:
                    temp_jeu.void_action(Direction.droite);
                    break;
                case 2:
                    temp_jeu.void_action(Direction.haut);
                    break;
                case 3:
                    temp_jeu.void_action(Direction.bas);
                    break;
                default:
                    break;
            }
            if (temp_jeu.hm.size()<hmsize){//si il y a eu un mouvement effectif alors c'est forcement une fusion (car grille pleine)
                hasChanged = true;
            }
        }
        if (!hasChanged){
            System.out.println("GAME OVER");
            gameover = true;
        }
        return hasChanged;
    }

    public void construireGrille(HashMap<Case, Point> new_hm){
        hm = tool.Tool.deepCopyHashMap(new_hm);
        clearGrid();
        hm.forEach((c, pt)->{
            tabCases[pt.x][pt.y] = c;
        });
    }
    
    private void clearGrid(){
        for (int i = 0; i < tabCases.length; i++) {
            for (int j = 0; j < tabCases.length; j++) {
                tabCases[i][j] = null;
            }
        }
    }

    //Renvoie true si aucun soucis
    public boolean undoMove(){
        HashMap<Case, Point> new_hm = historique.moveBackwardHM();
        if (new_hm != null){
            //reconstruire la grille grace a la nouvelle hm
            construireGrille(new_hm);
            score = historique.getCurrentScore();
            //gerer le cas où la partie est perdu et le joueur veut revenir en arriere
            if (gameover){
                gameover = false;
                testFinPartie();
            }
            setChanged();
            notifyObservers();
            return true;
        }
        mustBlink = true;
        setChanged();
        notifyObservers();
        return false;
    }

    //Renvoie true si aucun soucis
    public boolean redoMove(){
        HashMap<Case, Point> new_hm = historique.moveForwardHM();
        if (new_hm != null){
            //reconstruire la grille grace a la nouvelle hm
            construireGrille(new_hm);
            score = historique.getCurrentScore();
            //gerer le cas où la partie est perdu et le joueur veut revenir en arriere
            testFinPartie();
            setChanged();
            notifyObservers();
            return true;
        }
        mustBlink = true;
        setChanged();
        notifyObservers();
        return false;
    }

    //0 si la case est null
    public int getValeur(int i, int j){
        if (tabCases[i][j] == null){
            return 0;
        }
        return tabCases[i][j].getValeur();
    }

    //Sauvegarde dans un fichier externe (syntaxe csv)
    public void saveToFile() throws IOException{
        PrintWriter printWriter = new PrintWriter(new FileWriter("save.csv"));

        //grid to csv
        //score, taille_grille
        printWriter.printf("%d,%d",score,tabCases.length);
        printWriter.println();
        for(int i = 0; i<tabCases.length; i++){
            for(int j = 0; j<tabCases.length-1; j++){
                printWriter.printf("%d,",getValeur(i,j));
            }
            printWriter.print(getValeur(i,tabCases.length-1));
            printWriter.println();
        }
        printWriter.close();
    }

    //TODO charger depuis sauvegarde
    public boolean loadFromFile(){
        try {
            File saveFile = new File("save.csv");
            if (!saveFile.exists()){
                return false;
            }
            Scanner saveScanner = new Scanner(saveFile);
            saveScanner.useDelimiter(",|\\n");

            //On reinitialise le jeu avec les nouveaux attributs
            score = saveScanner.nextInt();
            int size = saveScanner.nextInt();
            tabCases = new Case[size][size];
            historique = new Historique(10);
            hm = new HashMap<Case, Point>();

            for(int i = 0; i<size; i++){
                for(int j = 0; j<size; j++){
                    int val = saveScanner.nextInt();
                    if (val == 0){
                        tabCases[i][j] = null;
                    }else{
                        tabCases[i][j] = new Case(val, this);
                        hm.put(tabCases[i][j], new Point(i,j));
                    }
                }
            }
            saveScanner.close();

            gameover = false;
            testFinPartie();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return false;
        }
        
        setChanged();
        notifyObservers();
        return true;
    }


    public int getScore(){
        return score;
    }

    public void ajouterScore(int n){
        score+=n;
        if (score>highscore){
            highscore = score;
            updateHighScore();
        }
    }

    public int getHighScore(){
        return highscore;
    }

    public int loadHighScore() throws IOException{
        File hsFile = new File("highscore.csv");
        if (!hsFile.exists()){
            return 0;
        }
        Scanner hScanner = new Scanner(hsFile);
        hScanner.reset();
        if(!hScanner.hasNextInt()) {
            hScanner.close();
            return 0;
        }
        int hs = hScanner.nextInt();
        hScanner.close();
        return hs;
    }

    public void updateHighScore() {
        PrintWriter hsWriter;
        try {
            hsWriter = new PrintWriter(new FileWriter("highscore.csv"));
            hsWriter.print(highscore);
            hsWriter.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}

