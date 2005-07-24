/*
 * created on 07-Jun-2005
 */
package com.anite.hivemind;

import java.util.Locale;

import org.apache.hivemind.ClassResolver;
import org.apache.hivemind.Registry;
import org.apache.hivemind.impl.DefaultClassResolver;
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
        ClassResolver resolver = new DefaultClassResolver();
        RegistryBuilder builder = new RegistryBuilder();

        builder.processModules(resolver);

        return builder.constructRegistry(Locale.getDefault());
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
