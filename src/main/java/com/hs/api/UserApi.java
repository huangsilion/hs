package com.hs.api;

import java.sql.Date;
import java.util.HashSet;
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
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import com.hs.apiResponse.ApiResponse;
import com.hs.apiResponse.ApiResponseBuilder;
import com.hs.apiResponse.MessageLevel;
import com.hs.localCache.ChannelUserCache;
import com.hs.model.Channel;
import com.hs.model.User;
import com.hs.model.UserChannelInfo;
import com.hs.model.UserChannelTimeStamp;
import com.hs.repository.UserChannelTimeStampRepository;
import com.hs.repository.UserRepository;

@Controller
@Scope("request")
@RequestMapping(value = "/api/userEntries")
public class UserApi extends ApiResponseBuilder {
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private UserChannelTimeStampRepository userChannelTimeStampRepository;
	@SuppressWarnings("rawtypes")
	private ResponseEntity responseEntity = null;

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@ResponseBody
	@RequestMapping(value = "/getUser", method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<ApiResponse<User>> getUser(@RequestParam String userId, @RequestParam String password) {
		User userInfo = userRepository.getUser(userId, password);
		if (userInfo == null) {
			responseEntity = new ResponseEntity<ApiResponse>(new ApiResponse("NO_RESULTS_FOUND", MessageLevel.INFO),
					HttpStatus.NOT_FOUND);
		} else {

			responseEntity = new ResponseEntity<ApiResponse<User>>(new ApiResponse(userInfo), HttpStatus.OK);
		}
		return responseEntity;
	}

	@ResponseBody
	@RequestMapping(value = "/registerUser", method = RequestMethod.GET, produces = {
			MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<ApiResponse<User>> registerUser(@RequestParam String userId, @RequestParam String password) {
		boolean updateStatus = false;
		updateStatus = userRepository.createUser(userId,password);
		if (updateStatus) {
			User userInfo = userRepository.getUser(userId, password);
			responseEntity = new ResponseEntity<ApiResponse<User>>(new ApiResponse(userInfo), HttpStatus.OK);
			if (userInfo == null) {
				responseEntity = new ResponseEntity<ApiResponse>(new ApiResponse("NO_RESULTS_FOUND", MessageLevel.INFO),
						HttpStatus.NOT_FOUND);
			} else {

				responseEntity = new ResponseEntity<ApiResponse<User>>(new ApiResponse(userInfo), HttpStatus.OK);
			}
		} else {
			responseEntity = new ResponseEntity<ApiResponse>(new ApiResponse("User existed", MessageLevel.INFO),
					HttpStatus.NOT_FOUND);
		}
		return responseEntity;
	}

	// update user info
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@ResponseBody
	@RequestMapping(value = "/updateUser", method = RequestMethod.POST, produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<ApiResponse<UserChannelTimeStamp>> updateUser(@RequestBody UserChannelInfo userChannelInfo) {
		boolean updateStatus = false;
		User user = userChannelInfo.getUser();
		Date date = new Date(System.currentTimeMillis());
		user.setLoginTimeStamp(date);
		updateStatus = userRepository.updateUser(user);
		if (updateStatus) {
			ChannelUserCache.updateChannelUserCache(user, userChannelInfo.getChannelid());
			UserChannelTimeStamp userChannelTimeStampInfo = new UserChannelTimeStamp(user.getUserid(),
					userChannelInfo.getChannelid(), date);
			boolean success = userChannelTimeStampRepository.updateUserChannelTimeStamp(userChannelTimeStampInfo);
			if (success)
				responseEntity = new ResponseEntity<ApiResponse<User>>(new ApiResponse(userChannelTimeStampInfo),
						HttpStatus.OK);
			else
				responseEntity = new ResponseEntity<ApiResponse>(new ApiResponse("NO_RESULTS_FOUND", MessageLevel.INFO),
						HttpStatus.NOT_FOUND);

		} else {
			responseEntity = new ResponseEntity<ApiResponse>(new ApiResponse("NO_RESULTS_FOUND", MessageLevel.INFO),
					HttpStatus.NOT_FOUND);
		}
		return responseEntity;
	}
}
