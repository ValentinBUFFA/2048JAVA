# 2048JAVA
MODELE: case, direction, jeu
VUE_CONTROLEUR: Console2048, Swing2048

### TODOLIST:
#### VUE:
- [N] Resize la taille de la police pour eviter l'overflow sur l'affichage graphique
- [N] Passer la police en vectoriel?
- Affichage lors de gameover
- [N] Affichage au lancement (reprendre partie, choisir taille, etc)
- [V] Bouton/keybind pour annuler un coup
- [V] Bouton/keybind pour sauvegarder la partie
- [V] Afficher le highscore, et le temps de la partie actuelle
- [V] Pour les boutons, utiliser menus de navigation?
- Ajouter des animations au déplacement des cases (avec possiblité de les desactiver)
- Ajouter des keybinds pour (presque) tout
- Deux Joueurs
- [V] Blink de la barre de menu

###### Barre de menus
- Page d'aide avec les keybindings
- Paramètres?

#### MODELE:
- [V] Adapter la taille du jeu selon l'argument du constructeur Jeu(taille)
- [V] Faire un historique des coups joués avec possibilité de revenir en arriere
- [V] Rendre possible de sauvegarder la partie avec le score (et le temps)
- [V] Rendre possible de charger une sauvegarde
- [V] Faire un fichier séparé, systematiquement lu à la creation d'un jeu pour stocker le highscore
- [V] Enregistrer le score
- [V] Enregistrer le score dans l'historique
- [V] Ne pas ajouter de cases aléatoire quand un deplacement ne fait aucun mouvement
- Deux Joueurs
- Randomizer le début
- Exec pool
- [V] Declencher blink quand il faut (historique limites, blink bitch)


### KNOWN BUGS
- [FIXED] !testFinPartie not working properly 
- [FIXED] gestion du score dans l'historique
- [~FIXED|REQUIRES TESTING] revenir en arriere quand la partie est perdue produit des comportements FORTEMENT indésirables
- [FIXED] blink bitch se déclenche à des moments inopportuns (bas, gauche, gauche)
- Bug à la restauration d'une partie après un game over
