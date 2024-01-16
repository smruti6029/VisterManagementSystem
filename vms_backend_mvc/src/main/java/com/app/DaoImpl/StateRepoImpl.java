package com.app.DaoImpl;

import java.util.List;

import javax.transaction.Transactional;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.app.Dao.StateDao;
import com.app.entity.State;

@Repository
@Transactional
public class StateRepoImpl implements StateDao {

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public State getStateById(Integer id) {
        return sessionFactory.getCurrentSession().get(State.class, id);
    }

    @Override
    public List<State> getAllStates() {
        Session session = sessionFactory.getCurrentSession();
        return session.createQuery("FROM State", State.class).list();
    }

    @Override
    public void saveState(State state) {
        sessionFactory.getCurrentSession().save(state);
    }

    @Override
    public void updateState(State state) {
        sessionFactory.getCurrentSession().update(state);
    }

    @Override
    public void deleteState(State state) {
        sessionFactory.getCurrentSession().delete(state);
    }

    @Override
	public State getByName(String name) {
	Criteria criteria=sessionFactory.getCurrentSession().createCriteria(State.class);
		criteria.add(Restrictions.eq("name", name));
		return (State) criteria.uniqueResult();
	}
}