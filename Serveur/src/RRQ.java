import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;

public class RRQ implements Runnable{

	private static final byte DATA = 3;
	private static final byte ACK = 4;
	private static final byte ERROR = 5;
	
	private static final byte FILE = 1;
	
	private final static int PACKETMAXSIZE = 516;
	private final static int PACKETDATASIZE = 512;
	
	private byte[] outBuffer;
	private DatagramPacket reponseClient;
	
	private String FileName;
	private InetAddress adresseClient;
	private int portClient;
	
	private DatagramSocket socket;

	public RRQ(String fileName, InetAddress inetAddress, int port) {
		this.FileName = fileName;
		this.adresseClient = inetAddress;
		this.portClient = port;
		try {
			this.socket = new DatagramSocket();
		} catch (SocketException e) {
			e.printStackTrace();
		}
		finally{
			if(socket.isClosed() || socket == null){
				socket.close();
			}
		}
	}

	public void run() {
		try {
			send();
		} catch (IOException e) {
			e.printStackTrace();
		}
		finally{
			if(socket.isClosed() || socket == null){
				socket.close();
			}
		}
	}
	
	private void send() throws IOException{
		int nbBlockServed = 0;
		socket.setSoTimeout(10000);
		try{
			FileInputStream lecteur = new FileInputStream(FileName);
			int nb = 0;
			
			//Envoie du premier paquet de data
			byte[] tmp = new byte[PACKETDATASIZE];
			nb = lecteur.read(tmp,0,PACKETDATASIZE);
			byte[] data = new byte[nb];
			for(int i= 0;i <nb;i++)data[i] = tmp[i];
			byte [] numeroBloc = {0,1};
			envoieData(numeroBloc, data);
			int nbErreurTransimission = 0;
			nbBlockServed++;
			
			while(!lectureFini(nb)){
				outBuffer = new byte[PACKETMAXSIZE];
				reponseClient = new DatagramPacket(outBuffer, outBuffer.length,adresseClient,portClient);
				try{
					socket.receive(reponseClient);
				}
				catch(SocketTimeoutException e){
					if(nbErreurTransimission  == 5){
						System.out.println("Le client ne peut pas �tre joint ou a mis fin a la connexion");
						socket.close();
						break;
					}
					else{
						nbErreurTransimission++;
						envoieData(numeroBloc,data);
					}
				}
				byte opCodeRetour = outBuffer[1]; 
				if(opCodeRetour == ERROR){
					byte  codeErreur = outBuffer[3];
					decodeErreur(codeErreur);
				}
				else if(opCodeRetour == ACK){
					nbErreurTransimission = 0;
					tmp = new byte[PACKETDATASIZE];
					nb = lecteur.read(tmp,0,PACKETDATASIZE);
					if(nb != -1)data = new byte[nb];
					else data = new byte[0];
					for(int i= 0;i <nb;i++)data[i] = tmp[i];
					numeroBloc[0] = outBuffer[2];
					numeroBloc[1] = outBuffer[3];
					//System.out.println(numeroBloc[0]+" "+numeroBloc[1]);
					envoieData(numeroBloc, data);
					nbBlockServed++;
				}
			}
			System.out.println(nbBlockServed+" Blocks Served");
			lecteur.close();
			}
		catch(FileNotFoundException e){
			sendError(FILE,e,reponseClient);
		}
		finally{
			if(socket.isClosed() || socket == null){
				socket.close();
			}
		}
	}
	
	private void sendError(byte ErrorCode, FileNotFoundException f, DatagramPacket reponseClient) {
		int length;
		int pos = 0;
		length = 2 + 2 + f.getMessage().length();
		byte toSend[] = new byte[length];
		toSend[pos] = 0;
		pos++;
		toSend[pos] = ERROR;
		pos++;
		toSend[pos] = 0;
		pos++;
		toSend[pos] = ErrorCode;
		pos++;
		byte[] message = f.getMessage().getBytes();
		for (int i = 0; i < message.length; i++) {
			toSend[pos] = message[i];
			pos++;
		}
		DatagramPacket paquet = new DatagramPacket(toSend, toSend.length,adresseClient,portClient);
		try {
			socket.send(paquet);
		} catch (IOException e) {                                                                                          
			e.printStackTrace();
		}
		finally{
			if(socket.isClosed() || socket == null){
				socket.close();
			}
		}
	}

	private boolean lectureFini(int nb) {
		if(nb < PACKETDATASIZE) return true;
		return false;
	}

	private void envoieData(byte[] numeroBloc,byte[] data){
		byte[] buffer = buildData(numeroBloc,data);
		DatagramPacket paquet = new DatagramPacket(buffer, buffer.length,adresseClient,portClient);
		try {
			socket.send(paquet);
		} catch (IOException e) {                                                                                          
			e.printStackTrace();
		}
		finally{
			if(socket.isClosed() || socket == null){
				socket.close();
			}
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
			System.out.println("Taille maximale d�pass�e");
		}
		return newNumber;
	}
	private void decodeErreur(byte codeErreur){
		String msgErreur;
		System.out.println(codeErreur);
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
		System.out.println("L'execution de la commande get a été imterompu: "+msgErreur);
	}

}
