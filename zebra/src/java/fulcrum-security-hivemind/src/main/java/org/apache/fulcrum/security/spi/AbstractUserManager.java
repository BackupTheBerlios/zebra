package org.apache.fulcrum.security.spi;
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
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.fulcrum.security.UserManager;
import org.apache.fulcrum.security.acl.AccessControlList;
import org.apache.fulcrum.security.authenticator.Authenticator;
import org.apache.fulcrum.security.entity.User;
import org.apache.fulcrum.security.model.ACLFactory;
import org.apache.fulcrum.security.util.DataBackendException;
import org.apache.fulcrum.security.util.EntityExistsException;
import org.apache.fulcrum.security.util.PasswordMismatchException;
import org.apache.fulcrum.security.util.UnknownEntityException;

/**
 * This implementation keeps all objects in memory.  This is mostly meant to help
 * with testing and prototyping of ideas.
 *
 * Implementing classes must inject an ACLFractory and Authenticator
 *
 * @todo Need to load up Crypto component and actually encrypt passwords!
 * @author <a href="mailto:epugh@upstate.com">Eric Pugh</a>
 * @version $Id: AbstractUserManager.java,v 1.3 2005/11/15 17:56:26 bgidley Exp $
 */
public abstract class AbstractUserManager
    extends AbstractEntityManager
    implements UserManager
{

    protected abstract User persistNewUser(User user)
        throws DataBackendException;

	private ACLFactory aclFactory;
    
    /**
     * Authenticator will be dependency injected
     */
	private Authenticator authenticator;

    public AccessControlList getACL(User user) throws UnknownEntityException
    {
        return getAclFactory().getAccessControlList(user);

    }

    /**
    	* Check whether a specified user's account exists.
    	*
    	* The login name is used for looking up the account.
    	*
    	* @param user The user to be checked.
    	* @return true if the specified account exists
    	* @throws DataBackendException if there was an error accessing
    	*         the data backend.
    	*/
    public boolean checkExists(User user) throws DataBackendException
    {
        return checkExists(user.getName());
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
    public User getUser(String userName, String password)
        throws PasswordMismatchException, UnknownEntityException, DataBackendException
    {
        User user = getUser(userName);
        authenticate(user, password);
        return user;
    }
    
    public User getUser(String name)
    	throws DataBackendException, UnknownEntityException
    {
    	User user = getAllUsers().getUserByName(name);
    	if (user == null)
    	{
    		throw new UnknownEntityException("The specified user does not exist");
    	}
    	return user;
    }    
    
    /**
	* Retrieve a User object with specified Id.
	*
	* @param id the id of the User.
	*
	* @return an object representing the User with specified id.
	*
	* @throws UnknownEntityException if the user does not
	*            exist in the database.
	* @throws DataBackendException if there is a problem accessing the
	*            storage.
	*/
    public User getUserById(Object id)
    	throws DataBackendException, UnknownEntityException
		{
    	User user = getAllUsers().getUserById(id);
    	if (user == null)
    	{
    		throw new UnknownEntityException("The specified user does not exist");
    	}
    	return user;
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
    public void authenticate(User user, String password)
        throws PasswordMismatchException, UnknownEntityException, DataBackendException
    {
        if (!authenticator.authenticate(user, password))
        {
            throw new PasswordMismatchException("Can not authenticate user.");
        }
    }
    /**
     * Change the password for an User. The user must have supplied the
     * old password to allow the change.
     *
     * @param user an User to change password for.
     * @param oldPassword The old password to verify
     * @param newPassword The new password to set
     * @exception PasswordMismatchException if the supplied password was
     *            incorrect.
     * @exception UnknownEntityException if the user's account does not
     *            exist in the database.
     * @exception DataBackendException if there is a problem accessing the
     *            storage.
     */
    public void changePassword(
        User user,
        String oldPassword,
        String newPassword)
        throws PasswordMismatchException, UnknownEntityException, DataBackendException
    {
        if (!checkExists(user))
        {
            throw new UnknownEntityException(
                "The account '" + user.getName() + "' does not exist");
        }
        if (!oldPassword.equals(user.getPassword()))
        {
            throw new PasswordMismatchException(
                "The supplied old password for '"
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
     *
     * This is supposed by the administrator to change the forgotten or
     * compromised passwords. Certain implementatations of this feature
     * would require administrative level access to the authenticating
     * server / program.
     *
     * @param user an User to change password for.
     * @param password the new password.
     * @exception UnknownEntityException if the user's record does not
     *            exist in the database.
     * @exception DataBackendException if there is a problem accessing the
     *            storage.
     */
    public void forcePassword(User user, String password)
        throws UnknownEntityException, DataBackendException
    {
        if (!checkExists(user))
        {
            throw new UnknownEntityException(
                "The account '" + user.getName() + "' does not exist");
        }
        user.setPassword(password);
        // save the changes in the database immediately, to prevent the
        // password being 'reverted' to the old value if the user data
        // is lost somehow before it is saved at session's expiry.
        saveUser(user);
    }
    /**
     * Construct a blank User object.
     *
     * This method calls getUserClass, and then creates a new object using
     * the default constructor.
     *
     * @return an object implementing User interface.
     * @throws DataBackendException if the object could not be instantiated.
     */
    public User getUserInstance() throws DataBackendException
    {
        User user;

        try
        {
            user = (User) Class.forName(getClassName()).newInstance();
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
    public User getUserInstance(String userName) throws DataBackendException
    {
        User user = getUserInstance();
        user.setName(userName);
        return user;
    }

    /**
       * Creates new user account with specified attributes.
       *
       * @param user the object describing account to be created.
       * @param password The password to use for the account.
       *
       * @throws DataBackendException if there was an error accessing the
       *         data backend.
       * @throws EntityExistsException if the user account already exists.
       */
    public User addUser(User user, String password)
        throws DataBackendException, EntityExistsException
    {
        if (StringUtils.isEmpty(user.getName()))
        {
            throw new DataBackendException(
                "Could not create " + "an user with empty name!");
        }
        if (checkExists(user))
        {
            throw new EntityExistsException(
                "The account '" + user.getName() + "' already exists");
        }
        user.setPassword(password);
        try
        {
            return persistNewUser(user);
        }
        catch (Exception e)
        {
            throw new DataBackendException(
                "Failed to create account '" + user.getName() + "'",
                e);
        }
    }

   
    public Authenticator getAuthenticator() {
        return authenticator;
    }

    public void setAuthenticator(Authenticator authenticator) {
        this.authenticator = authenticator;
    }

    public ACLFactory getAclFactory() {
        return aclFactory;
    }

    public void setAclFactory(ACLFactory aclFactory) {
        this.aclFactory = aclFactory;
    }

}