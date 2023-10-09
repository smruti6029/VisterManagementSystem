package com.vms2.daoImp;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

import javax.transaction.Transactional;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.vms2.dao.VisitorDao;
import com.vms2.dto.VisitormeetingDTO;
import com.vms2.entity.Visitor;

@Repository
@Transactional
public class VisitorDaoImpl implements VisitorDao {

	@Autowired
	private SessionFactory sessionFactory;

	@Override
	public List<Visitor> getAllVisitors() {

		Session session = sessionFactory.getCurrentSession();

		Criteria criteria = session.createCriteria(Visitor.class);

		return criteria.list();
	}

	@Override
	public Visitor addVisitor(VisitormeetingDTO visitorDto) {

		Session session = sessionFactory.getCurrentSession();

		Visitor visitor = VisitormeetingDTO.toVisitor(visitorDto);

		Serializable id = session.save(visitor);

		return session.get(Visitor.class, id);

	}

	@Override
	public Visitor getVisitorById(Integer id) {

		Session session = sessionFactory.getCurrentSession();

		return session.get(Visitor.class, id);

	}

	@Override
	public Visitor updateVisitor(Integer visiterId, VisitormeetingDTO visitorDto) {

		Session session = sessionFactory.getCurrentSession();

		Visitor existingVisiter = session.get(Visitor.class, visiterId);

		if (existingVisiter != null) {

			existingVisiter.setName(visitorDto.getName());
			existingVisiter.setEmail(visitorDto.getEmail());
			existingVisiter.setAddress(visitorDto.getAddress());
			existingVisiter.setCity(visitorDto.getCity());
			existingVisiter.setState(visitorDto.getState());
			existingVisiter.setImage(visitorDto.getImageUrl());
			existingVisiter.setAadhaarNumber(visitorDto.getAadhaarNumber());
			existingVisiter.setCompanyName(visitorDto.getCompanyName());
			existingVisiter.setGender(visitorDto.getGender());
			existingVisiter.setUpdatedBy(visitorDto.getName());
			existingVisiter.setModifiedOn(new Timestamp(System.currentTimeMillis()));

		}

		return existingVisiter;

	}

	@Override
	public Visitor addVisitor(Visitor visitor) {

		try {
			Session session = sessionFactory.getCurrentSession();

			session.save(visitor);
			return visitor;
		} catch (Exception e) {
			return null;
		}

	}

	@Override
	public Visitor getVisitorByPhone(String phoneNumber) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Visitor.class);
		criteria.add(Restrictions.eq("phoneNumber", phoneNumber));
		return (Visitor) criteria.uniqueResult();
	}

}
