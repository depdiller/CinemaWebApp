package com.depdiller.insertionapp;

import com.depdiller.insertionapp.service.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.resource.transaction.spi.TransactionStatus;

public class InsertionApp {
    public static void main(String... args) {
        Session session = HibernateUtil.createSession();
        Transaction transaction = session.getTransaction();
        try {
            transaction.begin();
            transaction.commit();
        } catch (Exception ex) {
            try {
                if(transaction.getStatus() == TransactionStatus.ACTIVE
                        || transaction.getStatus() == TransactionStatus.MARKED_ROLLBACK)
                    transaction.rollback();
            } catch (Exception rbEx) {
                System.err.println("Rollback of transaction failed, trace follows!");
                rbEx.printStackTrace(System.err);
            }
            throw new RuntimeException(ex);
        } finally {
            if (session.isOpen())
                session.close();
        }
    }
}