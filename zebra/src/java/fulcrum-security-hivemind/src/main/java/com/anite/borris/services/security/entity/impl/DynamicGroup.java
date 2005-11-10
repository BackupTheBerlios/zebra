package com.anite.borris.services.security.entity.impl;
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

import java.util.Set;



import com.anite.borris.services.security.entity.api.Group;
import com.anite.borris.services.security.entity.api.User;
import com.anite.borris.services.security.entity.api.Role;
import com.anite.borris.services.security.entity.utils.RoleSet;
import com.anite.borris.services.security.entity.utils.UserSet;


/**
 * Represents the "dynamic" model where permissions are related to roles,
 * roles are related to groups and groups are related to userSet,
 * all in many to many relationships.
 *
 * @author <a href="mailto:epugh@upstate.com">Eric Pugh</a>
 * @version $Id: DynamicGroup.java,v 1.1 2005/11/10 17:29:45 bgidley Exp $
 */
public class DynamicGroup extends SecurityEntityImpl implements Group
{
    private Set roleSet = new RoleSet();
    private Set userSet = new UserSet();
    /**
     * @return
     */
    public UserSet getUsers()
    {
    	if( userSet instanceof UserSet )
    		return (UserSet) userSet;
    	else {
    		userSet = new UserSet(userSet);
    		return (UserSet)userSet;
    	}
    }

    /**
     * @param userSet
     */
    public void setUsers(UserSet userSet)
    {
    	if( userSet != null )
    		this.userSet = userSet;
    	else
    		this.userSet = new UserSet();
    }

	/**
	 * @return
	 */
	public Set getUsersAsSet()
	{
		return userSet;
	}

	/**
	 * @param userSet
	 */
	public void setUsersAsSet(Set users)
	{
		this.userSet = users;
	}

    /**
     * @return
     */
    public RoleSet getRoles()
    {
    	if( roleSet instanceof RoleSet )
    		return (RoleSet) roleSet;
    	else {
    		roleSet = new RoleSet(roleSet);
    		return (RoleSet)roleSet;
    	}
    }
    /**
     * @param roleSet
     */
    public void setRoles(RoleSet roleSet)
    {
    	if( roleSet != null )
    		this.roleSet = roleSet;
    	else
    		this.roleSet = new RoleSet();
    }
    public void addRole(Role role)
    {
        getRoles().add(role);
    }
    public void removeRole(Role role)
    {
        getRoles().remove(role);
    }

    public void addUser(User user)
    {
        getUsers().add(user);
    }
    public void removeUser(User user)
    {
        getUsers().remove(user);
    }

	public void setRolesAsSet(Set roles)
	{
		this.roleSet = roles;
	}
	public Set getRolesAsSet()
	{
		return roleSet;
	}
}
