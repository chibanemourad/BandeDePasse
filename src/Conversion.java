import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Stroke;

import javax.swing.JPanel;

/**
 * Class de codage "Traitement" de la donnee binaire
 * @author Chibane Mourad 
 *
 */
@SuppressWarnings("serial")
public class Conversion extends JPanel{
	
	private Color col1 = new Color(51,153,255);
	private String chaine;		 /* Contient la chaine binaire a coder*/
	private int methode; 	 	 /* Contient la methode de codage a utiliser*/
	private int vPos;			 /* Contient la valeur du signal pour un byte a "1"*/
	private int vNeg;		 	 /* Contient la valeur du signal pour un byte a "0"*/
	private int spaceH;		 	 /* Contient la valeur de l'espacement entre chaque byte selon la methode utilisee*/
	
	/*Constructeur de la zone de l'affichage et initialisation des variables*/
	public Conversion(){
		this.chaine=null;
		this.methode=-1;
	}
	
	/**
	 * Fonction qui permet de definir les donnees utilent a la 
	 * creation du codage selon la chaine et la méthode 
	 * @param chaine : mot binaire a coder
	 * @param methode : methode de codage a utiliser
	 */
	public void SetArgs(String chaine,int methode){
		
		// Enregistrement des valeurs utilent a la creation du codage
		this.chaine=chaine;
		this.methode=methode;
		
		//calcul de valeur utile a la construction du codage
		vPos=this.getHeight()/4;
		vNeg=this.getHeight()/4+this.getHeight()/2;
		spaceH=this.getWidth()/chaine.length();
		
		// Affichage de la solution
		repaint();
	}
	
	/**
	 * Fonction qui va dessiner le signal graphique
	 */
	public void paintComponent(Graphics g){
		g.fillRect(0, 0, this.getWidth(), this.getHeight());
				
		//s'assurer qu'on les parametres requis pour le dessin du signal  
		if(chaine!=null || methode!=-1){
			dessineGrille(g);
			
			//Codage selon la methode choisie dans la JCombobox indexées de 0 à 4
			switch(methode){
			case 0:
				//index "0" pour la methode "NRZ"
				NRZ(chaine,g);
				break;
			case 1:
				//index "1" pour la methode "NRZI"
				NRZI(g);
				break;
				
			case 2:
				//index "2" pour la methode "Miller"
				miller(g);
				break;
				
			case 3:
				//index "3" pour la methode "manchester"
				manchester(g);	
				break;
				
			case 4:
				//index "4" pour la methode "manchester differentiel"
				manchesterDiff(g);
				break;
			}
		}
	}
	
	/**
	 * Fonction qui sectionne le graphe pour différencier les bites "grille"
	 * @param g objet pour dessiner la grille
	 */
	private void dessineGrille(Graphics g){	
		
		//J'ai besoin d'un objet Graphics2D pour faire des trait pointier
		Graphics2D g2 = (Graphics2D) g;
		
		//on fixe la couleur des traits en bleu
		g2.setColor(col1);
		
		// recuperation du mode standard de dessin 
		Stroke stroke = g2.getStroke();
		
		//reglage de la longeur des pointiers 
		float dash1[] = {5.0f};
		
		//application du mode pointier
		g2.setStroke(new BasicStroke(1.0f, BasicStroke.CAP_BUTT,BasicStroke.JOIN_MITER, 10.0f, dash1, 0.0f));
		
		//on trace les traits vertical
		for(int i=0;i<this.getWidth();i=i+spaceH)
				g2.drawLine(i, 0, i, this.getHeight());
			
		
		// On repasse en mode normale
		g2.setStroke(stroke);
		 
		//on fixe la couleur des traits en rouge
		g2.setColor(Color.RED);
		
		//on trace la ligne du potentiel null
		g2.drawLine(0, this.getHeight()/2, this.getWidth(), this.getHeight()/2);
		
	}

