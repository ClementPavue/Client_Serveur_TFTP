package command;

public class ProcessorException extends Exception {

	public ProcessorException(String err) {
		System.out.println("Commande inconnue. Pour vous renseignez sur les commandes disponible et leurs syntaxe veuillez écrire '-h'");
	}
	
}
