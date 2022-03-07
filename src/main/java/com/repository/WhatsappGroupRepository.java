package com.repository;

import java.util.List;

import javax.persistence.Query;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

import com.model.DataFile;
import com.model.WhatsappGroup;
import com.util.HibernateUtil;

public class WhatsappGroupRepository {
	
	public SessionFactory sessionFactory;
	public Session session;
	public Class persistedClass;
	
	public List<DataFile> lista;

	public WhatsappGroupRepository() {
		this.persistedClass = WhatsappGroup.class;
		this.sessionFactory = new HibernateUtil().getSessionFactory();
	}
	
	public WhatsappGroup findShopsByGroup(WhatsappGroup entity) {
		session = this.sessionFactory.getCurrentSession();
		session.beginTransaction();
		String hql = "select w from WhatsappGroup w where w.name=:name";
		try {
			Query query = session.createQuery(hql,WhatsappGroup.class);
			query.setParameter("name", entity.getName());
			return (WhatsappGroup) query.getSingleResult();
		}catch (Exception e) {
			return null;
		}finally {
			session.getTransaction().commit();
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<WhatsappGroup> findAll(Long idBrand) {
		session = this.sessionFactory.getCurrentSession();
		session.beginTransaction();
		String hql = "select w from WhatsappGroup w where w.brand.id=:idBrand";
		try {
		Query query = session.createQuery(hql,WhatsappGroup.class);
		query.setParameter("idBrand", idBrand);
		return query.getResultList();
		}catch (Exception e) {
			return null;
		}finally{
			session.getTransaction().commit();
		}
	}
	
	public List<WhatsappGroup> findAll() {
		session = this.sessionFactory.getCurrentSession();
		session.beginTransaction();
		String hql = "select w from WhatsappGroup w";
		try {
		Query query = session.createQuery(hql,WhatsappGroup.class);
		return query.getResultList();
		}catch (Exception e) {
			return null;
		}finally{
			session.getTransaction().commit();
		}
	}
}
