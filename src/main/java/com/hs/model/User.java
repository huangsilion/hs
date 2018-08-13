package com.hs.model;

import java.util.Date;
import java.util.HashMap;
import java.util.Set;

public class User {

	private String userid;
	private Date loginTimeStamp;
	private Set<Channel> channels;
	private String password;

	private HashMap<String, Date> channelsInfo;

	public User() {
	}

	public User(String id, Date loginTimeStamp) {
		this.userid = id;
		this.loginTimeStamp = loginTimeStamp;
	}

	public User(String id, String password, Date loginTimeStamp, Set<Channel> channels) {
		this.userid = id;
		this.password = password;
		this.loginTimeStamp = loginTimeStamp;
		this.channels = channels;
	}

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public Set<Channel> getChannels() {
		return channels;
	}

	public void setChannels(Set<Channel> channels) {
		this.channels = channels;
	}

	public Date getLoginTimeStamp() {
		return loginTimeStamp;
	}

	public void setLoginTimeStamp(Date loginTimeStamp) {
		this.loginTimeStamp = loginTimeStamp;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}
