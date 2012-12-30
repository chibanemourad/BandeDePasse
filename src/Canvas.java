import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.BorderFactory;
import javax.swing.JApplet;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;


/**
 * Class de gestion de l'applet
 * @author Chibane Mourad 
 *
 */
@SuppressWarnings("serial")
public class Canvas extends JApplet{
//
	private Color col1 = new Color(51,153,255);
	private Color col3 = new Color(255,255,255); 
	//private Color col2 = new Color(15,15,15);
	private Font font1 = new Font("Helvetica", Font.BOLD, 14);
	private JButton Coder;			/* Bouton permettant de lancer le codage */
	private JComboBox Type_codage;  /* Liste permettant de choisir le type de codage a utiliser */
	private JTextField chaine; 		/* Zone de texte pour la saisie de la chaine binaire */
	private Conversion Graph;	    /* Zone pour l'affichage graphique du codage  */
	
	/**
	 * Initialisation de l'interface de l'applet
	 */
	public void init(){
		
		this.setLayout(new BorderLayout());
		
		/*********** PARTIE HAUTE ET BASSE *************/
		
		// JPanel qui va contenir l'interface de controle
		JPanel panHaut=new JPanel();
		JPanel panBas=new JPanel();
		
		// Ajout  de la zone de saisie du mot binaire
		panHaut.add(new JLabel("TP 1 Transmission en Bande de Passe, Mot binaire à coder:"));
		panBas.add(new JLabel("TP 1 Transmission en Bande de Passe 2011-2012, CHIBANE MOURAD Master Informatique"));
		panBas.setBackground(col1);
		panHaut.setBackground(col1);
		chaine=new JTextField("11000011010110100",15);
		chaine.setFont(font1);
		chaine.addKeyListener(new EcouteurClavier());
		panHaut.add(chaine);
		
		
		// Ajout et remplissage de la liste permettant de choisir le type de codage a utiliser 
		Type_codage=new JComboBox();
		Type_codage.addItem("NRZ");
		Type_codage.addItem("NRZI");
		Type_codage.addItem("Miller");
		Type_codage.addItem("Manchester");
		Type_codage.addItem("Manchester differentiel");
		Type_codage.setFont(font1);
		
		
		panHaut.add(Type_codage);
		
		
		// Ajout du bouton d'execution du codage
		Coder=new JButton("  Démarrer  ");
		Coder.addActionListener(new EcouteurBouton());
		Coder.addKeyListener(new EcouteurClavier());
		Coder.setFont(font1);
		Coder.setFocusable(true);
		panHaut.add(Coder);
		
		
		add(panHaut,BorderLayout.NORTH);
		add(panBas,BorderLayout.SOUTH);
		
		
		/*********** PARTIE CENTRALE (GRAPHE) *************/
		
		// Ajout de la zone d'affichage graphique du codage
		Graph=new Conversion();
		Graph.setForeground(col3);
		Graph.setBorder(BorderFactory.createLineBorder(Color.black));
		add(Graph);
	}
	
	
	/**
	 * Class interne permettant de gerer l'appui sur la touche ENTER pour lancer le codage  
	 * @author Chibane Mourad
	 *
	 */
	
	public class EcouteurClavier implements KeyListener {

		@Override
		public void keyReleased(KeyEvent arg0) {
			// TODO Auto-generated method stub			
		}

		@Override
		public void keyPressed(KeyEvent e) {
			String mot=chaine.getText();
			
			if(e.getKeyCode()==10 || e.getKeyCode()==13 ){
				if(!mot.matches("[01]+")){
					
					// Detection d'un erreur, donc on declenche une popup et on vide la zone de saisi
					new Erreur();
					chaine.setText("");
				}
				else{
					
					// Pas d'erreur, donc on lance le codage
					Graph.SetArgs(mot,Type_codage.getSelectedIndex());
				}
				
			}
					
		}

		@Override
		public void keyTyped(KeyEvent e) {
			// TODO Auto-generated method stub						
		}

	}
	
	/**
	 * Class interne permettant de gerer l'appui sur le bouton d'execution
	 * @author Chibane Mourad
	 *
	 */
	public class EcouteurBouton implements ActionListener{

		public void actionPerformed(ActionEvent arg0) {
			
			// Recuperation du mot saisi par l'utilisateur.
			String mot=chaine.getText();
			
			//on test si le mot d'entree est correct (Forme binaire que des 0 et des 1)
			if(!mot.matches("[01]+")){
				
				// Detection d'un erreur, afficher le popup d'erreur et vidage de la zone de saisi.
				new Erreur();
				chaine.setText("");
			}
			else{
				
				// Pas d'erreur, donc on lance le codage
				Graph.SetArgs(mot,Type_codage.getSelectedIndex());
			}
		}
	}
	
}
