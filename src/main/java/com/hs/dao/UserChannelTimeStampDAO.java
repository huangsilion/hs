package com.hs.dao;

import java.util.List;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import com.hs.infrastructure.HibernateRepository;
import com.hs.infrastructure.HibernateSessionFactoryType;
import com.hs.model.User;
import com.hs.model.UserChannelTimeStamp;
import com.hs.repository.UserChannelTimeStampRepository;
import com.hs.repository.UserRepository;

@Repository
public class UserChannelTimeStampDAO extends HibernateRepository implements UserChannelTimeStampRepository {

	@Transactional(value = "webuiTransactionManager")
	public List<UserChannelTimeStamp> getUserChannelTimeStamp(String userid) {
		// when user login, determine the timeStamp for each channel that used for fetching message
		Query query = getSessionFactory(HibernateSessionFactoryType.SYSTEM)
				.createQuery("from UserChannelTimeStamp u where u.userid=:userid");
		query.setParameter("userid", userid);
		List<UserChannelTimeStamp> result = query.list();
		return result;
	}

	@SuppressWarnings("unused")
	@Transactional(value = "webuiTransactionManager")
	public boolean updateUserChannelTimeStamp(UserChannelTimeStamp userChannelTimeStampInfo)  {
		// when user subscribe a new channel or switch to a new channel.
		boolean status = saveOrUpdate(userChannelTimeStampInfo);
		return status;
	}
}
