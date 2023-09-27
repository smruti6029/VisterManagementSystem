package com.vms2.daoImp;

import java.util.List;

import javax.transaction.Transactional;

import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.vms2.entity.State;

@Repository
@Transactional
public class StateDao {
	
	@Autowired
	private SessionFactory sessionFactory;
	
	public List<State> getAll()
	{
		Criteria criteria=sessionFactory.getCurrentSession().createCriteria(State.class);
		return criteria.list();
	}

}
