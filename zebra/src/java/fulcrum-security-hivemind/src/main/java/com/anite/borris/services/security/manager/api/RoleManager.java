package com.anite.borris.services.security.manager.api;

import com.anite.borris.services.security.entity.utils.DataBackendException;
import com.anite.borris.services.security.entity.utils.UnknownEntityException;
import com.anite.borris.services.security.entity.utils.RoleSet;
import com.anite.borris.services.security.entity.utils.EntityExistsException;
import com.anite.borris.services.security.entity.api.Role;


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
 * An GroupManager performs {@link } objects
 * related tasks on behalf of the {@link }.
 *
 * The responsibilities of this class include loading data of an group from the
 * storage and putting them into the {@link } objects,
 * saving those data to the permanent storage.
 *
 * @author <a href="mailto:epugh@upstate.com">Eric Pugh</a>
 * @author <a href="mailto:ray@starstream-media.com">Ray Offiah</a>
 * @version $Id: RoleManager.java,v 1.1 2005/11/10 17:29:45 bgidley Exp $
 */

public interface RoleManager {
    /**
    	* Construct a blank Role object.
    	*
    	* This method calls getRoleClass, and then creates a new object using
    	* the default constructor.
    	*
    	* @return an object implementing Role interface.
    	* @throws DataBackendException if the object could not be instantiated.
    	*/
    Role getRoleInstance(Class className) throws DataBackendException;

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
    Role getRoleInstance(String roleName, Class className) throws DataBackendException;

    /**
    	* Retrieve a Role object with specified name.
    	*
    	* @param name the name of the Role.
    	* @return an object representing the Role with specified name.
    	* @throws DataBackendException if there was an error accessing the
    	*         data backend.
    	* @throws UnknownEntityException if the role does not exist.
    	*/
    Role getRoleByName(String name) throws DataBackendException, UnknownEntityException;

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
    Role getRoleById(Object id) throws DataBackendException, UnknownEntityException;

    /**
     * Retrieves all roles defined in the system.
     *
     * @return the names of all roles defined in the system.
     * @throws DataBackendException if there was an error accessing the
     *         data backend.
     */
    RoleSet getAllRoles() throws DataBackendException;

    /**
    	* Creates a new role with specified attributes.
    	*
    	* @param role the object describing the role to be created.
    	* @return a new Role object that has id set up properly.
    	* @throws DataBackendException if there was an error accessing the data
    	*         backend.
    	* @throws EntityExistsException if the role already exists.
    	*/
    Role addRole(Role role) throws DataBackendException, EntityExistsException;

    /**
    * Removes a Role from the system.
    *
    * @param role The object describing the role to be removed.
    * @throws DataBackendException if there was an error accessing the data
    *         backend.
    * @throws UnknownEntityException if the role does not exist.
    */
    void removeRole(Role role) throws DataBackendException, UnknownEntityException;

    /**
    * Renames an existing Role.
    *
    * @param role The object describing the role to be renamed.
    * @param name the new name for the role.
    * @throws DataBackendException if there was an error accessing the data
    *         backend.
    * @throws UnknownEntityException if the role does not exist.
    */
    void renameRole(Role role, String name) throws DataBackendException, UnknownEntityException;

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
    boolean checkExists(Role role) throws DataBackendException;

    /**
      * Determines if the <code>Role</code> exists in the security system.
      *
      * @param roleName a <code>Role</code> value
      * @return true if the role name exists in the system, false otherwise
      * @throws DataBackendException when more than one Role with
      *         the same name exists.
      */
    boolean checkExists(String roleName) throws DataBackendException;
}
