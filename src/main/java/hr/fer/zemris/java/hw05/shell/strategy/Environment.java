package hr.fer.zemris.java.hw05.shell.strategy;

import java.util.SortedMap;

public interface Environment {
	
	String readLine() throws ShellIOException;
	
	void write(String text) throws ShellIOException;
	
	void writeln(String text) throws ShellIOException;
	
	SortedMap<String, ShellCommand> commands();
	
	Character getMultilineSymbol();
	
	void setMultilineSymbol(Character symbol) throws ShellIOException;
	
	Character getPromptSymbol();
	
	void setPromptSymbol(Character symbol) throws ShellIOException;
	
	Character getMorelinesSymbol();
	
	void setMorelinesSymbol(Character symbol) throws ShellIOException;
	
	void setCharset(String charset) throws ShellIOException;

}
