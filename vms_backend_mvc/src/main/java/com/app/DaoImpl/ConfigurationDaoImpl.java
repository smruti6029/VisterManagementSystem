package com.app.DaoImpl;

import javax.transaction.Transactional;

import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.app.Dao.ConfigurationDao;
import com.app.entity.Configuration;

@Repository
@Transactional
public class ConfigurationDaoImpl implements ConfigurationDao {

	@Autowired
	private SessionFactory sessionFactory;

	@Override
	public Configuration getByKey(String key) {
		try {
			Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Configuration.class);
			criteria.add(Restrictions.eq("key", key));
			return (Configuration) criteria.uniqueResult();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

}
