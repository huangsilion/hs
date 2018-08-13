package com.hs.repository;

import java.util.List;
import com.hs.model.Channel;

public interface ChannelRepository {
	public List<Channel> getAllChannel();

	public List<Channel> getChannelsWithId(String channelid);

	public boolean createChannel(String channelid);
}
