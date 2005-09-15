/*
 * Created on Jul 8, 2004
 *
 */
package com.anite.zebra.avalon;

import org.apache.fulcrum.testcontainer.BaseUnitTest;

import com.anite.zebra.avalon.api.IAvalonDefsFactory;
import com.anite.zebra.core.api.IEngine;
import com.anite.zebra.core.factory.api.IStateFactory;


/**
 * @author Steve.Cowx
 *
 * Test the remote service impl
 */
public class StartingZebraComponentsTest extends BaseUnitTest {

    
    /**
     * @param arg0
     */
    public StartingZebraComponentsTest(String arg0) {
        super(arg0);
    }
    

    public void testStartingServices() throws Exception
    {
        IStateFactory service = (IStateFactory)lookup(IStateFactory.class.getName());
        assertNotNull(service);
        IEngine engine = (IEngine)lookup(IEngine.class.getName());
        assertNotNull(engine);
        IAvalonDefsFactory defsFactory = (IAvalonDefsFactory)lookup(IAvalonDefsFactory.class.getName());
        
        this.release(defsFactory);
   
    }
}
