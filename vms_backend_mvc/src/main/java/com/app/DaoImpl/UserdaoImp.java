package com.app.DaoImpl;

import java.util.List;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Conjunction;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.app.Dao.UserDao;
import com.app.dto.CompanyDepartmentResponseDTO;
import com.app.dto.IsActiveDto;
import com.app.entity.Department;
import com.app.entity.User;

@Repository
@Transactional
public class UserdaoImp implements UserDao {

	@Autowired
	private SessionFactory sessionFactory;

	public Integer saveUser(User user) {
		try {
			sessionFactory.getCurrentSession().save(user);
			return 1;
		} catch (Exception e) {
			return 0;
		}
	}

	public User getUserbyPhone(String phone) {
		try {
			Criteria criteria = sessionFactory.getCurrentSession().createCriteria(User.class);

			criteria.add(Restrictions.eq("phone", phone));
			criteria.add(Restrictions.eq("isActive", true));
			return (User) criteria.uniqueResult();

		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public Long countActiveUsers(Integer companyId) {
		try {
			Criteria criteria = sessionFactory.getCurrentSession().createCriteria(User.class);

			criteria.add(Restrictions.eq("company.id", companyId));
			criteria.add(Restrictions.eq("isActive", true));

			criteria.setProjection(Projections.rowCount());

			return (Long) criteria.uniqueResult();
		} catch (Exception e) {
			return null;
		}
	}

	public User getUserByEmpCode(String empCode) {

		try {
			Criteria criteria = sessionFactory.getCurrentSession().createCriteria(User.class);

			criteria.add(Restrictions.eq("empCode", empCode));
			criteria.add(Restrictions.eq("isActive", true));
			return (User) criteria.uniqueResult();
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public User getUserByGoveID(String govtId) {

		try {
			Criteria criteria = sessionFactory.getCurrentSession().createCriteria(User.class);

			criteria.add(Restrictions.eq("govtId", govtId));
			criteria.add(Restrictions.eq("isActive", true));
			return (User) criteria.uniqueResult();
		} catch (Exception e) {
			return null;
		}
	}

	public List<User> getallUser() {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(User.class);
		criteria.add(Restrictions.eq("isActive", true));

		return criteria.list();
	}

	@Override
	public User getuserByid(Integer id) {
		try {
			User user = sessionFactory.getCurrentSession().get(User.class, id);
			return user;

		} catch (Exception e) {
			return null;
		}

	}

	@Override
	public Integer updateUser(User user) {
		try {
			sessionFactory.getCurrentSession().update(user);
			return 1;
		} catch (Exception e) {
			return 0;
		}
	}

	@Override
	public List<User> getallUser(Integer company_id) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(User.class);
		criteria.add(Restrictions.eq("company.id", company_id));
		criteria.add(Restrictions.eq("isActive", true));
		criteria.add(Restrictions.eq("isActive", true));
		return criteria.list();
	}

	@Override
	public User getUserByEmail(String email) {
		try {
			Criteria criteria = sessionFactory.getCurrentSession().createCriteria(User.class);
			criteria.add(Restrictions.eq("email", email));
			criteria.add(Restrictions.eq("isActive", true));
			return (User) criteria.uniqueResult();
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public List<User> getallUsers() {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(User.class);
		criteria.add(Restrictions.eq("isActive", true));
		return criteria.list();
	}
	
	@Override
	public List<User> getallUser2(Integer company_id, Integer buildingID) {

	    Criteria criteria = sessionFactory.getCurrentSession().createCriteria(User.class);
	    Conjunction conjunction = Restrictions.conjunction();
	    
	    criteria.createAlias("company", "c");
	    
        if(buildingID == null && company_id != null) {
	    	
        	criteria.add(Restrictions.eq("c.id", company_id));
    		return criteria.list();
	        
	    }

	    if(company_id != null && buildingID != null) {
	    	
	        conjunction.add(Restrictions.eq("c.id", company_id)); 
	        criteria.createAlias("c.building", "b");
	        conjunction.add(Restrictions.eq("b.buildingId", buildingID)); 
	    }
	    
	    if(buildingID != null && company_id == null) {
	    	
	    	criteria.createAlias("c.building", "b");
	        conjunction.add(Restrictions.eq("b.buildingId", buildingID)); 
	        
	    }
	    
	    
	    
	    conjunction.add(Restrictions.eq("isActive", true));
	    criteria.add(conjunction);

	    return criteria.list();

	}
//	@Override
//	public User updateUserPresent(IsActiveDto activeDto) {
//		
//		Session session = sessionFactory.getCurrentSession();
//		
//		Criteria criteria = session.createCriteria(User.class);
//
//		criteria.add(Restrictions.eq("id", activeDto.getId()));
//
//		User user = (User) criteria.uniqueResult();
//		if (user != null) {
//			user.setIsPresent(false);
//			session.update(user); 
//			return user;
//		}else {
//			return null;
//		}

	@Override
	public User updateUserPresent(IsActiveDto activeDto) {
		Session session = sessionFactory.getCurrentSession();
		User user = getuserByid(activeDto.getId());
		if (user != null) {
			user.setIsPresent(false);
			session.update(user);
			return user;
		} else {
			return null;
		}

	}

	@Override
	public List<CompanyDepartmentResponseDTO> getUserCountByDepartmentInCompany(Integer companyId) {
		Session session = sessionFactory.getCurrentSession();
		CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
		CriteriaQuery<CompanyDepartmentResponseDTO> criteriaQuery = criteriaBuilder
				.createQuery(CompanyDepartmentResponseDTO.class);
		Root<Department> departmentRoot = criteriaQuery.from(Department.class);
		Join<Department, User> userJoin = departmentRoot.join("users", JoinType.LEFT);

		criteriaQuery.multiselect(departmentRoot.get("company").get("id").alias("companyId"),
				departmentRoot.get("company").get("name").alias("companyName"),
				departmentRoot.get("id").alias("departmentId"), departmentRoot.get("name").alias("departmentName"),
				criteriaBuilder.count(userJoin).alias("userCount"));

		criteriaQuery
				.where(criteriaBuilder.and(criteriaBuilder.equal(departmentRoot.get("company").get("id"), companyId),
						criteriaBuilder.isTrue(departmentRoot.get("isActive"))));

//		criteriaQuery.where(criteriaBuilder.equal(departmentRoot.get("company").get("id"), companyId));
		criteriaQuery.groupBy(departmentRoot.get("company").get("id"), departmentRoot.get("id"));

		TypedQuery<CompanyDepartmentResponseDTO> typedQuery = session.createQuery(criteriaQuery);
		return typedQuery.getResultList();
	}

	@Override
	public void absentAll() {
		Session session = sessionFactory.getCurrentSession();
		String hqlUpdate = "update User set isPresent = false";
		int updatedEntities = session.createQuery(hqlUpdate).executeUpdate();
		System.out.println("Updated " + updatedEntities + " User to absent");

	}
	@Override
	public void presentAll() {
		Session session = sessionFactory.getCurrentSession();
		String hqlUpdate = "update User set isPresent = true";
		int updatedEntities = session.createQuery(hqlUpdate).executeUpdate();
		System.out.println("Updated " + updatedEntities + " User to Present");

	}
	

}
