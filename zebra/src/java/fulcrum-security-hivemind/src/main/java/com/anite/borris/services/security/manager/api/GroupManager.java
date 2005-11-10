package com.anite.borris.services.security.manager.api;

import com.anite.borris.services.security.entity.api.Group;
import com.anite.borris.services.security.entity.utils.DataBackendException;
import com.anite.borris.services.security.entity.utils.UnknownEntityException;
import com.anite.borris.services.security.entity.utils.EntityExistsException;
import com.anite.borris.services.security.entity.utils.GroupSet;

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
 * An GroupManager performs {@link Group} objects
 * related tasks on behalf of the {@link }.
 *
 * The responsibilities of this class include loading data of an group from the
 * storage and putting them into the {@link Group} objects,
 * saving those data to the permanent storage.
 *
 * @author <a href="mailto:epugh@upstate.com">Eric Pugh</a>
 * @author <a href="mailto:ray@starstream-media.com">Ray Offiah</a>
 * @version $Id: GroupManager.java,v 1.1 2005/11/10 17:29:45 bgidley Exp $
 */

public interface GroupManager  {
    /**
    * Construct a blank Group object.
    *
    * This method calls getGroupClass, and then creates a new object using
    * the default constructor.
    *
    * @return an object implementing Group interface.
    * @throws DataBackendException if the object could not be instantiated.
    */
    Group getGroupInstance(Class className) throws DataBackendException;

    /**
    	* Construct a blank Group object.
    	*
    	* This method calls getGroupClass, and then creates a new object using
    	* the default constructor.
    	*
    	* @param groupName The name of the Group
    	*
    	* @return an object implementing Group interface.
    	*
    	* @throws DataBackendException if the object could not be instantiated.
    	*/
    Group getGroupInstance(String groupName, Class className) throws DataBackendException;

    /**
     * Retrieve a Group object with specified name.
     *
     * @param name the name of the Group.
     * @return an object representing the Group with specified name.
     * @throws DataBackendException if there was an error accessing the
     *         data backend.
     * @throws UnknownEntityException if the group does not exist.
     */
    Group getGroupByName(String name) throws DataBackendException, UnknownEntityException;

    /**
     * Retrieve a Group object with specified id.
     *
     * @param id
     *            the id of the Group.
     * @return an object representing the Group with specified id.
     * @throws DataBackendException
     *             if there was an error accessing the data backend.
     * @throws UnknownEntityException
     *             if the group does not exist.
     */
    Group getGroupById(Object id) throws DataBackendException, UnknownEntityException;

    /**
    	* Renames an existing Group.
    	*
    	* @param group The object describing the group to be renamed.
    	* @param name the new name for the group.
    	* @throws DataBackendException if there was an error accessing the data
    	*         backend.
    	* @throws UnknownEntityException if the group does not exist.
    	*/
    void renameGroup(Group group, String name) throws DataBackendException, UnknownEntityException;

    /**
    	* Removes a Group from the system.
    	*
    	* @param group The object describing the group to be removed.
    	* @throws DataBackendException if there was an error accessing the data
    	*         backend.
    	* @throws UnknownEntityException if the group does not exist.
    	*/
    void removeGroup(Group group) throws DataBackendException, UnknownEntityException;

    /**
    	* Creates a new group with specified attributes.
    	*
    	* @param group the object describing the group to be created.
    	* @return a new Group object that has id set up properly.
    	* @throws DataBackendException if there was an error accessing the data
    	*         backend.
    	* @throws EntityExistsException if the group already exists.
    	*/
    Group addGroup(Group group) throws DataBackendException, EntityExistsException;

    /**
     * Retrieves all groups defined in the system.
     *
     * @return the names of all groups defined in the system.
     * @throws DataBackendException if there was an error accessing the
     *         data backend.
     */
    GroupSet getAllGroups() throws DataBackendException;

    /**
     * Determines if the <code>Group</code> exists in the security system.
     *
     * @param groupName a <code>Group</code> value
     * @return true if the group name exists in the system, false otherwise
     * @throws DataBackendException when more than one Group with
     *         the same name exists.
     */
    boolean checkExists(String groupName) throws DataBackendException;

    /**
	* Check whether a specified group exists.
	*
	* The name is used for looking up the group
	*
	* @param  group to be checked.
	* @return true if the specified group exists
	* @throws DataBackendException if there was an error accessing
	*         the data backend.
	*/
    boolean checkExists(Group group) throws DataBackendException;
}
