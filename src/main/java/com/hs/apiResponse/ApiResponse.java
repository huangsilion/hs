package com.hs.apiResponse;

import com.fasterxml.jackson.annotation.JsonGetter;

public class ApiResponse<T> {

	private T body;
	private ResponseMessage responseMessage;
	private ResponseMessages additionalMessages = new ResponseMessages();
	private Boolean hasMoreResults = false;
	
	public ApiResponse(T body){
		this.body = body;
	}
	
	public ApiResponse(ResponseMessage responseMessage){
		this.responseMessage = responseMessage;
	}
	
	public ApiResponse(T body, ResponseMessage responseMessage){
		this(responseMessage);
		this.body = body;
	}
	
	public ApiResponse(T body, String responseMessage){
		this(new ResponseMessage(responseMessage));
		this.body = body;
	}
	
	public ApiResponse(T body, String responseMessage, MessageLevel level){
		this(new ResponseMessage(responseMessage, level));
		this.body = body;
	}
	
	public ApiResponse(String responseMessage, MessageLevel level){
		this(new ResponseMessage(responseMessage, level));
	}
	
//	public ApiResponse(ResponseMessages responseMessages){
//		this.body = null;
//		this.additionalMessages = responseMessages;
//	}
	
//	public ApiResponse(T body, ResponseMessages responseMessages){
//		this.body = body;
//		this.additionalMessages = responseMessages;
//	}
	
	public ApiResponse(T body, Boolean hasMoreResults){
		this.body = body;
		this.hasMoreResults = hasMoreResults;
	}
	
	public ApiResponse(T body, ResponseMessage responseMessage, Boolean hasMoreResults){
		this(responseMessage);
		this.body = body;
		this.hasMoreResults = hasMoreResults;
	}
	
//	public ApiResponse(T body, ResponseMessages responseMessages, Boolean hasMoreResults){
//		this.body = body;
//		this.additionalMessages = responseMessages;
//		this.hasMoreResults = hasMoreResults;
//	}
	
	public T getBody(){
		return this.body;
	}
	
	public ResponseMessages getAdditionalMessages(){
		return this.additionalMessages;
	}
	
	@JsonGetter
	public Boolean hasMoreResults(){
		return this.hasMoreResults;
	}
	
	public void addAdditionalResponseMessage(String message, MessageLevel level){
		this.additionalMessages.addResponseMessage(message, level);
	}
	
	public ResponseMessage getResponseMessage(){
		return this.responseMessage;
	}
}
