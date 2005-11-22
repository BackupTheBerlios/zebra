package org.apache.fulcrum.hibernate.factory;

import junit.framework.TestCase;

import org.apache.fulcrum.hivemind.RegistryManager;
import org.apache.hivemind.ServiceImplementationFactory;
import org.hibernate.Session;

public class HibernateSessionFactoryTest extends TestCase {
    public void testInitialiseService() {
        
        ServiceImplementationFactory hibernateSessionFactory = (ServiceImplementationFactory) RegistryManager
                .getInstance().getRegistry().getService("fulcrum.hibernate.HibernateSessionFactory",
                        ServiceImplementationFactory.class);
        assertNotNull(hibernateSessionFactory);

        Session session = (Session) hibernateSessionFactory.createCoreServiceImplementation(null);
        assertNotNull(session);

    }
}
