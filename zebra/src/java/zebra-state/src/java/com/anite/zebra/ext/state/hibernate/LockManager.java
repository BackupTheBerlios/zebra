/*
 * Created on 09-Feb-2005
 */
package com.anite.zebra.ext.state.hibernate;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Session;
import net.sf.hibernate.Transaction;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.anite.zebra.core.exceptions.LockException;
import com.anite.zebra.core.state.api.IProcessInstance;

/**
 * Locking code extracted from StateFactory into a Helper for profiling
 * 
 * @author Ben.Gidley
 */
public class LockManager {

    private final static Log log = LogFactory.getLog(LockManager.class);

    /**
     * @param processInstance
     * @throws LockException
     */
    public void aquireLockImpl(IProcessInstance processInstance,
            Session session, Class lockClass) throws LockException {
        boolean isLocked = false;
        while (!isLocked) {
            HibernateLock lock;
            try {
                lock = (HibernateLock) session.get(lockClass, processInstance
                        .getProcessInstanceId());
                if (lock != null) {
                    session.evict(lock);
                }
            } catch (HibernateException e2) {
                log.error("Unable to test for lock", e2);
                throw new LockException(e2);
            }
            if (lock == null) {
                try {
                    Class lockClazz = lockClass;

                    lock = (HibernateLock) lockClazz.newInstance();
                    lock.setProcessInstanceId(processInstance
                            .getProcessInstanceId());

                    Transaction t = session.beginTransaction();
                    session.save(lock);
                    t.commit();
                    isLocked = true;
                } catch (HibernateException e) {
                    // It is vaguely possible someone beat us to it 
                    try {
                        lock = null;
                        Thread.sleep(10);
                    } catch (InterruptedException e1) {
                        log
                                .error(
                                        "Interupted while trying to lock - this should not occur",
                                        e1);
                        throw new LockException(e1);
                    }
                } catch (InstantiationException e) {
                    log.error("Unable to create lock class", e);
                    throw new LockException();
                } catch (IllegalAccessException e) {
                    log.error("Unable to create lock class", e);
                    throw new LockException();
                }
            } else {
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e1) {
                    log
                            .error(
                                    "Interupted while trying to lock - this should not occur",
                                    e1);
                    throw new LockException(e1);
                }
            }
        }
    }

    /**
     * @param processInstance
     * @throws LockException
     */
    public void releaseLockImpl(IProcessInstance processInstance,
            Session session, Class lockClass) throws LockException {

        try {
            HibernateLock lock = (HibernateLock) session.load(lockClass,
                    processInstance.getProcessInstanceId());
            Transaction t = session.beginTransaction();
            // Make sure we have a lock (just in case)
            session.delete(lock);
            t.commit();

        } catch (HibernateException e) {
            log.error("Releasing Lock should never fail ", e);
            throw new LockException(e);
        }
    }

}