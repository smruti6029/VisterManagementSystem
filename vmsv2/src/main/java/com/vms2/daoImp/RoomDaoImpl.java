package com.vms2.daoImp;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;

import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.vms2.dao.RoomDao;
import com.vms2.dto.IdIsactiveDTO;
import com.vms2.entity.City;
import com.vms2.entity.Company;
import com.vms2.entity.Room;
import com.vms2.entity.State;

@Repository
@Transactional
public class RoomDaoImpl implements RoomDao{
	
	@Autowired
	private SessionFactory sessionFactory;

	@Override
	public Room save(Room room) {
		try
		{
		sessionFactory.getCurrentSession().save(room);
		return room;
		}
		catch (Exception e) {
			return null;
		}
		
		
	}

	@Override
	public Room getById(Integer id) {
		Criteria criteria=sessionFactory.getCurrentSession().createCriteria(Room.class);
		criteria.add(Restrictions.eq("id", id));
		return (Room) criteria.uniqueResult();
	}

	@Override
	public List<Room> getAll() {
		Criteria criteria=sessionFactory.getCurrentSession().createCriteria(Room.class);
		return criteria.list();
		
	}

	@Override
	public Room getByStatus(IdIsactiveDTO idIsactiveDTO) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Room update(Room room) {
		try
		{
		sessionFactory.getCurrentSession().update(room);
		return room;
		}catch (Exception e) {
			return null;
		}
		
		
	}

	@Override
	public Room delete(IdIsactiveDTO idIsactiveDTO) {
		// TODO Auto-generated method stub
		return null;
	}

	

}
