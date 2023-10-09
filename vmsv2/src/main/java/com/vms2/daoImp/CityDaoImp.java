package com.vms2.daoImp;



import java.util.List;

import javax.transaction.Transactional;

import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.vms2.dao.CityDao;
import com.vms2.entity.City;

@Repository
@Transactional
public class CityDaoImp implements CityDao {

	
	@Autowired
	private SessionFactory sessionFactory;
	
	@Override
	public List<City> getcitys(Integer id) {
		
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(City.class);
		
		criteria.add(Restrictions.eq("state.id", id));
		
		return criteria.list();
		
		
	}

	@Override
	public City getCityById(Integer id) {
		return sessionFactory.getCurrentSession().get(City.class, id);
	}

}
