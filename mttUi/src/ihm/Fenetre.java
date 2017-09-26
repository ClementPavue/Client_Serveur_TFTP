package ihm;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTextField;

import command.Get;
import command.Put;

public class Fenetre extends JFrame{
	
	private JPanel panel = new JPanel();
	private JLabel ip=new JLabel();
	private   JTextField adresseIP =null;
	private JLabel nameFile=new JLabel();
	private JTextField nomFichier = null;
	private JButton buttonGet;
	private JButton buttonPut;
	private String parameter;
	private String a1;
	private String a2;
	private String  instruction;

	public Fenetre(){
		 	
		   this.setTitle("MTT");
		   this.setSize(400, 150);
		   this.setResizable(false);
		   this.setLocationRelativeTo(null);
		   this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  
		   
		   JLabel ip=new JLabel("Adresse IP : ");
		   panel.add(ip);
		   adresseIP = new JTextField("10.117.61.11",20);
		   panel.add(adresseIP);
		   
		   JLabel nameFile=new JLabel("Nom du fichier : ");
		   panel.add(nameFile);
		   nomFichier = new JTextField("index.html",18);
		   panel.add(nomFichier);
		 

		  
		   JButton buttonGet= new JButton("Get");
		   buttonGet.addActionListener(new GestionButtonGet());
		   JButton buttonPut= new JButton("Put");
		   buttonPut.addActionListener(new GestionButtonPut());
		   panel.add(buttonGet);
		   panel.add(buttonPut);	
		   this.setContentPane(panel);
		   
		  
		   
		   
		   
		   
		   
		   
		   
		 
		   this.setVisible(true);


	
	 }
	public  String getNomFichier(){
		   return nomFichier.getText();
	   }	 
	
	public  String getAdresseIP(){
		   return adresseIP.getText();
	   }
	
	public class GestionButtonPut implements ActionListener{

		 public  void    actionPerformed(ActionEvent e)
	    {
			 instruction="Put "+adresseIP.getText()+" "+nomFichier.getText();
			// System.out.println(instruction);
			 a1=adresseIP.getText();
			 a2=nomFichier.getText();
			 Put p=new Put();
			 p.setX(a1);
			 p.setZ(a2);
			 p.envoi(a1, a2);
			 
			 
			 
			
	    }
		 
		 
		
	}
	public class GestionButtonGet implements ActionListener{

		 public  void    actionPerformed(ActionEvent e)
	    {		
			 instruction="Get "+adresseIP.getText()+" "+nomFichier.getText();
			 a1=adresseIP.getText();
			 a2=nomFichier.getText();
			 Get g=new Get();
			 g.setX(a1);
			 g.setZ(a2);
			 g.recevoir(a1, a2);
			 
	    }
		 
		
	}
	
	public String getInst1(){
		return this.a1;
	}
	public String getInst2(){
		return this.a2;
	}
	
	public void setInst1(String b1){
		this.a1=b1;
	}
	public void setInst2(String b2){
		this.a2=b2;
		
	}
	public String getInstruction(){
		return this.instruction;
	}
	
	public void setInstruction(String b1){
		this.instruction=b1;
	}
	}

