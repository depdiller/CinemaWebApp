package com.depdiller.insertionapp.config;

import jakarta.persistence.EntityManager;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateUtil {
    private static final SessionFactory sessionFactory;

    static {
        try {
            Configuration hibernateConfig = new Configuration().configure();
            sessionFactory = hibernateConfig.buildSessionFactory();
        } catch (Throwable ex) {
            System.err.println("Failed to create Session Factory object." + ex);
            throw new ExceptionInInitializerError();
        }
    }
    public static Session createSession() {
        return sessionFactory.openSession();
    }

    public static EntityManager creatEntityManager() {
        return sessionFactory.createEntityManager();
    }
}
