package com.vms2.daoImp;

import javax.transaction.Transactional;

import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.vms2.dao.CrediantialDao;
import com.vms2.entity.CredentialMaster;

@Repository
@Transactional
public class CrediantialDaoImp implements CrediantialDao {

	@Autowired
	private SessionFactory sessionFactory;

	@Override
	public Integer saveCrediantial(CredentialMaster credentialMaster) {
		try {
			sessionFactory.getCurrentSession().save(credentialMaster);
			return 1;

		} catch (Exception e) {
			return 0;
		}
	}

	@Override
	public CredentialMaster getUsername(String username) {
		Criteria criteria= sessionFactory.getCurrentSession().createCriteria(CredentialMaster.class);
		criteria.add(Restrictions.eq("username", username));
		return (CredentialMaster) criteria.uniqueResult();
		
	}

	@Override
	public Integer update(CredentialMaster user)
	{
		try
		{
		sessionFactory.getCurrentSession().update(user);
		return 1;
		}catch (Exception e) {
			return 0;
		}
		
	}

	@Override
	public CredentialMaster getcrediantialByuser(Integer id) {
		Criteria criteria= sessionFactory.getCurrentSession().createCriteria(CredentialMaster.class);
		criteria.add(Restrictions.eq("user.id", id));
		return (CredentialMaster) criteria.uniqueResult();
	}

}
