package com.anite.borris.services.security.manager.impl;

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

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Session;
import net.sf.hibernate.Transaction;

import com.anite.borris.services.hibernate.api.ISessionManager;
import com.anite.borris.services.security.entity.api.Group;
import com.anite.borris.services.security.entity.api.Permission;
import com.anite.borris.services.security.entity.api.Role;
import com.anite.borris.services.security.entity.api.User;
import com.anite.borris.services.security.entity.impl.DynamicGroup;
import com.anite.borris.services.security.entity.impl.DynamicPermission;
import com.anite.borris.services.security.entity.impl.DynamicRole;
import com.anite.borris.services.security.entity.impl.DynamicUser;
import com.anite.borris.services.security.entity.utils.DataBackendException;
import com.anite.borris.services.security.entity.utils.UnknownEntityException;
import com.anite.borris.services.security.manager.api.EntityManager;
import com.anite.borris.services.security.manager.api.GroupManager;
import com.anite.borris.services.security.manager.api.PermissionManager;
import com.anite.borris.services.security.manager.api.RoleManager;
import com.anite.borris.services.security.manager.api.UserManager;



/**
 * @author <a href="mailto:epugh@upstate.com">Eric Pugh</a>
 * @author <a href="mailto:ray@starstream-media.com">Ray Offiah</a>
 * @version $Id: HibernateDynamicModelManagerImpl.java,v 1.1 2005/11/10 17:29:44 bgidley Exp $
 */
public class HibernateDynamicModelManagerImpl extends AbstractEntityManager implements com.anite.borris.services.security.manager.api.DynamicModelManager {

    protected ISessionManager iSessionManager;
    protected EntityManager entityManager;
    protected Transaction transaction;

    protected GroupManager groupManager;
    protected RoleManager roleManager;
    protected PermissionManager permissionManager;
    protected UserManager userManager;

    /**
     * Grants a Group a Role
     *
     * @param group the Group.
     * @param role  the Role.
     * @throws DataBackendException   if there was an error accessing the data backend.
     * @throws UnknownEntityException if group or role is not present.
     */
    public void grant(Group group, Role role) throws DataBackendException, UnknownEntityException {
        boolean groupExists = false;
        boolean roleExists = false;
        try {
            groupExists = getGroupManager().checkExists(group);
            roleExists = getRoleManager().checkExists(role);
            if (groupExists && roleExists) {
                ((DynamicGroup) group).addRole(role);
                ((DynamicRole) role).addGroup(group);
                getSecurityServiceManager().updateEntity(group);
                getSecurityServiceManager().updateEntity(role);
                return;
            }
        } catch (Exception e) {
            throw new DataBackendException("grant(Group,Role) failed", e);
        }
        if (!groupExists) {
            throw new UnknownEntityException("Unknown group '" + group.getName() + "'");
        }
        if (!roleExists) {
            throw new UnknownEntityException("Unknown role '" + role.getName() + "'");
        }
    }

    /**
     * Revokes a Role from a Group.
     *
     * @param group the Group.
     * @param role  the Role.
     * @throws DataBackendException   if there was an error accessing the data backend.
     * @throws UnknownEntityException if group or role is not present.
     */
    public void revoke(Group group, Role role) throws DataBackendException, UnknownEntityException {
        boolean groupExists = false;
        boolean roleExists = false;
        try {
            groupExists = getGroupManager().checkExists(group);
            roleExists = getRoleManager().checkExists(role);
            if (groupExists && roleExists) {
                ((DynamicGroup) group).removeRole(role);
                ((DynamicRole) role).removeGroup(group);
                getSecurityServiceManager().updateEntity(group);
                //updateEntity(role);
                return;
            }
        } catch (Exception e) {
            throw new DataBackendException("revoke(Group,Role) failed", e);
        } finally {
        }
        if (!groupExists) {
            throw new UnknownEntityException("Unknown group '" + group.getName() + "'");
        }
        if (!roleExists) {
            throw new UnknownEntityException("Unknown role '" + role.getName() + "'");
        }
    }


    /**
     * Grants a Role a Permission
     *
     * @param role       the Role.
     * @param permission the Permission.
     * @throws DataBackendException   if there was an error accessing the data backend.
     * @throws UnknownEntityException if role or permission is not present.
     */
    public void grant(Role role, Permission permission) throws DataBackendException, UnknownEntityException {
        boolean roleExists = false;
        boolean permissionExists = false;
        try {
            roleExists = getRoleManager().checkExists(role);
            permissionExists = getPermissionManager().checkExists(permission);

            if (roleExists && permissionExists) {
                ((DynamicRole) role).addPermission(permission);
                ((DynamicPermission) permission).addRole(role);
                getSecurityServiceManager().updateEntity(permission);
                getSecurityServiceManager().updateEntity(role);
                return;
            }
        } catch (Exception e) {
            throw new DataBackendException("grant(Role,Permission) failed", e);
        }
        if (!roleExists) {
            throw new UnknownEntityException("Unknown role '" + role.getName() + "'");
        }
        if (!permissionExists) {
            throw new UnknownEntityException("Unknown permission '" + permission.getName() + "'");
        }
    }


