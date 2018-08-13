package com.hs.apiResponse;



public class ResponseMessage {

	private String message;
	private MessageLevel level;
	

	public ResponseMessage(String message, MessageLevel level) {
		this.message = message;
		this.level = level;
	}
	
	public ResponseMessage(String message){
		this(message, MessageLevel.INFO);
	}
	
	public String getMessage(){
		return message;
	}
	
	public MessageLevel getMessageLevel() {
		return level;
	}
}
