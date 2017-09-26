package command;


import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import javax.swing.JOptionPane;

public class Put {

	//Paramétres nécessaire é la connexion
	private static final int port = 69;
	private static final String mode = "octet";
	private String fileName;
	
	//Op Code du protocole TFTP
	private static final byte WRQ = 2;
	private static final byte DATA = 3;
	private static final byte ACK = 4;
	private static final byte ERROR = 5;
	
	private final static int PACKETMAXSIZE = 516;
	private final static int PACKETDATASIZE = 512;
	
	private DatagramSocket socket = null;
	private InetAddress adress = null;
	
	private byte[] inBuffer;
	private DatagramPacket inPacket;
	
	private byte[] outBuffer;
	String x;
	String z;
	
	int avancementFinal;
	 int avancement;

	
	
	
	public Put(){
		//Paramétres nécessaire é la connexion
		final int port = 69;
		 final String mode = "octet";
		 String fileName;
		
		//Op Code du protocole TFTP
		 final byte WRQ = 2;
		  final byte DATA = 3;
		 final byte ACK = 4;
		 final byte ERROR = 5;
		
		 final  int PACKETMAXSIZE = 516;
		 final  int PACKETDATASIZE = 512;
		
		 DatagramSocket socket = null;
		 InetAddress adress = null;
		
		 byte[] inBuffer;
		DatagramPacket inPacket;
		
		 byte[] outBuffer;
		String x;
		String z;
		int avancementFinal;
		int avancement;
		
	}
	
	public int getAvancementFinal() {
		return avancementFinal;
	}
	public void setAvancementFinal(int avancementFinal) {
		this.avancementFinal = avancementFinal;
	}
	
	public int getAvancement() {
		return avancement;
	}
	public void setAvancement(int avancement) {
		this.avancement = avancement;
	}
	
	public String getX() {
		return x;
	}


	public void setX(String x) {
		this.x = x;
	}

	public String getZ() {
		return z;
	}


