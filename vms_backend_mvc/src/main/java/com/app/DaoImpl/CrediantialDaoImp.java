package com.app.DaoImpl;

import javax.transaction.Transactional;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.app.Dao.CrediantialDao;
import com.app.entity.CredentialMaster;

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

	public CredentialMaster getUsername(String username) {
		try { 
		Session session = sessionFactory.getCurrentSession();
			Criteria criteria = session.createCriteria(CredentialMaster.class);
			criteria.createAlias("user", "u"); // Create a join alias for the 'user' association
			criteria.add(Restrictions.eq("username", username));
			criteria.add(Restrictions.eq("u.isActive", true)); // Add condition for user's isActive property

			return (CredentialMaster) criteria.uniqueResult();
		} catch (Exception e) {
			// Handle exceptions
			e.printStackTrace();
			return null; // or throw an exception based on your error handling strategy
		}
	}

	@Override
	public Integer updatePassword(CredentialMaster user)
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
	public void clearOtp() {
		Session session = sessionFactory.getCurrentSession() ;
			
			String hqlUpdate = "update CredentialMaster set otp = null";
			int updatedEntities = session.createQuery(hqlUpdate).executeUpdate();
			System.out.println("Updated " + updatedEntities + " OTP ");
		
	}


}
