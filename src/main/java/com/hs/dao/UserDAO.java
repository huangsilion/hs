package com.hs.dao;

import java.sql.Date;
import java.util.HashSet;
import java.util.List;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import com.hs.infrastructure.HibernateRepository;
import com.hs.infrastructure.HibernateSessionFactoryType;
import com.hs.model.User;
import com.hs.repository.UserRepository;

@Repository
public class UserDAO extends HibernateRepository implements UserRepository {

	@Transactional(value = "webuiTransactionManager")
	public User getUser(String userid, String password) {
		Query query = getSessionFactory(HibernateSessionFactoryType.SYSTEM)
				.createQuery("from User u where u.userid=:userid and u.password=:password");
		query.setParameter("userid", userid);
		query.setParameter("password", password);
		User result = (User) query.uniqueResult();
		return result;
	}

	@SuppressWarnings("unused")
	@Transactional(value = "webuiTransactionManager")
	public boolean updateUser(User user) {
		boolean status = saveOrUpdate(user);
		return status;
	}

	@SuppressWarnings("unused")
	@Transactional(value = "webuiTransactionManager")
	public boolean createUser(String userId, String password) {
		Date date = new Date(System.currentTimeMillis());
		User user = new User(userId, password, date, new HashSet());
		boolean status = save(user);
		return status;
	}

}
