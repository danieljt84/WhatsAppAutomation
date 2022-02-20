package repository;

import java.util.List;

import javax.persistence.Query;
import javax.transaction.Transactional;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

import model.Shop;
import util.HibernateUtil;

public class ShopRepository implements Repository<Shop> {

	public SessionFactory sessionFactory;
	public Session session;
	public Class persistedClass;

	public ShopRepository() {
		this.persistedClass = Shop.class;
		this.sessionFactory = new HibernateUtil().getSessionFactory();
	}

	@Override
	public Shop findById(Long id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Transactional
	public Shop findByName(String name) {
		session = this.sessionFactory.getCurrentSession();
		session.beginTransaction();
		String hql = "select s from Shop s where s.name=:name";
		try {
			Query query = session.createQuery(hql, Shop.class);
			query.setParameter("name", name);
			return (Shop) query.getSingleResult();
		} catch (Exception e) {
			return save(name);
		}finally {
			session.getTransaction().commit();
		}
	}

	public Shop save(String name) {
		Shop shop = new Shop();
		shop.setName(name);
		return (Shop) session.merge(shop);
	}

	@Override
	public void delete(Long id) {
		// TODO Auto-generated method stub

	}

	@SuppressWarnings("unchecked")
	@Override
	@Transactional
	public List<Shop> findAll() {
		String hql = "select s from Shop s";
		Query query = session.createQuery(hql, Shop.class);
		return query.getResultList();
	}

	@Override
	public long countAll() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void save(Shop entity) {
		// TODO Auto-generated method stub
		
	}

}
