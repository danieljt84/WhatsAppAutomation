package repository;

import java.time.LocalDate;
import java.util.List;

import javax.persistence.Query;
import javax.transaction.Transactional;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

import model.DataFile;
import util.HibernateUtil;
import util.Status;



public class DataFileRepository {

	public SessionFactory sessionFactory;
	public Session session;
	public Class persistedClass;

	public List<DataFile> lista;

	public DataFileRepository() {
		this.persistedClass = DataFile.class;
		this.sessionFactory = new HibernateUtil().getSessionFactory();
	}

	public DataFile findById(Long id) {
		return null;
	}

	@Transactional
	public void save(DataFile entity) {
		session = this.sessionFactory.getCurrentSession();
		session.beginTransaction();
		try {
			if (!checkDataFile(entity)) {
				session.save(entity);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			session.getTransaction().commit();
		}

	}

	// Verifica se existe datafile do dia

	public boolean checkDataFile(DataFile dataFile) {
		String hql = "select 1 from DataFile d where d.shop.id=:idLoja and d.data=:dataHoje";
		try {
			Query query = session.createQuery(hql);
			query.setParameter("idLoja", dataFile.getShop().getId());
			query.setParameter("dataHoje", LocalDate.now());
			query.getSingleResult();
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	@SuppressWarnings("unchecked")
	@Transactional()
	public List<DataFile> findByBrand(long idBrand) {
		// TODO Auto-generated method stub
		session = this.sessionFactory.getCurrentSession();
		session.beginTransaction();
		String hql = "select d from DataFile d where d.data=:dataHoje and d.brand.id=:idBrand";
		try {
			Query query = session.createQuery(hql, DataFile.class);
			query.setParameter("dataHoje", LocalDate.now());
			query.setParameter("idBrand", idBrand);
			return query.getResultList();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			session.getTransaction().commit();
		}
	}

	public void delete(Long id) {
		// TODO Auto-generated method stub

	}

	public long countAll() {
		// TODO Auto-generated method stub
		return 0;
	}


	public List<DataFile> findAll() {
		session = this.sessionFactory.getCurrentSession();
		session.beginTransaction();
		String hql = "select d from DataFile d where d.status=0 and d.data=:dataHoje";
		try {
			Query query = session.createQuery(hql, DataFile.class);
			query.setParameter("dataHoje", LocalDate.now());
			return query.getResultList();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			session.getTransaction().commit();
		}
	}

}
