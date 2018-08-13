package com.hs.api;

import java.sql.Date;
import java.util.Collections;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import com.hs.apiResponse.ApiResponse;
import com.hs.apiResponse.ApiResponseBuilder;
import com.hs.apiResponse.MessageLevel;
import com.hs.model.Message;
import com.hs.model.User;
import com.hs.repository.MessageRepository;

@Controller
@Scope("request")
@RequestMapping(value = "/api/MessageEntries")
public class MessageApi extends ApiResponseBuilder {
	@Autowired
	private MessageRepository messageRepository;
	@SuppressWarnings("rawtypes")
	private ResponseEntity responseEntity = null;
	public static int MAX_RESULT = 50;

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@ResponseBody
	@RequestMapping(value = "/getMessageSentByUser", method = RequestMethod.GET, produces = {
			MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<ApiResponse<Message>> getMessageSentByUser(@RequestParam String userId) {
		List<Message> messageInfo = messageRepository.getMessageSentByUser(userId);
		boolean hasMore = false;
		if (messageInfo == null) {
			responseEntity = new ResponseEntity<ApiResponse>(new ApiResponse("NO_RESULTS_FOUND", MessageLevel.INFO),
					HttpStatus.NOT_FOUND);
		} else {
			if (messageInfo.size() > MAX_RESULT) {
				hasMore = true;
			}
			Collections.reverse(messageInfo);
			responseEntity = new ResponseEntity<ApiResponse<User>>(new ApiResponse(messageInfo, hasMore),
					HttpStatus.OK);
		}
		return responseEntity;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@ResponseBody
	@RequestMapping(value = "/getPreviousMessage", method = RequestMethod.GET, produces = {
			MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<ApiResponse<Message>> getPreviousMessage(@RequestParam String userid,
			@RequestParam String channelid, @RequestParam String timeStamp) {
		return getMessageBaseOnTimeStamp(userid, channelid, timeStamp, true);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@ResponseBody
	@RequestMapping(value = "/getUnReadMessageForChannel", method = RequestMethod.GET, produces = {
			MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<ApiResponse<Message>> getUnReadMessagePerUserPerChannel(@RequestParam String userid,
			@RequestParam String channelid, @RequestParam String timeStamp) {
		return getMessageBaseOnTimeStamp(userid, channelid, timeStamp, false);
	}

	// save message info
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@ResponseBody
	@RequestMapping(value = "/saveMessage", method = RequestMethod.POST, produces = {
			MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<ApiResponse<Message>> saveMessage(@RequestBody Message message) {
		boolean updateStatus = false;
		// if user is not found,create a new user
		long currentTime = System.currentTimeMillis();
		Date date = new Date(currentTime);
		message.setTimeCreated(date);
		message.setMessageId(currentTime + ":" + message.getChannelid() + ":" + message.getUserid());
		updateStatus = messageRepository.saveMessage(message);
		if (updateStatus) {
			responseEntity = new ResponseEntity<ApiResponse<User>>(new ApiResponse(updateStatus), HttpStatus.OK);
		} else {
			responseEntity = new ResponseEntity<ApiResponse>(new ApiResponse("NO_RESULTS_FOUND", MessageLevel.INFO),
					HttpStatus.NOT_FOUND);
		}

		return responseEntity;
	}

	public ResponseEntity<ApiResponse<Message>> getMessageBaseOnTimeStamp(String userid, String channelid,
			String timeStamp, boolean isForHistoryRecord) {
		List<Message> messageInfo = messageRepository.getMessageForChannelBasedOnTimeStamp(userid, channelid,
				new Date(Long.parseLong(timeStamp)), isForHistoryRecord);
		boolean hasMore = false;
		if (messageInfo == null) {
			responseEntity = new ResponseEntity<ApiResponse>(new ApiResponse("NO_RESULTS_FOUND", MessageLevel.INFO),
					HttpStatus.NOT_FOUND);
		} else {
			if (messageInfo.size() > MAX_RESULT) {
				hasMore = true;
			}
			Collections.reverse(messageInfo);
			responseEntity = new ResponseEntity<ApiResponse<User>>(new ApiResponse(messageInfo, hasMore),
					HttpStatus.OK);
		}
		return responseEntity;
	}
}
