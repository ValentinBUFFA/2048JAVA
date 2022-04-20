package vue_controleur;
import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

import modele.Direction;
import modele.Jeu;

public class SwingMenu extends JMenuBar {

    private Jeu jeu;
    private JLabel hsL, scoreL;
    JTextField searchfField;
    private JMenuItem[][] items = new JMenuItem[][] {
        new JMenuItem[] {   // Items dans le menu Partie
            new JMenuItem("Sauver", 'S'),
            new JMenuItem("Restaurer", 'E'),
            new JMenuItem("Nouvelle Partie", 'N')
        }, 
        new JMenuItem[] {    // Items adns le menu Action
            new JMenuItem("Annuler", 'A'),
            new JMenuItem("Refaire", 'R'),
            new JMenuItem("Haut", 'H'),
            new JMenuItem("Bas", 'B'),
            new JMenuItem("Gauche", 'G'),
            new JMenuItem("Droite", 'D')
        }
    };
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
                    case "Nouvelle Partie": jeu.resetJeu();
                    case "Annuler": jeu.undoMove(); break;
                    case "Refaire": jeu.redoMove(); break;

                    case "Haut": jeu.action(Direction.haut); break;
                    case "Bas": jeu.action(Direction.bas); break;
                    case "Gauche": jeu.action(Direction.gauche); break;
                    case "Droite": jeu.action(Direction.droite); break;

                    case "Rechercher": 
                        System.out.println(searchfField.getText());
                    
                        if(search(searchfField.getText())) {
                            System.out.println("BITEE");
                        }
                        break;

                }

              }
        };
        JMenu partieMenu = new JMenu("Partie");
        partieMenu.setForeground(java.awt.Color.white);
        JMenu actionMenu = new JMenu("Actions");
        actionMenu.setForeground(Color.WHITE);

        for (int i = 0; i < items.length; i++) {
            for (int j = 0; j < items[i].length; j++) {
                items[i][j].addActionListener(afficherMenu);
                switch (i) {
                    case 0:
                        partieMenu.add(items[i][j]);
                        break;
                    case 1:
                        actionMenu.add(items[i][j]);
                        break;
                    default:
                        break;
                }
            }
        }

        partieMenu.insertSeparator(2);
        actionMenu.insertSeparator(2);

        JMenu aideMenu = new JMenu("Aide");
        aideMenu.setForeground(Color.white);
        searchfField = new JTextField();
        aideMenu.add(searchfField);
        JButton rechercher = new JButton("Rechercher");
        aideMenu.add(rechercher);
        rechercher.addActionListener(afficherMenu);

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
        add(aideMenu);
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

    public boolean search(String entry) {
        String s;
        for (int i = 0; i < items.length; i++) {
            for (int j = 0; j < items[i].length; j++) {
                if(items[i][j].getActionCommand().equals(entry))  {
                    return true;
                }
                
            }
        }
        return false;
    }
}
