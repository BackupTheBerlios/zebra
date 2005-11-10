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

import java.security.NoSuchAlgorithmException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import net.sf.hibernate.Hibernate;
import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Session;
import net.sf.hibernate.Transaction;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.NestableRuntimeException;

import com.anite.borris.services.authenticator.api.Authenticator;
import com.anite.borris.services.crypto.api.CryptoAlgorithm;
import com.anite.borris.services.crypto.api.CryptoService;
import com.anite.borris.services.hibernate.api.ISessionManager;
import com.anite.borris.services.security.acl.api.AccessControlList;
import com.anite.borris.services.security.entity.api.User;
import com.anite.borris.services.security.entity.utils.DataBackendException;
import com.anite.borris.services.security.entity.utils.EntityExistsException;
import com.anite.borris.services.security.entity.utils.PasswordMismatchException;
import com.anite.borris.services.security.entity.utils.UnknownEntityException;
import com.anite.borris.services.security.entity.utils.UserSet;
import com.anite.borris.services.security.manager.api.ACLFactory;
import com.anite.borris.services.security.manager.api.EntityManager;
import com.anite.borris.services.security.manager.api.UserManager;
import com.anite.borris.services.security.utils.ConfigurationService;

/**
 * This implementation persists to a database via Hibernate.
 *
 * @author <a href="mailto:epugh@upstate.com">Eric Pugh</a>
 * @author <a href="mailto:ray@starstream-media.com">Ray Offiah</a>
 * @version $Id: HibernateUserManagerImpl.java,v 1.1 2005/11/10 17:29:44 bgidley Exp $
 */
public class HibernateUserManagerImpl extends AbstractEntityManager implements UserManager {

    protected ISessionManager iSessionManager;
    protected EntityManager entityManager;

    private ConfigurationService configurationService;
    private CryptoService cryptoService;

    protected Transaction transaction;

    protected ACLFactory aclFactory;
    protected Authenticator authenticator;


    /**
     * Construct a blank User object.
     *
     * This method calls getUserClass, and then creates a new object using
     * the default constructor.
     *
     * @return an object implementing User interface.
     * @throws DataBackendException if the object could not be instantiated.
     */
    public User getUserInstance(Class className) throws DataBackendException {
        User user;

        try
        {
            user = (User) className.newInstance();
        }
        catch (Exception e)
        {
            throw new DataBackendException(
                "Problem creating instance of class " + getClassName(),
                e);
        }

        return user;
    }
    

    /**
     * Construct a blank User object.
     *
     * This method calls getUserClass, and then creates a new object using
     * the default constructor.
     *
     * @param userName The name of the user.
     *
     * @return an object implementing User interface.
     *
     * @throws DataBackendException if the object could not be instantiated.
     */    
    public User getUserInstance(String userName, Class className) throws DataBackendException {
        User user = getUserInstance(className);
        user.setName(userName);
        return user;
    }

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
    public boolean checkExists(User user) throws DataBackendException {
        return checkExists(user.getName());
    }

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
    public boolean checkExists(String userName) throws DataBackendException {
        List users = null;
        userName = userName.toLowerCase();
        try {
            users = retrieveSession().find("from " + User.class.getName() + " su where su.name=?", userName, Hibernate.STRING);
        } catch (HibernateException e) {
            throw new DataBackendException("Error retriving user information", e);
        }
        if (users.size() > 1) {
            throw new DataBackendException("Multiple Users with same username '" + userName + "'");
        }
        return (users.size() == 1);
    }


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
    public User getUser(String userName) throws UnknownEntityException, DataBackendException {
        List users = null;
        try {
            users =
                    retrieveSession().find("from " + User.class.getName() + " su where su.name=?",
                            userName.toLowerCase(),
                            Hibernate.STRING);
        } catch (HibernateException e) {
            throw new DataBackendException("Error retriving user information", e);
        }
        if (users.size() > 1) {
            throw new DataBackendException("Multiple Users with same username '" + userName + "'");
        }
        if (users.size() == 1) {
            return (User) users.get(0);
        }
        throw new UnknownEntityException("Unknown user '" + userName + "'");
    }

