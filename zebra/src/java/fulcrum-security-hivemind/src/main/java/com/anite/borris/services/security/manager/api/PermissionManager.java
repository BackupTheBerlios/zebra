package com.anite.borris.services.security.manager.api;

import com.anite.borris.services.security.entity.utils.UnknownEntityException;
import com.anite.borris.services.security.entity.utils.DataBackendException;
import com.anite.borris.services.security.entity.utils.PermissionSet;
import com.anite.borris.services.security.entity.utils.EntityExistsException;
import com.anite.borris.services.security.entity.api.Permission;

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
 * An GroupManager performs {@link} objects
 * related tasks on behalf of the {@link }.
 *
 * The responsibilities of this class include loading data of an group from the
 * storage and putting them into the {@link } objects,
 * saving those data to the permanent storage.
 *
 * @author <a href="mailto:epugh@upstate.com">Eric Pugh</a>
 * @author <a href="mailto:ray@starstream-media.com">Ray Offiah</a>
 * @version $Id: PermissionManager.java,v 1.1 2005/11/10 17:29:45 bgidley Exp $
 */

public interface PermissionManager {
    /**
	 * Construct a blank Permission object.
	 *
	 * This method calls getPermissionClass, and then creates a new object using the default
	 * constructor.
	 *
	 * @return an object implementing Permission interface.
	 * @throws UnknownEntityException if the object could not be instantiated.
	 */
    Permission getPermissionInstance(Class className) throws UnknownEntityException;

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
    Permission getPermissionInstance(String name, Class className) throws UnknownEntityException;

    /**
	 * Retrieve a Permission object with specified name.
	 *
	 * @param name the name of the Permission.
	 * @return an object representing the Permission with specified name.
	 * @throws DataBackendException if there was an error accessing the data backend.
	 * @throws UnknownEntityException if the permission does not exist.
	 */
    Permission getPermissionByName(String name) throws DataBackendException, UnknownEntityException;

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
    Permission getPermissionById(Object id) throws DataBackendException, UnknownEntityException;

    /**
    * Retrieves all permissions defined in the system.
    *
    * @return the names of all roles defined in the system.
    * @throws DataBackendException if there was an error accessing the
    *         data backend.
    */
    PermissionSet getAllPermissions() throws DataBackendException;

    /**
	 * Creates a new permission with specified attributes.
	 *
	 * @param permission the object describing the permission to be created.
	 * @return a new Permission object that has id set up properly.
	 * @throws DataBackendException if there was an error accessing the data backend.
	 * @throws EntityExistsException if the permission already exists.
	 */
    Permission addPermission(Permission permission) throws DataBackendException, EntityExistsException;

    /**
     * Removes a Permission from the system.
     *
     * @param permission The object describing the permission to be removed.
     * @throws DataBackendException if there was an error accessing the data
     *         backend.
     * @throws UnknownEntityException if the permission does not exist.
     */
    void removePermission(Permission permission) throws DataBackendException, UnknownEntityException;

    /**
    * Renames an existing Permission.
    *
    * @param permission The object describing the permission to be renamed.
    * @param name the new name for the permission.
    * @throws DataBackendException if there was an error accessing the data
    *         backend.
    * @throws UnknownEntityException if the permission does not exist.
    */
    void renamePermission(Permission permission, String name) throws DataBackendException, UnknownEntityException;

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
    boolean checkExists(Permission permission) throws DataBackendException;

    /**
    * Determines if the <code>Permission</code> exists in the security system.
    *
    * @param permissionName a <code>Permission</code> value
    * @return true if the permission name exists in the system, false otherwise
    * @throws DataBackendException when more than one Permission with
    */
    boolean checkExists(String permissionName) throws DataBackendException;
}
