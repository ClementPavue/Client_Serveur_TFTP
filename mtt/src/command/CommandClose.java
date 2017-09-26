package command;

public class CommandClose extends Command {

	public CommandClose() {
		super("Close");
		// TODO Auto-generated constructor stub
	}
	
	public void execute(Interprete i) {
		i.terminated();
		System.out.println("Connection closed");
	}

	public String Help(){
		
		return "Close : met un terme à la connexion";
}

}
