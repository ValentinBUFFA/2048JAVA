package vue_controleur;

import modele.Case;
import modele.Jeu;
import modele.Direction;

import tool.Tool;//Pour log2

import javax.swing.*;
import javax.swing.border.Border;

import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Observable;
import java.util.Observer;
@SuppressWarnings( "deprecation" )

public class Swing2048 extends JFrame implements Observer {
    private static final int PIXEL_PER_SQUARE = 120;
    // tableau de cases : i, j -> case graphique
    private JLabel[][] tabC;
    private Jeu jeu;
    private java.awt.Color[] tile_bg_colors = new java.awt.Color[]{
        new java.awt.Color(77,63,40),//null
        new java.awt.Color(57,42,26),//2
        new java.awt.Color(71,55,23),//4
        new java.awt.Color(127,65,12),//8
        new java.awt.Color(142,54,9),//16
        new java.awt.Color(144,34,8),//32
        new java.awt.Color(166,37,8),//64
        new java.awt.Color(97,77,12),//128
        new java.awt.Color(105,83,12),//256
        new java.awt.Color(237,200,80),//512
        new java.awt.Color(237,197,63),//1024
        new java.awt.Color(237,194,46),//2048
    };
    private Character[] gm = new Character[] {
        'G',
        'A',
        'M',
        'E',
        'O',
        'V',
        'E',
        'R',
    };
    private java.awt.Color bg_color = new java.awt.Color(87,74,62);
    private SwingMenu menuBar;
    private Font font = new Font(Font.SANS_SERIF, Font.BOLD, 40);

    public Swing2048(Jeu _jeu) {
        jeu = _jeu;
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(jeu.getSize() * PIXEL_PER_SQUARE, jeu.getSize() * PIXEL_PER_SQUARE);
        menuBar = new SwingMenu(jeu);
        setJMenuBar(menuBar);
        setBackground(tile_bg_colors[0]);
        
        dessinerGrille();

        ajouterEcouteurClavier();
        rafraichir();

    }

    /**
     * Correspond à la fonctionnalité de Vue : affiche les données du modèle
     */
    private void rafraichir()  {
        SwingUtilities.invokeLater(new Runnable() { // demande au processus graphique de réaliser le traitement
            @Override
            public void run() {
                if(!jeu.gameover) {
                    for (int i = 0; i < jeu.getSize(); i++) {
                        for (int j = 0; j < jeu.getSize(); j++) {
                            rafraichirCase(i, j);
                        }
                    }
                } else {                    
                    for (int i = 0; i < jeu.getSize(); i++) {
                        for (int j = 0; j < jeu.getSize(); j++) {
                            if (j>=4){
                                tabC[i][j].setText("");
                                tabC[i][j].setBackground(tile_bg_colors[0]);
                            }else{
                                switch (i) {
                                    case 1:
                                        tabC[i][j].setText(gm[j] + "");
                                        tabC[i][j].setBackground(tile_bg_colors[2]);
                                        break;
                                    case 2:
                                        tabC[i][j].setText(gm[j + 4] + "");
                                        tabC[i][j].setBackground(tile_bg_colors[2]);
                                        break;
                                    default:
                                        tabC[i][j].setText("");
                                        tabC[i][j].setBackground(tile_bg_colors[0]);
                                        break;
                                }
                            }
                        }
                    }
                }
            }
        });
    }

    private void rafraichirCase(int i, int j){
        Case c = jeu.getCase(i, j);
        if (c == null) {
            tabC[i][j].setText("");
            tabC[i][j].setBackground(tile_bg_colors[0]);

        } else {
            Color col = tile_bg_colors[Math.min(Tool.log2(c.getValeur()),11)];
            if (jeu.newCasePoint != null && i==jeu.newCasePoint.x && j==jeu.newCasePoint.y && menuBar.getEnableAnim()){
                tabC[i][j].setForeground(tile_bg_colors[0]);
                tabC[i][j].setBackground(tile_bg_colors[0]);
                tabC[i][j].setText(c.getValeur() + "");

                apparaitreCase(i, j, col);
            }else{
                tabC[i][j].setForeground(Color.WHITE);
                tabC[i][j].setText(c.getValeur() + "");
                tabC[i][j].setBackground(col);
            }
        }
    } 

    private void apparaitreCase(int i, int j, Color dest){
        try{
        new Thread(() -> {
            Case c = jeu.getCase(i, j);
            for(int k = 1; k<=20; k++){
                if(c != jeu.getCase(i, j)){
                    break;
                }
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                tabC[i][j].setForeground(tool.Tool.fadeTo(tile_bg_colors[0], Color.WHITE, k*50));
                tabC[i][j].setBackground(tool.Tool.fadeTo(tile_bg_colors[0], dest, k*50));
            }
            jeu.newCasePoint = null;
            if (c != jeu.getCase(i, j)){
                rafraichirCase(i, j);
            }
            }).start();
        }
        catch (Exception e){
            System.err.println(e);
        }
    }

    /**
     * Correspond à la fonctionnalité de Contrôleur : écoute les évènements, et déclenche des traitements sur le modèle
     */
    private void ajouterEcouteurClavier() {
        addKeyListener(new KeyAdapter() { // new KeyAdapter() { ... } est une instance de classe anonyme, il s'agit d'un objet qui correspond au controleur dans MVC
            @Override
            public void keyPressed(KeyEvent e) {
                switch(e.getKeyCode()) {  // on regarde quelle touche a été pressée
                    case KeyEvent.VK_LEFT : jeu.action(Direction.gauche); break;
                    case KeyEvent.VK_RIGHT : jeu.action(Direction.droite); break;
                    case KeyEvent.VK_DOWN : jeu.action(Direction.bas); break;
                    case KeyEvent.VK_UP : jeu.action(Direction.haut); break;

                    case KeyEvent.VK_BACK_SPACE: jeu.undoMove(); break;
                    case KeyEvent.VK_ENTER: jeu.redoMove(); break;
                    case KeyEvent.VK_S: menuBar.savePopUp(); menuBar.afficherSave(); break;
                    case KeyEvent.VK_R: menuBar.restorePopUp(); break;
                    case KeyEvent.VK_N: menuBar.nouvellePartiePopUp(); break;
                }
            }
        });
    }


    @Override
    public void update(Observable o, Object arg) {
        if (jeu.sizeChanged){
            dessinerGrille();
            jeu.sizeChanged = false;
        }
        rafraichir();
        menuBar.update();
        if (jeu.mustBlink && menuBar.getEnableAnim()){
            menuBar.blink();
            jeu.mustBlink = false;
        }
    }

    public void dessinerGrille(){
        JPanel contentPane = new JPanel(new GridLayout(jeu.getSize(), jeu.getSize()));
        contentPane.setBackground(bg_color);

        tabC = new JLabel[jeu.getSize()][jeu.getSize()];

        for (int i = 0; i < jeu.getSize(); i++) {
            for (int j = 0; j < jeu.getSize(); j++) {
                Border border = BorderFactory.createLineBorder(bg_color, 5);
                tabC[i][j] = new JLabel();
                tabC[i][j].setFont(font);
                tabC[i][j].setBorder(border);
                tabC[i][j].setForeground(java.awt.Color.white);
                tabC[i][j].setOpaque(true);

                tabC[i][j].setHorizontalAlignment(SwingConstants.CENTER);

                contentPane.add(tabC[i][j]);
            }
        }
        setContentPane(contentPane);
        resize(jeu.getSize() * PIXEL_PER_SQUARE, jeu.getSize() * PIXEL_PER_SQUARE);

        //System.out.println("GRILLE DESSINÉE");
    }
}