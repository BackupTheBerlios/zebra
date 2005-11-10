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

import java.util.List;

import net.sf.hibernate.Hibernate;
import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Session;
import net.sf.hibernate.Transaction;

import org.apache.commons.lang.StringUtils;

import com.anite.borris.services.hibernate.api.ISessionManager;
import com.anite.borris.services.security.entity.api.Permission;
import com.anite.borris.services.security.entity.utils.DataBackendException;
import com.anite.borris.services.security.entity.utils.EntityExistsException;
import com.anite.borris.services.security.entity.utils.PermissionSet;
import com.anite.borris.services.security.entity.utils.UnknownEntityException;
import com.anite.borris.services.security.manager.api.EntityManager;
import com.anite.borris.services.security.manager.api.PermissionManager;

/**
 * This implementation persists to a database via Hibernate.
 *
 * @author <a href="mailto:epugh@upstate.com">Eric Pugh</a>
 * @author <a href="mailto:ray@starstream-media.com">Ray Offiah</a>
 * @version $Id: HibernatePermissionManagerImpl.java,v 1.1 2005/11/10 17:29:44 bgidley Exp $
 */

public class HibernatePermissionManagerImpl extends AbstractEntityManager implements PermissionManager {

    protected ISessionManager iSessionManager;
    protected EntityManager entityManager;
    protected Transaction transaction;

    /**
	 * Construct a blank Permission object.
	 *
	 * This method calls getPermissionClass, and then creates a new object using the default
	 * constructor.
	 *
	 * @return an object implementing Permission interface.
	 * @throws UnknownEntityException if the object could not be instantiated.
	 */
    public Permission getPermissionInstance(Class className) throws UnknownEntityException {
        Permission permission;
        try
        {
			permission = (Permission) className.newInstance();
        }
        catch (Exception e)
        {
            throw new UnknownEntityException(
                "Failed to instantiate a Permission implementation object",
                e);
        }
        return permission;
    }

    /**
	 * Construct a blank Permission object.
	 *
	 * This method calls getPermissionClass, and then creates a new object using the default
	 * constructor.
	 *
	 * @param name The name of the permission.
	 *
	 * @return an object implementing Permission interface.
	 * @throws UnknownEntityException if the object could not be instantiated.
	 */
    public Permission getPermissionInstance(String name, Class className) throws UnknownEntityException {
        Permission perm = getPermissionInstance(className);
        perm.setName(name);
        return perm;
    }

    /**
	 * Retrieve a Permission object with specified name.
	 *
	 * @param name the name of the Permission.
	 * @return an object representing the Permission with specified name.
	 * @throws DataBackendException if there was an error accessing the data backend.
	 * @throws UnknownEntityException if the permission does not exist.
	 */
    public Permission getPermissionByName(String name) throws DataBackendException, UnknownEntityException {
        Permission permission = getAllPermissions().getPermissionByName(name);
        if (permission == null)
        {
            throw new UnknownEntityException("The specified permission does not exist");
        }
        return permission;    }

	/**
	 * Retrieve a Permission object with specified id.
	 *
	 * @param id
	 *            the id of the Permission.
	 * @return an object representing the Permission with specified id.
	 * @throws DataBackendException
	 *             if there was an error accessing the data backend.
	 * @throws UnknownEntityException
	 *             if the permission does not exist.
	 */
    public Permission getPermissionById(Object id) throws DataBackendException, UnknownEntityException {
		Permission permission = null;

		if (id != null)
			try {
				List permissions =
					retrieveSession().find(
							"from " + Permission.class.getName() + " sp where sp.id=?",
							id,
							Hibernate.LONG);
				if (permissions.size() == 0) {
					throw new UnknownEntityException(
							"Could not find permission by id " + id);
				}
				permission = (Permission) permissions.get(0);

			} catch (HibernateException e) {
				throw new DataBackendException(
						"Error retriving permission information",
						e);
			}

		return permission;
	}


    /**
    * Retrieves all permissions defined in the system.
    *
    * @return the names of all roles defined in the system.
    * @throws DataBackendException if there was an error accessing the
    *         data backend.
    */
    public PermissionSet getAllPermissions() throws DataBackendException {
        PermissionSet permissionSet = new PermissionSet();
        try
        {

            List permissions = retrieveSession().find("from " + Permission.class.getName() + "");
            permissionSet.add(permissions);

        }
        catch (HibernateException e)
        {
            throw new DataBackendException("Error retriving permission information", e);
        }
        return permissionSet;
    }

