package org.apache.fulcrum.hivemind;



import junit.framework.TestCase;

import org.apache.hivemind.Registry;

public class RegistryManagerTest extends TestCase {

    /*
     * Test method for 'org.apache.fulcrum.hivemind.RegistryManager.getInstance()'
     */
    public void testGetInstance() {
        RegistryManager manager = RegistryManager.getInstance();
        assertNotNull(manager);
    }

    /*
     * Test method for 'org.apache.fulcrum.hivemind.RegistryManager.getRegistry()'
     */
    public void testGetRegistry() {
        Registry registry = RegistryManager.getInstance().getRegistry();
        assertNotNull(registry);
    }

}
