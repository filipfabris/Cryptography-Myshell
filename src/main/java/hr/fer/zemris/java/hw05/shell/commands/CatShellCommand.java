package hr.fer.zemris.java.hw05.shell.commands;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

import hr.fer.zemris.java.hw05.shell.strategy.Environment;
import hr.fer.zemris.java.hw05.shell.strategy.ShellCommand;
import hr.fer.zemris.java.hw05.shell.strategy.ShellIOException;
import hr.fer.zemris.java.hw05.shell.strategy.ShellStatus;

public class CatShellCommand implements ShellCommand{
	
    private static final String COMMAND_NAME = "cat";
    
    private static final List<String> COMMAND_DESCRIPTION = List.of("file data on standart output");

    private String charset = "UTF-8";
    
	private StringBuilder sb = new StringBuilder();

    
	@Override
	public ShellStatus executeCommand(Environment env, String arguments) throws ShellIOException {
		UtilitySharedCommand.checkArguments(env, arguments);

		String[] argumentsArray = arguments.trim().split("[\\s]+");

		// Input mora imati jedan ili dva argument
		if (argumentsArray.length > 2) {
			UtilitySharedCommand.oneOrtwoArguments(env, arguments, argumentsArray.length);
			return ShellStatus.CONTINUE;
		}
		
		//Ako postoji charset zadan
		if(argumentsArray.length == 2) {
			charset = argumentsArray[1];
			Charset a = Charset.forName(charset);
			System.out.println(a.toString());
		}
		
		// Ako je path null
		Path path = UtilitySharedCommand.getPath(env, argumentsArray[0]);
		if (path == null) {
			return ShellStatus.CONTINUE;
		}

		// Ako navedeni path ne postoji
		if (UtilitySharedCommand.checkIfExists(env, path) == false) {
			env.writeln("Given path \"" + path.toString() + "\" does not exist");
			return ShellStatus.CONTINUE;
		}
		
		//Provjeri radi li se o filu
		if (UtilitySharedCommand.checkifFile(path) == false) {
			env.writeln("Given path \"" + argumentsArray[0] + "\" is not a file");
			return ShellStatus.CONTINUE;
		}
			
		this.readFile(env, path);
		
		env.writeln(sb.toString());
		
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
	
	private void readFile(Environment env, Path path) throws ShellIOException {
		try(BufferedReader reader = Files.newBufferedReader(path, Charset.forName(charset))){
			char buffer[] = new char[2048];
			int counter = 0;
			
			while(true) {
				counter = reader.read(buffer);
				if(counter == -1) {
					break;
				}

                if (counter < buffer.length) {
                    buffer = Arrays.copyOfRange(buffer, 0, counter);
                }
				sb.append(buffer);
			}
			
		} catch (IOException e) {
			env.writeln("Error reading file \"" + path.getFileName().toString() + "\"");
		}
	}

}