    /**
     * Revokes a Permission from a Role.
     *
     * @param role       the Role.
     * @param permission the Permission.
     * @throws DataBackendException   if there was an error accessing the data backend.
     * @throws UnknownEntityException if role or permission is not present.
     */
    public void revoke(Role role, Permission permission) throws DataBackendException, UnknownEntityException {
        boolean roleExists = false;
        boolean permissionExists = false;
        try {
            roleExists = getRoleManager().checkExists(role);
            permissionExists = getPermissionManager().checkExists(permission);
            if (roleExists && permissionExists) {
                ((DynamicRole) role).removePermission(permission);
                ((DynamicPermission) permission).removeRole(role);
                getSecurityServiceManager().updateEntity(role);
                getSecurityServiceManager().updateEntity(permission);
                return;
            }
        } catch (Exception e) {
            throw new DataBackendException("revoke(Role,Permission) failed", e);
        } finally {
        }
        if (!roleExists) {
            throw new UnknownEntityException("Unknown role '" + role.getName() + "'");
        }
        if (!permissionExists) {
            throw new UnknownEntityException("Unknown permission '" + permission.getName() + "'");
        }
    }

    /**
     * Puts a user in a group.
     * <p/>
     * This method is used when adding a user to a group
     *
     * @param user the User.
     * @throws DataBackendException   if there was an error accessing the data backend.
     * @throws UnknownEntityException if the account is not present.
     */
    public synchronized void grant(User user, Group group) throws DataBackendException, UnknownEntityException {
        boolean groupExists = false;
        boolean userExists = false;
        try {
            groupExists = getGroupManager().checkExists(group);
            userExists = getUserManager().checkExists(user);
            if (groupExists && userExists) {
                ((DynamicUser) user).addGroup(group);
                ((DynamicGroup) group).addUser(user);
                getSecurityServiceManager().updateEntity(group);
                getSecurityServiceManager().updateEntity(user);
                return;
            }
        } catch (Exception e) {
            throw new DataBackendException("grant(Role,Permission) failed", e);
        } finally {
        }
        if (!groupExists) {
            throw new UnknownEntityException("Unknown group '" + group.getName() + "'");
        }
        if (!userExists) {
            throw new UnknownEntityException("Unknown user '" + user.getName() + "'");
        }
    }

    /**
     * Removes a user in a group.
     * <p/>
     * This method is used when removing a user to a group
     *
     * @param user the User.
     * @throws DataBackendException   if there was an error accessing the data backend.
     * @throws UnknownEntityException if the user or group is not present.
     */
    public synchronized void revoke(User user, Group group) throws DataBackendException, UnknownEntityException {
        boolean groupExists = false;
        boolean userExists = false;
        try {
            groupExists = getGroupManager().checkExists(group);
            userExists = getUserManager().checkExists(user);
            if (groupExists && userExists) {

                Session session = retrieveSession();
                Transaction transaction = session.beginTransaction();
                ((DynamicUser) user).removeGroup(group);
                ((DynamicGroup) group).removeUser(user);
                session.update(user);
                session.update(group);
                transaction.commit();
                return;
            }
        } catch (Exception e) {
            throw new DataBackendException("grant(Role,Permission) failed", e);
        } finally {
        }
        if (!groupExists) {
            throw new UnknownEntityException("Unknown group '" + group.getName() + "'");
        }
        if (!userExists) {
            throw new UnknownEntityException("Unknown user '" + user.getName() + "'");
        }
    }

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
    public void revokeAll(User user) throws DataBackendException, UnknownEntityException {
        boolean userExists = false;
        userExists = getUserManager().checkExists(user);
        if (userExists) {
            Object groups[] = ((DynamicUser) user).getGroups().toArray();
            for (int i = 0; i < groups.length; i++) {
                revoke(user, (Group) groups[i]);
            }

            return;
        } else {
            throw new UnknownEntityException("Unknown user '" + user.getName()
                    + "'");
        }
    }

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
    public void revokeAll(Permission permission) throws DataBackendException, UnknownEntityException {

        boolean permissionExists = false;
        permissionExists = getPermissionManager().checkExists(permission);
        if (permissionExists) {
            Object roles[] = ((DynamicPermission) permission).getRoles()
                    .toArray();
            for (int i = 0; i < roles.length; i++) {
                revoke((Role) roles[i], permission);
            }

            return;
        } else {
            throw new UnknownEntityException("Unknown permission '"
                    + permission.getName() + "'");
        }
    }

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
    public void revokeAll(Role role) throws DataBackendException, UnknownEntityException {
        boolean roleExists = false;
        roleExists = getRoleManager().checkExists(role);
        if (roleExists) {
            Object groups[] = ((DynamicRole) role).getGroups().toArray();
            for (int i = 0; i < groups.length; i++) {
                revoke((Group) groups[i], role);
            }

            Object permissions[] = ((DynamicRole) role).getPermissions()
                    .toArray();
            for (int i = 0; i < permissions.length; i++) {
                revoke(role, (Permission) permissions[i]);
            }
        } else {
            throw new UnknownEntityException("Unknown role '" + role.getName()
                    + "'");
        }
    }

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
    public void revokeAll(Group group) throws DataBackendException, UnknownEntityException {
        boolean groupExists = false;
        groupExists = getGroupManager().checkExists(group);
        if (groupExists) {
            Object users[] = ((DynamicGroup) group).getUsers().toArray();
            for (int i = 0; i < users.length; i++) {
                revoke((User) users[i], group);
            }

            Object roles[] = ((DynamicGroup) group).getRoles().toArray();
            for (int i = 0; i < roles.length; i++) {
                revoke(group, (Role) roles[i]);
            }

            return;
        } else {
            throw new UnknownEntityException("Unknown group '"
                    + group.getName() + "'");
        }
    }

