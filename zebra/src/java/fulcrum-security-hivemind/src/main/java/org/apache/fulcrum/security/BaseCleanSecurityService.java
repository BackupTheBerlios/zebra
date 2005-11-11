package org.apache.fulcrum.security;

public class BaseCleanSecurityService implements CleanSecurityService {

    protected ModelManager modelManager;
    protected GroupManager groupManager;
    protected PermissionManager permissionManager;
    protected RoleManager roleManager;
    protected UserManager userManager;
    
    public GroupManager getGroupManager() {
        return groupManager;
    }
    public void setGroupManager(GroupManager groupManager) {
        this.groupManager = groupManager;
    }
    public ModelManager getModelManager() {
        return modelManager;
    }
    public void setModelManager(ModelManager modelManager) {
        this.modelManager = modelManager;
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
