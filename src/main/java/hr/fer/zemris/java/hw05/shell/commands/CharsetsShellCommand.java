package hr.fer.zemris.java.hw05.shell.commands;

import java.nio.charset.Charset;
import java.util.List;

import hr.fer.zemris.java.hw05.shell.strategy.Environment;
import hr.fer.zemris.java.hw05.shell.strategy.ShellCommand;
import hr.fer.zemris.java.hw05.shell.strategy.ShellIOException;
import hr.fer.zemris.java.hw05.shell.strategy.ShellStatus;

public class CharsetsShellCommand implements ShellCommand{
	
    private static final String COMMAND_NAME = "charsets";
    
    private static final List<String> COMMAND_DESCRIPTION = List.of("available charsets");
    
	@Override
	public ShellStatus executeCommand(Environment env, String arguments) throws ShellIOException {
		UtilitySharedCommand.checkArguments(env, arguments);
		
		String[] argumentsArray = arguments.trim().split("[\\s]+");

		
        if (arguments.isBlank() == false) {
        	UtilitySharedCommand.noArguments(env, COMMAND_NAME, argumentsArray.length);
            return ShellStatus.CONTINUE;
        }
        
        for(String tmp: Charset.availableCharsets().keySet()) {
        	env.writeln(tmp);
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
