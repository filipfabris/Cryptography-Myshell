package hr.fer.zemris.java.hw05.shell.strategy;

import java.util.List;

public interface ShellCommand {
	
	ShellStatus executeCommand(Environment env, String arguments) throws ShellIOException;
	
	String getCommandName();
	
	List<String> getCommandDescription();

}
