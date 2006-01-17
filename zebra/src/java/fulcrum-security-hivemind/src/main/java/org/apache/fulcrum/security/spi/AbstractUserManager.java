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
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.fulcrum.security.UserManager;
import org.apache.fulcrum.security.acl.AccessControlList;
import org.apache.fulcrum.security.authenticator.Authenticator;
import org.apache.fulcrum.security.entity.User;
import org.apache.fulcrum.security.model.ACLFactory;
import org.apache.fulcrum.security.util.DataBackendException;
import org.apache.fulcrum.security.util.EntityExistsException;
import org.apache.fulcrum.security.util.PasswordExpiredException;
import org.apache.fulcrum.security.util.PasswordHistoryException;
import org.apache.fulcrum.security.util.PasswordMismatchException;
import org.apache.fulcrum.security.util.UnknownEntityException;
import org.apache.fulcrum.security.util.UserLockedException;

/**
 * This implementation keeps all objects in memory. This is mostly meant to help
 * with testing and prototyping of ideas.
 * 
 * Implementing classes must inject an ACLFractory and Authenticator
 * 
 * @todo Need to load up Crypto component and actually encrypt passwords!
 * @author <a href="mailto:epugh@upstate.com">Eric Pugh</a>
 * @version $Id: AbstractUserManager.java,v 1.4 2006/01/17 09:17:25 biggus_richus Exp $
 */
