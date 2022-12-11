package hr.fer.zemris.java.hw05.shell.commands;

import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;

import hr.fer.zemris.java.hw05.shell.strategy.Environment;
import hr.fer.zemris.java.hw05.shell.strategy.ShellIOException;

public class UtilitySharedCommand {

	public static void checkArguments(Environment env, String arguments) {

		if (env == null) {
			throw new NullPointerException("Given environment cannot be null");
		}
		if (arguments == null) {
			throw new NullPointerException("Given string of arguments cannot be null");
		}
	}

	public static void noArguments(Environment env, String commandName, int length) throws ShellIOException {
		env.writeln(commandName + "  cannot take any arguments, recived: " + length);
	}

	public static void singleArgument(Environment env, String commandName, int length) throws ShellIOException {
		env.writeln(commandName + "  expects only one argument, recived: " + length);
	}

	public static void twoArguments(Environment env, String commandName, int length) throws ShellIOException {
		env.writeln(commandName + " expects two arguments, recived: " + length);
	}
	
	public static void oneOrtwoArguments(Environment env, String commandName, int length) throws ShellIOException {
		env.writeln(commandName + " expects one or two arguments, recived: " + length);
	}
	
	public static void oneOrNone(Environment env, String commandName, int length) throws ShellIOException {
		env.writeln(commandName + " expects one or none arguments, recived: " + length);
	}
	
	public static void commandNotExists(Environment env, String commandName) throws ShellIOException {
		env.writeln("command by name: " + commandName + " does not exists");
	}
	
	public static Path getPath(Environment env, String pathString) throws ShellIOException {
		Path path = null; //Bolje
//		 File file = null; //legacy
		try {
			path = Paths.get(pathString);
//            file = new File(pathString);
		} catch (InvalidPathException e) {
			env.writeln("Invalid path for given input: \"" + pathString + "\"");
		}
		
//		return file.toPath(); //Vraca path
//		return file.getPath(); //Vraca string
		return path;
	}

	public static boolean checkIfExists(Environment env, Path path) throws ShellIOException {
//		File file = new File(path.toString()); //Legacy
//		if(file.exists()) {
//			return true;
//
//		}
		
		if (Files.exists(path)) {
			return true;
		}
		
		return false;
	}
	
	public static boolean checkifDirectory(Path path) {
		if(Files.isDirectory(path)) {
			return true;
        }
		return false;
	}
	
	public static boolean checkifFile(Path path) {
		if(Files.isRegularFile(path)) {
			return true;
        }
		return false;
	}
	

}
