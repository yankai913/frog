package com.zoo.frog.exception;
/**
 * 
 * @author <a href="mailto:yankai913@gmail.com">yankai</a>
 * @date 2013-7-13
 */
public class TracerException extends Exception {

	private static final long serialVersionUID = -9215363088480193629L;

	public TracerException() {
		super();
	}
	
	public TracerException(String message) {
		super(message);
	}
	
	public TracerException(String message, Throwable t) {
		super(message, t);
	}
	
	public TracerException(Throwable t) {
		super(t);
	}
}
