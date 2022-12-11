package hr.fer.zemris.java.hw05.shell;

import java.io.PrintStream;
import java.nio.charset.Charset;
import java.util.Scanner;
import java.util.SortedMap;
import java.util.TreeMap;

import hr.fer.zemris.java.hw05.shell.commands.CatShellCommand;
import hr.fer.zemris.java.hw05.shell.commands.CharsetsShellCommand;
import hr.fer.zemris.java.hw05.shell.commands.CopyShellCommand;
import hr.fer.zemris.java.hw05.shell.commands.ExitShellCommand;
import hr.fer.zemris.java.hw05.shell.commands.HelpShellCommand;
import hr.fer.zemris.java.hw05.shell.commands.HexShellCommand;
import hr.fer.zemris.java.hw05.shell.commands.LsShellCommand;
import hr.fer.zemris.java.hw05.shell.commands.MkdirShellCommand;
import hr.fer.zemris.java.hw05.shell.commands.SymbolShellCommand;
import hr.fer.zemris.java.hw05.shell.commands.TreeShellCommand;
import hr.fer.zemris.java.hw05.shell.strategy.Environment;
import hr.fer.zemris.java.hw05.shell.strategy.ShellCommand;
import hr.fer.zemris.java.hw05.shell.strategy.ShellIOException;
import hr.fer.zemris.java.hw05.shell.strategy.ShellStatus;

public class MyShell implements Environment {

	private Scanner scener;
	
	private ShellStatus status;

	private Character promptSymbol;

	private Character moreLinesSymbol;

	private Character multilineSymbol;

	private static final SortedMap<String, ShellCommand> commands;
	
	private PrintStream ps = new PrintStream(System.out, true, Charset.forName("UTF-8"));
	
	static {
		commands =  new TreeMap<>();
		commands.put("charsets", new CharsetsShellCommand());
		commands.put("exit", new ExitShellCommand());
		commands.put("ls", new LsShellCommand());
		commands.put("tree", new TreeShellCommand());
		commands.put("hexdump", new HexShellCommand());
		commands.put("cat", new CatShellCommand());
		commands.put("copy", new CopyShellCommand());
		commands.put("mkdir", new MkdirShellCommand());
		commands.put("help", new HelpShellCommand());
		commands.put("symbol", new SymbolShellCommand());
	}

	public MyShell(Scanner scener) {
		this.scener = scener;
		this.promptSymbol = '>';
		this.moreLinesSymbol = '\\';
		this.multilineSymbol = '|';
		this.status = ShellStatus.CONTINUE;
	}

	public static void main(String[] args) {
		try (Scanner scener = new Scanner(System.in)) {

			MyShell myShell = new MyShell(scener);
			myShell.writeln("Welcome to MyShell v1.0");

			while (true) {

				try {
					if (myShell.status.equals(ShellStatus.TERMINATE)) {
						break;
					}

					myShell.write(myShell.getPromptSymbol().toString() + " ");

					String[] userInputSeparated = myShell.readLine().trim().split("[\\s]+", 2);

					String commandName = userInputSeparated[0].toLowerCase();
					String arguments;

					if (userInputSeparated.length != 1) {
						arguments = userInputSeparated[1].toLowerCase();
					} else {
						arguments = "";
					}

					ShellCommand command = MyShell.commands.get(commandName);

					if (command == null) {
						myShell.writeln("Unknown command \"" + commandName + "\"!");
						continue;
					}

					myShell.status = command.executeCommand(myShell, arguments);

				} catch (ShellIOException e) {
					return;
				} catch (RuntimeException e) {
					myShell.writeln(e.getMessage());
				}

			}

		} catch (ShellIOException e) {
			e.printStackTrace();
		}

	}

	@Override
	public String readLine() throws ShellIOException {
		try {
			StringBuilder sb = new StringBuilder();

			while (true) {
				//Ukloni bijeline na kraju i pocetku
				String line = this.scener.nextLine().trim();
				
				//Nema more lines
				if (line.endsWith(getMorelinesSymbol().toString()) == false) {
					return sb.append(line).toString();					
				}

				//ukloni more lines
				sb.append(line, 0, line.length() - 1);
				 this.write(this.getMultilineSymbol().toString() + " ");
			}

		} catch (RuntimeException e) {
			throw new ShellIOException(e.getMessage());
		}
	}

	@Override
	public void write(String text) throws ShellIOException {
		try {
			if (text != null) {
				ps.print(text);
			} else {
				throw new NullPointerException("text should not be null");
			}
		} catch (RuntimeException e) {
			throw new ShellIOException(e.getMessage());
		}

	}

	@Override
	public void writeln(String text) throws ShellIOException {
		try {
			if (text != null) {
				ps.println(text);
			} else {
				throw new NullPointerException("text should not be null");
			}
		} catch (RuntimeException e) {
			throw new ShellIOException(e.getMessage());
		}

	}

	@Override
	public SortedMap<String, ShellCommand> commands() {
		return commands;
	}

	@Override
	public Character getMultilineSymbol() {
		return multilineSymbol;
	}

	@Override
	public void setMultilineSymbol(Character symbol) throws ShellIOException {
		if (symbol != null) {
			this.multilineSymbol = symbol;
		} else {
//			throw new NullPointerException("symbol can not be null");
			this.writeln("symbol can not be null");
		}
	}

	@Override
	public Character getPromptSymbol() {
		return promptSymbol;
	}

	@Override
	public void setPromptSymbol(Character symbol) throws ShellIOException {
		if (symbol != null) {
			this.promptSymbol = symbol;
		} else {
//			throw new NullPointerException("symbol can not be null");
			this.writeln("symbol can not be null");
		}
	}

	@Override
	public Character getMorelinesSymbol() {
		return moreLinesSymbol;
	}

	public void setMorelinesSymbol(Character symbol) throws ShellIOException {
		if (symbol != null) {
			this.moreLinesSymbol = symbol;
		} else {
//			throw new NullPointerException("symbol can not be null");
			this.writeln("charset should not be null");
		}

	}

	@Override
	public void setCharset(String charset) throws ShellIOException {
		if(charset == null) {
//			throw new NullPointerException("charset should not be null");
			this.writeln("charset should not be null");
		}
	
		try {			
			ps = new PrintStream(System.out, true, Charset.forName(charset));
		}catch(RuntimeException e) {
			this.writeln("wrong charset name");
		}
		
	}

}
