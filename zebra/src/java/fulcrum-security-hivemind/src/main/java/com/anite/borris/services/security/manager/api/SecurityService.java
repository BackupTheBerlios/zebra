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
 * The Security Service manages Users, Groups Roles and Permissions in the
 * system.
 *
 * The task performed by the security service include creation and removal of
 * accounts, groups, roles, and permissions; assigning users roles in groups;
 * assigning roles specific permissions and construction of objects
 * representing these logical entities.
 *
 * <p> Because of pluggable nature of the Services, it is possible to create
 * multiple implementations of SecurityService, for example employing database
 * and directory server as the data backend.<br>
 *
 * @author <a href="mailto:Rafal.Krzewski@e-point.pl">Rafal Krzewski</a>
 * @author <a href="mailto:hps@intermeta.de">Henning P. Schmiedehausen</a>
 * @author <a href="mailto:marco@intermeta.de">Marco Kn&uuml;ttel</a>
 * @version $Id: SecurityService.java,v 1.1 2005/11/10 17:29:45 bgidley Exp $
 */

public interface SecurityService {

    public ModelManager getModelManager();

    public void setModelManager(ModelManager modelManager);

    public EntityManager getEntityManager();

    public void setEntityManager(EntityManager entityManager);

    public GroupManager getGroupManager();

    public void setGroupManager(GroupManager groupManager);

    public PermissionManager getPermissionManager();

    public void setPermissionManager(PermissionManager permissionManager);

    public RoleManager getRoleManager();

    public void setRoleManager(RoleManager roleManager);

    public UserManager getUserManager();

    public void setUserManager(UserManager userManager);
}