    /**
     * Retrieve a User object with specified id.
     *
     * @param id the id of the User.
     * @return an object representing the User with specified id.
     * @throws DataBackendException   if there was an error accessing the data backend.
     * @throws UnknownEntityException if the user does not exist.
     */
    public User getUserById(Object id) throws UnknownEntityException, DataBackendException {
        User user = null;

        if (id != null)
            try {
                List users =
                        retrieveSession().find("from " + User.class.getName() + " su where su.id=?",
                                id,
                                Hibernate.LONG);
                if (users.size() == 0) {
                    throw new UnknownEntityException("Could not find user by id " + id);
                }
                user = (User) users.get(0);
                //session.close();
            } catch (HibernateException e) {
                throw new DataBackendException("Error retriving user information",
                        e);
            }

        return user;
    }

    /**
     * Retrieve a user from persistent storage using username as the
     * key, and authenticate the user. The implementation may chose
     * to authenticate to the server as the user whose data is being
     * retrieved.
     *
     * @param userName the name of the user.
     * @param password the user supplied password.
     * @return an User object.
     * @exception PasswordMismatchException if the supplied password was
     *            incorrect.
     * @exception UnknownEntityException if the user's account does not
     *            exist in the database.
     * @exception DataBackendException if there is a problem accessing the
     *            storage.
     */
    public User getUser(String userName, String password) throws PasswordMismatchException, UnknownEntityException, DataBackendException {
        User user = getUser(userName);
        authenticate(user, password);
        return user;
    }

    /**
     * Retrieves all users defined in the system.
     *
     * @return the names of all users defined in the system.
     * @throws DataBackendException if there was an error accessing the data
     *                              backend.
     */
    public UserSet getAllUsers() throws DataBackendException {
        UserSet userSet = new UserSet();
        try {

            List users =
                    retrieveSession().find("from " + User.class.getName() + " u order by u.name asc");
            userSet.add(users);
        } catch (HibernateException e) {
            throw new DataBackendException("Error retriving all users",
                    e);
        }
        return userSet;
    }

    /**
     * Stores User attributes. The User is required to exist in the system.
     *
     * @param user to be stored.
     * @throws DataBackendException   if there was an error accessing the data
     *                                backend.
     * @throws UnknownEntityException if the role does not exist.
     */
    public void saveUser(User user) throws UnknownEntityException, DataBackendException {
        boolean userExists = false;
        userExists = checkExists(user);
        if (userExists) {
            getSecurityServiceManager().updateEntity(user);
        } else {
            throw new UnknownEntityException("Unknown user '" + user + "'");
        }
    }

    /**
     * Authenticate an User with the specified password. If authentication
     * is successful the method returns nothing. If there are any problems,
     * exception was thrown.
     *
     * @param user an User object to authenticate.
     * @param password the user supplied password.
     * @exception PasswordMismatchException if the supplied password was
     *            incorrect.
     * @exception UnknownEntityException if the user's account does not
     *            exist in the database.
     * @exception DataBackendException if there is a problem accessing the
     *            storage.
     */
    public void authenticate(User user, String password) throws PasswordMismatchException, UnknownEntityException, DataBackendException {

        if (!authenticator.authenticate(user, password))
        {
            throw new PasswordMismatchException("Can not authenticate user.");
        }
    }

    /**
     * Creates new user account with specified attributes.
     *
     * @param user     the object describing account to be created.
     * @param password The password to use for the account.
     * @throws DataBackendException  if there was an error accessing the
     *                               data backend.
     * @throws EntityExistsException if the user account already exists.
     */
    public User addUser(User user, String password) throws EntityExistsException, DataBackendException {
        if (StringUtils.isEmpty(user.getName())) {
            throw new DataBackendException("Could not create " + "an user with empty name!");
        }
        if (checkExists(user)) {
            throw new EntityExistsException("The account '" + user.getName() + "' already exists");
        }
        user.setPassword(password);
        try {
            return persistNewUser(user);
        } catch (Exception e) {
            throw new DataBackendException("Failed to create account '" + user.getName() + "'",
                    e);
        }
    }

