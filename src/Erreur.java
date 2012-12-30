import java.awt.Font;
import javax.swing.JDialog;
import javax.swing.JLabel;

/**
 * Class de la popup d'erreur
 * @author Chibane Mourad 
 *
 */
@SuppressWarnings("serial")
public class Erreur extends JDialog{
	private Font font1 = new Font("Helvetica", Font.BOLD, 14);

	public Erreur(){
		setTitle("Erreur");
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setSize(600, 80);
		setLocationRelativeTo(null);
		
		
		JLabel lab=new JLabel("Le mot à coder n'est pas en binaire, Veuillez entrer un mot binaire !");
		lab.setFont(font1);
		lab.setHorizontalAlignment(JLabel.CENTER);
		lab.setVerticalAlignment(JLabel.CENTER);
		add(lab);
		
		
		setVisible(true);
	}
	
}
