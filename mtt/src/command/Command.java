package command;

import command.Interprete;

public abstract class Command {
	
	private String name;
	private String parameters[];

	private Interprete i;
	
	public Command(String s){
		this.name = s;
		this.i = null;
	}
	
	public String getName(){
		return this.name;
	}
	
	public void setParameters(String p[]){
		this.parameters = p;
	}
	public String[] getParameters() {
		return parameters;
	}
	
	public abstract void execute(Interprete i);
}
