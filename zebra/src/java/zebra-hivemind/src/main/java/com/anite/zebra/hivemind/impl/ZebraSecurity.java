package com.anite.zebra.hivemind.impl;

import org.apache.commons.lang.exception.NestableException;
import org.apache.commons.logging.Log;
import org.apache.fulcrum.security.PermissionManager;
import org.apache.fulcrum.security.entity.Permission;
import org.apache.fulcrum.security.util.DataBackendException;
import org.apache.fulcrum.security.util.EntityExistsException;
import org.apache.fulcrum.security.util.PermissionSet;
import org.apache.fulcrum.security.util.UnknownEntityException;

/**
 * A service to help Zebra Manager its link to the Permission Service
 * @author ben.gidley
 *
 */
public class ZebraSecurity {

	private Log log;

	private PermissionManager permissionManager;

	/**
	 * Returns set of permissions for passed String
	 * 
	 * @param permissionsString
	 *            as a ; seperated string
	 * @return
	 * @throws NestableException
	 */
	public PermissionSet getPermissionSet(String permissionsString)
			throws NestableException {
		if (permissionsString != null) {
			String[] permissions = permissionsString.split(";");
			return getPermissionSet(permissions);
		} else {
			return new PermissionSet();
		}
	}

	/**
	 * Gets a permission set for String[] of permission names
	 * 
	 * @param permissions
	 * @return
	 * @throws NestableException
	 */
	public PermissionSet getPermissionSet(String[] permissions)
			throws NestableException {
		try {

			PermissionSet permissionSet = new PermissionSet();
			for (int i = 0; i < permissions.length; i++) {
				try {
					permissionSet.add(this.permissionManager
							.getPermissionByName(permissions[i]));
				} catch (UnknownEntityException e1) {
					// Does not exist yet so create it
					try {
						Permission permission = this.permissionManager
								.getPermissionInstance(permissions[i]);
						this.permissionManager.addPermission(permission);
						permissionSet.add(permission);
					} catch (UnknownEntityException e) {
						this.log.error("Cannot find permission", e);
						throw new NestableException(e);
					} catch (EntityExistsException e) {
						this.log.error(
								"Somehow the entity exists and does not exist",
								e);
						throw new NestableException(e);
					}
				}
			}

			return permissionSet;

		} catch (DataBackendException e) {
			this.log.error(
					"Trying to initialize start permissions not possible", e);
			throw new NestableException(e);
		}
	}

	/**
	 * loads a permission or creates it if it doesn't already exist.
	 * 
	 * @param permissionName
	 * @return
	 * @throws InitializationException
	 */
	public Permission loadOrCreatePermission(String permissionName)
			throws NestableException {

		try {

			Permission permission = this.permissionManager
					.getPermissionInstance(permissionName);
			if (this.permissionManager.checkExists(permission)) {
				return this.permissionManager
						.getPermissionByName(permissionName);
			}
			this.permissionManager.addPermission(permission);
			return permission;
		} catch (DataBackendException e) {
			this.log.error("Failed to find or create permission:"
					+ permissionName, e);
			throw new NestableException(e);
		} catch (UnknownEntityException e) {
			this.log.error("Failed to find or create permission:"
					+ permissionName, e);
			throw new NestableException(e);

		} catch (EntityExistsException e) {
			this.log.error("Failed to find or create permission:"
					+ permissionName, e);
			throw new NestableException(e);
		}
	}

	public PermissionManager getPermissionManager() {
		return this.permissionManager;
	}

	public void setPermissionManager(PermissionManager permissionManager) {
		this.permissionManager = permissionManager;
	}


	public void setLog(Log log) {
		this.log = log;
	}
}
