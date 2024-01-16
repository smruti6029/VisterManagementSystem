package com.app.DaoImpl;

import java.util.List;

import javax.transaction.Transactional;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;

import com.app.Dao.CityDao;
import com.app.dto.CityDto;
import com.app.entity.City;
import com.app.entity.State;

@Repository
@Transactional
public class CityRepoImpl implements CityDao {

    @Autowired
    private SessionFactory sessionFactory;
    


    @Override
    public City getCityById(Integer id) {
        return sessionFactory.getCurrentSession().get(City.class, id);
    }

    @Override
    public List<City> getAllCities() {
        Session session = sessionFactory.getCurrentSession();
        return session.createQuery("FROM City", City.class).list();
    }

    @Override
    public void saveCity(City city) {
        sessionFactory.getCurrentSession().save(city);
    }

    @Override
    public void updateCity(City city) {
        sessionFactory.getCurrentSession().update(city);
    }

    @Override
    public void deleteCity(City city) {
        sessionFactory.getCurrentSession().delete(city);
    }

	@Override
	public List<City> getAllCityByStateId(Integer stateId) {
		
		
		
//		  Session session = sessionFactory.getCurrentSession();
//		  
//	        String hql = "FROM City WHERE state.id = :stateId";
//	        return session.createQuery(hql, City.class)
//	                .setParameter("stateId", stateId)
//	                .getResultList();
		

		
	       Session session = sessionFactory.getCurrentSession();

	        Criteria criteria = session.createCriteria(City.class);
	        
	        criteria.add(Restrictions.eq("state.id", stateId));

	        return criteria.list();
		
		
	}

	@Override
	public City getByName(String cityName) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(City.class);
		
		criteria.add(Restrictions.eq("name", cityName));
		return (City) criteria.uniqueResult();
	}

	@Override
	public List<CityDto> getCityByName(String cityName) {
		
		 Session session = sessionFactory.getCurrentSession();
		 
	        Criteria criteria = session.createCriteria(City.class);

	        criteria.add(Restrictions.ilike("name", cityName + "%"));

	        return criteria.list();
	}
	
	
}	
