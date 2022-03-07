package com.util;

import java.io.File;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateUtil {
	
	private static final SessionFactory sessionFactory = buildSessionFactory();
	
	private static SessionFactory buildSessionFactory() {
		try {
			return new Configuration().configure(new File("resources\\hibernate.cfg.xml")).buildSessionFactory();
		}catch (Exception e) {
			System.err.println("Initial SessionFactory creation failed." + e);
            throw new ExceptionInInitializerError(e);
		}
	}
	
	public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }
	
	public static void shutdown() {
        // Close caches and connection pools
        getSessionFactory().close();
    }
	
}
