package com.hs.repository;

import java.util.List;

import com.hs.model.User;
import com.hs.model.UserChannelTimeStamp;

public interface UserChannelTimeStampRepository {
	public List<UserChannelTimeStamp> getUserChannelTimeStamp(String userid);
	public boolean updateUserChannelTimeStamp(UserChannelTimeStamp userChannelTimeStampInfo);
}
