package com.hs.api;

import java.sql.Date;
import java.util.HashSet;
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
import com.hs.localCache.ChannelUserCache;
import com.hs.model.User;
import com.hs.model.UserChannelTimeStamp;
import com.hs.repository.UserChannelTimeStampRepository;
import com.hs.repository.UserRepository;

@Controller
@Scope("request")
@RequestMapping(value = "/api/userChannelTimeStampEntries")
public class UserChannelTimeStampApi extends ApiResponseBuilder {
	@Autowired
	private UserChannelTimeStampRepository userChannelTimeStampRepository;
	@SuppressWarnings("rawtypes")
	private ResponseEntity responseEntity = null;

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@ResponseBody
	@RequestMapping(value = "/getAllForUser", method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<ApiResponse<UserChannelTimeStamp>> getUser(@RequestParam String userId) {
		List<UserChannelTimeStamp> userChannelTimeStampInfo = userChannelTimeStampRepository.getUserChannelTimeStamp(userId);
		if (userChannelTimeStampInfo == null) {
			responseEntity = new ResponseEntity<ApiResponse>(new ApiResponse("NO_RESULTS_FOUND", MessageLevel.INFO),
					HttpStatus.NOT_FOUND);
		}else {
			responseEntity = new ResponseEntity<ApiResponse<User>>(new ApiResponse(userChannelTimeStampInfo), HttpStatus.OK);
		}
		return responseEntity;
	}

	// update user info
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@ResponseBody
	@RequestMapping(value = "/updateUserChannelTimeStamp", method = RequestMethod.POST, produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<ApiResponse<UserChannelTimeStamp>> updateUser(@RequestBody UserChannelTimeStamp userChannelTimeStampInfo) {
		boolean updateStatus = false;
		updateStatus = userChannelTimeStampRepository.updateUserChannelTimeStamp(userChannelTimeStampInfo);
		if (updateStatus) {
			responseEntity = new ResponseEntity<ApiResponse<User>>(new ApiResponse(userChannelTimeStampInfo), HttpStatus.OK);
		} else {
			responseEntity = new ResponseEntity<ApiResponse>(new ApiResponse("NO_RESULTS_FOUND", MessageLevel.INFO),
					HttpStatus.NOT_FOUND);
		}
		return responseEntity;
	}
}
