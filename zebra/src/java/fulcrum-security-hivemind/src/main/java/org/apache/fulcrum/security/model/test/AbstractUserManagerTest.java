package org.apache.fulcrum.security.model.test;

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
import org.apache.fulcrum.security.acl.AccessControlList;
import org.apache.fulcrum.security.entity.User;
import org.apache.fulcrum.security.util.EntityExistsException;
import org.apache.fulcrum.security.util.PasswordMismatchException;
import org.apache.fulcrum.security.util.UnknownEntityException;
import org.apache.fulcrum.security.util.UserSet;

/**
 * @author Eric Pugh
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public abstract class AbstractUserManagerTest extends AbstractSecurityServiceTest {

    public void testCheckExists() throws Exception {
        User user = getUserManager().getUserInstance("Philip");
        getUserManager().addUser(user, "bobo");
        assertTrue(getUserManager().checkExists("philip"));
        assertTrue(getUserManager().checkExists(user));
        assertFalse(getUserManager().checkExists("ImaginaryFriend"));
        user = getUserManager().getUserInstance("ImaginaryFriend");
        assertFalse(getUserManager().checkExists(user));
    }

    public void testCheckExistsWithString() throws Exception {
        User user = getUserManager().getUserInstance("Philip2");
        getUserManager().addUser(user, "bobo");
        assertTrue(getUserManager().checkExists("philip2"));
        assertTrue(getUserManager().checkExists(user.getName()));
        assertFalse(getUserManager().checkExists("ImaginaryFriend2"));
        user = getUserManager().getUserInstance("ImaginaryFriend2");
        assertFalse(getUserManager().checkExists(user.getName()));
    }

    /*
     * Class to test for User retrieve(String)
     */
    public void testGetUserString() throws Exception {
        User user = getUserManager().getUserInstance("QuietMike");
        getUserManager().addUser(user, "bobo");
        user = getUserManager().getUser("QuietMike");
        assertNotNull(user);
    }

    public void testGetUserById() throws Exception {
        User user = getUserManager().getUserInstance("QuietMike2");
        getUserManager().addUser(user, "bobo");
        User user2 = getUserManager().getUserById(user.getId());
        assertEquals(user.getName(), user2.getName());
        assertEquals(user.getId(), user2.getId());
    }

    /*
     * Class to test for User retrieve(String, String)
     */
    public void testGetUserStringString() throws Exception {
        User user = getUserManager().getUserInstance("Richard");
        getUserManager().addUser(user, "va");
        user = getUserManager().getUser("Richard", "va");
        assertNotNull(user);
        user = getUserManager().getUser("richard", "va");
        assertNotNull(user);
        try {
            user = getUserManager().getUser("richard", "VA");
            fail("should have thrown PasswordMismatchException");
        } catch (PasswordMismatchException pme) {
            //good
        }
    }

    public void testGetAllUsers() throws Exception {
        int size = getUserManager().getAllUsers().size();
        User user = getUserManager().getUserInstance("Bob");
        getUserManager().addUser(user, "");
        UserSet userSet = getUserManager().getAllUsers();
        assertEquals(size + 1, userSet.size());
    }

    public void testAuthenticate() throws Exception {
        User user = getUserManager().getUserInstance("Kay");
        getUserManager().addUser(user, "jc");
        getUserManager().authenticate(user, "jc");
        try {
            getUserManager().authenticate(user, "JC");
            fail("should have thrown PasswordMismatchException");
        } catch (PasswordMismatchException pme) {
            //good
        }
    }

    public void testChangePassword() throws Exception {
        User user = getUserManager().getUserInstance("Jonathan");
        getUserManager().addUser(user, "jc");
        try {
            getUserManager().changePassword(user, "WrongPWD", "JC");
            fail("should have thrown PasswordMismatchException");
        } catch (PasswordMismatchException pme) {
            //good
        }
        getUserManager().changePassword(user, "jc", "JC");
        getUserManager().authenticate(user, "JC");
    }

    public void testForcePassword() throws Exception {
        User user = getUserManager().getUserInstance("Connor");
        getUserManager().addUser(user, "jc_subset");
        getUserManager().forcePassword(user, "JC_SUBSET");
        getUserManager().authenticate(user, "JC_SUBSET");
    }

    /*
     * Class to test for User getUserInstance()
     */
    public void testGetUserInstance() throws Exception {
        User user = getUserManager().getUserInstance();
        assertNotNull(user);
        assertTrue(user.getName() == null);
    }

    /*
     * Class to test for User getUserInstance(String)
     */
    public void testGetUserInstanceString() throws Exception {
        User user = getUserManager().getUserInstance("Philip");
        assertEquals("philip", user.getName());
    }

    /**
     * Need to figure out if save is something we want..
     * right now it just bloes up if you actually cahnge anything.
     * @todo figur out what to do here...
     * @throws Exception
     */
    public void testSaveUser() throws Exception {
        User user = getUserManager().getUserInstance("Kate");
        getUserManager().addUser(user, "katiedid");
        user = getUserManager().getUser(user.getName());
        // user.setName("Katherine");
        getUserManager().saveUser(user);
        assertEquals("kate", getUserManager().getUser(user.getName()).getName());
    }

    public void testGetACL() throws Exception {
        User user = getUserManager().getUserInstance("Tony");
        getUserManager().addUser(user, "california");
        AccessControlList acl = getUserManager().getACL(user);
        assertNotNull(acl);
    }

    public void testRemoveUser() throws Exception {
        User user = getUserManager().getUserInstance("Rick");
        getUserManager().addUser(user, "nb");
        getUserManager().removeUser(user);
        try {
            User user2 = getUserManager().getUser(user.getName());
            fail("Should have thrown UEE");
        } catch (UnknownEntityException uee) {
            //good
        }
    }

    public void testAddUser() throws Exception {
        User user = getUserManager().getUserInstance("Joe1");
        assertNull(user.getId());
        getUserManager().addUser(user, "mc");
        user = getUserManager().getUserInstance("Joe2");
        assertNull(user.getId());
        getUserManager().addUser(user, "mc");
        assertNotNull(user.getId());
        assertNotNull(getUserManager().getUser(user.getName()));
    }

    /*
     * Class to test for boolean checkExists(string)
     */
    public void testAddUserTwiceFails() throws Exception {
        User user = getUserManager().getUserInstance("EATLUNCH");
        getUserManager().addUser(user, "bob");
        assertTrue(getUserManager().checkExists(user.getName()));
        User user2 = getUserManager().getUserInstance("EATLUNCH");
        try {
            getUserManager().addUser(user2, "bob");
        } catch (EntityExistsException uee) {
            //good
        }
        try {
            getUserManager().addUser(user2, "differentpassword");
        } catch (EntityExistsException uee) {
            //good
        }
    }

    public void testCheckUserCaseSensitiveExists() throws Exception {
        User user = getUserManager().getUserInstance("borrisJohnson");
        getUserManager().addUser(user, "bob");

        assertTrue(getUserManager().checkExists("borrisJohnson"));
    }

}
