package hr.fer.zemris.java.hw05.shell.commands;

import java.util.List;

import hr.fer.zemris.java.hw05.shell.strategy.Environment;
import hr.fer.zemris.java.hw05.shell.strategy.ShellCommand;
import hr.fer.zemris.java.hw05.shell.strategy.ShellIOException;
import hr.fer.zemris.java.hw05.shell.strategy.ShellStatus;

public class ExitShellCommand implements ShellCommand{
	
    private static final String COMMAND_NAME = "exit";
    
    private static final List<String> COMMAND_DESCRIPTION = List.of("exit, terminate shell");


	@Override
	public ShellStatus executeCommand(Environment env, String arguments) throws ShellIOException {
		
        UtilitySharedCommand.checkArguments(env, arguments);
        
		String[] argumentsArray = arguments.trim().split("[\\s]+");
        
		//tekst iza komande, bijeline dopustamo
        if (arguments.isBlank() == false) {
        	UtilitySharedCommand.noArguments(env, COMMAND_NAME, argumentsArray.length);
            return ShellStatus.CONTINUE;
        }
        
        return ShellStatus.TERMINATE;
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
