
package com.app.DaoImpl;

import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Conjunction;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.app.Dao.RoomDao;
import com.app.dto.IsActiveDto;
import com.app.entity.Room;

@Repository
@Transactional
public class RoomDaoImpl implements RoomDao {

	@Autowired
	private SessionFactory sessionFactory;

	@Override
	public Room save(Room room) {
		Session session = this.sessionFactory.getCurrentSession();
		session.save(room);
		return room;
	}

	@Override
	public Room getById(Integer id) {
		Criteria criteria = this.sessionFactory.getCurrentSession().createCriteria(Room.class);
		Room room = (Room) criteria.add(Restrictions.eq("id", id)).uniqueResult();
		return room;
	}

	@Override
	public List<Room> getAll(Integer companyId) {

		Criteria criteria = this.sessionFactory.getCurrentSession().createCriteria(Room.class);

		// Add criteria to filter rooms based on the companyId
		// criteria.add(Restrictions.eq("isActive", true));
		// criteria.add(Restrictions.eq("isAvailable" , true));

		// Create an alias for the "company" property to be able to reference it in the
		// criteria
		criteria.createAlias("company", "c");

		// Add a condition to match the companyId
		criteria.add(Restrictions.eq("c.id", companyId));

		return criteria.list();
	}

	@Override
	public List<Room> getAllactive(Integer companyId) {

		Criteria criteria = this.sessionFactory.getCurrentSession().createCriteria(Room.class);

		// Add criteria to filter rooms based on the companyId
		criteria.add(Restrictions.eq("isActive", true));
		// criteria.add(Restrictions.eq("isAvailable" , true));

		// Create an alias for the "company" property to be able to reference it in the
		// criteria
		criteria.createAlias("company", "c");

		// Add a condition to match the companyId
		criteria.add(Restrictions.eq("c.id", companyId));

		return criteria.list();
	}

	@Override
	public List<Room> getAll2(Integer companyId, Integer buildingId) {

	    Criteria criteria = this.sessionFactory.getCurrentSession().createCriteria(Room.class);
	    Conjunction conjunction = Restrictions.conjunction();
	    
	    criteria.createAlias("company", "c");
	    criteria.createAlias("c.building", "b");
	    

	    if(buildingId ==null && companyId !=null) {

			criteria.add(Restrictions.eq("c.id", companyId));

			return criteria.list();
	    }
	    
	    if(buildingId != null &&  companyId == null) {
	    	conjunction.add(Restrictions.eq("b.buildingId", buildingId)); 
	    }

	    if(buildingId != null && companyId != null) {
	    	 conjunction.add(Restrictions.eq("c.id", companyId));
	         conjunction.add(Restrictions.eq("b.buildingId", buildingId)); 
	    }
	    
	    criteria.add(conjunction);
	    

	    return criteria.list();
	}
	
	@Override
	public List<Room> getAllactive2(Integer companyId, Integer buildingId) {
		
	    Criteria criteria = this.sessionFactory.getCurrentSession().createCriteria(Room.class);
	    Conjunction conjunction = Restrictions.conjunction();
	   
	    
	    criteria.createAlias("company", "c");
	    criteria.createAlias("c.building", "b");
	    criteria.add(Restrictions.eq("isActive", true));
	    

	    if(buildingId != null &&  companyId == null) {
	    	conjunction.add(Restrictions.eq("b.buildingId", buildingId)); 
	    }

	    if(buildingId != null && companyId != null) {
	    	 conjunction.add(Restrictions.eq("c.id", companyId));
	         conjunction.add(Restrictions.eq("b.buildingId", buildingId)); 
	    }
	    
	    criteria.add(conjunction);
	    

	    return criteria.list();
	}

	
	@Override
	public Room update(Room room) {
		Session session = this.sessionFactory.getCurrentSession();
		Room roomToBeUpdated = session.get(Room.class, room.getId());

		if (room.getRoomName() != null) {
			roomToBeUpdated.setRoomName(room.getRoomName());
		}

		if (room.getCapacity() != null) {
			roomToBeUpdated.setCapacity(room.getCapacity());
		}

		roomToBeUpdated.setUpdatedAt(new Date());

		session.update(roomToBeUpdated);
		return room;
	}

	public Room roomAvailability(IsActiveDto isActiveDto) {
		Session session = this.sessionFactory.getCurrentSession();
		Room room = session.get(Room.class, isActiveDto.getId());
		room.setIsAvailable(isActiveDto.getIsActive());
		session.update(room);
		return room;
	}

	@Override
	public Room delete(IsActiveDto isActiveDto) {
		Session session = this.sessionFactory.getCurrentSession();
		Room room = session.get(Room.class, isActiveDto.getId());
		System.out.println(room.getRoomName());
		room.setIsActive(isActiveDto.getIsActive());
		room.setIsAvailable(isActiveDto.getIsActive());
		session.update(room);
		return room;
	}

	@Override
	public void availableAllroom() {
		Session session = sessionFactory.getCurrentSession();

		String hqlUpdate = "update Room set isAvailable = true";
		int updatedEntities = session.createQuery(hqlUpdate).executeUpdate();
		System.out.println("Updated " + updatedEntities + " rooms to available.");

	}

	@Override
	public void delete(Integer roomId) {

		Session session = sessionFactory.getCurrentSession();

		Room roomByID = session.get(Room.class, roomId);

		if (roomByID != null) {
			session.delete(roomByID);
		}

	}

	@Override
	public Room getRoomByName(Room room) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Room.class);
		criteria.add(Restrictions.eq("roomName", room.getRoomName()));
		criteria.add(Restrictions.eq("company", room.getCompany()));
		return (Room) criteria.uniqueResult();

	}

}
