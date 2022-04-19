package vue_controleur;

import modele.Case;
import modele.Jeu;
import modele.Direction;

import tool.Tool;//Pour log2

import javax.swing.*;
import javax.swing.border.Border;

import javafx.scene.paint.Color;

import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.TimeUnit;
@SuppressWarnings( "deprecation" )

public class Swing2048 extends JFrame implements Observer {
    private static final int PIXEL_PER_SQUARE = 120;
    // tableau de cases : i, j -> case graphique
    private JLabel[][] tabC;
    private Jeu jeu;
    private java.awt.Color[] bg_colors = new java.awt.Color[]{
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
    private SwingMenu menuBar;


    public Swing2048(Jeu _jeu) {
        jeu = _jeu;
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(jeu.getSize() * PIXEL_PER_SQUARE, jeu.getSize() * PIXEL_PER_SQUARE);
        menuBar = new SwingMenu(jeu);
        setJMenuBar(menuBar);
        
        tabC = new JLabel[jeu.getSize()][jeu.getSize()];

        JPanel contentPane = new JPanel(new GridLayout(jeu.getSize(), jeu.getSize()));

        Font font = new Font(Font.SANS_SERIF, Font.BOLD, 40);

        for (int i = 0; i < jeu.getSize(); i++) {
            for (int j = 0; j < jeu.getSize(); j++) {
                Border border = BorderFactory.createLineBorder(new java.awt.Color(87,74,62), 5);
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
                for (int i = 0; i < jeu.getSize(); i++) {
                    for (int j = 0; j < jeu.getSize(); j++) {
                        Case c = jeu.getCase(i, j);

                        if (c == null) {

                            tabC[i][j].setText("");
                            tabC[i][j].setBackground(bg_colors[0]);

                        } else {
                            tabC[i][j].setText(c.getValeur() + "");
                            tabC[i][j].setBackground(bg_colors[Math.min(Tool.log2(c.getValeur()),11)]);
                        }


                    }
                }
            
            }
        });


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
                }
            }
        });
    }


    @Override
    public void update(Observable o, Object arg) {
        rafraichir();
        menuBar.update();
    }
}