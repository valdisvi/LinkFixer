import java.util.Arrays;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Utils {
// should import and use log4j logger
	private static Logger logger = Logger.getLogger("Utils");

	/**
	 * Creates separate (asynchronous) process with passed parameters
	 * 
	 * @param array of params
	 * @return reference to created process
	 */
	public static Process createProcess(String[] params) {
		// A Runtime object has methods for dealing with the OS
		Runtime runtime = Runtime.getRuntime();
		// Process
		Process process = null;
		try {
			process = runtime.exec(params);
		} catch (Exception e) {
			logger.log(Level.SEVERE, e.toString());
		}
		return process;
	}

	/**
	 * Executes external process synchronously
	 * 
	 * @param cmd array of command parameters
	 * @return output+error status as String
	 */
	public static String executeCmd(String[] cmd) {
		CommandResult result = executeCmdResult(cmd);
		String error = result.err;
		String output = result.out;
		if (!(output.equals("")))
			logger.log(Level.SEVERE, "executeCmd(" + Arrays.toString(cmd) + ")\nOutput message: " + output);
		if (!error.equals("")) {
			error += ("Command output error message: " + error);
			output += error;
		}
		return output;
	}

	/**
	 * Executes external process synchronously
	 * 
	 * @param cmd array of command parameters
	 * @return CommandOutput with out and err fields
	 */
	public static CommandResult executeCmdResult(String[] cmd) {
		String error = "";
		String output = "";
		CommandResult commandOutput = new CommandResult(output, error);
		try {
			Process pb = createProcess(cmd);
			output = getOutput(pb);
			error = getError(pb);
			pb.waitFor();

			if (!(output.equals("")))
				logger.log(Level.SEVERE, "executeCmd(" + Arrays.toString(cmd) + ")\nOutput message: " + output);
			else
				logger.log(Level.SEVERE, "executeCmd(" + Arrays.toString(cmd) + ") executed successfully");

			if (!error.equals(""))
				logger.log(Level.SEVERE, error);

			commandOutput = new CommandResult(output, error);
		} catch (Exception e) {
			logger.log(Level.SEVERE, e.toString());
		}

		return commandOutput;
	}

	/**
	 * Executes external process with bash interpreter synchronously
	 * 
	 * @param command line of the bash script
	 * 
	 * @return output/error status
	 */
	public static String executeCmd(String command) {
		String[] cmd = new String[3];
		cmd[0] = "/bin/bash";
		cmd[1] = "-c";
		cmd[2] = command;
		return executeCmd(cmd);
	}

	/**
	 * @param process , which is created by createProcess()
	 * @return string gathered from the process output
	 */
	public static String getOutput(Process process) {
		// See
		// http://web.archive.org/web/20140531042945/https://weblogs.java.net/blog/pat/archive/2004/10/stupid_scanner_1.html
		Scanner s = new Scanner(process.getInputStream()).useDelimiter("\\A");
			return s.hasNext() ? s.next() : "";
	}

	/**
	 * @param process , which is created by createProcess()
	 * @return string gathered from the process error output
	 */
	public static String getError(Process process) {
		Scanner s = new Scanner(process.getErrorStream()).useDelimiter("\\A");
			return s.hasNext() ? s.next() : "";
	}

	public static void sleep(int milliseconds) {
		try {
			Thread.sleep(milliseconds);
		} catch (Exception e) {
			logger.log(Level.SEVERE, e.toString());
		}
	}

}

