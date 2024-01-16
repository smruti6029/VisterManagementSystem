package com.app.DaoImpl;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import com.app.Dao.CityDao;
import com.app.Dao.CompanyDao;
import com.app.Dao.StateDao;
import com.app.dto.CompanyDTO;
import com.app.dto.IsActiveDto;
import com.app.dto.PaginationRequest;
import com.app.entity.Building;
import com.app.entity.City;
import com.app.entity.Company;
import com.app.entity.State;

@Repository
@Transactional
public class CompanyRepositoryImpl implements CompanyDao {

	@Autowired
	private SessionFactory sessionFactory;

	@Autowired
	private StateDao stateRepo;

	@Autowired
	private CityDao cityRepo;

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public Company getCompanyById(Integer id) {

		try {

			Session session = sessionFactory.getCurrentSession();

			return session.get(Company.class, id);

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}

	@Override
	public Company saveCompany(Company company) {

		try {
			Session session = sessionFactory.getCurrentSession();

			Serializable id = session.save(company);

			return session.get(Company.class, id);

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}

	@Override
	public Company updateCompany(Integer companyId, Company updatedCompanyDto) {

		try {

			Session session = sessionFactory.getCurrentSession();

			Company existingCompany = session.get(Company.class, companyId);

			if (existingCompany != null) {

				existingCompany.setName(updatedCompanyDto.getName());
				existingCompany.setEmail(updatedCompanyDto.getEmail());
				existingCompany.setPhoneNumber(updatedCompanyDto.getPhoneNumber());
				existingCompany.setAddress(updatedCompanyDto.getAddress());
				existingCompany.setLogo(updatedCompanyDto.getLogo());
				existingCompany.setPincode(updatedCompanyDto.getPincode());
				existingCompany.setIndustry(updatedCompanyDto.getIndustry());
				existingCompany.setAboutUs(updatedCompanyDto.getAboutUs());

				State state = stateRepo.getStateById(updatedCompanyDto.getState().getId());

				City city = cityRepo.getCityById(updatedCompanyDto.getCity().getId());

				existingCompany.setState(state);
				existingCompany.setCity(city);
				existingCompany.setModifiedOn(new Timestamp(System.currentTimeMillis()));

				session.update(existingCompany);

			}

			return existingCompany;

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}

	@Override
	public Company updateCompany(Company company) {

		try {

			Session session = sessionFactory.getCurrentSession();

			session.update(company);

			return company;

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}

	@Override
	public void deleteCompany(Integer id) {
		Session session = sessionFactory.getCurrentSession();
		Company company = session.get(Company.class, id);
		if (company != null) {
			session.delete(company);
		}
	}

	@Override
	public List<Company> getAllCompanies() {

		try {

			Session session = sessionFactory.getCurrentSession();

			Criteria criteria = session.createCriteria(Company.class);

			return criteria.list();

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}

	@Override
	public List<Company> getCompanyByStateAndCity(State state, City city) {

		try {

			Session session = sessionFactory.getCurrentSession();

			Query<Company> query = session.createQuery("FROM Company WHERE state = :state AND city = :city",
					Company.class);

			query.setParameter("state", state);

			query.setParameter("city", city);

			return query.getResultList();

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}

	@Override
	public Integer updateActiveCompany(IsActiveDto activeDto) {

		try {

			Session session = sessionFactory.getCurrentSession();

			Criteria criteria = session.createCriteria(Company.class);

			criteria.add(Restrictions.eq("id", activeDto.getId()));

			Company company = (Company) criteria.uniqueResult();
			if (company != null) {
				company.setActive(activeDto.getIsActive());
				session.update(company);
				return company.getId();
			} else {
				return null;
			}

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}

	@Override
	public Company findByEmail(String email) {

		try {

			Session session = sessionFactory.getCurrentSession();

			Query<Company> query = session.createQuery("FROM Company WHERE email = :email", Company.class);

			query.setParameter("email", email);

			return query.uniqueResult();

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}
	
	@Override
	public Company findByCompanyName(String name) {

		try {

			
			Session session = sessionFactory.getCurrentSession();

			Query<Company> query = session.createQuery("FROM Company WHERE name = :name", Company.class);

			query.setParameter("name", name);

			return query.uniqueResult();

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}
	
	
	
	@Override
	public Company findByPhone(String phone) {
		try {

			Session session = sessionFactory.getCurrentSession();

			Query<Company> query = session.createQuery("FROM Company WHERE phoneNumber = :phoneNumber", Company.class);

			query.setParameter("phoneNumber", phone);

			return query.uniqueResult();

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public List<Object[]> findAllCompaniesStatus() {

		try {

			Session session = entityManager.unwrap(Session.class);

			String hql = "SELECT c.id, c.active FROM Company c";

			Query<Object[]> query = session.createQuery(hql, Object[].class);

			return query.getResultList();

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}

	@Override
	public boolean existsByNameAndStateIdAndCityId(String name, Integer stateId, Integer cityId) {

		Session session = sessionFactory.getCurrentSession();
		String hql = "SELECT COUNT(*) FROM Company c WHERE c.name = :name AND c.state.id = :stateId AND c.city.id = :cityId";
		Long count = (Long) session.createQuery(hql).setParameter("name", name).setParameter("stateId", stateId)
				.setParameter("cityId", cityId).uniqueResult();
		return count != null && count > 0;
	}

	@Override
	public List<Company> getAllCompaniesByBuildingId(Integer buildingId) {
		try {
			Criteria criteria = this.sessionFactory.getCurrentSession().createCriteria(Company.class);
			// criteria.createAlias("building", "b");
			criteria.add(Restrictions.eq("building.buildingId", buildingId));
			return criteria.list();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public List<Company> getBuildingById(Integer Id) {

		try {

			Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Building.class);

			criteria.add(Restrictions.eq("id", Id));

			return criteria.list();

		} catch (Exception e) {

			e.printStackTrace();

			return null;

		}

	}

	@Override
	public List<Company> searchCompany(Integer stateId, String companyName, Boolean isActive) {

		Session session = sessionFactory.openSession();
		try {
			CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();

			CriteriaQuery<Company> criteriaQuery = criteriaBuilder.createQuery(Company.class);

			Root<Company> root = criteriaQuery.from(Company.class);

			List<Predicate> predicates = new ArrayList<>();

			if (stateId != null) {
				Join<Company, State> stateJoin = root.join("state");
				predicates.add(criteriaBuilder.equal(stateJoin.get("id"), stateId));
			}

			if (companyName != null && !companyName.isEmpty()) {
				predicates.add(criteriaBuilder.like(root.get("name"), "%" + companyName + "%"));
			}

			if (isActive != null) {
				predicates.add(criteriaBuilder.equal(root.get("active"), isActive));
			}

			criteriaQuery.where(predicates.toArray(new Predicate[0]));

			TypedQuery<Company> typedQuery = session.createQuery(criteriaQuery);
			return typedQuery.getResultList();
		} finally {
			session.close();
		}

	}

	@Override
	public Page<Company> getAllCompanies(PaginationRequest request) {

		Session session = sessionFactory.getCurrentSession();

		Criteria criteria = session.createCriteria(Company.class);

		// Apply ordering
		criteria.addOrder(Order.desc("createdOn"));

		// Apply filtering based on the provided criteria
		if (request.getPhoneNumber() != null) {
			criteria.add(Restrictions.eq("phoneNumber", request.getPhoneNumber()));
		}

		if (request.getBuildingId() != null) {
			criteria.createAlias("building", "b");
			criteria.add(Restrictions.eq("b.buildingId", request.getBuildingId()));
		}

		if (request.getCompanyName() != null) {
			criteria.add(Restrictions.ilike("name", request.getCompanyName(), MatchMode.ANYWHERE));
		}

		// Calculate total count without pagination
		int totalCount = getTotalCompanyCount(request);

		// Apply pagination
		criteria.setFirstResult(request.getPage() * request.getSize());
		criteria.setMaxResults(request.getSize());

		List<Company> resultList = criteria.list();

		return new PageImpl<>(resultList, PageRequest.of(request.getPage(), request.getSize()), totalCount);
	}

	private int getTotalCompanyCount(PaginationRequest request) {
		Session session = sessionFactory.getCurrentSession();

		Criteria criteria = session.createCriteria(Company.class);

		// Apply filtering based on the provided criteria
		if (request.getPhoneNumber() != null) {
			criteria.add(Restrictions.eq("phoneNumber", request.getPhoneNumber()));
		}

		if (request.getBuildingId() != null) {
			criteria.createAlias("building", "b");
			criteria.add(Restrictions.eq("b.buildingId", request.getBuildingId()));
		}

		if (request.getCompanyName() != null) {
			criteria.add(Restrictions.ilike("name", request.getCompanyName(), MatchMode.ANYWHERE));
		}

		// Count the total results
		criteria.setProjection(Projections.rowCount());
		return ((Number) criteria.uniqueResult()).intValue();
	}

	@Override
	public List<Company> getCompanyByNameAndBuilding(String companyName, int buildingId) {
		try {
			Session session = sessionFactory.getCurrentSession();

			Criteria criteria = session.createCriteria(Company.class).add(Restrictions.eq("name", companyName))
					.add(Restrictions.eq("building.buildingId", buildingId));

			return criteria.list();

		} catch (Exception e) {
			e.printStackTrace();
			return Collections.emptyList();
		}
	}

	@Override
	public Company getcompanyByphoneWithBuildingID(CompanyDTO companyDTO) {
		try {
			Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Company.class);
			criteria.add(Restrictions.eq("phoneNumber", companyDTO.getPhoneNumber()))
					.add(Restrictions.eq("building.buildingId", companyDTO.getBuilding().getBuildingId()));
			return (Company) criteria.uniqueResult();
		} catch (Exception e) {

			e.printStackTrace();
			return null;
		}
	}

}
