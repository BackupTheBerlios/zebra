/*
 * Copyright 2004 Anite - Central Government Division
 *    http://www.anite.com/publicsector
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.anite.zebra.ext.state.hibernate;

import net.sf.hibernate.Session;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.anite.zebra.core.definitions.api.IProcessDefinition;
import com.anite.zebra.core.definitions.api.ITaskDefinition;
import com.anite.zebra.core.exceptions.LockException;
import com.anite.zebra.core.factory.api.IStateFactory;
import com.anite.zebra.core.factory.exceptions.CreateObjectException;
import com.anite.zebra.core.factory.exceptions.StateFailureException;
import com.anite.zebra.core.state.api.IFOE;
import com.anite.zebra.core.state.api.IProcessInstance;
import com.anite.zebra.core.state.api.IStateObject;
import com.anite.zebra.core.state.api.ITaskInstance;
import com.anite.zebra.core.state.api.ITransaction;

/**
 * @author Eric Pugh
 * @author Ben Gidley
 * 
 */
public abstract class HibernateStateFactory implements IStateFactory {

    private LockManager lockManager = new LockManager();

    private static Log log = LogFactory.getLog(HibernateStateFactory.class);

    public abstract Session getSession() throws StateFailureException;

    public ITransaction beginTransaction() throws StateFailureException {
        try {
            return new HibernateTransaction(getSession());
        } catch (Exception e1) {
            log.error(e1);
            throw new StateFailureException("Problem beginning transaction", e1);
        }
    }

    public IFOE createFOE(IProcessInstance processInstance)
            throws CreateObjectException {
        return new HibernateFOE(processInstance);
    }

    public IProcessInstance createProcessInstance(IProcessDefinition processDef)
            throws CreateObjectException {
        HibernateProcessInstance hpi = new HibernateProcessInstance();
        hpi.setProcessDef(processDef);
        return hpi;
    }

    public ITaskInstance createTaskInstance(ITaskDefinition taskDef,
            IProcessInstance processInstance, IFOE foe)
            throws CreateObjectException {
        HibernateTaskInstance hti = new HibernateTaskInstance();
        hti.setFOE(foe);
        hti.setProcessInstance(processInstance);
        hti.setTaskDefinition(taskDef);
        processInstance.getTaskInstances().add(hti);
        return hti;
    }

    public void deleteObject(IStateObject so) throws StateFailureException {
        if (so instanceof HibernateTaskInstance) {
            IProcessInstance processInstance = ((HibernateTaskInstance) so)
                    .getProcessInstance();
            processInstance.getTaskInstances().remove(so);
            saveObject(processInstance);
        }
        try {

            getSession().delete(so);
        } catch (Exception e1) {
            log.error(e1);
            throw new StateFailureException("Failed to delete State Object", e1);
        }

    }

    public void saveObject(IStateObject so) throws StateFailureException {
        try {
            getSession().save(so);
        } catch (Exception e1) {
            log.error(e1);
            throw new StateFailureException("Failed to save State Object", e1);
        }

    }

    public void acquireLock(IProcessInstance processInstance)
            throws LockException {
        try {
            getLockManager().aquireLockImpl(processInstance, getSession(),
                    getLockClass());

        } catch (StateFailureException e) {
            log.error("Unable to save", e);
            throw new LockException(e);
        }
    }

    /**
     * Release the lock using the lock class
     */
    public void releaseLock(IProcessInstance processInstance)
            throws LockException {
        try {
            getLockManager().releaseLockImpl(processInstance, getSession(),
                    getLockClass());
        } catch (StateFailureException e) {
            log.error("Releasing Lock should never fail ", e);
            throw new LockException(e);
        }
    }

    /**
     * Returns actual hibernate lock class
     * @return
     */
    public abstract Class getLockClass();
    
    public LockManager getLockManager() {
        return lockManager;
    }
}