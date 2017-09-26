import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;

public class WRQ implements Runnable{

	private static final byte DATA = 3;
	private static final byte ACK = 4;
	private static final byte ERROR = 5;
	
	private final static int PACKETMAXSIZE = 516;
	private final static int PACKETDATASIZE = 512;
	
	private byte[] outBuffer;
	private DatagramPacket reponseClient;
	
	private String FileName;
	private InetAddress adresseClient;
	private int portClient;
	
	private DatagramSocket socket;

	public WRQ(String fileName, InetAddress inetAddress, int port) {
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
			ByteArrayOutputStream resultat = receive();
			ecriture(resultat);
		} catch (IOException e) {
			e.printStackTrace();
		}
		finally{
			if(socket.isClosed() || socket == null){
				socket.close();
			}
		}
	}
	
	private void ecriture(ByteArrayOutputStream resultat) {
		try {
			OutputStream outputStream = new FileOutputStream(FileName);
			resultat.writeTo(outputStream);
			outputStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		finally{
			if(socket.isClosed() || socket == null){
				socket.close();
				
			}
		}
	}

	private ByteArrayOutputStream receive() throws IOException{
		byte [] numeroBloc = null;
		int nbErreurTransimission = 0;
		socket.setSoTimeout(10000);
		ByteArrayOutputStream toReturn = new ByteArrayOutputStream();
		int nbBlockServed = 0;
		//Envoie de l'ACK indiquant le d�but de transmission
		byte InitialBlockNumber[] = {0,0};
		numeroBloc = InitialBlockNumber;
		envoieConfirmation(InitialBlockNumber, reponseClient);
		do{
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
					envoieConfirmation(numeroBloc,reponseClient);
				}
			}
			byte opCodeRetour = outBuffer[1]; 
			if(opCodeRetour == ERROR){
				byte  codeErreur = outBuffer[3];
				decodeErreur(codeErreur);
			}
			else if(opCodeRetour == DATA){
				nbErreurTransimission = 0;
				numeroBloc[0] = outBuffer[2];
				numeroBloc[1] = outBuffer[3];
				toReturn.write(reponseClient.getData(),4,reponseClient.getLength() - 4);
				envoieConfirmation(numeroBloc,reponseClient);
				nbBlockServed++;
			}
		}while(!transmissionFini(reponseClient));
		System.out.println(nbBlockServed+" Blocks Received");
		return toReturn;
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
	
	private void envoieConfirmation(byte[] numeroBloc, DatagramPacket reponseClient) {
		byte[] buffer = {0, ACK, numeroBloc[0],numeroBloc[1]};
		DatagramPacket paquet = new DatagramPacket(buffer, buffer.length,adresseClient,portClient);
		try {
			socket.send(paquet);
			//System.out.println(numeroBloc[0]+":"+numeroBloc[1]);
		} catch (IOException e) {                                                                                          
			e.printStackTrace();
		}
		finally{
			if(socket.isClosed() || socket == null){
				socket.close();
			}
		}
	}
	
	private boolean transmissionFini(DatagramPacket reponseServeur) {
		if(reponseServeur.getLength() < PACKETDATASIZE) return true;
		return false;
	}

}
