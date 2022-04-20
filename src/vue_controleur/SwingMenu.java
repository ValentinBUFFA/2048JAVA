package vue_controleur;
import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

import modele.Direction;
import modele.Jeu;

public class SwingMenu extends JMenuBar {

    private Jeu jeu;
    private JLabel hsL, scoreL;
    private java.awt.Color bg_color = new java.awt.Color(77,63,40);
    private java.awt.Color blink_color = new java.awt.Color(87,74,62);
    
    public SwingMenu(Jeu _jeu) {
        jeu = _jeu;
        
       
        super.setBackground(bg_color);
        this.setBorder(BorderFactory.createEmptyBorder());

        ActionListener afficherMenu = new ActionListener(){
            public void actionPerformed(ActionEvent event) {
                System.out.println(event.getActionCommand());
                switch (event.getActionCommand()) {
                    case "Sauver": jeu.saveToFile(); break;
                    case "Restaurer": 
                        if(!jeu.loadFromFile()) {
                            System.out.println("Erreur : fichier de sauvergarde introuvable");
                        }
                        break;
                    case "Annuler": jeu.undoMove(); break;
                    case "Refaire": jeu.redoMove(); break;

                    case "Haut": jeu.action(Direction.haut); break;
                    case "Bas": jeu.action(Direction.bas); break;
                    case "Gauche": jeu.action(Direction.gauche); break;
                    case "Droite": jeu.action(Direction.droite); break;
                }

              }
        };
        JMenu partieMenu = new JMenu("Partie");
        partieMenu.setForeground(java.awt.Color.black);
        JMenuItem item = new JMenuItem("Sauver", 'S');
        item.addActionListener(afficherMenu);
        partieMenu.add(item);
        item = new JMenuItem("Restaurer", 'E');
        item.addActionListener(afficherMenu);
        partieMenu.add(item);
        partieMenu.setOpaque(true);


        JMenu actionMenu = new JMenu("Actions");
        actionMenu.setForeground(Color.WHITE);
        item = new JMenuItem("Annuler", 'A');
        item.addActionListener(afficherMenu);
        actionMenu.add(item);
        item = new JMenuItem("Refaire", 'R');
        item.addActionListener(afficherMenu);
        actionMenu.add(item);
        actionMenu.insertSeparator(2);
        item = new JMenuItem("Haut", 'H');
        item.addActionListener(afficherMenu);
        actionMenu.add(item);
        item = new JMenuItem("Bas", 'B');
        item.addActionListener(afficherMenu);
        actionMenu.add(item);
        item = new JMenuItem("Gauche", 'G');
        item.addActionListener(afficherMenu);
        actionMenu.add(item);
        item = new JMenuItem("Droite", 'D');
        item.addActionListener(afficherMenu);
        actionMenu.add(item);
        partieMenu.setOpaque(true);

        JPanel scoresPane = new JPanel();

        JLabel hsText = new JLabel("HIGHSCORE:");
        hsText.setForeground(Color.WHITE);
        scoresPane.add(hsText);
        hsL = new JLabel(Integer.toString(jeu.getHighScore()));
        hsL.setForeground(Color.WHITE);
        scoresPane.add(hsL);

        JLabel scoreText = new JLabel("SCORE:");
        scoreText.setForeground(Color.white);
        scoresPane.add(scoreText);
        scoreL = new JLabel(Integer.toString(jeu.getScore()));
        scoreL.setForeground(Color.white);
        scoresPane.add(scoreL);
        scoresPane.setOpaque(false);

        add(partieMenu);
        add(actionMenu);
        add(scoresPane);
        
    }

    public void update(){
        hsL.setText(Integer.toString(jeu.getHighScore()));
        scoreL.setText(Integer.toString(jeu.getScore()));
    }

    public void blink(){
        this.setBackground(blink_color);
        
        new Thread(() -> {
            try {
                Thread.sleep(200);
                this.setBackground(bg_color);
            }
            catch (Exception e){
                System.err.println(e);
            }
        }).start();
    }
}
