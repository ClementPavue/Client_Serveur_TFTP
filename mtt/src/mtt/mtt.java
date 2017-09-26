package mtt;

import command.Interprete;
import command.ProcessorException;

public class mtt {
	
	public static void main(String[] args) {
		Interprete i = new Interprete();
			try {
				i.execute(i.decode(args));
			} 
			catch (ProcessorException e) {

			}
	}
}
