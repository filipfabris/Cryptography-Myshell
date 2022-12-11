package hr.fer.zemris.java.hw05.shell.commands;

import java.util.List;

import hr.fer.zemris.java.hw05.shell.strategy.Environment;
import hr.fer.zemris.java.hw05.shell.strategy.ShellCommand;
import hr.fer.zemris.java.hw05.shell.strategy.ShellIOException;
import hr.fer.zemris.java.hw05.shell.strategy.ShellStatus;

public class HelpShellCommand implements ShellCommand{
	
    private static final String COMMAND_NAME = "help";
    
    private static final List<String> COMMAND_DESCRIPTION = List.of("command descriptions");

	@Override
	public ShellStatus executeCommand(Environment env, String arguments) throws ShellIOException {

		
        UtilitySharedCommand.checkArguments(env, arguments);
        
		String[] argumentsArray = arguments.trim().split("[\\s]+");
		
		if(argumentsArray.length > 1) {
			UtilitySharedCommand.oneOrNone(env, arguments, argumentsArray.length);
            return ShellStatus.CONTINUE;
		}
		
		ShellCommand command;
		String output;
		StringBuilder sb = new StringBuilder();;
		
		//help za jednu commandu
		if(argumentsArray.length == 1) {
			command = env.commands().get(argumentsArray[0]);
			
			if(command == null) {
				UtilitySharedCommand.commandNotExists(env, argumentsArray[0]);
			}
			
			command.getCommandDescription().stream().forEach(s -> sb.append(s));
			
			output = String.format("Command name: %s\nCommand description: %s\n", command.getCommandName(), sb.toString());
			env.write(output);
            return ShellStatus.CONTINUE;
		}
		
		//help all
		for(String key: env.commands().keySet()) {
			sb.setLength(0);
			command = env.commands().get(key);
			output = String.format("Command name: %s\nCommand description: %s\n", command.getCommandName(),sb.toString());
			env.write(output);
		}

        return ShellStatus.CONTINUE;
	}

	@Override
	public String getCommandName() {
		return COMMAND_NAME;
	}

	@Override
	public List<String> getCommandDescription() {
		return COMMAND_DESCRIPTION;
	}

}
