public class CommandResult {
	String out;
	String err;

	public CommandResult(String stdout, String stderr) {
		this.out = stdout;
		this.err = stderr;
	}

	public String out() {
		return this.out;
	}

	public String err() {
		return this.err;
	}
}