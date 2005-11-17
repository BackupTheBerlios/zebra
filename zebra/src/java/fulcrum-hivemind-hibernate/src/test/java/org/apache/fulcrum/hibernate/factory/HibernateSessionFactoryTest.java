package org.apache.fulcrum.hibernate.factory;

import junit.framework.TestCase;
import net.sf.hibernate.Session;

import org.apache.fulcrum.hivemind.RegistryManager;
import org.apache.hivemind.Registry;
import org.apache.hivemind.ServiceImplementationFactory;

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
