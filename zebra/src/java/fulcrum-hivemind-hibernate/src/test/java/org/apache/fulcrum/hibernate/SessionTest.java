package org.apache.fulcrum.hibernate;


import junit.framework.TestCase;
import net.sf.hibernate.Session;

import org.apache.fulcrum.hivemind.RegistryManager;

public class SessionTest extends TestCase{
    
    public void testGetSession(){
        Session session = (Session) RegistryManager.getInstance().getRegistry().getService("fulcrum.hibernate.Session", Session.class);
        
        assertNotNull(session);
    }

}