	/**
	 * Fonction qui affiche le codage NRZ de  la chaine
	 * @param chaine chaine a coder
	 * @param g objet pour dessiner le codage
	 */
	private void NRZ(String chaine,Graphics g){
		
		//on fixe la couleur des traits en noir
		g.setColor(Color.BLACK);
		
		// position dans le codage
		int indexPosition=0;
		
		// valeur du bit precedement code (-1 si rien avant)
		int valPred=-1;
		
		//codage de la donnee
		for(int i=0;i<chaine.length();i++){
			
			//si la valeur du bit est 0 ou 1
			if(chaine.charAt(i)=='0'){
				
				//on trace la liaison verticale avec le precedent byte si il est different de celui en cour de traitement
				if(valPred!=0 && valPred!=-1)
					g.drawLine(indexPosition, vPos, indexPosition,vNeg);
				valPred=0;
				
				//on trace le codage
				g.drawLine(indexPosition, vNeg, (indexPosition+spaceH),vNeg);
				indexPosition+=spaceH;
			
			}
			else{
				//on trace la liaison verticale avec le precedent byte si il est different de celui en cour de traitement
				if(valPred!=1 && valPred!=-1)
					g.drawLine(indexPosition, vPos, indexPosition,vNeg);
				valPred=1;
				
				//on trace le codage
				g.drawLine(indexPosition, vPos, (indexPosition+spaceH),vPos);
				indexPosition+=spaceH;
			}
			
			
		}		
	}
	
	/**
	 * Fonction qui affiche le codage NRZI de la chaine
	 * @param g objet pour dessiner le codage
	 */
	private void NRZI(Graphics g){
		
		//inversion des bit de la  chaine
		String chaineNRZI="";
		for(int i=0;i<chaine.length();i++){
			if(chaine.charAt(i)=='0')
				chaineNRZI+="1";
			else
				chaineNRZI+="0";
		}
		
		//appele de la fonction NRZ avec la chaine NRZI
		NRZ(chaineNRZI,g);
	}
	
	
	/**
	 * Fonction qui affiche le codage de Miller
	 * @param g objet pour dessiner le codage
	 */
	private void miller(Graphics g){
		
		//on fixe la couleur des traits en blanc
		g.setColor(Color.black);
		
		// Calcul de la longeur d'un demi espace
		int midSpaceH=spaceH/2;
		
		//valeur de voltage du predecesseur
		int vPred=0;
		
		//valeur du precedent byte
		int pred=-1;
		
		// position dans le codage
		int indexPosition=0;
		
		// Parcour de la donnee a coder
		for(int i=0;i<chaine.length();i++){
			
			// si le byte est un 0
			if(chaine.charAt(i)=='0'){
				
				//si c'est le premier byte,on choisie aleatoirement de commencer en vPos ou vNeg
				if(i==0){
					
					double r=Math.random();
					if(r<0.5){
						
						//demarrage en vPos
						g.drawLine(indexPosition, vPos, indexPosition+spaceH, vPos);
						vPred=vPos;
					}
					else{
						
						//demarrage en vNeg
						g.drawLine(indexPosition, vNeg, indexPosition+spaceH, vNeg);
						vPred=vNeg;
					}
					
					indexPosition+=spaceH;
					pred=0;
				}
				else{
					
					//si le byte precedent est un 0, on fait une transition sur le temps d'horloge
					if(pred==0){
						
						//si il etait en vPos on va en vNeg et inversement
						if(vPred==vPos){
							g.drawLine(indexPosition, vPos, indexPosition, vNeg);
							g.drawLine(indexPosition, vNeg, indexPosition+spaceH, vNeg);
							
							vPred=vNeg;
						}
						else{
							g.drawLine(indexPosition, vNeg, indexPosition, vPos);
							g.drawLine(indexPosition, vPos, indexPosition+spaceH, vPos);
							
							vPred=vPos;
						}
						
						indexPosition+=spaceH;
						pred=0;
					}else{
						
						//le precedent n'est pas un 0 donc on n'effectue pas de transition
						
						if(vPred==vPos){
							g.drawLine(indexPosition, vPos, indexPosition+spaceH, vPos);
							
							vPred=vPos;
						}
						else{
							g.drawLine(indexPosition, vNeg, indexPosition+spaceH, vNeg);
							
							vPred=vNeg;
						}
						
						indexPosition+=spaceH;
						pred=0;
					}
				
				}
				

			}
			else{ // byte a 1
				
				//si c'est le premier byte, on choisie aleatoirement de commencer en vPos ou vNeg
				if(i==0){
					
					double r=Math.random();
					if(r<0.5){
						//demarrage en vPos
						g.drawLine(0, vPos, midSpaceH, vPos);
						indexPosition=midSpaceH;
						
						g.drawLine(indexPosition, vPos, indexPosition, vNeg);
						
						g.drawLine(indexPosition, vNeg, indexPosition+midSpaceH, vNeg);
						
						vPred=vNeg;
					}
					else{
						
						//demarrage en vNeg
						g.drawLine(0, vNeg, midSpaceH, vNeg);
						indexPosition=midSpaceH;
						
						g.drawLine(indexPosition, vNeg, indexPosition, vPos);
						g.drawLine(indexPosition, vPos, indexPosition+midSpaceH, vPos);
						
						vPred=vPos;
					}
					
					indexPosition+=midSpaceH;
					pred=1;
				}
				else{ //ce n'est pas le premier byte
									
					if(vPred==vPos){
						
						g.drawLine(indexPosition, vPos, indexPosition+midSpaceH, vPos);
						indexPosition+=midSpaceH;
						
						g.drawLine(indexPosition, vPos, indexPosition, vNeg);
						vPred=vNeg;
						
						g.drawLine(indexPosition, vNeg,indexPosition+midSpaceH, vNeg);
						
						
					}
					else{
						g.drawLine(indexPosition, vNeg, indexPosition+midSpaceH, vNeg);
						indexPosition+=midSpaceH;
						
						g.drawLine(indexPosition, vNeg, indexPosition, vPos);
						vPred=vPos;
						
						g.drawLine(indexPosition, vPos,indexPosition+midSpaceH, vPos);
						
					}
					indexPosition+=midSpaceH;
					pred=1;
						
				}
				
			}
			
		}
		
	}

	
	
	
	
