package be.tomcools.atprotocol.codegen.errors;

public class ATPCodeGenException extends RuntimeException {
	public ATPCodeGenException(String message) {
		super(message);
	}

	public ATPCodeGenException(String message, Throwable cause) {
		super(message, cause);
	}
}
