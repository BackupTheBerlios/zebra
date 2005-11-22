/*
 * Created on 24-May-2005
 */
package org.apache.fulcrum.hibernate.factory;

import org.apache.commons.lang.exception.NestableRuntimeException;
import org.apache.hivemind.ServiceImplementationFactory;
import org.apache.hivemind.ServiceImplementationFactoryParameters;
import org.apache.hivemind.events.RegistryShutdownListener;
import org.apache.hivemind.service.ThreadCleanupListener;
import org.apache.hivemind.service.ThreadEventNotifier;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.tool.hbm2ddl.SchemaExport;
import org.hibernate.tool.hbm2ddl.SchemaUpdate;

/**
 * Creates the Hibernate Session and manages the lifecycle.
 * 
 * The application simply gets the HibernateSession as a service on a Threaded model. 
 * 
 * @author Mike Jones
 * @author ben.gidley
 *
 */
public class HibernateSessionFactory implements ServiceImplementationFactory, RegistryShutdownListener {

    private SessionFactory sessionFactory;

    private ThreadEventNotifier threadEventNotifier;

    private boolean updateSchema = false;
    private boolean createSchema = false;

    /**
     * Called by factory when creating service
     *
     */
    public void initializeService() {
        try {
            Configuration config = new Configuration();
            config.configure();

            if (createSchema){ 
                SchemaExport export = new SchemaExport(config);
                export.drop(true, true);
                export.create(true, true);
            }
            else if (updateSchema) {
                new SchemaUpdate(config).execute(true, true);
            }
            sessionFactory = config.buildSessionFactory();

        } catch (HibernateException e) {
            throw new NestableRuntimeException(e);
        }
    }

    /*
     *  (non-Javadoc)
     * @see org.apache.hivemind.ServiceImplementationFactory#createCoreServiceImplementation(org.apache.hivemind.ServiceImplementationFactoryParameters)
     */
    public Object createCoreServiceImplementation(ServiceImplementationFactoryParameters arg0) {
        try {
            Session session = sessionFactory.openSession();
            threadEventNotifier.addThreadCleanupListener(new SessionCloser(session));
            return session;
        } catch (HibernateException e) {
            throw new NestableRuntimeException(e);
        }

    }

    /*
     *  (non-Javadoc)
     * @see org.apache.hivemind.events.RegistryShutdownListener#registryDidShutdown()
     */
    public void registryDidShutdown() {
        try {
            sessionFactory.close();
        } catch (HibernateException e) {
            throw new NestableRuntimeException(e);
        }
    }

    public void setThreadEventNotifier(ThreadEventNotifier notifier) {
        this.threadEventNotifier = notifier;
    }

    public void setUpdateSchema(boolean updateSchema) {
        this.updateSchema = updateSchema;
    }

    private class SessionCloser implements ThreadCleanupListener {
        private final Session session;

        public SessionCloser(Session session) {
            this.session = session;
        }

        public void threadDidCleanup() {
            try {
                session.close();
            } catch (HibernateException e) {
                throw new NestableRuntimeException(e);
            }
            threadEventNotifier.removeThreadCleanupListener(this);
        }
    }

    public boolean isCreateSchema() {
        return createSchema;
    }

    public void setCreateSchema(boolean createSchema) {
        this.createSchema = createSchema;
    }

}
