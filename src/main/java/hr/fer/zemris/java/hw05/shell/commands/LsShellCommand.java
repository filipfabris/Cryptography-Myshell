package hr.fer.zemris.java.hw05.shell.commands;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributeView;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import hr.fer.zemris.java.hw05.shell.strategy.Environment;
import hr.fer.zemris.java.hw05.shell.strategy.ShellCommand;
import hr.fer.zemris.java.hw05.shell.strategy.ShellIOException;
import hr.fer.zemris.java.hw05.shell.strategy.ShellStatus;

public class LsShellCommand implements ShellCommand {

	private static final String COMMAND_NAME = "ls";

	private static final List<String> COMMAND_DESCRIPTION = List.of("writes a directory listing");

	private static final SimpleDateFormat DATE = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

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

		try (DirectoryStream<Path> stream = Files.newDirectoryStream(path)) {
			for (Path filePath : stream) {
				String output = formatOutput(env, filePath);
				if (output == null) {
					return ShellStatus.CONTINUE;
				}
				env.writeln(output);
			}
		} catch (IOException e) {
			env.writeln("Cannot get directory listing of (\"" + argumentsArray[0] + "\")!");
		}
 
//    	System.out.println(path.toAbsolutePath().toString());

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

	private String formatOutput(Environment env, Path path) throws ShellIOException {
		BasicFileAttributeView faView = Files.getFileAttributeView(path, BasicFileAttributeView.class,
				LinkOption.NOFOLLOW_LINKS);
		try {
			BasicFileAttributes attributes = faView.readAttributes();

			FileTime fileTime = attributes.creationTime();
			String formattedDateTime = DATE.format(new Date(fileTime.toMillis()));

			String directory;
			if (attributes.isDirectory()) {
				directory = "d";
			} else {
				directory = "-";
			}

			String readable;
			if (Files.isReadable(path)) {
				readable = "r";
			} else {
				readable = "-";
			}

			String writable;
			if (Files.isWritable(path)) {
				writable = "w";
			} else {
				writable = "-";
			}

			String executable;
			if (Files.isExecutable(path)) {
				executable = "x";
			} else {
				executable = "-";
			}

			Long size = attributes.size();
			String name = path.getFileName().toString();

			return String.format("%s%s%s%s %10d %s %s", directory, readable, writable, executable, size,
					formattedDateTime, name);

		} catch (IOException e) {
			env.writeln("Cannot read properties for the file with the path (\"" + path.toString() + "\")!");
			return null;
		}
	}

}
