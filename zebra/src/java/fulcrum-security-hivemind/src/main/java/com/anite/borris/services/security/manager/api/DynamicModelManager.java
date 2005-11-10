package com.anite.borris.services.security.manager.api;

import com.anite.borris.services.security.entity.api.Group;
import com.anite.borris.services.security.entity.api.Permission;
import com.anite.borris.services.security.entity.api.User;
import com.anite.borris.services.security.entity.api.Role;
import com.anite.borris.services.security.entity.utils.DataBackendException;
import com.anite.borris.services.security.entity.utils.UnknownEntityException;


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


/**
 * Describes all the relationships between entities in the "Dynamic" model.
 *
 * @author <a href="mailto:epugh@upstate.com">Eric Pugh</a>
 * @author <a href="mailto:ben@gidley.co.uk">Ben Gidley</a>
 * @author <a href="mailto:ray@starstream-media.com">Ray Offiah</a>
 * @version $Id: DynamicModelManager.java,v 1.1 2005/11/10 17:29:45 bgidley Exp $
 */
public interface DynamicModelManager {
    /**
     * Grants a Group a Role
     *
     * @param group the Group.
     * @param role  the Role.
     * @throws DataBackendException   if there was an error accessing the data backend.
     * @throws UnknownEntityException if group or role is not present.
     */
    void grant(Group group, Role role) throws DataBackendException, UnknownEntityException;

    /**
     * Revokes a Role from a Group.
     *
     * @param group the Group.
     * @param role  the Role.
     * @throws DataBackendException   if there was an error accessing the data backend.
     * @throws UnknownEntityException if group or role is not present.
     */
    void revoke(Group group, Role role) throws DataBackendException, UnknownEntityException;

    /**
     * Grants a Role a Permission
     *
     * @param role       the Role.
     * @param permission the Permission.
     * @throws DataBackendException   if there was an error accessing the data backend.
     * @throws UnknownEntityException if role or permission is not present.
     */
    void grant(Role role, Permission permission) throws DataBackendException, UnknownEntityException;

    /**
     * Revokes a Permission from a Role.
     *
     * @param role       the Role.
     * @param permission the Permission.
     * @throws DataBackendException   if there was an error accessing the data backend.
     * @throws UnknownEntityException if role or permission is not present.
     */
    void revoke(Role role, Permission permission) throws DataBackendException, UnknownEntityException;

    /**
     * Puts a user in a group.
     * <p/>
     * This method is used when adding a user to a group
     *
     * @param user the User.
     * @throws DataBackendException   if there was an error accessing the data backend.
     * @throws UnknownEntityException if the account is not present.
     */
    void grant(User user, Group group) throws DataBackendException, UnknownEntityException;

    /**
     * Removes a user in a group.
     * <p/>
     * This method is used when removing a user to a group
     *
     * @param user the User.
     * @throws DataBackendException   if there was an error accessing the data backend.
     * @throws UnknownEntityException if the user or group is not present.
     */
    void revoke(User user, Group group) throws DataBackendException, UnknownEntityException;

    /**
	 * Revokes all groups from a user
	 *
	 * This method is used when deleting an account.
	 *
	 * @param user
	 *            the User.
	 * @throws DataBackendException
	 *             if there was an error accessing the data backend.
	 * @throws UnknownEntityException
	 *             if the account is not present.
	 */
    void revokeAll(User user) throws DataBackendException, UnknownEntityException;

    /**
	 * Revokes all roles from a permission
	 *
	 * This method is used when deleting a permission.
	 *
	 * @param permission
	 *            the permission.
	 * @throws DataBackendException
	 *             if there was an error accessing the data backend.
	 * @throws UnknownEntityException
	 *             if the account is not present.
	 */
    void revokeAll(Permission permission) throws DataBackendException, UnknownEntityException;

    /**
	 * Revokes all permissions and groups from a Role.
	 *
	 * This method is used when deleting a Role.
	 *
	 * @param role
	 *            the Role
	 * @throws DataBackendException
	 *             if there was an error accessing the data backend.
	 * @throws UnknownEntityException
	 *             if the Role is not present.
	 */
    void revokeAll(Role role) throws DataBackendException, UnknownEntityException;

    /**
	 * Revokes all users and roles from a group
	 *
	 * This method is used when deleting a group.
	 *
	 * @param group
	 *            the Group.
	 * @throws DataBackendException
	 *             if there was an error accessing the data backend.
	 * @throws UnknownEntityException
	 *             if the account is not present.
	 */
    void revokeAll(Group group) throws DataBackendException, UnknownEntityException;

    /**
	 * It is expected the real implementation will overide this and save either
	 * side of the function. It is not abstract as a in memory implementation
	 * would not need to do anything.
	 */
    void addDelegate(User delegator, User delegatee) throws DataBackendException, UnknownEntityException;

    /**
	 * Implementors should overide this to save and call super if
	 * they want the base class to do the work
	 */
    void removeDelegate(User delegator, User delegatee) throws DataBackendException, UnknownEntityException;
}
