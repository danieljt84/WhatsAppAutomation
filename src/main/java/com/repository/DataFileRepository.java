package com.repository;

import java.time.Duration;
import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.Optional;

import javax.persistence.Query;
import javax.transaction.Transactional;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

import com.model.DataFile;
import com.model.DetailProduct;
import com.model.SendStatus;
import com.util.HibernateUtil;
import com.util.Status;

public class DataFileRepository {

	public SessionFactory sessionFactory;
	public Session session;
	public List<DataFile> lista;

	public DataFileRepository() {
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
		String hql = "select 1 from DataFile d where d.shop.id=:idLoja and d.data=:dataHoje and d.brand.id=:idBrand";
		try {
			Query query = session.createQuery(hql);
			query.setParameter("idLoja", dataFile.getShop().getId());
			query.setParameter("dataHoje", LocalDate.now());
			query.setParameter("idBrand", dataFile.getBrand().getId());
			query.getSingleResult();
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	@SuppressWarnings("unchecked")
	public List<DataFile> findByBrand(long idBrand) {
		// TODO Auto-generated method stub
		session = this.sessionFactory.getCurrentSession();
		session.beginTransaction();
		String hql = "select d from DataFile d where d.data=:dataHoje and d.brand.id=:idBrand and d.id not in (select s.id from SendStatus s)";
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

	public List<DetailProduct> checkLastSentDetails(DataFile dataFile, Integer diferenceDays) {
		session = this.sessionFactory.getCurrentSession();
		session.beginTransaction();
		int days;
		try {
			Period period = Period.between(getLastData(dataFile), LocalDate.now());
			days = period.getDays();
		} catch (Exception e) {
			days = 2;
		}
		try {
			if (days >= diferenceDays) {
				Thread.sleep(1000);
				dataFile = session.find(DataFile.class, dataFile.getId());
				Thread.sleep(1000);
				dataFile.getDetailProducts();
				return dataFile.getDetailProducts();
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			session.getTransaction().commit();
		}
		return null;
	}

	public LocalDate getLastData(DataFile dataFile) {
		String hql = "select max(d.data) from DataFile d where d.shop.id = :idShop"
				+ " and (select 1 from SendStatus s where s.id = d.id and s.sendDetail = true) = 1";
		Query query = session.createQuery(hql);
		// query.setParameter("dataHoje", LocalDate.now());
		// query.setParameter("diferenceDays", diferenceDays);
		query.setParameter("idShop", dataFile.getShop().getId());
		try {
			return (LocalDate) query.getSingleResult();
		} catch (Exception e) {
			return null;
		}
	}

	public List<DetailProduct> getList() {
		session = this.sessionFactory.getCurrentSession();
		session.beginTransaction();
		String hql = "select dp from DetailProduct dp where (select id)";
		Query query = session.createQuery(hql);
		return query.getResultList();
	}
	

	public void updateStatus(DataFile dataFile, Status status) {
		try {
			session = this.sessionFactory.getCurrentSession();
			session.beginTransaction();
			SendStatus sendStatus = new SendStatus();
			sendStatus.setDatafile(dataFile);
			sendStatus.setSendPhoto(true);
			if (status.equals(Status.ONLY_PHOTOS)) {
				sendStatus.setSendDetail(false);
			} else {
				sendStatus.setSendDetail(true);
			}
			session.save(sendStatus);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			session.getTransaction().commit();
		}
	}

}
