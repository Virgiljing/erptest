package cn.itcast.erp.exception;

public class ErpException extends RuntimeException{
	private static final long serialVersionUID = 1L;

	public ErpException(String message) {
		super(message);
	}
	
}
