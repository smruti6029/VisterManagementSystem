package com.vms2.daoImp;



import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
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
import org.hibernate.criterion.Restrictions;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.vms2.dao.CityDao;
import com.vms2.dao.CompanyRepo;
import com.vms2.dao.StateDao;
import com.vms2.dto.CompanyDTO;
import com.vms2.dto.IdIsactiveDTO;
import com.vms2.entity.City;
import com.vms2.entity.Company;
import com.vms2.entity.State;



@Repository
@Transactional
public class CompanyRepositoryImpl implements CompanyRepo {

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
		Session session = sessionFactory.getCurrentSession();
		return session.get(Company.class, id);
	}

	@Override
	public Company saveCompany(Company company) {

		Session session = sessionFactory.getCurrentSession();

		Serializable id = session.save(company);

		return session.get(Company.class, id);
	}

	@Override
	public Company updateCompany(Integer companyId, Company updatedCompanyDto) {

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
	}

	@Override
	public Company updateCompany(Integer companyId) {
		Session session = sessionFactory.getCurrentSession();
		Company existingCompany = session.get(Company.class, companyId);

		CompanyDTO updatedCompanyDto = new CompanyDTO();

		State state = stateRepo.getStateById(updatedCompanyDto.getState());

		City city = cityRepo.getCityById(updatedCompanyDto.getCity());

		if (existingCompany != null) {

			existingCompany.setName(updatedCompanyDto.getName());
			existingCompany.setEmail(updatedCompanyDto.getEmail());
			existingCompany.setPhoneNumber(updatedCompanyDto.getPhoneNumber());
			existingCompany.setAddress(updatedCompanyDto.getAddress());
			existingCompany.setLogo(updatedCompanyDto.getLogo());
			existingCompany.setPhoneNumber(updatedCompanyDto.getPincode());
			existingCompany.setIndustry(updatedCompanyDto.getIndustry());
			existingCompany.setAboutUs(updatedCompanyDto.getAboutUs());
			existingCompany.setState(state);
			existingCompany.setCity(city);

			session.update(existingCompany);

		}

		return existingCompany;
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

		   CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		 
		    CriteriaQuery<Company> criteriaQuery = criteriaBuilder.createQuery(Company.class);
		    
		    Root<Company> root = criteriaQuery.from(Company.class);

		    criteriaQuery.select(root);

		    return entityManager.createQuery(criteriaQuery).getResultList();

	}

	@Override
	public List<Company> getCompanyByStateAndCity(State state, City city) {

		Session session = sessionFactory.getCurrentSession();

		Query<Company> query = session.createQuery("FROM Company WHERE state = :state AND city = :city", Company.class);
		query.setParameter("state", state);
		query.setParameter("city", city);

		return query.getResultList();

	}

	@Override
	public List<Company> serachCompany(Integer stateId, Integer cityId, String companyName ,Boolean isActive) {
		
		   CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		   
	        CriteriaQuery<Company> criteriaQuery = criteriaBuilder.createQuery(Company.class);
	        
	        Root<Company> root = criteriaQuery.from(Company.class);

	        List<Predicate> predicates = new ArrayList<>();

	        if (stateId != null) {
	            Join<Company, State> stateJoin = root.join("state");
	            predicates.add(criteriaBuilder.equal(stateJoin.get("id"), stateId));
	        }

	        if (cityId != null) {
	            Join<Company, City> cityJoin = root.join("city");
	            predicates.add(criteriaBuilder.equal(cityJoin.get("id"), cityId));
	        }

	        if (companyName != null && !companyName.isEmpty()) {
	            predicates.add(criteriaBuilder.like(root.get("name"), "%" + companyName + "%"));
	        }
	        
	        if (isActive != null) {
	            predicates.add(criteriaBuilder.equal(root.get("active"), isActive));
	        }

	        criteriaQuery.where(predicates.toArray(new Predicate[0]));

	        TypedQuery<Company> typedQuery = entityManager.createQuery(criteriaQuery);
	        return typedQuery.getResultList();
	    
	}

	@Override
	public Integer updateActiveCompany(IdIsactiveDTO activeDto) {
		
		Session session = sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(Company.class);

		criteria.add(Restrictions.eq("id", activeDto.getId()));

		Company company = (Company) criteria.uniqueResult();
		if (company != null) {
			company.setActive(activeDto.getIsActive());
			session.update(company); // Update the entity
			return company.getId();
		}else {
			return null;
		}
			
	}

	@Override
	public Company findByEmail(String email) {
		
		   Session session = sessionFactory.getCurrentSession();
		   
	        Query<Company> query = session.createQuery("FROM Company WHERE email = :email", Company.class);
	        query.setParameter("email", email);
	        return query.uniqueResult();
	}
}
