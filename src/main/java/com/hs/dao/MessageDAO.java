package com.hs.dao;

import java.sql.Date;
import java.util.List;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.hs.api.MessageApi;
import com.hs.infrastructure.HibernateRepository;
import com.hs.infrastructure.HibernateSessionFactoryType;
import com.hs.model.Message;
import com.hs.repository.MessageRepository;

@Repository
public class MessageDAO extends HibernateRepository implements MessageRepository {

	/**
	 * get all distinct messages, first load 101 result
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Transactional(value = "webuiTransactionManager")
	public List<Message> getMessageSentByUser(String userid) {
		Query query = getSessionFactory(HibernateSessionFactoryType.SYSTEM)
				.createQuery("from Message where userid=:userid order by timeCreated desc");
		query.setParameter("userid", userid);
		query.setMaxResults(MessageApi.MAX_RESULT);
		List<Message> result = query.list();
		return result;
	}

	/**
	 * get message per channel per user and timeCreate is later than timeStamp
	 * of last channel opened
	 */
	@SuppressWarnings("unchecked")
	@Transactional(value = "webuiTransactionManager")
	public List<Message> getMessageForChannelBasedOnTimeStamp(String userid, String channelid, Date timeStamp,
			boolean isForHistoryRecord) {
		Query query = null;
		if (isForHistoryRecord) {
			// the timeStamp is either getting from oldest message from existed
			// list or current time.
			// So it should not include equal condition(should be "<" not "<=").
			query = getSessionFactory(HibernateSessionFactoryType.SYSTEM).createQuery(
					"from Message where channelid=:channelid and timeCreated<:timeStamp order by timeCreated desc");
		} else {
			query = getSessionFactory(HibernateSessionFactoryType.SYSTEM).createQuery(
					"from Message where channelid=:channelid and timeCreated>:timeStamp order by timeCreated desc");
		}
		query.setParameter("channelid", channelid);
		query.setParameter("timeStamp", timeStamp);
		query.setMaxResults(MessageApi.MAX_RESULT);
		List<Message> result = query.list();
		return result;
	}

	/**
	 * Save message to DB
	 */
	@SuppressWarnings("unchecked")
	@Transactional(value = "webuiTransactionManager")
	public boolean saveMessage(Message message) {
		boolean status = save(message);
		return status;
	}

	public void notificateAllSubscriber(Message message) {
		// TODO Auto-generated method stub
	}

}
