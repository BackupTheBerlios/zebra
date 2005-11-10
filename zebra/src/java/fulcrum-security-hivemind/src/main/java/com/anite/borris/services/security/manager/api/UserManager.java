package com.anite.borris.services.security.manager.api;

import com.anite.borris.services.security.entity.api.User;
import com.anite.borris.services.security.entity.utils.*;
import com.anite.borris.services.security.acl.api.AccessControlList;

import java.util.Date;

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
 * An UserManager performs {@link User} objects
 * related tasks on behalf of the {}.
 *
 * The responsibilities of this class include loading data of an user from the
 * storage and putting them into the {@link User} objects,
 * saving those data to the permanent storage, and authenticating users.
 *
 * @author <a href="mailto:Rafal.Krzewski@e-point.pl">Rafal Krzewski</a>
 * @author <a href="mailto:ray@starstream-media.com">Ray Offiah</a>
 * @version $Id: UserManager.java,v 1.1 2005/11/10 17:29:45 bgidley Exp $
 */

public interface UserManager {
    User getUserInstance(Class className) throws DataBackendException;

    User getUserInstance(String s, Class className) throws DataBackendException;
    
    /**
     * Check whether a specified user's account exists.
     * <p/>
     * The login name is used for looking up the account.
     *
     * @param user The user to be checked.
     * @return true if the specified account exists
     * @throws DataBackendException if there was an error accessing
     *                              the data backend.
     */
    boolean checkExists(User user) throws DataBackendException;

    /**
     * Check whether a specified user's account exists.
     * <p/>
     * The login name is used for looking up the account.
     *
     * @param userName The name of the user to be checked.
     * @return true if the specified account exists
     * @throws DataBackendException if there was an error accessing
     *                              the data backend.
     */
    boolean checkExists(String userName) throws DataBackendException;

    /**
     * Retrieve a user from persistent storage using username as the
     * key.
     *
     * @param userName the name of the user.
     * @return an User object.
     * @throws UnknownEntityException if the user's account does not
     *                                exist in the database.
     * @throws DataBackendException   if there is a problem accessing the
     *                                storage.
     */
    User getUser(String userName) throws UnknownEntityException, DataBackendException;

    /**
     * Retrieve a User object with specified id.
     *
     * @param id the id of the User.
     * @return an object representing the User with specified id.
     * @throws DataBackendException   if there was an error accessing the data backend.
     * @throws UnknownEntityException if the user does not exist.
     */
    User getUserById(Object id) throws UnknownEntityException, DataBackendException;

    User getUser(String s, String s1) throws PasswordMismatchException, UnknownEntityException, DataBackendException;

    /**
     * Retrieves all users defined in the system.
     *
     * @return the names of all users defined in the system.
     * @throws DataBackendException if there was an error accessing the data
     *                              backend.
     */
    UserSet getAllUsers() throws DataBackendException;

    /**
     * Stores User attributes. The User is required to exist in the system.
     *
     * @param user to be stored.
     * @throws DataBackendException   if there was an error accessing the data
     *                                backend.
     * @throws UnknownEntityException if the role does not exist.
     */
    void saveUser(User user) throws UnknownEntityException, DataBackendException;

    void authenticate(User user, String s) throws PasswordMismatchException, UnknownEntityException, DataBackendException;

    /**
     * Creates new user account with specified attributes.
     *
     * @param user     the object describing account to be created.
     * @param password The password to use for the account.
     * @throws DataBackendException  if there was an error accessing the
     *                               data backend.
     * @throws EntityExistsException if the user account already exists.
     */
    User addUser(User user, String password) throws EntityExistsException, DataBackendException;

    /**
     * Removes an user account from the system.
     *
     * @param user the object describing the account to be removed.
     * @throws DataBackendException   if there was an error accessing the data
     *                                backend.
     * @throws UnknownEntityException if the user account is not present.
     */
    void removeUser(User user) throws UnknownEntityException, DataBackendException;

    /**
     * Change the password for an User. The user must have supplied the
     * old password to allow the change.
     *
     * @param user        an User to change password for.
     * @param oldPassword The old password to verify
     * @param newPassword The new password to set
     * @throws PasswordMismatchException if the supplied password was
     *                                   incorrect.
     * @throws UnknownEntityException    if the user's account does not
     *                                   exist in the database.
     * @throws DataBackendException      if there is a problem accessing the
     *                                   storage.
     */
    void changePassword(User user, String oldPassword, String newPassword) throws PasswordMismatchException, UnknownEntityException, DataBackendException;

    /**
     * Forcibly sets new password for an User.
     * <p/>
     * This is supposed by the administrator to change the forgotten or
     * compromised passwords. Certain implementatations of this feature
     * would require administrative level access to the authenticating
     * server / program.
     *
     * @param user     an User to change password for.
     * @param password the new password.
     * @throws UnknownEntityException if the user's record does not
     *                                exist in the database.
     * @throws DataBackendException   if there is a problem accessing the
     *                                storage.
     */
    void forcePassword(User user, String password) throws UnknownEntityException, DataBackendException;

    AccessControlList getACL(User user) throws UnknownEntityException;

    ACLFactory getACLFactory();

    void setACLFactory(ACLFactory aclFactory);
    
    String encryptIt(String strToEncrypt);

    /**
     * get the expiry date
     * @return
     */
    public Date getExpiryDate();
}
