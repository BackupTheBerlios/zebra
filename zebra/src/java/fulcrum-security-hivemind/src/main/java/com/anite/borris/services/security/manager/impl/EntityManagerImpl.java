package com.anite.borris.services.security.manager.impl;

/*
 *  Copyright 2001-2004 The Apache Software Foundation
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Session;
import net.sf.hibernate.Transaction;


import com.anite.borris.services.hibernate.api.ISessionManager;
import com.anite.borris.services.security.manager.api.EntityManager;
import com.anite.borris.services.security.entity.api.SecurityEntity;
import com.anite.borris.services.security.entity.utils.DataBackendException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Base Entirt Manager
 *
 * @author <a href="mailto:epugh@upstate.com">Eric Pugh</a>
 * @author <a href="mailto:ray@starstream-media.com">Ray Offiah</a>
 * @version $Id: EntityManagerImpl.java,v 1.1 2005/11/10 17:29:44 bgidley Exp $
 */

public class EntityManagerImpl implements PersistenceHelper, EntityManager {

    /** Logging */
    private static Log log = LogFactory.getLog(EntityManagerImpl.class);

    protected ISessionManager iSessionManager;
    protected Transaction transaction;

    /**
     * Removes the security entity from the database
     * @param securityEntity
     * @throws DataBackendException
     */
    public void removeEntity(SecurityEntity securityEntity) throws DataBackendException {
        try {
            Session session = retrieveSession();
            transaction = session.beginTransaction();
            session.delete(securityEntity);
            transaction.commit();
        } catch (HibernateException he) {
            try {
                transaction.rollback();
            } catch (HibernateException hex) {
            }
            throw new DataBackendException("Problem removing entity:"
                    + he.getMessage(), he);
        }
    }

    /**
     * Updates the given security object
     * @param securityEntity
     * @throws DataBackendException
     */
    public void updateEntity(SecurityEntity securityEntity) throws DataBackendException {
        try {

            Session session = retrieveSession();

            transaction = session.beginTransaction();
            session.update(securityEntity);
            transaction.commit();

        } catch (HibernateException he) {
            try {
                if (transaction != null) {
                    transaction.rollback();
                }
                if (he.getMessage().indexOf(
                        "Another object was associated with this id") > -1) {
                    //session.close();
                    updateEntity(securityEntity);
                } else {
                    throw new DataBackendException("updateEntity(" + securityEntity
                            + ")", he);
                }
            } catch (HibernateException hex) {
                log.error(hex);
                throw new DataBackendException("updateEntity(" + securityEntity
                        + ")", hex);
            }

        }
        return;
    }

    /**
     * Adds new security object to the database
     * @param securityEntity
     * @throws DataBackendException
     */
    public void addEntity(SecurityEntity securityEntity) throws DataBackendException {
        try {
            Session session = retrieveSession();
            transaction = session.beginTransaction();
            session.save(securityEntity);
            session.flush();
            transaction.commit();
        } catch (HibernateException he) {
            try {
                transaction.rollback();
            } catch (HibernateException hex) {
                log.error(hex);
            }
            throw new DataBackendException("addEntity(s,name)", he);
        }
        return;
    }


    /**
     * Retrieves the Hibernate session from the Hivemind registry
     * @return
     * @throws HibernateException
     */
    public Session retrieveSession() throws HibernateException {
      return getSessionManager().getSession();
    }

    /**
     * Pick up the Hibernate session manager from the registry
     * @return
     */
    public ISessionManager getSessionManager() {
        return iSessionManager;
    }

    /**
     * Sets the session manager. This will be called automatically
     * by HiveMind, so don't worry about it ... :-)
     * @param iSessionManager
     */
    public void setSessionManager(ISessionManager iSessionManager) {
        this.iSessionManager = iSessionManager;
    }
}
