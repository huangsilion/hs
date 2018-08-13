package com.hs.apiResponse;

import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;


public class ApiResponseBuilder {
	@Autowired(required = true)
	public MessageSource messages;

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public ResponseEntity<ApiResponse> succeeResponse(Object obj) {
		String msg = messages.getMessage("SUCCESS", null, Locale.US);
		return new ResponseEntity<ApiResponse>(new ApiResponse(obj, msg),
				HttpStatus.OK);

	};

	@SuppressWarnings("rawtypes")
	public ResponseEntity<ApiResponse> errorResponseBuilder(String code,
			Object[] args, Locale locale, MessageLevel level,
			HttpStatus httpStatus) {
		String msg = messages.getMessage(code, args, locale);
		return new ResponseEntity<ApiResponse>(new ApiResponse(msg, level),
				httpStatus);
	};

}
