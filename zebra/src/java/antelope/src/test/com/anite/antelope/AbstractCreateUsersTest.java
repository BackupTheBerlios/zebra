/*
 * Copyright 2004 Anite - Central Government Division
 * http://www.anite.com/publicsector
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.anite.antelope;

import org.apache.fulcrum.security.GroupManager;
import org.apache.fulcrum.security.PermissionManager;
import org.apache.fulcrum.security.RoleManager;
import org.apache.fulcrum.security.SecurityService;
import org.apache.fulcrum.security.UserManager;
import org.apache.fulcrum.security.entity.Group;
import org.apache.fulcrum.security.entity.Permission;
import org.apache.fulcrum.security.entity.Role;
import org.apache.fulcrum.security.entity.User;
import org.apache.fulcrum.security.model.dynamic.DynamicModelManager;
import org.apache.fulcrum.testcontainer.BaseUnitTest;

import com.anite.antelope.utils.AntelopeConstants;

/**
 * This in a absract class the will build up a set of users/groups/roles and
 * premission for Antelope.
 * 
 * @author <a href="mailTo:michael.jones@anite.com">Michael.Jones </a>
 *  
 */
public abstract class AbstractCreateUsersTest extends BaseUnitTest {

	/**
	 * @param arg0
	 */
	public AbstractCreateUsersTest(String arg0) {
		super(arg0);
		// TODO Auto-generated constructor stub
	}

	protected SecurityService securityService;

	protected DynamicModelManager modelManager;

	protected UserManager userManager;

	protected GroupManager groupManager;

	protected RoleManager roleManager;

	protected PermissionManager permissionManager;

	public void testCreateUsers() throws Exception {
		User user;
		Group group;
		Role role;
		Permission permission;

		// build up a simple dynanic user permission model
		modelManager = (DynamicModelManager) securityService.getModelManager();

		// get all the managers
		userManager = securityService.getUserManager();
		groupManager = securityService.getGroupManager();
		roleManager = securityService.getRoleManager();
		permissionManager = securityService.getPermissionManager();

		// Add all the permissions
		permission = permissionManager
				.getPermissionInstance(AntelopeConstants.PERMISSION_ADD_USER);
		permissionManager.addPermission(permission);
		permission = permissionManager
				.getPermissionInstance(AntelopeConstants.PERMISSION_EDIT_PERMISSIONS);
		permissionManager.addPermission(permission);
		permission = permissionManager
				.getPermissionInstance(AntelopeConstants.PERMISSION_CHANGE_PASSWORD);
		permissionManager.addPermission(permission);
		permission = permissionManager.getPermissionInstance("security_add");
		permissionManager.addPermission(permission);
		permission = permissionManager.getPermissionInstance("security_edit");
		permissionManager.addPermission(permission);
		permission = permissionManager.getPermissionInstance("security_delete");
		permissionManager.addPermission(permission);

		// Add all roles
		role = roleManager.getRoleInstance(AntelopeConstants.ROLE_USER_ADMIN);
		roleManager.addRole(role);
		role = roleManager.getRoleInstance("security");
		roleManager.addRole(role);
		role = roleManager.getRoleInstance(AntelopeConstants.ROLE_USER_BASIC);
		roleManager.addRole(role);

		// Add all Groups
		group = groupManager.getGroupInstance(AntelopeConstants.GROUP_ADMIN);
		groupManager.addGroup(group);
		group = groupManager.getGroupInstance(AntelopeConstants.GROUP_BASIC);
		groupManager.addGroup(group);
		group = groupManager.getGroupInstance("test2");
		groupManager.addGroup(group);
		group = groupManager.getGroupInstance("test3");
		groupManager.addGroup(group);

		// add all users
		user = userManager.getUserInstance("antelope");
		userManager.addUser(user, "test");
		user = userManager.getUserInstance("basic");
		userManager.addUser(user, "test");

		// set up the stutcuture
		// add perms to roles
		modelManager
				.grant(
						roleManager
								.getRoleByName(AntelopeConstants.ROLE_USER_ADMIN),
						permissionManager
								.getPermissionByName(AntelopeConstants.PERMISSION_ADD_USER));
		modelManager
				.grant(
						roleManager
								.getRoleByName(AntelopeConstants.ROLE_USER_ADMIN),
						permissionManager
								.getPermissionByName(AntelopeConstants.PERMISSION_EDIT_PERMISSIONS));
		modelManager
				.grant(
						roleManager
								.getRoleByName(AntelopeConstants.ROLE_USER_ADMIN),
						permissionManager
								.getPermissionByName(AntelopeConstants.PERMISSION_CHANGE_PASSWORD));

		modelManager
				.grant(
						roleManager
								.getRoleByName(AntelopeConstants.ROLE_USER_BASIC),
						permissionManager
								.getPermissionByName(AntelopeConstants.PERMISSION_CHANGE_PASSWORD));

		modelManager.grant(roleManager.getRoleByName("security"),
				permissionManager.getPermissionByName("security_add"));
		modelManager.grant(roleManager.getRoleByName("security"),
				permissionManager.getPermissionByName("security_edit"));
		modelManager.grant(roleManager.getRoleByName("security"),
				permissionManager.getPermissionByName("security_delete"));

		// add roles to groups
		modelManager.grant(groupManager
				.getGroupByName(AntelopeConstants.GROUP_ADMIN), roleManager
				.getRoleByName(AntelopeConstants.ROLE_USER_ADMIN));
		modelManager.grant(groupManager
				.getGroupByName(AntelopeConstants.GROUP_ADMIN), roleManager
				.getRoleByName("security"));
		modelManager.grant(groupManager
				.getGroupByName(AntelopeConstants.GROUP_BASIC), roleManager
				.getRoleByName(AntelopeConstants.ROLE_USER_BASIC));

		// add groups to users
		modelManager.grant(userManager.getUser("antelope"), groupManager
				.getGroupByName(AntelopeConstants.GROUP_ADMIN));
		modelManager.grant(userManager.getUser("basic"), groupManager
				.getGroupByName(AntelopeConstants.GROUP_BASIC));

		//TODO should really put a real test in here to make sure the
		// users have been made!
		assertTrue(true);
	}
}