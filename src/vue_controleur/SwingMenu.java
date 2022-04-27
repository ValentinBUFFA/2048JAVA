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
    private boolean enableAnim;
    JCheckBox animCheckBox;
    private JMenuItem[][] items = new JMenuItem[][] {
        new JMenuItem[] {   // Items dans le menu Partie
            new JMenuItem("Créer une sauvegarde", 'C'),
            new JMenuItem("Sauver", 'S'),
            new JMenuItem("Restaurer", 'E'),
            new JMenuItem("Nouvelle Partie", 'N')
        }, 
        new JMenuItem[] {    // Items dans le menu Action
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
    private JLabel saveIndicator = new JLabel("");
    private String[] saves_list;

    public SwingMenu(Jeu _jeu) {
        jeu = _jeu;
        enableAnim = true;
        saves_list = tool.Tool.saveSearch();

        this.setBackground(bg_color);
        this.setBorder(BorderFactory.createEmptyBorder());

        

        ActionListener afficherMenu = new ActionListener(){
            public void actionPerformed(ActionEvent event) {
                doAction(event.getActionCommand());
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
        aideMenu.insertSeparator(3);
        animCheckBox = new JCheckBox("Animations");
        animCheckBox.addActionListener(afficherMenu);
        animCheckBox.setSelected(true);
        aideMenu.add(animCheckBox);

        JPanel scoresPane = new JPanel();

        JLabel hsText = new JLabel("HIGHSCORE:");
        hsText.setForeground(Color.WHITE);
        scoresPane.add(hsText);

        hsL = new JLabel(Integer.toString(jeu.getHighScore()));
        hsL.setForeground(Color.WHITE);
        scoresPane.add(hsL);

        JLabel scoreText = new JLabel("SCORE:");
        scoreText.setForeground(Color.WHITE);
        scoresPane.add(scoreText);

        scoreL = new JLabel(Integer.toString(jeu.getScore()));
        scoreL.setForeground(Color.WHITE);
        scoresPane.add(scoreL);
        scoresPane.setOpaque(false);

        saveIndicator.setMinimumSize(new Dimension(43,17));
        saveIndicator.setPreferredSize(new Dimension(43,17));
        saveIndicator.setMaximumSize(new Dimension(43,17));
        saveIndicator.setForeground(Color.WHITE);
        saveIndicator.setHorizontalAlignment(SwingConstants.RIGHT);
        
        
        add(partieMenu);
        add(actionMenu);
        add(aideMenu);
        add(scoresPane);

        add(saveIndicator);
        
    }

    public void doAction(String event) {
        enableAnim = animCheckBox.getModel().isSelected();
        switch (event) {
            case "Sauver":
                savePopUp();
                break;
            case "Créer une sauvegarde": 
                while(!saveAsPopUp()) {}
                break;
            case "Restaurer": 
                restorePopUp();
                /*if(!jeu.loadFromFile()) {
                    System.out.println("Erreur : fichier de sauvergarde introuvable");
                }*/
                break;
            case "Nouvelle Partie": nouvellePartiePopUp(); break;
            case "Annuler": jeu.undoMove(); break;
            case "Refaire": jeu.redoMove(); break;

            case "Haut": jeu.action(Direction.haut); break;
            case "Bas": jeu.action(Direction.bas); break;
            case "Gauche": jeu.action(Direction.gauche); break;
            case "Droite": jeu.action(Direction.droite); break;
            
            case "Rechercher": 
                String s = searchfField.getText();
                if(searchResultPopUp(search(s))) {
                    doAction(s);
                }
                break;
        }
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

    public void afficherSave() {
        saveIndicator.setForeground(Color.WHITE);
        saveIndicator.setText("(sauvé)");
        if (enableAnim) {
            new Thread(() -> {
                try {
                    for(int k = 1; k<=20; k++){
                        Thread.sleep(50);
                        saveIndicator.setForeground(tool.Tool.fadeTo(Color.WHITE, bg_color, k*50));
                    }
                }
                catch (Exception e){
                    System.err.println(e);
                }
                saveIndicator.setText("");
    
            }).start();
        } else {
            new Thread(() -> {
                try {
                    Thread.sleep(300);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                saveIndicator.setText("");
            }).start();
        }
        saves_list = tool.Tool.saveSearch();
    }

    public void nouvellePartiePopUp(){
        JFrame popup = new JFrame();
        try {
            int new_size = Integer.parseInt(JOptionPane.showInputDialog(popup, "Taille", jeu.getSize()));
            if (new_size > 1){
                jeu.resetJeu(new_size);
            }
        } catch (Exception e) {
            return;
        }

    }
    
    public boolean searchResultPopUp(boolean b) {
        JFrame jFrame = new JFrame();
        if (b) {
            int result = JOptionPane.showConfirmDialog(jFrame, "Commande existante, voulez-vous l'executer ? ", "Aide", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
            
            if(result == JOptionPane.YES_OPTION) {
                return true;
            } 
            
        } else {
            JOptionPane.showMessageDialog(jFrame, "Commande inexistante !", "Aide", JOptionPane.INFORMATION_MESSAGE);
        }
        return false;
        
    }

    public void restorePopUp() {
        JFrame jFrame = new JFrame();
        if (saves_list.length > 0) {
            String filename = (String) JOptionPane.showInputDialog(jFrame, "Sélectionnez la sauvergarde à restaurer :", "Restaurer", JOptionPane.QUESTION_MESSAGE, null, saves_list, saves_list[0]);
            jeu.loadFromFile(filename);
            // TODO : Si le fichier choisi est corrompu : afficher une erreur et ne pas sauvegarder (à voir aussi dans loadFromFile())
        } else {
            errorPopUp(jFrame, "Aucune sauvergarde à restaurer !");
        }
    }

    public void savePopUp() {
        if(!jeu.saveToFile()) {
            JFrame jFrame = new JFrame();
            errorPopUp(jFrame, "Aucun fichier de sauvegarde pour cette partie, vous devez d'abord créer une sauvegarde.");
            while(!saveAsPopUp());
        }
    }
    

    public boolean saveAsPopUp() {
        JFrame jFrame = new JFrame();

        String filename = JOptionPane.showInputDialog(jFrame, "L'enregistrement s'effectue dans saves/. \n Enregistrer sous : ", JOptionPane.INFORMATION_MESSAGE);
        if (filename == null) {
            return true;
        } else if (filename.equals("") ) {
            errorPopUp(jFrame, "Nom incorrect, essayez en un autre.");
            return false;
        }

       
        if (tool.Tool.saveNameCompare(filename)) {
            //errorPopUp(jFrame, "Fichier existant déjà !");
            int rc = JOptionPane.showConfirmDialog(jFrame, "Le fichier existe déjà, voulez-vous le remplacer ?", "Sauvegarde", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE);
            
            switch (rc) {
                case JOptionPane.YES_OPTION:
                    jeu.saveToFile(filename);
                    break;                    
                case JOptionPane.CANCEL_OPTION:
                    errorPopUp(jFrame, "La sauvegarde n'a pas été effectuée !");
                    return true;
                default:
                    return false;
            }
        }  else {
            jeu.saveToFile(filename);
        }
        saves_list = tool.Tool.saveSearch();
        return true;
    }

    public void errorPopUp(JFrame jFrame, String errorDiscourse) {
        JOptionPane.showMessageDialog(jFrame, errorDiscourse, "Erreur", JOptionPane.ERROR_MESSAGE);
    }
    
    public boolean search(String entry) {
        for (int i = 0; i < items.length; i++) {
            for (int j = 0; j < items[i].length; j++) {
                if(items[i][j].getActionCommand().equals(entry))  {
                    return true;
                }
            }
        }
        
        return false;
    }

    public boolean getEnableAnim() {
        return enableAnim;
    }
}
