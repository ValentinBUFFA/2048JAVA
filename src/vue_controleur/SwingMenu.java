package vue_controleur;
import java.awt.event.*;
import javax.swing.*;

import modele.Case;
import modele.Direction;
import modele.Jeu;

public class SwingMenu extends JMenuBar {
    private Jeu jeu;
    
    
    public SwingMenu(Jeu _jeu) {
        jeu = _jeu;
        
        ActionListener afficherMenu = new ActionListener(){
            public void actionPerformed(ActionEvent event) {
                System.out.println(event.getActionCommand());
                switch (event.getActionCommand()) {
                    case "Haut": jeu.action(Direction.haut); break;
                    case "Bas": jeu.action(Direction.bas); break;
                    case "Gauche": jeu.action(Direction.gauche); break;
                    case "Droite": jeu.action(Direction.droite); break;
                }

              }
        };

        JMenu actionMenu = new JMenu("Action");
        JMenuItem item = new JMenuItem("Haut", 'H');
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

        add(actionMenu);
    }
}
