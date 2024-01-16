package com.app.DaoImpl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.app.Dao.DepartmentDao;
import com.app.dto.CompanyDepartmentResponseDTO;
import com.app.dto.IsActiveDto;
import com.app.entity.Department;
import com.app.entity.Role;
import com.app.entity.State;
import com.app.entity.User;

@Repository
@Transactional
public class DepartmentDaoImpl implements DepartmentDao {

	@Autowired
	private SessionFactory sessionFactory;

	@Override
	public Department save(Department department) {
		try {
			Session session = this.sessionFactory.getCurrentSession();
			session.save(department);
			return department;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public Department getById(Integer id) {
		try {
			Criteria criteria = this.sessionFactory.getCurrentSession().createCriteria(Department.class);
			criteria.add(Restrictions.eq("id", id));
			return (Department) criteria.uniqueResult();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public List<Department> getAll() {
		try {
			Criteria criteria = this.sessionFactory.getCurrentSession().createCriteria(Department.class);
			return criteria.list();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public List<Department> getAllByCompanyId(Integer companyId) {
		try {
			Criteria criteria = this.sessionFactory.getCurrentSession().createCriteria(Department.class);
			criteria.createAlias("company", "c");
			criteria.add(Restrictions.eq("c.id", companyId));
			return criteria.list();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public Department getByCompanyIdAndDepartmentName(Integer companyId, String departmentName) {
		try {
			Criteria criteria = this.sessionFactory.getCurrentSession().createCriteria(Department.class);
			criteria.createAlias("company", "c");
			criteria.add(Restrictions.eq("c.id", companyId));
			criteria.add(Restrictions.eq("name", departmentName));
			return (Department) criteria.uniqueResult();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public List<CompanyDepartmentResponseDTO> getDepartmentsWithUserCount(Integer companyId) {
		List<CompanyDepartmentResponseDTO> responseDTOList = new ArrayList<>();

		try (Session session = sessionFactory.openSession()) {
			CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
			CriteriaQuery<CompanyDepartmentResponseDTO> criteriaQuery = criteriaBuilder
					.createQuery(CompanyDepartmentResponseDTO.class);
			Root<User> userRoot = criteriaQuery.from(User.class);

			criteriaQuery.multiselect(userRoot.get("company").get("id").alias("companyId"),
					userRoot.get("company").get("name").alias("companyName"),
					userRoot.get("department").get("id").alias("departmentId"),
					userRoot.get("department").get("name").alias("departmentName"),
					criteriaBuilder.count(userRoot.get("id")).alias("userCount"));

			criteriaQuery.where(criteriaBuilder.equal(userRoot.get("company").get("id"), companyId));

			// Use LEFT JOIN to include departments with no users
			criteriaQuery.groupBy(userRoot.get("company").get("id"), userRoot.get("department").get("id"));

			TypedQuery<CompanyDepartmentResponseDTO> typedQuery = session.createQuery(criteriaQuery);
			responseDTOList = typedQuery.getResultList();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return responseDTOList;
	}

	@Override
	public Department update(Department department) {
		try {
			Session session = this.sessionFactory.getCurrentSession();
			session.update(department);
			return department;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public Department delete(IsActiveDto isActiveDto) {
		try {
			Session session = this.sessionFactory.getCurrentSession();
			Department department = session.get(Department.class, isActiveDto.getId());
			department.setIsActive(isActiveDto.getIsActive());
			department.setUpdatedOn(new Date());
			session.update(department);
			return department;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public Department getByName(String departmentName) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Department.class);
		criteria.add(Restrictions.eq("name", departmentName));
		return (Department) criteria.list().get(0);
	}

}
