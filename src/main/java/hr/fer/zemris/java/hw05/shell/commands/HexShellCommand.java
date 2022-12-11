package hr.fer.zemris.java.hw05.shell.commands;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedList;
import java.util.List;

import hr.fer.zemris.java.hw05.shell.strategy.Environment;
import hr.fer.zemris.java.hw05.shell.strategy.ShellCommand;
import hr.fer.zemris.java.hw05.shell.strategy.ShellIOException;
import hr.fer.zemris.java.hw05.shell.strategy.ShellStatus;

public class HexShellCommand implements ShellCommand{
	
    private static final String COMMAND_NAME = "hexdump";
    
    private static final List<String> COMMAND_DESCRIPTION = List.of("produces hex-output");

    private static final int FORMAT_SIZE = 16;

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

		// Ako navedeni path ne postoji
		if (UtilitySharedCommand.checkIfExists(env, path) == false) {
			env.writeln("Given path \"" + path.toString() + "\" does not exist");
			return ShellStatus.CONTINUE;
		}
		
		//Provjeri radi li se o filu
		if (UtilitySharedCommand.checkifFile(path) == false) {
			env.writeln("Given path \"" + argumentsArray[0] + "\" is not a file!");
			return ShellStatus.CONTINUE;
		}
		
        try(BufferedInputStream reader = new BufferedInputStream(Files.newInputStream(path))) {
            
            byte[] buf = reader.readAllBytes();
            
            List<Byte> tmp = new LinkedList<>();
            int rowNumber = 0;
            int counter = 0;
            
            for(int i = 0; i<buf.length; i++) {
            	//Ispis ide po 16 bytova
            	if(counter < FORMAT_SIZE) {
            		tmp.add(buf[i]);
            		counter++;
            	}else {
            		env.write(formatRow(tmp, rowNumber));
            		tmp = new LinkedList<>();
            		tmp.add(buf[i]);
            		counter = 1;
            		rowNumber++;
            	}
            }
            if(tmp.size() != 0) {
            	env.write(formatRow(tmp, rowNumber));
            }
        } catch (IOException e) {
            env.writeln("An exception occurred while reading from the given file with the path (\"" + path.toString() + "\")!");
        }
		
		
        return ShellStatus.CONTINUE;

	}
	
	   private String formatRow( List<Byte> array, int rowNumber) {
		   
	        StringBuilder line = new StringBuilder();
	        StringBuilder hex1 = new StringBuilder();
	        StringBuilder hex2 = new StringBuilder();
	        StringBuilder charSet = new StringBuilder();

	        line.append(String.format("%08d:", rowNumber));
	        
	        int i = 0;
	        int length = array.size();
	        for (; i < length; i++) {
	            if(i < FORMAT_SIZE/2) {
	            	hex1.append(String.format(" %02X", array.get(i)));
//	            	if(i == FORMAT_SIZE/2 -1) { //bezveze kompicira logiku
//	            		hex1.append(" ");
//	            		hex2.append(" ");
//	            	}
	            } else {
	            	hex2.append(String.format("%02X ", array.get(i)));
	            }
	            
	            if(array.get(i) < 32 || array.get(i) > 127) {	
	            	charSet.append(".");
	            }else {	            	
	            	charSet.append(String.format("%c", array.get(i)));
	            }
	        }
	        
	        
	        if(length < FORMAT_SIZE) {
	        		while(i < FORMAT_SIZE/2) {
		            	hex1.append("   ");	 
		            	i++;
	        		}
	        		while(i < FORMAT_SIZE) {
		            	hex2.append("   ");
		            	i++;
	        		}
	        }

	        line.append(hex1);
	        line.append("|");
	        line.append(hex2);
	        line.append("|");
	        line.append(" ");
	        line.append(charSet);
	        line.append("\n");

	        return line.toString();
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
