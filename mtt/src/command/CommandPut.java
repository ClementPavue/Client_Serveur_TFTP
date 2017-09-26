package command;

import java.io.ByteArrayOutputStream;
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

public class CommandPut extends Command{

	//Paramï¿½tres nï¿½cessaire ï¿½ la connexion
	private static final int port = 69;
	private static final String mode = "octet";
	private String fileName;
	
	private static final int nbParametres = 3;
	
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
	
	public CommandPut() {
		super("Put");
	}

	@Override
	public void execute(Interprete i) {
		String parameters[] = this.getParameters();
		if(parameters.length == nbParametres){
			try {
				this.adress = InetAddress.getByName(parameters[1]);
				this.fileName = parameters[2];
				socket = new DatagramSocket();
				inBuffer = createBuffer(WRQ,fileName,mode);
				inPacket = new DatagramPacket(inBuffer, inBuffer.length,adress,port);
				socket.send(inPacket);
				send();
				System.out.println("Transmission terminï¿½e");
			} 
			catch (UnknownHostException e) {
				e.printStackTrace();
			}catch (SocketException e) {
				e.printStackTrace();
			}catch (IOException e) {
				e.printStackTrace();
			}
		}
		else{
			System.out.println("L'appel à la fonction Put a échouée. Veuillez vérifiez la syntaxe de cette commande à l'aide de la commande -h");
		}
	}
	

	private void send() throws IOException{
		int nbBlockServed = 0;
		int nbErreurTransimission = 0;
		DatagramPacket reponseServeur = null;
		socket.setSoTimeout(1000);
		try{
			FileInputStream lecteur = new FileInputStream(fileName);
			System.out.println(lecteur.available()/(512) + " : nombre de paquets à émettre");
			int nb = 513;
			do{
				outBuffer = new byte[PACKETMAXSIZE];
				reponseServeur = new DatagramPacket(outBuffer, outBuffer.length,adress,socket.getLocalPort());
				try{
					socket.receive(reponseServeur);
				}
				catch(SocketTimeoutException e){
					if(nbErreurTransimission == 5){
						System.out.println("Le serveur ne peut pas être joint ou a mis fin a la connexion");
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
					System.out.print(numeroBloc[0]+":"+numeroBloc[1]+" ");
					envoieData(numeroBloc, data, reponseServeur);
					nbBlockServed++;
				}
			}while(!lectureFini(nb));
			System.out.println(nbBlockServed+" Blocks Served");
		}
		catch(FileNotFoundException e){
			System.out.println("Le fichier spécifié n'existe pas");
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
		if(numeroBloc[1] != -1 && numeroBloc[0] != -1){
			newNumber[0] = numeroBloc[0];
			newNumber[1] = (byte) (numeroBloc[1] + 1);
		}
		else if(numeroBloc[1] == -1){
			newNumber[0] = (byte) (numeroBloc[0]+1);
			newNumber[1] = 0;
		}
		else if(numeroBloc[0] == -1){
			System.out.println("Taille maximale dépassée");
		}
		return newNumber;
	}
	private void decodeErreur(byte codeErreur){
		String msgErreur;
		switch (codeErreur){
		case 1: msgErreur = "Le fichier demandé n'existe pas"; break;
		case 2: msgErreur = "Violation d'accès"; break;
		case 3: msgErreur = "Disque Plein"; break;
		case 4: msgErreur = "Opï¿½ration TFTP illégalle"; break;
		case 5: msgErreur = "ID de transfert inconnu"; break;
		case 6: msgErreur = "Le fichier existe déjà"; break;
		case 7: msgErreur = "Pas d'utilisateur"; break;
		default: msgErreur ="Une erreur inconnu est survenu"; break;
		}
		System.out.println("L'execution de la commande get a été imterompu: "+msgErreur);
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
public String Help(){
		
		return "Put : envoie un fichier ( Put IP nomFichier) ";
}
}
