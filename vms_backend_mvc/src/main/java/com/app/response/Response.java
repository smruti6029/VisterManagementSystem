package com.app.response;

public class Response<T> {
	
	
	 private String message;
	 
	 private Object data;
	 
	 private int status;
	 
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public Object getData() {
		return data;
	}
	public void setData(Object data) {
		this.data = data;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public Response(String message, Object data, int status) {
		super();
		this.message = message;
		this.data = data;
		this.status = status;
	}
	public Response() {
		super();
	}
	 
	 
	 
	
	

}
