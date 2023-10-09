package com.vms2.daoImp;

import java.util.List;

import javax.transaction.Transactional;

import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.vms2.dao.StateDao;
import com.vms2.entity.State;

@Repository
@Transactional
public class StateDaoIMP  implements StateDao{
	
	@Autowired
	private SessionFactory sessionFactory;
	
	@Override
	public List<State> getAll()
	{
		Criteria criteria=sessionFactory.getCurrentSession().createCriteria(State.class);
		return criteria.list();
	}

	@Override
	public State getStateById(Integer id) {
		 return sessionFactory.getCurrentSession().get(State.class, id);
	}

}
