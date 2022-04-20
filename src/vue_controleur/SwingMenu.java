package vue_controleur;
import java.awt.event.*;
import java.io.IOException;

import javax.swing.*;

import modele.Direction;
import modele.Jeu;

public class SwingMenu extends JMenuBar {
    private Jeu jeu;
    private JLabel hsL, scoreL;
    
    public SwingMenu(Jeu _jeu) {
        jeu = _jeu;
        
        ActionListener afficherMenu = new ActionListener(){
            public void actionPerformed(ActionEvent event) {
                System.out.println(event.getActionCommand());
                switch (event.getActionCommand()) {
                    case "Sauver": try {
                            jeu.saveToFile();
                        } catch (IOException e) {
                            e.printStackTrace();
                        } break;
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
        JMenuItem item = new JMenuItem("Sauver", 'S');
        item.addActionListener(afficherMenu);
        partieMenu.add(item);
        item = new JMenuItem("Restaurer", 'E');
        item.addActionListener(afficherMenu);
        partieMenu.add(item);


        JMenu actionMenu = new JMenu("Actions");
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

        JPanel scoresPane = new JPanel();
        scoresPane.add(new JLabel("HIGHSCORE:"));
        hsL = new JLabel(Integer.toString(jeu.getHighScore()));
        scoresPane.add(hsL);
        scoresPane.add(new JLabel("SCORE:"));
        scoreL = new JLabel(Integer.toString(jeu.getScore()));
        scoresPane.add(scoreL);

        add(partieMenu);
        add(actionMenu);
        add(scoresPane);
    }

    public void update(){
        hsL.setText(Integer.toString(jeu.getHighScore()));
        scoreL.setText(Integer.toString(jeu.getScore()));
    }

    public void blink(){
        this.setBackground(new java.awt.Color(87,74,62));
        new Thread(() -> {
            try {
                Thread.sleep(100);
                this.setBackground(new java.awt.Color(255,255,255));
            }
            catch (Exception e){
                System.err.println(e);
            }
        }).start();
    }
}
