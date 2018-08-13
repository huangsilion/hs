package com.hs.model;

public class UserChannelInfo {
	private User user;
	private String channelid;

	public UserChannelInfo() {
	}
	public UserChannelInfo(String id) {
		this.channelid=id;
	}
	public UserChannelInfo(User user, String channelid) {
		this.user = user;
		this.channelid = channelid;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getChannelid() {
		return channelid;
	}

	public void setChannelid(String channelid) {
		this.channelid = channelid;
	}
}
