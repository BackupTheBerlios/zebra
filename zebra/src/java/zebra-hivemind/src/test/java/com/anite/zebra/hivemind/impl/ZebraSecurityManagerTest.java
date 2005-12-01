/*
 * Copyright 2004, 2005 Anite 
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
package com.anite.zebra.hivemind.impl;

import junit.framework.TestCase;

import org.apache.fulcrum.hivemind.RegistryManager;
import org.apache.fulcrum.security.entity.Permission;
import org.apache.fulcrum.security.util.PermissionSet;
import org.apache.hivemind.Resource;
import org.apache.hivemind.impl.DefaultClassResolver;
import org.apache.hivemind.util.ClasspathResource;

public class ZebraSecurityManagerTest extends TestCase {

    /*
     * @see TestCase#setUp()
     */
    protected void setUp() throws Exception {

        Resource resource = new ClasspathResource(new DefaultClassResolver(),
                "META-INF/hivemodule_zebradefinitions.xml");
        RegistryManager.getInstance().getResources().add(resource);

    }

    public void testGetService() {
        ZebraSecurity security = (ZebraSecurity) RegistryManager.getInstance().getRegistry().getService(
                "zebra.zebraSecurity", ZebraSecurity.class);
        
        assertNotNull(security);
        assertNotNull(security.getPermissionManager());
    }
    
    /** 
     * Test getPermission test related methods
     * We only need to call the version using a string as the other one is call by that
     * 
     */
    public void testGetPermissionSet(){
        ZebraSecurity security = (ZebraSecurity) RegistryManager.getInstance().getRegistry().getService(
                "zebra.zebraSecurity", ZebraSecurity.class);
        
        PermissionSet results = security.getPermissionSet("bob;jack;harry");
        assertEquals(results.size(), 3);
        
        Permission[] permissions = results.getPermissionsArray();
        for (Permission permission : permissions) {
            assertNotNull(permission);
            assertNotNull(permission.getName());
        }
    }
    
    public void testGetTaskList(){
       // TODO write me
        
        // Create a user object with a permission
        
        // Start a flow with permissions on it that the user has
        
        // Check if the task is on the list
        
        // start a flow without this permission and then check it is not on the list
    }
    

}
