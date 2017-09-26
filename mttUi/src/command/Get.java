package command;

import java.io.ByteArrayOutputStream;
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

public class Get{

	//Paramétres nécessaire à la connexion
	private static final int port = 69;
	private static final String mode = "octet";
	private String fileName;
	
	//Op Code du protocole TFTP
	private static final byte RRQ = 1;
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
	private DatagramPacket outPacket;
	
	private String spec="Teclé";
	String x;
	String z;
	
	public Get(){
		final int port = 69;
		final String mode = "octet";
		String fileName;
		final byte RRQ = 1;
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


	public void recevoir(String arg1,String arg2) {
		try {
			this.adress = InetAddress.getByName(arg1);
			this.fileName = arg2;
			socket = new DatagramSocket();
			inBuffer = createBuffer(RRQ,fileName,mode);
			inPacket = new DatagramPacket(inBuffer, inBuffer.length,adress,port);
			socket.send(inPacket);
			ByteArrayOutputStream resultat = receive();
			ecriture(resultat);
			//System.out.println("Transmission terminée");
			JOptionPane transencours=new JOptionPane();
			transencours.showMessageDialog(transencours, "Transmission terminée");
		} 
		catch (UnknownHostException e) {
			e.printStackTrace();
		}catch (SocketException e) {
			e.printStackTrace();
		}catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void ecriture(ByteArrayOutputStream resultat) {
		try {
			OutputStream outputStream = new FileOutputStream(fileName);
			resultat.writeTo(outputStream);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private ByteArrayOutputStream receive() throws IOException{
		ByteArrayOutputStream toReturn = new ByteArrayOutputStream();
		int nbErreurTransimission = 0;
		socket.setSoTimeout(10000);
		DatagramPacket reponseServeur;
		
		JOptionPane encours=new JOptionPane();
		encours.showMessageDialog(encours, "Reception en cours");
		
		
		do{
			outBuffer = new byte[PACKETMAXSIZE];
			reponseServeur = new DatagramPacket(outBuffer, outBuffer.length,adress,socket.getLocalPort());
			try{
				socket.receive(reponseServeur);
			}
			catch(SocketTimeoutException e){
				if(nbErreurTransimission == 5){
					//System.out.println("Le serveur ne peut pas être joint ou a mit fin a la connexion");
					JOptionPane transencours=new JOptionPane();
					transencours.showMessageDialog(transencours, "Le serveur ne peut pas être joint ou a mit fin a la connexion");
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
			else if(opCodeRetour == DATA){
				nbErreurTransimission  = 0;
				byte [] numeroBloc = {outBuffer[2],outBuffer[3]};
				toReturn.write(reponseServeur.getData(),4,reponseServeur.getLength() - 4);
				envoieConfirmation(numeroBloc,reponseServeur);
			}
		}while(!transmissionFini(reponseServeur));
		return toReturn;
	}
	
	private void envoieConfirmation(byte[] numeroBloc, DatagramPacket reponseServeur) {
		byte[] buffer = {0, ACK, numeroBloc[0],numeroBloc[1]};
		DatagramPacket paquet = new DatagramPacket(buffer, buffer.length,adress,reponseServeur.getPort());
		try {
			socket.send(paquet);
		} catch (IOException e) {                                                                                          
			e.printStackTrace();
		}
	}

	private void decodeErreur(byte codeErreur){
		String msgErreur;
		switch (codeErreur){
		case 1: msgErreur = "Le fichier demandé n'existe pas"; break;
		case 2: msgErreur = "Violation d'accés"; break;
		case 3: msgErreur = "Disque Plein"; break;
		case 4: msgErreur = "Opération TFTP illégalle"; break;
		case 5: msgErreur = "ID de transfert inconnu"; break;
		case 6: msgErreur = "Le fichier existe déjà"; break;
		case 7: msgErreur = "Pas d'utilisateur"; break;
		default: msgErreur ="Une erreur inconnu est survenu"; break;
		}
		//System.out.println("L'execution de la commande get à été imterompu: "+msgErreur);
		JOptionPane popupErreur=new JOptionPane();
		popupErreur.showMessageDialog(popupErreur,"L'execution de la commande get à été imterompu: "+msgErreur);
		
	}
	private boolean transmissionFini(DatagramPacket reponseServeur) {
		if(reponseServeur.getLength() < PACKETDATASIZE) return true;
		return false;
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