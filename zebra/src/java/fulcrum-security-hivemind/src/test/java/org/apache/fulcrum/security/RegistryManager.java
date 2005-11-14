package org.apache.fulcrum.security;


import org.apache.hivemind.Registry;
import org.apache.hivemind.impl.RegistryBuilder;

public class RegistryManager {

    private Registry registry;

    private static RegistryManager _instance;

    private RegistryManager() {
        this.registry = constructRegistry();
    }

    /**
     * Build the registry, can be overridden to change how its created
     * @return
     */
    protected Registry constructRegistry() {
        
        return RegistryBuilder.constructDefaultRegistry();
    }

    public static RegistryManager getInstance() {
        if (_instance == null) {
            _instance = new RegistryManager();
        }
        return _instance;
    }

    public Registry getRegistry() {
        return this.registry;
    }
    
    

}
