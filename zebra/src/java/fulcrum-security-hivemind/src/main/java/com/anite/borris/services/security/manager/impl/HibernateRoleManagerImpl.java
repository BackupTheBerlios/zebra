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
import com.anite.borris.services.security.entity.api.Role;
import com.anite.borris.services.security.entity.utils.DataBackendException;
import com.anite.borris.services.security.entity.utils.EntityExistsException;
import com.anite.borris.services.security.entity.utils.RoleSet;
import com.anite.borris.services.security.entity.utils.UnknownEntityException;
import com.anite.borris.services.security.manager.api.EntityManager;

 /**
 *
 * This implementation persists to a database via Hibernate.
 *
 * @author <a href="mailto:epugh@upstate.com">Eric Pugh</a>
 * @author <a href="mailto:ray@starstream-media.com">Ray Offiah</a>
 * @version $Id: HibernateRoleManagerImpl.java,v 1.1 2005/11/10 17:29:44 bgidley Exp $
 */

public class HibernateRoleManagerImpl extends AbstractEntityManager implements com.anite.borris.services.security.manager.api.RoleManager {

    protected ISessionManager iSessionManager;
    protected EntityManager entityManager;
    protected Transaction transaction;

    /**
    	* Construct a blank Role object.
    	*
    	* This method calls getRoleClass, and then creates a new object using
    	* the default constructor.
    	*
    	* @return an object implementing Role interface.
    	* @throws DataBackendException if the object could not be instantiated.
    	*/
    public Role getRoleInstance(Class className) throws DataBackendException {
        Role role;
        try
        {
            role = (Role) className.newInstance();
        }
        catch (Exception e)
        {
            throw new DataBackendException(
                "Problem creating instance of class " + getClassName(),
                e);
        }

        return role;
    }

    /**
    	* Construct a blank Role object.
    	*
    	* This method calls getRoleClass, and then creates a new object using
    	* the default constructor.
    	*
    	* @param roleName The name of the role.
    	*
    	* @return an object implementing Role interface.
    	*
    	* @throws DataBackendException if the object could not be instantiated.
    	*/
    public Role getRoleInstance(String roleName, Class className) throws DataBackendException {
        Role role = getRoleInstance(className);
        role.setName(roleName);
        return role;
    }

    /**
    	* Retrieve a Role object with specified name.
    	*
    	* @param name the name of the Role.
    	* @return an object representing the Role with specified name.
    	* @throws DataBackendException if there was an error accessing the
    	*         data backend.
    	* @throws UnknownEntityException if the role does not exist.
    	*/
    public Role getRoleByName(String name) throws DataBackendException, UnknownEntityException {
        Role role = getAllRoles().getRoleByName(name);
        if (role == null)
        {
            throw new UnknownEntityException("The specified role does not exist");
        }
        return role;
    }

	/**
	 * Retrieve a Role object with specified id.
	 *
	 * @param id
	 *            the id of the Role.
	 * @return an object representing the Role with specified id.
	 * @throws DataBackendException
	 *             if there was an error accessing the data backend.
	 * @throws UnknownEntityException
	 *             if the role does not exist.
	 */
    public Role getRoleById(Object id) throws DataBackendException, UnknownEntityException {
		Role role = null;

		if (id != null)
			try {
				List roles =
					retrieveSession().find(
							"from " + Role.class.getName() + " sr where sr.id=?",
							id,
							Hibernate.LONG);
				if (roles.size() == 0) {
					throw new UnknownEntityException(
							"Could not find role by id " + id);
				}
				role = (Role) roles.get(0);

			} catch (HibernateException e) {
				throw new DataBackendException(
						"Error retriving role information",
						e);
			}

		return role;
	}

    /**
     * Retrieves all roles defined in the system.
     *
     * @return the names of all roles defined in the system.
     * @throws DataBackendException if there was an error accessing the
     *         data backend.
     */
    public RoleSet getAllRoles() throws DataBackendException {
        RoleSet roleSet = new RoleSet();
        try
        {
            List roles = retrieveSession().find("from " + Role.class.getName() + "");
            roleSet.add(roles);
        }
        catch (HibernateException e)
        {
            throw new DataBackendException("Error retriving role information", e);
        }
        return roleSet;
    }