	/**
	 * Fonction qui affiche le codage de Manchester
	 * @param g objet pour dessiner le codage
	 */
	private void manchester(Graphics g){
		
		//on fixe la couleur des traits en noire
		g.setColor(Color.black);
		
		// Calcul de la longeur d'un demi espace
		int midSpaceH=spaceH/2;
		
		// position dans le codage
		int indexPosition=0;
		
		// valeur du precedent byte traite
		int pred=-1;
		
		// Parcour de la donnee a coder
		for(int i=0;i<chaine.length();i++){
			
			// Choix du trace suivant la valeur du byte
			if(chaine.charAt(i)=='0'){
				
				//si c'est le premier byte
				if(i==0){
					
					//par convention je place le premier trait pour le millieu d'horloge en bas si le byte est a zero//
					g.drawLine(0, vNeg, midSpaceH, vNeg);
					indexPosition=midSpaceH;
				}
				else {
					if (pred==0){
					
					//on trace le trait qui descent seulement si le precedent byte etait un zero
					g.drawLine(indexPosition, vPos, indexPosition, vNeg);
					
					}
					
					//on trace le trait pour atteindre le millieu d'horloge	
					g.drawLine(indexPosition, vNeg, (indexPosition+midSpaceH),vNeg);
					indexPosition+=midSpaceH;
				}
				
			

				
				//on trace un trait qui monte
				g.setColor(Color.orange);
				g.drawLine(indexPosition, vNeg, indexPosition,vPos );
				
				// dessin d'un triangle en bout de fleche
				int x[]={indexPosition,indexPosition-5,indexPosition+5};
				int y[]={vPos,vPos+10,vPos+10};
				g.fillPolygon(x,y, 3);

				//on trace le trait pour arriver a l'autre top d'horloge
				g.setColor(Color.black);
				g.drawLine(indexPosition, vPos, (indexPosition+midSpaceH), vPos);
				
				indexPosition+=midSpaceH;
				pred=0;
			}
			else{
				if(i==0){
					
					//par convention je place le premier trait pour le millieu d'horloge en haut si le byte est a 1//
					g.drawLine(0, vPos, midSpaceH, vPos);
					indexPosition=midSpaceH;
				}
				else{
					if (pred==1){
				
					
					//on trace le trait qui monte seulement si le precedent byte etait un 1
					g.drawLine(indexPosition, vNeg, indexPosition, vPos);
					}
					
					//on trace le trait pour atteindre le millieu d'horloge
					g.drawLine(indexPosition, vPos, (indexPosition+midSpaceH), vPos);
					indexPosition+=midSpaceH;
					
				}
				
				
				//on trace un trait qui descent
				g.setColor(Color.magenta);
				g.drawLine(indexPosition, vPos, indexPosition, vNeg);
				
				// dessin d'un triangle en bout de fleche
				int x[]={indexPosition,indexPosition-5,indexPosition+5};
				int y[]={vNeg,vNeg-10,vNeg-10};
				g.fillPolygon(x,y, 3);
				
				//on trace le trait pour arriver a l'autre top d'horloge
				g.setColor(Color.black);
				g.drawLine(indexPosition, vNeg, (indexPosition+midSpaceH), vNeg);
				
				indexPosition+=midSpaceH;
				
				pred=1;

			}
		}
	}
	
