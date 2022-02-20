package repository;

import java.time.LocalDate;
import java.util.List;

import javax.persistence.Query;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

import model.Brand;
import model.DataFile;
import util.HibernateUtil;

public class BrandRepository implements Repository<Brand> {

	public SessionFactory sessionFactory;
	public Session session;
	public Class persistedClass;
	
	public BrandRepository() {
		this.persistedClass = DataFile.class;
		this.sessionFactory = new HibernateUtil().getSessionFactory();
	}

	@Override
	public Brand findById(Long id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void save(Brand entity) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void delete(Long id) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<Brand> findAll() {
		session = this.sessionFactory.getCurrentSession();
		session.beginTransaction();
		try {
			String hql = "select b from Brand b";
			Query query = session.createQuery(hql);
			return query.getResultList();
		}catch (Exception e) {
			e.printStackTrace();
			return null;
		}finally {
			session.getTransaction().commit();
		}
	}

	@Override
	public long countAll() {
		// TODO Auto-generated method stub
		return 0;
	}
}
