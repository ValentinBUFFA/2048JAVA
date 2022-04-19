# 2048JAVA
MODELE: case, direction, jeu
VUE_CONTROLEUR: Console2048, Swing2048

### TODOLIST:
#### VUE:
- Resize la taille de la police pour eviter l'overflow sur l'affichage graphique
- Passer la police en vectoriel?
- Affichage lors de gameover
- Affichage au lancement (reprendre partie, choisir taille, etc)
- [V] Bouton/keybind pour annuler un coup
- [V] Bouton/keybind pour sauvegarder la partie
- [V] Afficher le highscore, et le temps de la partie actuelle
- [V] Pour les boutons, utiliser menus de navigation?
- Ajouter des animations au déplacement des cases (avec possiblité de les desactiver)

###### Barre de menus
- Page d'aide avec les keybindings
- Paramètres?

#### MODELE:
- Adapter la taille du jeu selon l'argument du constructeur Jeu(taille)
- [V] Faire un historique des coups joués avec possibilité de revenir en arriere
- [V] Rendre possible de sauvegarder la partie avec le score (et le temps)
- Rendre possible de charger une sauvegarde
- [V] Faire un fichier séparé, systematiquement lu à la creation d'un jeu pour stocker le highscore
- [V] Enregistrer le score
- Enregistrer le score dans l'historique
- [V] Ne pas ajouter de cases aléatoire quand un deplacement ne fait aucun mouvement
- (Optimiser la verification de fin de partie en gardant en mémoire les etats de la grille)  


### KNOWN BUGS
- [FIXED] !testFinPartie not working properly 
- [FIXED] gestion du score dans l'historique
- revenir en arriere quand la partie est perdue produit des comportements FORTEMENT indésirables
- blink bitch se déclenche à des moments inopportuns