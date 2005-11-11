package org.apache.fulcrum.security;

/**
 * A modification of the Fulcrum SecurityService class to remove the uncesscarry dependency on Avalon.
 * @author ben.gidley
 *
 */
public interface CleanSecurityService {
    /**
     * Returns the configured UserManager.
     * 
     * @return An UserManager object
     */
    UserManager getUserManager();
    /**
     * Returns the configured GroupManager.
     * 
     * @return An UserManager object
     */
    GroupManager getGroupManager();
    /**
     * Returns the configured RoleManager.
     * 
     * @return An RoleManager object
     */
    RoleManager getRoleManager();
    /**
     * Returns the configured PermissionManager.
     * 
     * @return An PermissionManager object
     */
    PermissionManager getPermissionManager();
    /**
     * Returns the configured ModelManager object that can then
     * be casted to the specific model.
     * 
     * @return An ModelManager object
     */
    ModelManager getModelManager();    

}
