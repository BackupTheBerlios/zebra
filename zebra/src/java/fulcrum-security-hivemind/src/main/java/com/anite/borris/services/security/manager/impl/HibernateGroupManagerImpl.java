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
import com.anite.borris.services.security.entity.api.Group;
import com.anite.borris.services.security.entity.utils.DataBackendException;
import com.anite.borris.services.security.entity.utils.EntityExistsException;
import com.anite.borris.services.security.entity.utils.GroupSet;
import com.anite.borris.services.security.entity.utils.UnknownEntityException;
import com.anite.borris.services.security.manager.api.EntityManager;
import com.anite.borris.services.security.manager.api.GroupManager;

/**
 * This implementation persists to a database via Hibernate.
 *
 * @author <a href="mailto:epugh@upstate.com">Eric Pugh</a>
 * @author <a href="mailto:ray@starstream-media.com">Ray Offiah</a>
 * @version $Id: HibernateGroupManagerImpl.java,v 1.1 2005/11/10 17:29:44 bgidley Exp $
 */
public class HibernateGroupManagerImpl extends AbstractEntityManager implements
        GroupManager {

    protected ISessionManager iSessionManager;

    protected EntityManager entityManager;

    protected Transaction transaction;

    /**
     * Construct a blank Group object.
     *
     * This method calls getGroupClass, and then creates a new object using
     * the default constructor.
     *
     * @return an object implementing Group interface.
     * @throws DataBackendException if the object could not be instantiated.
     */
    public Group getGroupInstance(Class className) throws DataBackendException {
        Group group;
        try {
            group = (Group) className.newInstance();
        } catch (Exception e) {
            throw new DataBackendException(
                    "Problem creating instance of class " + getClassName(), e);
        }

        return group;
    }

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
    public Group getGroupInstance(String groupName, Class className)
            throws DataBackendException {
        Group group;

        group = getGroupInstance(className);
        group.setName(groupName);
        return group;
    }

    /**
     * Retrieve a Group object with specified name.
     *
     * @param name the name of the Group.
     * @return an object representing the Group with specified name.
     * @throws DataBackendException if there was an error accessing the
     *         data backend.
     * @throws UnknownEntityException if the group does not exist.
     */
    public Group getGroupByName(String name) throws DataBackendException,
            UnknownEntityException {
        Group group = null;
        try {

            List groups = retrieveSession().find(
                    "from " + Group.class.getName() + " g where g.name=?",
                    name.toLowerCase(), Hibernate.STRING);
            if (groups.size() == 0) {
                throw new UnknownEntityException("Could not find group" + name);
            }
            group = (Group) groups.get(0);
            //session.close();
        } catch (HibernateException e) {
            throw new DataBackendException("Error retriving group information",
                    e);
        }
        return group;
    }

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
    public Group getGroupById(Object id) throws DataBackendException,
            UnknownEntityException {
        Group group = null;

        if (id != null)
            try {
                List groups = retrieveSession().find(
                        "from " + Group.class.getName() + " sr where sr.id=?",
                        id, Hibernate.LONG);
                if (groups.size() == 0) {
                    throw new UnknownEntityException(
                            "Could not find group by id " + id);
                }
                group = (Group) groups.get(0);

            } catch (HibernateException e) {
                throw new DataBackendException(
                        "Error retriving group information", e);
            }

        return group;
    }

    /**
     * Renames an existing Group.
     *
     * @param group The object describing the group to be renamed.
     * @param name the new name for the group.
     * @throws DataBackendException if there was an error accessing the data
     *         backend.
     * @throws UnknownEntityException if the group does not exist.
     */
    public void renameGroup(Group group, String name)
            throws DataBackendException, UnknownEntityException {
        boolean groupExists = false;
        groupExists = checkExists(group);
        if (groupExists) {
            group.setName(name);
            getSecurityServiceManager().updateEntity(group);
        } else {
            throw new UnknownEntityException("Unknown group '" + group + "'");
        }
    }

    /**
     * Removes a Group from the system.
     *
     * @param group The object describing the group to be removed.
     * @throws DataBackendException if there was an error accessing the data
     *         backend.
     * @throws UnknownEntityException if the group does not exist.
     */
    public synchronized void removeGroup(Group group)
            throws DataBackendException, UnknownEntityException {

        getSecurityServiceManager().removeEntity(group);
    }

    /**
     * Creates a new group with specified attributes.
     *
     * @param group the object describing the group to be created.
     * @return a new Group object that has id set up properly.
     * @throws DataBackendException if there was an error accessing the data
     *         backend.
     * @throws EntityExistsException if the group already exists.
     */
    public Group addGroup(Group group) throws DataBackendException,
            EntityExistsException {
        boolean groupExists = false;
        if (StringUtils.isEmpty(group.getName())) {
            throw new DataBackendException(
                    "Could not create a group with empty name!");
        }
        if (group.getId() != null) {
            throw new DataBackendException(
                    "Could not create a group with an id!");
        }
        groupExists = checkExists(group);
        if (!groupExists) {

            // return the object with correct id
            return persistNewGroup(group);
        } else {
            throw new EntityExistsException("Group '" + group
                    + "' already exists");
        }
    }

    /**
     * Retrieves all groups defined in the system.
     *
     * @return the names of all groups defined in the system.
     * @throws DataBackendException if there was an error accessing the
     *         data backend.
     */
    public GroupSet getAllGroups() throws DataBackendException {
        GroupSet groupSet = new GroupSet();
        try {

            List groups = retrieveSession().find(
                    "from " + Group.class.getName() + "");
            groupSet.add(groups);
        } catch (HibernateException e) {
            throw new DataBackendException("Error retriving group information",
                    e);
        }
        return groupSet;
    }

    /**
     * Determines if the <code>Group</code> exists in the security system.
     *
     * @param groupName a <code>Group</code> value
     * @return true if the group name exists in the system, false otherwise
     * @throws DataBackendException when more than one Group with
     *         the same name exists.
     */
    public boolean checkExists(String groupName) throws DataBackendException {
        List groups;
        try {

            groups = retrieveSession().find(
                    "from " + Group.class.getName() + " sg where sg.name=?",
                    groupName, Hibernate.STRING);
        } catch (HibernateException e) {
            throw new DataBackendException("Error retriving user information",
                    e);
        }
        if (groups.size() > 1) {
            throw new DataBackendException("Multiple groups with same name '"
                    + groupName + "'");
        }
        return (groups.size() == 1);
    }

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
    public boolean checkExists(Group group) throws DataBackendException {
        return checkExists(group.getName());
    }

    /**
     * Retrieves the Hibernate session from the Hivemind registry
     * @return
     * @throws net.sf.hibernate.HibernateException
     */
    public Session retrieveSession() throws HibernateException {
        return getiSessionManager().getSession();
    }

    public EntityManager getSecurityServiceManager() {
        return entityManager;
    }

    public void setSecurityManager(EntityManager securityManager) {
        this.entityManager = securityManager;
    }

    /**
     * Creates a new group with specified attributes.
     *
     * @param group the object describing the group to be created.
     * @return a new Group object that has id set up properly.
     * @throws DataBackendException if there was an error accessing the data
     *         backend.
     * @throws DataBackendException if the group already exists.
     */
    protected synchronized Group persistNewGroup(Group group)
            throws DataBackendException {

        getSecurityServiceManager().addEntity(group);
        return group;
    }

    public ISessionManager getiSessionManager() {
        return iSessionManager;
    }

    public void setiSessionManager(ISessionManager iSessionManager) {
        this.iSessionManager = iSessionManager;
    }
}