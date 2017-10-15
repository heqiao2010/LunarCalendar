package com.github.heqiao2010;
/**
 * 农历日期异常
 * @author joel
 *
 */
public class LunarException extends RuntimeException {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 3274596943314243191L;

	private String message;

	// constructor
	public LunarException(String message) {
		super(message);
		this.message = message;
	}

	public LunarException() {
		super();
	}

	public LunarException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, writableStackTrace, writableStackTrace);
	}

	public LunarException(String message, Throwable t) {
		super(message, t);
		this.message = message;
	}

	public LunarException(Throwable t) {
		super(t);
	}

	// getter and setter
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