	public void setZ(String y) {
		this.z = y;
	}
	public void envoi(String arg1,String arg2) {
		
		try {
			this.adress = InetAddress.getByName(arg1);
			this.fileName = arg2;
			socket = new DatagramSocket();
			inBuffer = createBuffer(WRQ,fileName,mode);
			inPacket = new DatagramPacket(inBuffer, inBuffer.length,adress,port);
			socket.send(inPacket);
			send();
			
			JOptionPane transencours=new JOptionPane();
			transencours.showMessageDialog(transencours, "Transmission terminée");
			//System.out.println("Transmission terminée");
			
		} 
		catch (UnknownHostException e) {
			e.printStackTrace();
		}catch (SocketException e) {
			e.printStackTrace();
		}catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	

	private void send() throws IOException{
		int nbBlockServed = 0;
		int nbErreurTransimission = 0;
		DatagramPacket reponseServeur = null;
		socket.setSoTimeout(3000);
		try{
			
			FileInputStream lecteur = new FileInputStream(fileName);
			//System.out.println(lecteur.available()/(512) + " paquets à émettre");
			avancementFinal=lecteur.available()/(512);
			JOptionPane transencours=new JOptionPane();
			transencours.showMessageDialog(transencours, "Transmission en cours de "+lecteur.available()/(512)+" paquets");
			
			int nb = 513;
			do{		
				outBuffer = new byte[PACKETMAXSIZE];
				reponseServeur = new DatagramPacket(outBuffer, outBuffer.length,adress,socket.getLocalPort());
				try{
					socket.receive(reponseServeur);
				}
				catch(SocketTimeoutException e){
					if(nbErreurTransimission == 5){
						//System.out.println("Le serveur ne peut pas être joint ou a mis fin a la connexion");

						JOptionPane notjoinable=new JOptionPane();
						notjoinable.showMessageDialog(notjoinable, "Le serveur ne peut pas être joint ou a mis fin a la connexion");
						break;
						
					}
					else{
						nbErreurTransimission++;
					}
				}
				byte opCodeRetour = outBuffer[1]; 
				if(opCodeRetour == ERROR){
					byte  codeErreur = outBuffer[3];
					decodeErreur(codeErreur);
				}
				else if(opCodeRetour == ACK){
					nbErreurTransimission = 0;
					byte[] tmp = new byte[PACKETDATASIZE];
					nb = lecteur.read(tmp,0,PACKETDATASIZE);
					byte[] data ;
					if(nb == -1){
						data = new byte[0];
					}
					else{
						data = new byte[nb];
					}
					
					for(int i= 0;i <nb;i++)data[i] = tmp[i];
					byte [] numeroBloc = {outBuffer[2],outBuffer[3]};
					//System.out.print(numeroBloc[0]+":"+numeroBloc[1]+" ");
					envoieData(numeroBloc, data, reponseServeur);
					nbBlockServed++;
					avancement=nbBlockServed;
				}
			}while(!lectureFini(nb));
			//System.out.println(nbBlockServed+" Blocks Served");
		}
		catch(FileNotFoundException e){
			//System.out.println("Le fichier spécifié n'existe pas");
			
			JOptionPane notfound=new JOptionPane();
			notfound.showMessageDialog(notfound, "Le fichier spécifié n'existe pas");
			
		}
		
	}
	
	private boolean lectureFini(int nb) {
		if(nb < PACKETDATASIZE) return true;
		return false;
	}

	private void envoieData(byte[] numeroBloc,byte[] data, DatagramPacket reponseServeur){
		byte[] buffer = buildData(numeroBloc,data);
		DatagramPacket paquet = new DatagramPacket(buffer, buffer.length,adress,reponseServeur.getPort());
		try {
			//System.out.println(numeroBloc[0]+":"+numeroBloc[1]);
			socket.send(paquet);
		} catch (IOException e) {                                                                                          
			e.printStackTrace();
		}
	}

	private byte[] buildData(byte[] numeroBloc, byte[] data) {
		int length;
		int pos = 0;
		length = 2 + 2 + data.length;
		byte toReturn[] = new byte[length];
		toReturn[pos] = 0;
		pos++;
		toReturn[pos] = DATA;
		pos++;
		byte[] numero  =incrementeNumeroBloc(numeroBloc);
		toReturn[pos] = numero[0];
		pos++;
		toReturn[pos] = numero[1];
		pos++;
		for (int i = 0; i < data.length; i++) {
			toReturn[pos] = data[i];
			pos++;
		}
		return toReturn;
	}

	private byte[] incrementeNumeroBloc(byte[] numeroBloc){
		byte newNumber[] = {0,0};
		if(numeroBloc[1] != -1){
			newNumber[0] = numeroBloc[0];
			newNumber[1] = (byte) (numeroBloc[1] + 1);
		}
		else if(numeroBloc[1] == -1){
			newNumber[0] = (byte) (numeroBloc[0]+1);
			newNumber[1] = 0;
		}
		else if(numeroBloc[0] == -1){
			//System.out.println("Taille maximale dépassée");
		}
		return newNumber;
	}
	private void decodeErreur(byte codeErreur){
		String msgErreur;
		//System.out.println(codeErreur);
		switch (codeErreur){
		case 1: msgErreur = "Le fichier demandé n'existe pas"; break;
		case 2: msgErreur = "Violation d'accés"; break;
		case 3: msgErreur = "Disque Plein"; break;
		case 4: msgErreur = "Opération TFTP illégalle"; break;
		case 5: msgErreur = "ID de transfert inconnu"; break;
		case 6: msgErreur = "Le fichier existe déjé"; break;
		case 7: msgErreur = "Pas d'utilisateur"; break;
		default: msgErreur ="Une erreur inconnu est survenu"; break;
		}
		//System.out.println("L'execution de la commande get a été imterompu: "+msgErreur);
		JOptionPane transencours=new JOptionPane();
		transencours.showMessageDialog(transencours, "L'execution de la commande get a été imterompu: "+msgErreur);
		
		
	}
	
	private byte[] createBuffer(byte opCode, String fileName, String mode) {
		byte separator = 0;
		int length;
		length = 2 + fileName.length() + 1 + mode.length() + 1;
		byte tmp[] = new byte[length];
		int pos = 0;
		tmp[pos] = separator;
		pos++;
		tmp[pos] = opCode;
		pos++;
		for (int i = 0; i < fileName.length(); i++) {
			tmp[pos] = (byte) fileName.charAt(i);
			pos++;
		}
		tmp[pos] = separator;
		pos++;
		for (int i = 0; i < mode.length(); i++) {
			tmp[pos] = (byte) mode.charAt(i);
			pos++;
		}
		tmp[pos] = separator;
		pos++;
		return tmp;
	}

}