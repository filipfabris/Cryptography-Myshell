package hr.fer.zemris.java.hw05.shell.commands;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import hr.fer.zemris.java.hw05.shell.strategy.Environment;
import hr.fer.zemris.java.hw05.shell.strategy.ShellCommand;
import hr.fer.zemris.java.hw05.shell.strategy.ShellIOException;
import hr.fer.zemris.java.hw05.shell.strategy.ShellStatus;

public class MkdirShellCommand implements ShellCommand{
	
	private static final String COMMAND_NAME = "mkdir";

	private static final List<String> COMMAND_DESCRIPTION = List.of("creates the appropriate directory structure");

	@Override
	public ShellStatus executeCommand(Environment env, String arguments) throws ShellIOException {

		UtilitySharedCommand.checkArguments(env, arguments);

		String[] argumentsArray = arguments.trim().split("[\\s]+");

		// Input mora imati samo jedan argument
		if (argumentsArray.length != 1) {
			UtilitySharedCommand.singleArgument(env, COMMAND_NAME, argumentsArray.length);
			return ShellStatus.CONTINUE;
		}

		// Ako je path null
		Path path = UtilitySharedCommand.getPath(env, argumentsArray[0]);
		if (path == null) {
			return ShellStatus.CONTINUE;
		}
		
        try {
        	System.out.println(path.toAbsolutePath().toString());
            Files.createDirectories(path.toAbsolutePath());
        } catch (IOException e) {
            env.writeln("Error creating \"" + argumentsArray[0] + "\" directory structure");
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
