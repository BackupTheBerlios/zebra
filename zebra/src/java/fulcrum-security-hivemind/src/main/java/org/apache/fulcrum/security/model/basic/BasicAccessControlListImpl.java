package org.apache.fulcrum.security.model.basic;
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
import org.apache.fulcrum.security.entity.Group;
import org.apache.fulcrum.security.util.GroupSet;
/**
 * This is a control class that makes it easy to find out if a
 * particular User has a given Permission.  It also determines if a
 * User has a a particular Role.
 *
 * @todo Need to rethink the two maps..  Why not just a single list of groups?  That would
 * then cascade down to all the other roles and so on..
 * @author <a href="mailto:epugh@upstate.com">Eric Pugh</a>
 * @version $Id: BasicAccessControlListImpl.java,v 1.1 2005/11/14 18:20:47 bgidley Exp $
 */
public class BasicAccessControlListImpl implements BasicAccessControlList
{
    
    /** The distinct list of groups that this user is part of */
    private GroupSet groupSet = new GroupSet();
    
    /**
     * Constructs a new AccessControlList.
     *
     * This class follows 'immutable' pattern - it's objects can't be modified
     * once they are created. This means that the permissions the users have are
     * in effect form the moment they log in to the moment they log out, and
     * changes made to the security settings in that time are not reflected
     * in the state of this object. If you need to reset an user's permissions
     * you need to invalidate his session. <br>
     * The objects that constructs an AccessControlList must supply hashtables
     * of role/permission sets keyed with group objects. <br>
     *
     * @param roleSets a hashtable containing RoleSet objects keyed with Group objects
     * @param permissionSets a hashtable containing PermissionSet objects keyed with Roles objects
     * @todo need to check this method over...
     */
    public BasicAccessControlListImpl(GroupSet groupSet)
    {
        this.groupSet = groupSet;
        
    }
    
    /**
     * Retrieves a set of Groups an user is assigned 
     *
     * @return the set of Groups
     */
    public GroupSet getGroups()
    {
        return groupSet;
    }
    
    /**
     * Checks if the user is assigned a specific Group
     *
     * @param role the Group
     * @return <code>true</code> if the user is assigned the Group
     */
    public boolean hasGroup(Group group)
    {
        return groupSet.contains(group);
    }
    /**
     * Checks if the user is assigned a specific Group
     *
     * @param role the Group name
     * @return <code>true</code> if the user is assigned the Group
     */
    public boolean hasGroup(String group)
    {
        try
        {
            return groupSet.containsName(group);
        }
        catch (Exception e)
        {
            return false;
        }
    }
    }