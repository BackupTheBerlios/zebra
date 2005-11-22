package org.apache.fulcrum.hibernate;


import junit.framework.TestCase;

import org.apache.fulcrum.hivemind.RegistryManager;
import org.hibernate.Session;

public class SessionTest extends TestCase{
    
    public void testGetSession(){
        Session session = (Session) RegistryManager.getInstance().getRegistry().getService("fulcrum.hibernate.Session", Session.class);
        
        assertNotNull(session);
    }

}
