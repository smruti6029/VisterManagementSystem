package com.app.DaoImpl;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.app.Dao.VisitorCompanyDao;
import com.app.dto.VisitorCompanyDto;
import com.app.entity.Department;
import com.app.entity.VisitorCompany;

@Repository
@Transactional
public class VisitorCompanyDaoImpl implements VisitorCompanyDao {

	@Autowired
	private SessionFactory sessionFactory;

	@Override
	public VisitorCompany save(VisitorCompany visitorCompany) {

		try {
			Session session = this.sessionFactory.getCurrentSession();
			session.save(visitorCompany);
			return visitorCompany;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}

	@Override
	public VisitorCompany getBycompanyName(String name) {

		try {

			Criteria criteria = sessionFactory.getCurrentSession().createCriteria((VisitorCompany.class));
			criteria.add(Restrictions.eq("name", name));

			return (VisitorCompany) criteria.uniqueResult();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}

	@Override
	public VisitorCompany getById(Integer id) {

		VisitorCompany visitorCompany = sessionFactory.getCurrentSession().get(VisitorCompany.class, id);

		if (visitorCompany != null) {
			return visitorCompany;

		}

		return null;

	}

	@Override
	public List<VisitorCompany> getAll() {
		try {
			Criteria criteria = this.sessionFactory.getCurrentSession().createCriteria(VisitorCompany.class);
			return criteria.list();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public List<VisitorCompany> searchCompanyByName(String companyName) {
		Session session = sessionFactory.openSession();
		List<VisitorCompany> resultList = new ArrayList<>();
		try {
			String hql = "FROM VisitorCompany WHERE name LIKE :companyName";
			Query<VisitorCompany> query = session.createQuery(hql, VisitorCompany.class);
			query.setParameter("companyName", "%" + companyName + "%");

			resultList = query.getResultList();

			if (resultList.isEmpty()) {
				return resultList; // or handle it according to your business logic
			} else if (resultList.size() > 1) {
				// Handle case when multiple results are found
				// You might want to log a warning or throw an exception
				return resultList; // Or handle it according to your business logic
			} else {
				return resultList; // Single result, return it
			}

		} catch (Exception e) {
			return resultList;
		}
	}

}