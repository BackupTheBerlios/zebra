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

import com.anite.borris.services.security.manager.api.*;

/**
 * Security service wrapper
 *
 * @author <a href="mailto:ray@starstream-media.com">Ray Offiah</a>
 * @version $Id: SecurityServiceManagerImpl.java,v 1.1 2005/11/10 17:29:44 bgidley Exp $
 */

public class SecurityServiceManagerImpl implements SecurityServiceManager {

    protected ModelManager modelManager;
    protected EntityManager entityManager;
    protected GroupManager groupManager;
    protected PermissionManager permissionManager;
    protected RoleManager roleManager;
    protected UserManager userManager;


    public ModelManager getDynamicModelManager() {
        return modelManager;
    }

    public void setDynamicModelManager(ModelManager modelManager) {
        this.modelManager = modelManager;
    }

    public EntityManager getEntityManager() {
        return entityManager;
    }

    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public GroupManager getGroupManager() {
        return groupManager;
    }

    public void setGroupManager(GroupManager groupManager) {
        this.groupManager = groupManager;
    }

    public PermissionManager getPermissionManager() {
        return permissionManager;
    }

    public void setPermissionManager(PermissionManager permissionManager) {
        this.permissionManager = permissionManager;
    }

    public RoleManager getRoleManager() {
        return roleManager;
    }

    public void setRoleManager(RoleManager roleManager) {
        this.roleManager = roleManager;
    }

    public UserManager getUserManager() {
        return userManager;
    }

    public void setUserManager(UserManager userManager) {
        this.userManager = userManager;
    }
}