public abstract class AbstractUserManager extends AbstractEntityManager
		implements UserManager {

	protected abstract User persistNewUser(User user)
			throws DataBackendException;

	private ACLFactory aclFactory;

	/**
	 * Authenticator will be dependency injected
	 */
	private Authenticator authenticator;

	// Password Cycle Policy, Password Duration, Login Attempts and
	// Lock Reset will be dependency injected.
	private int passwordCyclePolicy;

	private int passwordDuration;
	
	private int maxLoginAttempts;
	
	private int lockReset;

	public AccessControlList getACL(User user) throws UnknownEntityException {
		return getAclFactory().getAccessControlList(user);
	}

	/**
	 * Check whether a specified user's account exists.
	 * 
	 * The login name is used for looking up the account.
	 * 
	 * @param user
	 *            The user to be checked.
	 * @return true if the specified account exists
	 * @throws DataBackendException
	 *             if there was an error accessing the data backend.
	 */
	public boolean checkExists(User user) throws DataBackendException {
		return checkExists(user.getName());
	}

	/**
	 * Retrieve a user from persistent storage using username as the key, and
	 * authenticate the user. The implementation may chose to authenticate to
	 * the server as the user whose data is being retrieved.
	 * 
	 * @param userName
	 *            the name of the user.
	 * @param password
	 *            the user supplied password.
	 * @return a User object.
	 * @exception PasswordMismatchException
	 *                if the supplied password was incorrect.
	 * @exception UnknownEntityException
	 *                if the user's account does not exist in the database.
	 * @exception DataBackendException
	 *                if there is a problem accessing the storage.
	 * @throws PasswordExpiredException 
	 *                if the user's password has expired.
	 */
	public User getUser(String userName, String password)
			throws PasswordMismatchException, UnknownEntityException,
			UserLockedException, DataBackendException, PasswordExpiredException {
		User user = getUser(userName);
		authenticate(user, password);
		return user;
	}

	public User getUser(String name) throws DataBackendException,
			UnknownEntityException {
		User user = getAllUsers().getUserByName(name);
		if (user == null) {
			throw new UnknownEntityException(
					"The specified user does not exist");
		}
		return user;
	}

	/**
	 * Retrieve a User object with specified Id.
	 * 
	 * @param id
	 *            the id of the User.
	 * 
	 * @return an object representing the User with specified id.
	 * 
	 * @throws UnknownEntityException
	 *             if the user does not exist in the database.
	 * @throws DataBackendException
	 *             if there is a problem accessing the storage.
	 */
	public User getUserById(Object id) throws DataBackendException,
			UnknownEntityException {
		User user = getAllUsers().getUserById(id);
		if (user == null) {
			throw new UnknownEntityException(
					"The specified user does not exist");
		}
		return user;
	}

	/**
	 * Authenticate an User with the specified password. If authentication is
	 * successful the method returns nothing. If there are any problems,
	 * exception was thrown.
	 * 
	 * @param user
	 *            an User object to authenticate.
	 * @param password
	 *            the user supplied password.
	 * @exception PasswordMismatchException
	 *                if the supplied password was incorrect.
	 * @exception UnknownEntityException
	 *                if the user's account does not exist in the database.
	 * @exception DataBackendException
	 *                if there is a problem accessing the storage.
	 * @throws PasswordExpiredException
	 * 				  is the user's password has expired
	 */
	public void authenticate(User user, String password)
			throws PasswordMismatchException, UnknownEntityException,
			DataBackendException, UserLockedException, PasswordExpiredException {

		if (user.getLockedDate() != null) {
			Calendar cal = Calendar.getInstance();
			cal.add(Calendar.HOUR_OF_DAY, (-1 * lockReset));
			if (cal.getTime().before(user.getLockedDate())) {
				throw new UserLockedException("User is locked");
			}
		}
		
		if (user.getPasswordExpiryDate().compareTo(new Date()) <= 0) {
			throw new PasswordExpiredException("Password expired on "+user.getPasswordExpiryDate());
		}
		
		if (!authenticator.authenticate(user, password)) {
			user.setLoginAttempts(user.getLoginAttempts()+1);
			System.out.println(user.getLoginAttempts()+" max="+maxLoginAttempts+" lockTime= "+lockReset);
			if (user.getLoginAttempts() == maxLoginAttempts) {
				user.setLockedDate(new Date());
				user.setLoginAttempts(0);
				System.out.println("User locked");
			}
			throw new PasswordMismatchException("Can not authenticate user.");
		}
		user.setLoginAttempts(0);
	}

	/**
	 * Change the password for an User. The user must have supplied the old
	 * password to allow the change.
	 * 
	 * @param user
	 *            an User to change password for.
	 * @param oldPassword
	 *            The old password to verify
	 * @param newPassword
	 *            The new password to set
	 * @exception PasswordMismatchException
	 *                if the supplied password was incorrect.
	 * @exception UnknownEntityException
	 *                if the user's account does not exist in the database.
	 * @exception DataBackendException
	 *                if there is a problem accessing the storage.
	 * @throws PasswordExpiredException 
	 */
	public void changePassword(User user, String oldPassword, String newPassword)
			throws PasswordMismatchException, PasswordHistoryException,
			UserLockedException, UnknownEntityException, DataBackendException, PasswordExpiredException {
		if (!checkExists(user)) {
			throw new UnknownEntityException("The account '" + user.getName()
					+ "' does not exist");
		}

		authenticate(user, oldPassword);
		cyclePassword(user, newPassword);
		// save the changes in the database imediately, to prevent the password
		// being 'reverted' to the old value if the user data is lost somehow
		// before it is saved at session's expiry.
		saveUser(user);
	}

	/**
	 * Utility method to set new password and maintain the password history.
	 * 
	 * @param user User.
	 * @param newPassword User's new password.
	 * @throws DataBackendException
	 * @throws PasswordHistoryException
	 * 				if the password is contained in the history
	 *
	 * @author richard.brooks
	 * Created on Jan 16, 2006
	 */
	private void cyclePassword(User user, String newPassword)
			throws DataBackendException, PasswordHistoryException {
		String cryptoPassword = authenticator.getCryptoPassword(newPassword);
		List passwordHistory = user.getPasswordHistory();

		if (passwordHistory.contains(cryptoPassword)) {
			throw new PasswordHistoryException("Password invalid");
		} else {
			if (passwordHistory.size() >= getPasswordCyclePolicy()) {
				for (int i = 0; i < getPasswordCyclePolicy() - 1; i++) {
					passwordHistory.set(i, passwordHistory.get(i + 1));
				}
				passwordHistory.remove(getPasswordCyclePolicy() - 1);
			}
			passwordHistory.add(user.getPassword());
			user.setPassword(cryptoPassword);
			setPasswordExpiry(user);
		}
	}

	/**
	 * Utility method that sets the user's password expiry date.
	 * @param user The user whose password is to be changed.
	 *
	 * @author richard.brooks
	 * Created on Jan 16, 2006
	 */
	private void setPasswordExpiry(User user) {
		Calendar date = Calendar.getInstance();
		GregorianCalendar passwordExpiry = new GregorianCalendar(date.get(Calendar.YEAR),
                												 date.get(Calendar.MONTH),
                												 date.get(Calendar.DAY_OF_MONTH));
		passwordExpiry.add(Calendar.DAY_OF_MONTH, getPasswordDuration());
		user.setPasswordExpiryDate(passwordExpiry.getTime());
	}

	/**
	 * Forcibly sets new password for a User.
	 * 
	 * This is supposed by the administrator to change the forgotten or
	 * compromised passwords. Certain implementatations of this feature would
	 * require administrative level access to the authenticating server /
	 * program.
	 * 
	 * @param user
	 *            an User to change password for.
	 * @param password
	 *            the new password.
	 * @exception UnknownEntityException
	 *                if the user's record does not exist in the database.
	 * @exception DataBackendException
	 *                if there is a problem accessing the storage.
	 */
	public void forcePassword(User user, String password)
			throws UnknownEntityException, DataBackendException,
			PasswordHistoryException {
		if (!checkExists(user)) {
			throw new UnknownEntityException("The account '" + user.getName()
					+ "' does not exist");
		}
		// user.setPassword(authenticator.getCryptoPassword(password));
		cyclePassword(user, password);
		// save the changes in the database immediately, to prevent the
		// password being 'reverted' to the old value if the user data
		// is lost somehow before it is saved at session's expiry.
		saveUser(user);
	}

	/**
	 * Construct a blank User object.
	 * 
	 * This method calls getUserClass, and then creates a new object using the
	 * default constructor.
	 * 
	 * @return an object implementing User interface.
	 * @throws DataBackendException
	 *             if the object could not be instantiated.
	 */
	public User getUserInstance() throws DataBackendException {
		User user;

		try {
			user = (User) Class.forName(getClassName()).newInstance();
		} catch (Exception e) {
			throw new DataBackendException(
					"Problem creating instance of class " + getClassName(), e);
		}

		return user;
	}

	/**
	 * Construct a blank User object.
	 * 
	 * This method calls getUserClass, and then creates a new object using the
	 * default constructor.
	 * 
	 * @param userName
	 *            The name of the user.
	 * 
	 * @return an object implementing User interface.
	 * 
	 * @throws DataBackendException
	 *             if the object could not be instantiated.
	 */
	public User getUserInstance(String userName) throws DataBackendException {
		User user = getUserInstance();
		user.setName(userName);
		return user;
	}

	/**
	 * Creates new user account with specified attributes.
	 * 
	 * @param user
	 *            the object describing account to be created.
	 * @param password
	 *            The password to use for the account.
	 * 
	 * @throws DataBackendException
	 *             if there was an error accessing the data backend.
	 * @throws EntityExistsException
	 *             if the user account already exists.
	 */
	public User addUser(User user, String password)
			throws DataBackendException, EntityExistsException {
		if (StringUtils.isEmpty(user.getName())) {
			throw new DataBackendException(
					"Could not create a user with empty name!");
		}
		if (checkExists(user)) {
			throw new EntityExistsException("The account '" + user.getName()
					+ "' already exists");
		}
		user.setPassword(authenticator.getCryptoPassword(password));
		setPasswordExpiry(user);
		user.setLockedDate(null);
		user.setLoginAttempts(0);
		try {
			return persistNewUser(user);
		} catch (Exception e) {
			throw new DataBackendException("Failed to create account '"
					+ user.getName() + "'", e);
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

	/**
	 * 
	 * @return The number of previous passwords stored.
	 * 
	 * @author richard.brooks Created on Jan 11, 2006
	 */
	public int getPasswordCyclePolicy() {
		return this.passwordCyclePolicy;
	}

	/**
	 * Sets the number of previous passwords stored.
	 * 
	 * @param passwordCyclePolicy Number of passwords to store
	 * 
	 * @author richard.brooks Created on Jan 11, 2006
	 */
	public void setPasswordCyclePolicy(int passwordCyclePolicy) {
		this.passwordCyclePolicy = passwordCyclePolicy;
	}

	public int getPasswordDuration() {
		return this.passwordDuration;
	}

	public void setPasswordDuration(int passwordDuration) {
		this.passwordDuration = passwordDuration;
	}

	public int getLockReset() {
		return lockReset;
	}

	public void setLockReset(int lockReset) {
		this.lockReset = lockReset;
	}

	public int getMaxLoginAttempts() {
		return maxLoginAttempts;
	}

	public void setMaxLoginAttempts(int maxLoginAttempts) {
		this.maxLoginAttempts = maxLoginAttempts;
	}
}
