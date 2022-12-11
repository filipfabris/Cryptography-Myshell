package hr.fer.zemris.java.hw05.shell.commands;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import hr.fer.zemris.java.hw05.shell.strategy.Environment;
import hr.fer.zemris.java.hw05.shell.strategy.ShellCommand;
import hr.fer.zemris.java.hw05.shell.strategy.ShellIOException;
import hr.fer.zemris.java.hw05.shell.strategy.ShellStatus;

public class CopyShellCommand implements ShellCommand{
	
	private static final String COMMAND_NAME = "copy";

	private static final List<String> COMMAND_DESCRIPTION = List.of("copy file");

	@Override
	public ShellStatus executeCommand(Environment env, String arguments) throws ShellIOException {
		
		String[] argumentsArray = arguments.trim().split("[\\s]+");

		// Input mora imati jedan ili dva argument
		if (argumentsArray.length > 2) {
			UtilitySharedCommand.oneOrtwoArguments(env, arguments, argumentsArray.length);
			return ShellStatus.CONTINUE;
		}
		
		

		// Ako je path null sourca
		Path pathSource = UtilitySharedCommand.getPath(env, argumentsArray[0]);
		if (pathSource == null) {
			return ShellStatus.CONTINUE;
		}

		// Ako navedeni path sourca ne postoji
		if (UtilitySharedCommand.checkIfExists(env, pathSource) == false) {
			env.writeln("Given path \"" + pathSource.toString() + "\" does not exist");
			return ShellStatus.CONTINUE;
		}

		//Provjeri radi li se o filu sourca patha
		if (UtilitySharedCommand.checkifFile(pathSource) == false) {
			env.writeln("Given path \"" + argumentsArray[0] + "\" is not a file");
			return ShellStatus.CONTINUE;
		}
		
		
		
		
		//Ako destination nije null
		Path pathDestination = UtilitySharedCommand.getPath(env, argumentsArray[1]);
		if (pathDestination == null) {
			return ShellStatus.CONTINUE;
		}
		
		// Ako navedeni path destinationa postoji
		if (UtilitySharedCommand.checkIfExists(env, pathDestination) == true) {
			
			//Ako direktoriji uredi path tako da bude isto ime
			if(UtilitySharedCommand.checkifDirectory(pathDestination)) {
				pathDestination = UtilitySharedCommand.getPath(env, argumentsArray[1] + pathSource.getFileName());

			}
			
			//Provjeri da ne postoji vec file pod tim pathom
			if (UtilitySharedCommand.checkifFile(pathDestination) == true) {
				env.writeln("On given path \"" + argumentsArray[0] + "\" file already exists");
				
				env.writeln("Do you want to overwrite it: YES / NO");
				
				env.write(env.getPromptSymbol() + " ");
				String response = env.readLine().trim();
				
				if(response.equalsIgnoreCase("NO")) {
					return ShellStatus.CONTINUE;				
				}
			}			
		}
		

		
		this.copy(pathSource, pathDestination);
			
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
	
	public void copy(Path pathSource, Path pathDestination) {
		
		//Ocekujem da su promjene napravljene 
		if(pathSource == null || pathDestination == null) {
			throw new NullPointerException("Path is null");
		}
		
		try(
			BufferedInputStream reader = new BufferedInputStream(Files.newInputStream(pathSource));
			BufferedOutputStream writer = new BufferedOutputStream(Files.newOutputStream(pathDestination));
			){
			
			byte[] buffer = new byte[1024];
			int lenght;
			while(true) {
				
				lenght = reader.read(buffer);
				
				if(lenght < 1) {
					break;
				}
				
				writer.write(buffer, 0, lenght);
			}
			
		} catch (IOException e) {
			throw new RuntimeException("Error while copy-ing file");
		}
		
		
		

		
		
	}

}
