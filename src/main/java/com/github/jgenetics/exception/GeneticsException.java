package com.github.jgenetics.exception;


/**
 * The GeneticsException is throwing when error occurred in the jGenetics library.
 */
public class GeneticsException extends Exception {

	private static final long serialVersionUID = 3085950178489284752L;

	public GeneticsException(String message) {
        super(message);
    }

	public GeneticsException(String message, Exception e) {
		super(message, e);
	}
}