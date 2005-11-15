/*
 * Created on 09-Mar-2005
 */
package com.anite.dennery.services.hibernate.impl;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Session;
import net.sf.hibernate.Transaction;

import org.apache.commons.lang.exception.NestableRuntimeException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.anite.dennery.services.hibernate.api.SessionManager;

/**
 * This service is based on the thread local implementation of a hibernate
 * utility class. The session is injected by hivemind.
 * 
 * @author <a href="mailTo:michael.daniel.jones@gmail.com" >mike</a>
 */
public class HivemindSessionManager implements SessionManager {

    private static Log log = LogFactory.getLog(HivemindSessionManager.class);

    private Session session;

    private Transaction transaction;
    
    /**
     * need for hivemind to populate it with the session
     * @param session
     */
    public void setSession(Session session) {
        this.session = session;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.anite.borris.webservice.service.api.ISessionManager#getSession()
     */
    public Session getSession() {
        return session;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.anite.borris.webservice.service.api.ISessionManager#closeSession()
     */
    public void closeSession() {
        try {           
            session.close();
        } catch (HibernateException e) {
            log.error("Unable to close session");
            log.error(e);
            throw new NestableRuntimeException(e);
        }
        session = null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.anite.borris.webservice.service.api.ISessionManager#beginTransaction()
     */
    public void beginTransaction() {

        if (transaction == null) {
            try {
                transaction = getSession().beginTransaction();
            } catch (HibernateException e) {
                log.error("Unable to begin transcation");
                log.error(e);
                throw new NestableRuntimeException(e);
            }
        }
    }

    
    public void commitTransaction() {        
        try {
            if (transaction != null && !transaction.wasCommitted() && !transaction.wasRolledBack()) {
                transaction.commit();
                transaction = null;
            }
        } catch (HibernateException e) {
            rollbackTransaction();
            log.error("Unable to rollback transcation");
            log.error(e);
            throw new NestableRuntimeException(e);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.anite.borris.webservice.service.api.ISessionManager#rollbackTransaction()
     */
    public void rollbackTransaction() {        
        try {            
            if (transaction != null && !transaction.wasCommitted() && !transaction.wasRolledBack()) {
                transaction.rollback();
            }
        } catch (HibernateException e) {
            log.error("failed to roll back");
            log.error(e);
            throw new NestableRuntimeException(e);
        } finally {            
            transaction = null;
            closeSession();            
        }
    }

}
