package com.hs.localCache;

import com.hs.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import com.hs.model.Channel;

public class ChannelUserCache {
	private static Map<String, Set<User>> channelUserCache = new HashMap<String, Set<User>>();

	public static Map<String, Set<User>> getChannelUserCache() {
		return channelUserCache;
	}

	public static void updateChannelUserCache(User user) {
		Set<Channel> channels = user.getChannels();
		if (channels != null) {
			Iterator<Channel> setIterator = channels.iterator();
			while (setIterator.hasNext()) {
				Channel c = setIterator.next();
				updateChannelUserCache(user, c.getChannelid());
			}
		}
	}

	public static void updateChannelUserCache(User user, String channelid) {
		if (channelUserCache.containsKey(channelid)) {
			channelUserCache.get(channelid).add(user);
		} else {
			channelUserCache.put(channelid, new HashSet<User>());
		}
	}
}