	/**
	 * Fonction qui affiche le codage de Manchester Differentiel
	 * @param g objet pour dessiner le codage
	 */
	private void manchesterDiff(Graphics g){
		
		//on fixe la couleur des traits en blanc
		g.setColor(Color.black);
		
		// Calcul de la longeur d'un demi espace
		int midSpaceH=spaceH/2;
		
		//valeur de voltage du predecesseur
		int vPred=0;
		
		// position dans le codage
		int indexPosition=0;
		
		// Parcour de la donnee a coder
		for(int i=0;i<chaine.length();i++){
			
			// si le byte est un 0
			if(chaine.charAt(i)=='0'){
				
				
				//si c'est le premier byte
				if(i==0){
					
					//on choisi aleatoirement de commencer a vPos ou vNeg
					double r=Math.random();
					
					if(r<0.5){
						g.drawLine(0, vNeg, 0, vPos);
						
						g.drawLine(0, vPos, midSpaceH, vPos);
						indexPosition=midSpaceH;
						
						g.drawLine(indexPosition, vPos, indexPosition, vNeg);
						vPred=vNeg;
						
						g.drawLine(indexPosition, vNeg, indexPosition+midSpaceH, vNeg);
						
					}
					else{
				
						g.drawLine(0, vPos, 0, vNeg);
						
						g.drawLine(0, vNeg, midSpaceH, vNeg);
						indexPosition=midSpaceH;
						
						g.drawLine(indexPosition, vNeg, indexPosition, vPos);
						vPred=vPos;
						
						g.drawLine(indexPosition, vPos, indexPosition+midSpaceH, vPos);
					}
					indexPosition+=midSpaceH;
				}
				else{ 
					//ici ce n'est pas le premier byte
					
					//suivant la valeur de courant du precedent byte
					if(vPred==vPos){
						g.drawLine(indexPosition, vPos, indexPosition, vNeg);
						
						g.drawLine(indexPosition, vNeg, indexPosition+midSpaceH, vNeg);
						indexPosition+=midSpaceH;
						
						g.drawLine(indexPosition, vNeg, indexPosition, vPos);
						vPred=vPos;
						
						g.drawLine(indexPosition, vPos, indexPosition+midSpaceH, vPos);
						
					}
					else{
						g.drawLine(indexPosition, vNeg, indexPosition,vPos);

						
						g.drawLine(indexPosition, vPos, indexPosition+midSpaceH, vPos);
						indexPosition+=midSpaceH;
						
						g.drawLine(indexPosition, vPos, indexPosition, vNeg);
						vPred=vNeg;
						
						g.drawLine(indexPosition, vNeg, indexPosition+midSpaceH, vNeg);
					}
					indexPosition+=midSpaceH;
				}
			}
			else{ //byte a 1
				
				//si c'est le premier byte
				if(i==0){
					
					//on choisi aleatoirement de commencer a vPos ou vNeg
					double r=Math.random();
					
					if(r<0.5){
						
						g.drawLine(0, vPos, midSpaceH, vPos);
						indexPosition=midSpaceH;
						
						g.drawLine(indexPosition, vPos, indexPosition, vNeg);
						vPred=vNeg;
						
						g.drawLine(indexPosition, vNeg, indexPosition+midSpaceH, vNeg);
						
					}
					else{
						
						g.drawLine(0, vNeg, midSpaceH, vNeg);
						indexPosition=midSpaceH;
						
						g.drawLine(indexPosition, vNeg, indexPosition, vPos);
						vPred=vPos;
						
						g.drawLine(indexPosition, vPos, indexPosition+midSpaceH, vPos);
					}
					indexPosition+=midSpaceH;
				}
				else{ 
					//ici ce n'est pas le premier byte
					
					//suivant la precedente tension
					if(vPred==vPos){
						g.drawLine(indexPosition, vPos, indexPosition+midSpaceH, vPos);
						indexPosition+=midSpaceH;
						
						g.drawLine(indexPosition, vPos, indexPosition, vNeg);
						vPred=vNeg;
						
						g.drawLine(indexPosition, vNeg, indexPosition+midSpaceH, vNeg);
					}
					else{
						
						g.drawLine(indexPosition, vNeg, indexPosition+midSpaceH, vNeg);
						indexPosition+=midSpaceH;
						
						g.drawLine(indexPosition, vNeg, indexPosition, vPos);
						vPred=vPos;
						
						g.drawLine(indexPosition, vPos, indexPosition+midSpaceH, vPos);
					}
					indexPosition+=midSpaceH;					
				}
				
			}
			
		}
		
	}
	
	
}