package com.app.DaoImpl;

import java.util.List;

import javax.transaction.Transactional;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;

import com.app.Dao.StateCityRepo;
import com.app.entity.City;
import com.app.entity.State;

@Repository
@Transactional
public class StateCityRepoImpl implements StateCityRepo{
	
	@Autowired
	private SessionFactory sessionFactory;
	
	

    @Override
    public List<State> getAllStates() {
        Session session = sessionFactory.getCurrentSession();
        return session.createQuery("FROM State", State.class).getResultList();
    }

    @Override
    @Cacheable("getAllCities")
    public List<City> getAllCities() {
        Session session = sessionFactory.getCurrentSession();
        return session.createQuery("FROM City", City.class).getResultList();
    }

}
