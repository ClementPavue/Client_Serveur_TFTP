import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServeurEcoute  implements Runnable{

	private static final int port = 69;
	
	private static final byte RRQ = 1;
	private static final byte WRQ = 2;
	
	private DatagramSocket socketEcoute = null;

	private InetAddress adress = null;
	
	private byte[] outBuffer;
	private DatagramPacket reponseClient;
	
	private final static int PACKETMAXSIZE = 516;
	
	private String FileName;
	
	final ExecutorService ThreadPool = Executors.newFixedThreadPool(100);
	
	@Override
	public void run() {
		try {
			adress = InetAddress.getLocalHost();
		} catch (UnknownHostException e1) {
			e1.printStackTrace();
		}
		System.out.println("Listening on:"+adress.toString());
		try {
			socketEcoute = new DatagramSocket(port,adress);
		} catch (SocketException e1) {
			e1.printStackTrace();
		}
		finally{
			if(socketEcoute.isClosed() || socketEcoute == null){
				socketEcoute.close();
			}
		}
		
		while(true){
			try {
				outBuffer = new byte[PACKETMAXSIZE];
				reponseClient = new DatagramPacket(outBuffer, outBuffer.length);
				System.out.println("Serveur en attente de connexion:");
				socketEcoute.receive(reponseClient);
				System.out.println("Une réponse est arrivée:");
				byte opCodeRetour = outBuffer[1]; 
				FileName = getFileName();
				if(opCodeRetour == RRQ){
					ThreadPool.execute(new RRQ(FileName,reponseClient.getAddress(),reponseClient.getPort()));
				}
				else if(opCodeRetour == WRQ){
					ThreadPool.execute(new WRQ(FileName,reponseClient.getAddress(),reponseClient.getPort()));
				}
				System.out.println("Transmission terminée");
			}
			catch (UnknownHostException e) {
				e.printStackTrace();
			}catch (SocketException e) {
				e.printStackTrace();
			}catch (IOException e) {
				e.printStackTrace();
			}
			finally{
				if(socketEcoute.isClosed() || socketEcoute == null){
					socketEcoute.close();
				}
			}
			
		}
		
	}
	
	private String getFileName() {
		ByteArrayOutputStream toReturn= new ByteArrayOutputStream();
		int i = 2;
		while(outBuffer[i] !=0){
			toReturn.write(outBuffer[i]);
			i++;
		}
		return toReturn.toString();
	}

}
