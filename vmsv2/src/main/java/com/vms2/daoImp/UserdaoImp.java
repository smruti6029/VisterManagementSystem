package com.vms2.daoImp;

import java.util.List;

import javax.transaction.Transactional;

import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.vms2.dao.UserDao;
import com.vms2.entity.User;


@Repository
@Transactional
public class UserdaoImp implements UserDao {
	
	@Autowired
	private SessionFactory sessionFactory;
	
	public Integer saveUser(User user)
	{
		try
		{
		sessionFactory.getCurrentSession().save(user);
		return 1;
		}catch (Exception e) {
			return 0;
		}
	}
	
	public User getUserbyPhone(String phone)
	{
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(User.class);
		
		criteria.add(Restrictions.eq("phone", phone));
		return (User) criteria.uniqueResult();
	}

	@Override
	public List<User> getallUser() {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(User.class);
		
		return criteria.list();
	}

	@Override
	public User getuserByid(Integer id) {
		try
		{
		User user = sessionFactory.getCurrentSession().get(User.class, id);
		return user;
		
		}catch (Exception e) {
			return null;
		}
	
	}

	@Override
	public Integer updateUser(User user) {
		try
		{
		sessionFactory.getCurrentSession().update(user);
		return 1;
		}catch (Exception e) {
			return 0;
		}
	}

}
