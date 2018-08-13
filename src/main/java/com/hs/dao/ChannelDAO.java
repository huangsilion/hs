package com.hs.dao;

import java.util.List;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import com.hs.infrastructure.HibernateRepository;
import com.hs.infrastructure.HibernateSessionFactoryType;
import com.hs.model.Channel;
import com.hs.repository.ChannelRepository;

@Repository
public class ChannelDAO extends HibernateRepository implements ChannelRepository {

	/**
	 * get all distinct channels
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Transactional(value = "webuiTransactionManager")
	public List<Channel> getAllChannel() {
		Query query = getSessionFactory(HibernateSessionFactoryType.SYSTEM).createQuery("from Channel");
		List<Channel> result = query.list();
		return result;
	}

	@SuppressWarnings("unused")
	@Transactional(value = "webuiTransactionManager")
	public boolean createChannel(String channelid) {
		boolean status = saveOrUpdate(new Channel(channelid));
		return status;
	}

	@SuppressWarnings("unchecked")
	@Transactional(value = "webuiTransactionManager")
	public List<Channel> getChannelsWithId(String channelid) {
		Query query = getSessionFactory(HibernateSessionFactoryType.SYSTEM)
				.createQuery("from Channel where channelid like:channelid");
		query.setParameter("channelid", channelid);
		List<Channel> result = query.list();
		return result;
	}
}