    /**
	 * Creates a new permission with specified attributes.
	 *
	 * @param permission the object describing the permission to be created.
	 * @return a new Permission object that has id set up properly.
	 * @throws DataBackendException if there was an error accessing the data backend.
	 * @throws EntityExistsException if the permission already exists.
	 */
    public Permission addPermission(Permission permission) throws DataBackendException, EntityExistsException {
        boolean permissionExists = false;
        if (StringUtils.isEmpty(permission.getName()))
        {
            throw new DataBackendException("Could not create a permission with empty name!");
        }
        if (permission.getId() != null)
        {
            throw new DataBackendException("Could not create a permission with an id!");
        }
        try
        {
            permissionExists = checkExists(permission);
            if (!permissionExists)
            {
               return persistNewPermission(permission);
            }
        }
        catch (Exception e)
        {
            throw new DataBackendException("addPermission(Permission) failed", e);
        }
        // the only way we could get here without return/throw tirggered
        // is that the permissionExists was true.
        throw new EntityExistsException("Permission '" + permission + "' already exists");
    }

    /**
     * Removes a Permission from the system.
     *
     * @param permission The object describing the permission to be removed.
     * @throws DataBackendException if there was an error accessing the data
     *         backend.
     * @throws UnknownEntityException if the permission does not exist.
     */
    public synchronized void removePermission(Permission permission) throws DataBackendException, UnknownEntityException {
        boolean permissionExists = false;
        permissionExists = checkExists(permission);
        if (permissionExists)
        {
			getSecurityServiceManager().removeEntity(permission);
        }
        else
        {
            throw new UnknownEntityException("Unknown permission '" + permission + "'");
        }
    }

    /**
    * Renames an existing Permission.
    *
    * @param permission The object describing the permission to be renamed.
    * @param name the new name for the permission.
    * @throws DataBackendException if there was an error accessing the data
    *         backend.
    * @throws UnknownEntityException if the permission does not exist.
    */
    public void renamePermission(Permission permission, String name) throws DataBackendException, UnknownEntityException {
        boolean permissionExists = false;
        permissionExists = checkExists(permission);
        if (permissionExists)
        {
            permission.setName(name);
			getSecurityServiceManager().updateEntity(permission);
            return;
        }
        else
        {
            throw new UnknownEntityException("Unknown permission '" + permission + "'");
        }
    }

    /**
	* Check whether a specifieds permission exists.
	*
	* The name is used for looking up the permission
	*
	* @param  permission to be checked.
	* @return true if the specified permission exists
	* @throws DataBackendException if there was an error accessing
	*         the data backend.
	*/
    public boolean checkExists(Permission permission) throws DataBackendException {
        return checkExists(permission.getName());
    }

    /**
    * Determines if the <code>Permission</code> exists in the security system.
    *
    * @param permissionName a <code>Permission</code> value
    * @return true if the permission name exists in the system, false otherwise
    * @throws DataBackendException when more than one Permission with
    */
    public boolean checkExists(String permissionName) throws DataBackendException {
        List permissions;
        try
        {

            permissions =
			retrieveSession().find("from " + Permission.class.getName() + " sp where sp.name=?", permissionName, Hibernate.STRING);

        }
        catch (HibernateException e)
        {
            throw new DataBackendException("Error retriving permission information", e);
        }
        if (permissions.size() > 1)
        {
            throw new DataBackendException("Multiple permissions with same name '" + permissionName + "'");
        }
        return (permissions.size() == 1);
    }

    /**
     * Retrieves the Hibernate session from the Hivemind registry
     * @return
     * @throws net.sf.hibernate.HibernateException
     */
    public Session retrieveSession() throws HibernateException {
      return getSessionManager().getSession();
    }

    /**
     * Pick up the Hibernate session manager from the registry
     * @return
     */
    public ISessionManager getSessionManager() {
        return iSessionManager;
    }

    /**
     * Sets the session manager. This will be called automatically
     * by HiveMind, so don't worry about it ... :-)
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

    /**
    * Creates a new permission with specified attributes.
    *
    * @param permission the object describing the permission to be created.
    * @return a new Permission object that has id set up properly.
    * @throws DataBackendException if there was an error accessing the data
    *         backend.
    * @throws DataBackendException if the permission already exists.
    */
    protected synchronized Permission persistNewPermission(Permission permission)
        throws DataBackendException
    {

		getSecurityServiceManager().addEntity(permission);
        return permission;
    }

}