    /**
     * Removes an user account from the system.
     *
     * @param user the object describing the account to be removed.
     * @throws DataBackendException   if there was an error accessing the data
     *                                backend.
     * @throws UnknownEntityException if the user account is not present.
     */
    public void removeUser(User user) throws UnknownEntityException, DataBackendException {
        getSecurityServiceManager().removeEntity(user);
    }

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
    public void changePassword(User user, String oldPassword, String newPassword) throws PasswordMismatchException, UnknownEntityException, DataBackendException {
        if (!checkExists(user)) {
            throw new UnknownEntityException("The account '" + user.getName() + "' does not exist");
        }
        if (!oldPassword.equals(user.getPassword())) {
            throw new PasswordMismatchException("The supplied old password for '"
                    + user.getName()
                    + "' was incorrect");
        }
        user.setPassword(newPassword);
        // save the changes in the database imediately, to prevent the password
        // being 'reverted' to the old value if the user data is lost somehow
        // before it is saved at session's expiry.
        saveUser(user);
    }

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
    public void forcePassword(User user, String password) throws UnknownEntityException, DataBackendException {
        if (!checkExists(user)) {
            throw new UnknownEntityException("The account '" + user.getName() + "' does not exist");
        }
        user.setPassword(password);
        // save the changes in the database immediately, to prevent the
        // password being 'reverted' to the old value if the user data
        // is lost somehow before it is saved at session's expiry.
        saveUser(user);
    }

    public AccessControlList getACL(User user) throws UnknownEntityException {
        return getACLFactory().getAccessControlList(user);
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


    /**
     * Creates new user account with specified attributes.
     *
     * @param user the object describing account to be created.
     * @throws DataBackendException if there was an error accessing the
     *                              data backend.
     * @throws DataBackendException if the user account already exists.
     */
    protected User persistNewUser(User user) throws DataBackendException {

        getSecurityServiceManager().addEntity(user);
        return user;
    }

    public ACLFactory getACLFactory() {
        return aclFactory;
    }

    public void setACLFactory(ACLFactory aclFactory) {
        this.aclFactory = aclFactory;
    }

    public Authenticator getAuthenticator() {
        return authenticator;
    }

    public void setAuthenticator(Authenticator authenticator) {
        this.authenticator = authenticator;
    }
    
    public String encryptIt(String strToEncrypt){
		if(strToEncrypt != null) {
			try {
				String cipherType = "MD5";
				String algorithmType  = "java";
				CryptoService cryptoService = getCryptoService();
				CryptoAlgorithm cryptoAlgorithm = cryptoService.getCryptoAlgorithm(algorithmType);
				cryptoAlgorithm.setCipher(cipherType);
				cryptoAlgorithm.setSeed(strToEncrypt);
				strToEncrypt = cryptoAlgorithm.encrypt(strToEncrypt);
			} catch (NoSuchAlgorithmException e) {
				// TODO Auto-generated catch block
				throw new NestableRuntimeException();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				throw new NestableRuntimeException();
			}
		}
	  return strToEncrypt;
	}

    /**
     * Returns the expiry date to be written for
     * the current record. The number of days to be
     * added to the date of change, is picked up from
     * the Hivemind registry
     *
     * @return
     */
    public Date getExpiryDate() {

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());

        Map configuration = getConfigurationService().getDataMap();

        String daysToExpire = (String) configuration.get("passwordExpiryTimeInDays");

        calendar.add(Calendar.DATE, Integer.parseInt(daysToExpire));

        return calendar.getTime();
    }

    public ConfigurationService getConfigurationService() {
        return configurationService;
    }

    public void setConfigurationService(ConfigurationService configurationService) {
        this.configurationService = configurationService;
    }


    public CryptoService getCryptoService() {
        return cryptoService;
    }

    public void setCryptoService(CryptoService cryptoService) {
        this.cryptoService = cryptoService;
    }
}
