package command;

import java.util.Map;
import java.util.Scanner;
import java.util.StringTokenizer;
import java.util.TreeMap;

public class Interprete {
	
	private boolean terminated;
	private Map commands;
	
	public Interprete() {
		super();
		this.terminated = false;
		this.commands = new TreeMap();
		this.add(new CommandClose());
		this.add(new CommandHelp());
		this.add(new CommandGet());
		this.add(new CommandPut());
	}
	
	public Command decode(String[] toDecode)throws ProcessorException{
		if(toDecode.length != 0){
			Command c = (Command) commands.get(toDecode[0]);
			c.setParameters(toDecode);
			return c;
		}
		throw new ProcessorException("Unknown command :" + toDecode);
	}

	public void execute(Command toExecute){
		toExecute.execute(this);
	}
	
	public void add(Command c){
		this.commands.put(c.getName(), c); 
	}
	
	public Map getCommand(){
		return this.commands;
	}

	public void terminated() {
		this.terminated = true;		
	}

	public boolean isTerminated() {
		return terminated;
	}

}
