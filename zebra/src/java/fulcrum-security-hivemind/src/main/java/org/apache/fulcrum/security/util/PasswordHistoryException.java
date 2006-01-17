package org.apache.fulcrum.security.util;

/**
 * Thrown upon an attempt to change password to one that exists in the password
 * history
 * 
 * @author richard.brooks
 * Created on Jan 13, 2006
 */
public class PasswordHistoryException extends TurbineSecurityException {

	/**
     * Construct a PasswordHistoryException with specified detail message.
     * @param msg The detail message.
     *
     * @author richard.brooks
     * Created on Jan 13, 2006
     */
	public PasswordHistoryException(String msg)
    {
        super(msg);
    }

}
