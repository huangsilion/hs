package com.hs.model;

import java.util.Date;

public class UserChannelTimeStamp implements java.io.Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 3221322592573727562L;
	private String userid;
	private String channelid;
	private Date timeUpdated;

	public UserChannelTimeStamp() {
	}

	public UserChannelTimeStamp(String userid, String channelid, Date timeUpdated) {
		this.userid = userid;
		this.channelid = channelid;
		this.timeUpdated = timeUpdated;
	}

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public String getChannelid() {
		return channelid;
	}

	public void setChannelid(String channelid) {
		this.channelid = channelid;
	}

	public Date getTimeUpdated() {
		return timeUpdated;
	}

	public void setTimeUpdated(Date timeUpdated) {
		this.timeUpdated = timeUpdated;
	}
}
