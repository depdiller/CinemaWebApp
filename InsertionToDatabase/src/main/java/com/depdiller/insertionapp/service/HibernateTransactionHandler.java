package com.depdiller.insertionapp.service;

import com.depdiller.insertionapp.config.HibernateUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class HibernateTransactionHandler {
    private EntityTransaction tx;
    private EntityManager entityManager;
    private static final ThreadLocal<EntityManager> HIBERNATE_SESSION =
            new ThreadLocal<>() {
                @Override
                protected EntityManager initialValue() {
                    return HibernateUtil.creatEntityManager();
                }
            };

    {
        entityManager = HIBERNATE_SESSION.get();
        tx = entityManager.getTransaction();
    }

    public void runTransaction(TransactionRunner runner) {
        try {
            tx.begin();
            runner.run();
            tx.commit();
        } catch (Exception ex) {
            try {
                tx.rollback();
            } catch (Exception rbEx) {
                System.err.println("Rollback of transaction failed, trace follows!");
                rbEx.printStackTrace(System.err);
            }
            throw new RuntimeException(ex);
        } finally {
            if (entityManager.isOpen())
                entityManager.close();
        }
    }
}
