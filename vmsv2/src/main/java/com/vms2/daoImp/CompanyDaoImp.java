package com.vms2.daoImp;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.stereotype.Repository;

import com.vms2.dao.CompanyDao;
import com.vms2.entity.Company;

@Repository
public class CompanyDaoImp implements CompanyDao {
	
		
		@Autowired
		private SessionFactory sessionFactory;
		
		@Override
		public int addCompany(Company company)
		{	
			try
			{
			sessionFactory.getCurrentSession().save(company);
			return 1;
			}
			catch (Exception e) {
				return 0;			}
		}
}