	/**
	 * It is expected the real implementation will overide this and save either
	 * side of the function. It is not abstract as a in memory implementation
	 * would not need to do anything.
	 */
    public void addDelegate(User delegator, User delegatee) throws DataBackendException, UnknownEntityException {
        DynamicUser dynamicDelegator = (DynamicUser) delegator;
        DynamicUser dynamicDelegatee = (DynamicUser) delegatee;

        // check it hasn't already been done
        // It is NOT an error to call this twice
        if (!(dynamicDelegator.getDelegatees().contains(delegatee) || dynamicDelegatee
                .getDelegators().contains(delegator))) {
            dynamicDelegator.getDelegatees().add(dynamicDelegatee);
            dynamicDelegatee.getDelegators().add(dynamicDelegator);
        }
    }

	/**
	 * Implementors should overide this to save and call super if
	 * they want the base class to do the work
	 */
    public void removeDelegate(User delegator, User delegatee) throws DataBackendException, UnknownEntityException {
        DynamicUser dynamicDelegator = (DynamicUser) delegator;
        DynamicUser dynamicDelegatee = (DynamicUser) delegatee;

        if (dynamicDelegator.getDelegatees().contains(dynamicDelegatee)){
            dynamicDelegator.getDelegatees().remove(dynamicDelegatee);
            dynamicDelegatee.getDelegators().remove(dynamicDelegator);
        }else {
            throw new UnknownEntityException("Tried to remove a delegate that does not exist");
        }

    }

    /**
     * Retrieves the Hibernate session from the Hivemind registry
     *
     * @return
     * @throws net.sf.hibernate.HibernateException
     *
     */
    public Session retrieveSession() throws HibernateException {
        return getSessionManager().getSession();
    }

    /**
     * Pick up the Hibernate session manager from the registry
     *
     * @return
     */
    public ISessionManager getSessionManager() {
        return iSessionManager;
    }

    /**
     * Sets the session manager. This will be called automatically
     * by HiveMind, so don't worry about it ... :-)
     *
     * @param iSessionManager
     */
    public void setSessionManager(ISessionManager iSessionManager) {
        this.iSessionManager = iSessionManager;
    }

    public EntityManager getSecurityServiceManager() {
        return entityManager;
    }

    public void setSecurityManager(EntityManager securityManager) {
        this.entityManager = securityManager;
    }

    public GroupManager getGroupManager() {
        return groupManager;
    }

    public void setGroupManager(GroupManager groupManager) {
        this.groupManager = groupManager;
    }

    public RoleManager getRoleManager() {
        return roleManager;
    }

    public void setRoleManager(RoleManager roleManager) {
        this.roleManager = roleManager;
    }

    public PermissionManager getPermissionManager() {
        return permissionManager;
    }

    public void setPermissionManager(PermissionManager permissionManager) {
        this.permissionManager = permissionManager;
    }

    public UserManager getUserManager() {
        return userManager;
    }

    public void setUserManager(UserManager userManager) {
        this.userManager = userManager;
    }
}
