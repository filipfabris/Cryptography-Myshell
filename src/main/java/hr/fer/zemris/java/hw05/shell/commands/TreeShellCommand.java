package hr.fer.zemris.java.hw05.shell.commands;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.List;

import hr.fer.zemris.java.hw05.shell.strategy.Environment;
import hr.fer.zemris.java.hw05.shell.strategy.ShellCommand;
import hr.fer.zemris.java.hw05.shell.strategy.ShellIOException;
import hr.fer.zemris.java.hw05.shell.strategy.ShellStatus;

public class TreeShellCommand implements ShellCommand {

	private static final String COMMAND_NAME = "tree";

	private static final List<String> COMMAND_DESCRIPTION = List.of("directory name and prints a tree");
	
	private static class TreeFileVisitor extends SimpleFileVisitor<Path> {

		private final Environment env;

		private int depth;

		private static final int SPACE = 2;

		public TreeFileVisitor(Environment env) {
			this.env = env;
			this.depth = 0;
		}

		@Override
		public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) {

			if (dir == null) {
				throw new NullPointerException("Path dir is null");
			}

			try {
				printRow(env, dir);
			} catch (ShellIOException e) {
				e.printStackTrace();
				return FileVisitResult.TERMINATE;
			}

			depth++;

			return FileVisitResult.CONTINUE;
		}

		@Override
		public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {

			if (file == null) {
				throw new NullPointerException("Path dir is null");
			}

			try {
				printRow(env, file);
			} catch (ShellIOException e) {
				e.printStackTrace();
				return FileVisitResult.TERMINATE;
			}

			return FileVisitResult.CONTINUE;
		}

		@Override
		public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {

			depth--;
			return FileVisitResult.CONTINUE;
		}

		private void printRow(Environment env, Path file) throws ShellIOException {
			
			String name = file.getFileName().toString();
			StringBuilder sb = new StringBuilder();
			int length = depth * SPACE;
			
			for(int i = 0; i<length; i++) {
				sb.append("-");
			}
			
			env.writeln(String.format("%s%s", sb.toString() , name));
		}
	}


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

		// Ako se ne radi o direktoriju
		if (UtilitySharedCommand.checkifDirectory(path) == false) {
			env.writeln("Given path \"" + argumentsArray[0] + "\" is not a directory!");
			return ShellStatus.CONTINUE;
		}

		try {
			Files.walkFileTree(path, new TreeFileVisitor(env));
		} catch (IOException e) {
			env.writeln("Error occurred while going through the given directory");
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
