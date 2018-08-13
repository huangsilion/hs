package com.hs.repository;

import java.sql.Date;
import java.util.List;
import com.hs.model.Message;
import com.hs.model.User;

public interface MessageRepository {
	public List<Message> getMessageSentByUser(String id);
	public List<Message> getMessageForChannelBasedOnTimeStamp(String userid,String channelid,Date timeStamp,boolean isForHistoryRecord);
	public boolean saveMessage(Message message);
	public void notificateAllSubscriber(Message message);
}
