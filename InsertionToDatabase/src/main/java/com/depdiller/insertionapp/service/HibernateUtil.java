package com.depdiller.insertionapp.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateUtil {
    private static final EntityManagerFactory entityManagerFactory;
    private static final SessionFactory sessionFactory;

    static {
        try {
            // Hibernate Sessions
            Configuration hibernateConfig = new Configuration().configure();
            sessionFactory = hibernateConfig.buildSessionFactory();

            // JPA Entity Managers
            entityManagerFactory = Persistence.createEntityManagerFactory("Insertion application");
        } catch (Throwable ex) {
            System.err.println("Failed to create EntityManager object." + ex);
            throw new ExceptionInInitializerError();
        }
    }
    public static Session createSession() {
        return sessionFactory.openSession();
    }

    public static EntityManager createEntityManager() {
        return entityManagerFactory.createEntityManager();
    }
}
