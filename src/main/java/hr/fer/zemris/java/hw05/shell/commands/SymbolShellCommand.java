package hr.fer.zemris.java.hw05.shell.commands;

import java.util.List;

import hr.fer.zemris.java.hw05.shell.strategy.Environment;
import hr.fer.zemris.java.hw05.shell.strategy.ShellCommand;
import hr.fer.zemris.java.hw05.shell.strategy.ShellIOException;
import hr.fer.zemris.java.hw05.shell.strategy.ShellStatus;

public class SymbolShellCommand implements ShellCommand{
	
	private static final String COMMAND_NAME = "symbol";

	private static final List<String> COMMAND_DESCRIPTION = List.of("use this command to chnage design");

	private static final List<String> SYMBOLS = List.of("prompt", "morelines", "multiline");
	
	@Override
	public ShellStatus executeCommand(Environment env, String arguments) throws ShellIOException {
		UtilitySharedCommand.checkArguments(env, arguments);

		String[] argumentsArray = arguments.trim().split("[\\s]+");
		
		if (argumentsArray.length > 2) {
			UtilitySharedCommand.oneOrtwoArguments(env, arguments, 0);
			return ShellStatus.CONTINUE;
		}
		
		if(argumentsArray.length == 1) {
			this.writeSymbol(env, argumentsArray[0]);
		}

		if(argumentsArray.length == 2) {
			this.changeSymbol(env, argumentsArray[0], argumentsArray[1]);
		}

		return ShellStatus.CONTINUE;
	}
	
	
    private void writeSymbol(Environment env, String symbol) throws ShellIOException {
        
        symbol = symbol.toLowerCase();
        Character output = null;
        
        if(this.checkSymbol(symbol) == false) {
        	env.writeln(("Symbol for " + symbol.toUpperCase() + " does not exists"));
        	return;
        }
        
        if(symbol.equals("prompt")) {
        	output = env.getPromptSymbol();
        }else if(symbol.equals("morelines")) {
        	output = env.getMorelinesSymbol();
        }else if(symbol.equals("multiline")) {
        	output = env.getMultilineSymbol();
        }
        
        env.writeln("Symbol for " + symbol.toUpperCase() + " is '" + output + "'");
    }
    
    private void changeSymbol(Environment env, String symbol, String input) throws ShellIOException {
        
        symbol = symbol.toLowerCase();
        Character output = null;
        
        if(this.checkSymbol(symbol) == false) {
        	env.writeln(("Symbol for " + symbol.toUpperCase() + " does not exists"));
        	return;
        }
        
        if(input.length() != 1) {
        	env.writeln("New input must me one character long");
        	return;
        }
        
        Character value = input.charAt(0);
        
        if(symbol.equals("prompt")) {
        	output = env.getPromptSymbol();
        	env.setPromptSymbol(value);
        	
        }else if(symbol.equals("morelines")) {
        	output = env.getMorelinesSymbol();
        	env.setMorelinesSymbol(value);
        	
        }else if(symbol.equals("multiline")) {
        	output = env.getMultilineSymbol();
        	env.setPromptSymbol(value);
        }
        
        env.writeln("Symbol for " + symbol.toUpperCase() + " changed from '" + output + "' to '" + input + "'");
    }
    
    private boolean checkSymbol(String symbol) {
    	if(SYMBOLS.contains(symbol)) {
    		return true;
    	}
    	return false;
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