    /**
    	* Creates a new role with specified attributes.
    	*
    	* @param role the object describing the role to be created.
    	* @return a new Role object that has id set up properly.
    	* @throws DataBackendException if there was an error accessing the data
    	*         backend.
    	* @throws EntityExistsException if the role already exists.
    	*/
    public Role addRole(Role role) throws DataBackendException, EntityExistsException {
        boolean roleExists = false;
        if (StringUtils.isEmpty(role.getName()))
        {
            throw new DataBackendException("Could not create a role with empty name!");
        }
        if (role.getId() != null)
        {
            throw new DataBackendException("Could not create a role with an id!");
        }
        try
        {
            roleExists = checkExists(role);
            if (!roleExists)
            {
                return persistNewRole(role);
            }
        }
        catch (Exception e)
        {
            throw new DataBackendException("addRole(Role) failed", e);
        }
        finally
        {
        }
        // the only way we could get here without return/throw tirggered
        // is that the roleExists was true.
        throw new EntityExistsException("Role '" + role + "' already exists");
    }

    /**
    * Removes a Role from the system.
    *
    * @param role The object describing the role to be removed.
    * @throws DataBackendException if there was an error accessing the data
    *         backend.
    * @throws UnknownEntityException if the role does not exist.
    */
    public void removeRole(Role role) throws DataBackendException, UnknownEntityException {
        boolean roleExists = false;
        try
        {
            roleExists = checkExists(role);
            if (roleExists)
            {
				getSecurityServiceManager().removeEntity(role);
            }
            else
            {
                throw new UnknownEntityException("Unknown role '" + role + "'");
            }
        }
        catch (Exception e)
        {
            throw new DataBackendException("removeRole(Role) failed", e);
        }
    }

    /**
    * Renames an existing Role.
    *
    * @param role The object describing the role to be renamed.
    * @param name the new name for the role.
    * @throws DataBackendException if there was an error accessing the data
    *         backend.
    * @throws UnknownEntityException if the role does not exist.
    */
    public void renameRole(Role role, String name) throws DataBackendException, UnknownEntityException {
        boolean roleExists = false;
        roleExists = checkExists(role);
        if (roleExists)
        {
            role.setName(name);
            getSecurityServiceManager().updateEntity(role);
            return;
        }
        else
        {
            throw new UnknownEntityException("Unknown role '" + role + "'");
        }
    }

    /**
	* Check whether a specified role exists.
	*
	* The name is used for looking up the role
	*
	* @param role The role to be checked.
	* @return true if the specified role exists
	* @throws DataBackendException if there was an error accessing
	*         the data backend.
	*/
    public boolean checkExists(Role role) throws DataBackendException {
        return checkExists(role.getName());
    }

    /**
      * Determines if the <code>Role</code> exists in the security system.
      *
      * @param roleName a <code>Role</code> value
      * @return true if the role name exists in the system, false otherwise
      * @throws DataBackendException when more than one Role with
      *         the same name exists.
      */
    public boolean checkExists(String roleName) throws DataBackendException {
        List roles;
        try
        {

            roles = retrieveSession().find("from " + Role.class.getName() + " sr where sr.name=?", roleName, Hibernate.STRING);

        }
        catch (HibernateException e)
        {
            throw new DataBackendException("Error retriving role information", e);
        }
        if (roles.size() > 1)
        {
            throw new DataBackendException("Multiple roles with same name '" + roleName + "'");
        }
        return (roles.size() == 1);
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
    * Creates a new role with specified attributes.
    *
    * @param role the object describing the role to be created.
    * @return a new Role object that has id set up properly.
    * @throws DataBackendException if there was an error accessing the data
    *         backend.
    * @throws DataBackendException if the role already exists.
    */
    protected synchronized Role persistNewRole(Role role) throws DataBackendException
    {

		getSecurityServiceManager().addEntity(role);
        return role;
    }
}
