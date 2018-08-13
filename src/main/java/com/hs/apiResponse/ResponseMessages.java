package com.hs.apiResponse;

import java.util.ArrayList;


public class ResponseMessages {

	private ArrayList<ResponseMessage> responseMessages = new ArrayList<ResponseMessage>();
	
	public ResponseMessages addResponseMessage(String message, MessageLevel level){
		ResponseMessage msg = new ResponseMessage(message, level);
		responseMessages.add(msg);
		return this;
	}
	
	public ArrayList<ResponseMessage> getMessages(){
		return responseMessages;
	}
}