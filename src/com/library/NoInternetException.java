package com.library;

public class NoInternetException extends Exception{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public NoInternetException() {
	  }

	  public NoInternetException(String msg) {
	    super(msg);
	  }
}
