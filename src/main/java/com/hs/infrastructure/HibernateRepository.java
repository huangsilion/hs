package com.hs.infrastructure;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.transaction.annotation.Transactional;

/**
 * Functionality common to all Hibernate repositories.
 */
public abstract class HibernateRepository {

	private SessionFactory webuiSessionFactory;

	/**
	 * Sets the webui session factory.
	 * 
	 * @param webuiSessionFactory the new webui session factory
	 */
	@Autowired
	@Required
	@Qualifier("webuiSessionFactory")
	public void setWebuiSessionFactory(SessionFactory webuiSessionFactory) {
		this.webuiSessionFactory = webuiSessionFactory;
	}

	/**
	 * Gets the session factory.
	 * 
	 * @param factoryType  the factory type
	 * @return the session factory
	 */
	public Session getSessionFactory(HibernateSessionFactoryType factoryType) {

		return webuiSessionFactory.getCurrentSession();
	}

	/**
	 * Gets the session factory for save.
	 * 
	 * @param factoryType the factory type
	 * @return the session factory for save
	 */
	public Session getSessionFactoryForSave(HibernateSessionFactoryType factoryType) {

		return webuiSessionFactory.openSession();
	}

	/**
	 * Gets the transaction.
	 * 
	 * @param session the session
	 * @return the transaction
	 */
	public Transaction getTransaction(Session session) {
		Transaction tx = null;
		try {
			if (session.getTransaction() != null && session.getTransaction().isActive()) {
				tx = session.getTransaction();
			} else {
				tx = session.beginTransaction();
			}
		} catch (HibernateException he) {
			he.printStackTrace();
		}
		return tx;
	}

	/**
	 * Save or update.This method use to save or update any given object in database
	 * 
	 * @param obj the obj
	 * @return true, if successful
	 */
	@Transactional(value = "webuiTransactionManager")
	public boolean saveOrUpdate(Object obj) {
		Session session = getSessionFactoryForSave(HibernateSessionFactoryType.SYSTEM);
		Transaction tx = null;
		try {
			if (session.getTransaction() != null && session.getTransaction().isActive()) {
				tx = session.getTransaction();
			} else {
				tx = session.beginTransaction();
			}

			session.saveOrUpdate(obj);

			tx.commit();
		} catch (HibernateException e) {
			if (tx != null) {
				if (!tx.wasRolledBack()) {
					tx.rollback();
				}

			}

			return false;
		} finally {
			session.close();
		}
		return true;
	}
	
	
	/**
	 * This method use to save any given object in database
	 * 
	 * @param obj the obj
	 * @return true, if successful
	 */
	@Transactional(value = "webuiTransactionManager")
	public boolean save(Object obj) {
		Session session = getSessionFactoryForSave(HibernateSessionFactoryType.SYSTEM);
		Transaction tx = null;
		try {
			if (session.getTransaction() != null && session.getTransaction().isActive()) {
				tx = session.getTransaction();
			} else {
				tx = session.beginTransaction();
			}

			session.save(obj);

			tx.commit();
		} catch (HibernateException e) {
			if (tx != null) {
				if (!tx.wasRolledBack()) {
					tx.rollback();
				}
			}
			return false;
		} finally {
			session.close();
		}
		return true;
	}
}
