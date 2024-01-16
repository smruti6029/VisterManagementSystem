
package com.app.DaoImpl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.app.Dao.BuildingDao;
import com.app.entity.Building;

@Repository
@Transactional
public class BuildingDaoImpl implements BuildingDao {

	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	private SessionFactory sessionFactory;

	public Building createBuilding(Building building) {
		try {
			entityManager.persist(building);
			return building;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public Building getBuildingById(String buildingId) {
		try {
			return entityManager.find(Building.class, buildingId);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public Building getBuildingById1(Integer id) {
		try {
			return entityManager.createQuery("SELECT b FROM Building b WHERE b.id = :id", Building.class)
					.setParameter("id", id).getSingleResult();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public List<Building> getAllBuildings() {
		try {
			return entityManager.createQuery("FROM Building", Building.class).getResultList();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public Building updateBuilding(Building building) {
		try {
			return entityManager.merge(building);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public Building deleteBuilding(Building building) {
		try {
			entityManager.remove(entityManager.contains(building) ? building : entityManager.merge(building));
			return building;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public Building getBuildingById(Integer buildingId) {
		try {
			// Use find method to get the entity by its primary key
			return entityManager.find(Building.class, buildingId);
		} catch (NoResultException e) {
			// Handle the case when no building with the given ID is found
			return null;
		} catch (Exception e) {
			// Handle other exceptions
			e.printStackTrace(); // Log the exception or handle it according to your needs
			return null;
		}
	}

	@Override
	public Building getBuildingName(String name) {
		try {
			Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Building.class);
			criteria.add(Restrictions.eq("name", name));
			return (Building) criteria.uniqueResult();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}

}
