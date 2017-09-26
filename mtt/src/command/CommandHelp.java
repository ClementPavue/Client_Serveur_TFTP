package command;

public class CommandHelp extends Command {

	public CommandHelp() {
		super("-h");
	}

	@Override
	public void execute(Interprete i) {
		String[] p = this.getParameters();
		CommandGet cg=new CommandGet();
		CommandPut cp=new CommandPut();
		CommandClose cc=new CommandClose();
		if(p.length != 1){
			switch (p[1]){
			case "Get": System.out.println(cg.Help());; break;
			case "Put": System.out.println(cp.Help());; break;
			case "Close": System.out.println(cc.Help());; break;
			}
		}
		else{
			System.out.println(cg.Help());
			System.out.println(cp.Help());
			System.out.println(cc.Help());
		}

		}
	}
