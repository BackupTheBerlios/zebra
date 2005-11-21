package org.apache.fulcrum.hivemind;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.apache.hivemind.Registry;
import org.apache.hivemind.Resource;
import org.apache.hivemind.impl.DefaultClassResolver;
import org.apache.hivemind.impl.RegistryBuilder;
import org.apache.hivemind.impl.XmlModuleDescriptorProvider;

public class RegistryManager {

    private Registry registry = null;

    private List<Resource> resources = new ArrayList<Resource>();

    private static RegistryManager _instance;

    private RegistryManager() {
        
    }

    /**
     * Build the registry, can be overridden to change how its created
     * @return
     */
    protected Registry constructRegistry() {

        RegistryBuilder builder = new RegistryBuilder();

        builder.addDefaultModuleDescriptorProvider();

        if (resources.size() > 0) {
            builder.addModuleDescriptorProvider(new XmlModuleDescriptorProvider(new DefaultClassResolver(), resources));
        }

        return builder.constructRegistry(Locale.getDefault());

    }

    public static RegistryManager getInstance() {
        if (_instance == null) {
            _instance = new RegistryManager();
        }
        return _instance;
    }

    public Registry getRegistry() {
        if (this.registry==null){
            this.registry = constructRegistry();
        }        
        return this.registry;
    }

    public List<Resource> getResources() {
        return resources;
    }

    /**
     * Call the prior to first call to getInstance to override default module list
     * This is typically used in Unit Testing to swap an implementation
     * @param resources
     */
    public void setResources(List<Resource> resources) {
        this.resources = resources;
    }

}
