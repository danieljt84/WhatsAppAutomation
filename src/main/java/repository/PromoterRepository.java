package repository;

import javax.persistence.Query;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

import model.DataFile;
import model.Promoter;
import model.Shop;
import util.HibernateUtil;

public class PromoterRepository {

	private SessionFactory sessionFactory;
	private Session session;
	private Class persistedClass;
	
	public PromoterRepository() {
		this.persistedClass = Promoter.class;
		this.sessionFactory = new HibernateUtil().getSessionFactory();
	}
	
	public Promoter findById(Long id) {
		session = this.sessionFactory.getCurrentSession();
		session.beginTransaction();
		String hql = "select p from Promoter p where p.id=:id";
		try {
			Query query = session.createQuery(hql,Promoter.class);
			query.setParameter("id", id);
			return (Promoter) query.getSingleResult();
		}catch(Exception e) {
			return null;
		}finally {
			session.getTransaction().commit();
		}
	}
	
	public Promoter save(String name) {
		Promoter promoter = new Promoter();
		promoter.setName(name);
		return (Promoter) session.merge(promoter);
	}
	
	public Promoter findByName(String name) {
		session = this.sessionFactory.getCurrentSession();
		session.beginTransaction();
		String hql = "select p from Promoter p where p.name=:name";
		try {
			Query query = session.createQuery(hql,Promoter.class);
			query.setParameter("name", name);
			return (Promoter) query.getSingleResult();
		}catch(Exception e) {
            save(name);
            return null;
		}finally {
			session.getTransaction().commit();
		}
	}

}
