package com.anite.borris.services.security.manager.api;

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
 * Service manager used by Hibernate
 * @author <a href="mailto:ray@starstream-media.com">Ray Offiah</a>
 * @version $Id: SecurityServiceManager.java,v 1.1 2005/11/10 17:29:45 bgidley Exp $
 */
public interface SecurityServiceManager {
    ModelManager getDynamicModelManager();

    void setDynamicModelManager(ModelManager modelManager);

    EntityManager getEntityManager();

    void setEntityManager(EntityManager entityManager);

    GroupManager getGroupManager();

    void setGroupManager(GroupManager groupManager);

    PermissionManager getPermissionManager();

    void setPermissionManager(PermissionManager permissionManager);

    RoleManager getRoleManager();

    void setRoleManager(RoleManager roleManager);

    UserManager getUserManager();

    void setUserManager(UserManager userManager);
}
