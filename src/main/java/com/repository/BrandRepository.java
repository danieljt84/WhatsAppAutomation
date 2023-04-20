package com.repository;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.List;
import java.util.Properties;

import javax.persistence.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import com.model.Brand;
import com.model.DataFile;
import com.util.HibernateUtil;

public class BrandRepository {

	public SessionFactory sessionFactory;
	public Session session;
	public FileReader reader;
	Properties properties;

	public BrandRepository() {
		this.sessionFactory = new HibernateUtil().getSessionFactory();
		FileReader reader;
	}

	public List<Brand> findAll() {
		session = this.sessionFactory.getCurrentSession();
		session.beginTransaction();
		try {
			String hql = "select b from Brand b";
			Query query = session.createQuery(hql);
			return query.getResultList();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			session.getTransaction().commit();
		}
	}
}
