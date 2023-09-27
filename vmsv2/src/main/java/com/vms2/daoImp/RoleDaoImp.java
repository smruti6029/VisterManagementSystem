package com.vms2.daoImp;

import java.io.Serializable;
import java.util.List;

import javax.transaction.Transactional;

import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.vms2.dao.RoleDao;
import com.vms2.entity.Role;

@Repository
@Transactional
public class RoleDaoImp implements RoleDao {
	
	
	@Autowired
	private SessionFactory sessionFactory;
	
	@Override
	public Integer addRole(Role role)
	{
		try
		{
		sessionFactory.getCurrentSession().save(role);
		return 1;
		}catch (Exception e) {
			return 0;
		}
	}

	@Override
	public Role getRole(String name) {
		Criteria criteria=sessionFactory.getCurrentSession().createCriteria(Role.class);
		
		criteria.add(Restrictions.eq("name", name));
		return (Role) criteria.uniqueResult();
		
	}

	@Override
	public List<Role> getallRole() {
		Criteria criteria=sessionFactory.getCurrentSession().createCriteria(Role.class);
		return criteria.list();
		
	}

	@Override
	public Role getroleByid(Integer id) {
		try
		{
		Role role = sessionFactory.getCurrentSession().get(Role.class, id);
		return role;
		}catch (Exception e) {
			return null;
		}
	}

	@Override
	public Integer deleteRole(Role role) {
		try
		{
		sessionFactory.getCurrentSession().delete(role);
		return 1;
		}catch (Exception e) {
			return 0;
		}
		
	}

}
