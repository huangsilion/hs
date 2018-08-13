package com.hs.model;

import java.util.Date;
import java.util.TimeZone;

import com.fasterxml.jackson.annotation.JsonFormat;

public class Message {
	TimeZone tz = TimeZone.getDefault();
	private String userid;
	private String messageId;
	private String channelid;
	private Date timeCreated;
	private String context;

	public Message() {

	}

	public Message(String userid, String messageId, String channelid, Date timeCreated, String context) {
		this.userid = userid;
		this.messageId = messageId;
		this.channelid = channelid;
		this.timeCreated = timeCreated;
		this.context = context;
	}

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public String getMessageId() {
		return messageId;
	}

	public void setMessageId(String messageId) {
		this.messageId = messageId;
	}

	public String getChannelid() {
		return channelid;
	}

	public void setChannelid(String channelid) {
		this.channelid = channelid;
	}

	public Date getTimeCreated() {
		return timeCreated;
	}

	public void setTimeCreated(Date timeCreated) {
		this.timeCreated = timeCreated;
	}

	public String getContext() {
		return context;
	}

	public void setContext(String context) {
		this.context = context;
	}
}
