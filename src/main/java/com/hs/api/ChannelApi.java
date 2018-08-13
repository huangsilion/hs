package com.hs.api;

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
import com.hs.model.Channel;
import com.hs.model.User;
import com.hs.repository.ChannelRepository;
import com.hs.repository.UserRepository;

@Controller
@Scope("request")
@RequestMapping(value = "/api/ChannelEntries")
public class ChannelApi extends ApiResponseBuilder {
	@Autowired
	private ChannelRepository channelRepository;
	@SuppressWarnings("rawtypes")
	private ResponseEntity responseEntity = null;

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@ResponseBody
	@RequestMapping(value = "/getChannels", method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<ApiResponse<User>> getAllChannel(@RequestParam boolean forAllChannels) {
		List<Channel> channels = channelRepository.getAllChannel();
		if (channels == null) {
			responseEntity = new ResponseEntity<ApiResponse>(new ApiResponse("NO_RESULTS_FOUND", MessageLevel.INFO),
					HttpStatus.NOT_FOUND);
		} else {
			responseEntity = new ResponseEntity<ApiResponse<User>>(new ApiResponse(channels), HttpStatus.OK);
		}
		return responseEntity;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@ResponseBody
	@RequestMapping(value = "/getChannelWithId", method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<ApiResponse<User>> getChannelsWithId(@RequestParam String channelid) {
		List<Channel> channels = channelRepository.getChannelsWithId(channelid);
		if (channels == null) {
			responseEntity = new ResponseEntity<ApiResponse>(new ApiResponse("NO_RESULTS_FOUND", MessageLevel.INFO),
					HttpStatus.NOT_FOUND);
		} else {
			responseEntity = new ResponseEntity<ApiResponse<User>>(new ApiResponse(channels), HttpStatus.OK);
		}
		return responseEntity;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@ResponseBody
	@RequestMapping(value = "/createChannel", method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<ApiResponse<User>> createChannel(@RequestParam String channelid) {
		boolean status = channelRepository.createChannel(channelid);
		if (!status) {
			responseEntity = new ResponseEntity<ApiResponse>(new ApiResponse("NO_RESULTS_FOUND", MessageLevel.INFO),
					HttpStatus.NOT_FOUND);
		} else {
			responseEntity = new ResponseEntity<ApiResponse<User>>(new ApiResponse("SUCCESS"), HttpStatus.OK);
		}
		return responseEntity;
	}
}
