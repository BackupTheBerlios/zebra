package org.apache.fulcrum.security.model.basic.test;

/*
 *  Copyright 2001-2004 The Apache Software Foundation
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

import org.apache.fulcrum.security.AbstractSecurityServiceTest;
import org.apache.fulcrum.security.entity.Group;
import org.apache.fulcrum.security.entity.User;
import org.apache.fulcrum.security.model.basic.BasicModelManager;
import org.apache.fulcrum.security.model.basic.entity.BasicGroup;
import org.apache.fulcrum.security.model.basic.entity.BasicUser;

/**
 * This is in the main source code as implementors inherit the test
 * @author Eric Pugh
 * 
 */
public abstract class AbstractModelManagerTest extends AbstractSecurityServiceTest {
    private BasicModelManager modelManager;

    

    public void setUp() throws Exception {
        super.setUp();    
        modelManager = (BasicModelManager) getModelManager();
    }

    public void testRevokeAllUser() throws Exception {
        Group group = getGroupManager().getGroupInstance();
        group.setName("TEST_REVOKEALL");
        getGroupManager().addGroup(group);
        Group group2 = getSecurityService().getGroupManager().getGroupInstance();
        group2.setName("TEST_REVOKEALL2");
        getSecurityService().getGroupManager().addGroup(group2);
        User user = getUserManager().getUserInstance("Clint2");
        getUserManager().addUser(user, "clint");
        modelManager.grant(user, group);
        modelManager.grant(user, group2);

        modelManager.revokeAll(user);
        assertEquals(0, ((BasicUser) user).getGroups().size());
        group = getGroupManager().getGroupByName("TEST_REVOKEALL");
        group2 = getGroupManager().getGroupByName("TEST_REVOKEALL2");
        assertFalse(((BasicGroup) group).getUsersAsSet().contains(user));
        assertFalse(((BasicGroup) group2).getUsers().contains(user));
    }

    public void testGrantUserGroup() throws Exception {
        Group group = getGroupManager().getGroupInstance();
        group.setName("TEST_GROUP");
        getGroupManager().addGroup(group);
        User user = getUserManager().getUserInstance("Clint");
        getUserManager().addUser(user, "clint");
        modelManager.grant(user, group);
        assertTrue(((BasicUser) user).getGroups().contains(group));
        assertTrue(((BasicGroup) group).getUsers().contains(user));
    }

    public void testRevokeUserGroup() throws Exception {
        Group group = getGroupManager().getGroupInstance();
        group.setName("TEST_REVOKE");
        getGroupManager().addGroup(group);
        User user = getUserManager().getUserInstance("Lima");
        getUserManager().addUser(user, "pet");
        modelManager.revoke(user, group);
        assertFalse(((BasicUser) user).getGroups().contains(group));
        assertFalse(((BasicGroup) group).getUsers().contains(user));
        user = getUserManager().getUser("Lima");
        assertFalse(((BasicUser) user).getGroups().contains(group));
    }    
}
