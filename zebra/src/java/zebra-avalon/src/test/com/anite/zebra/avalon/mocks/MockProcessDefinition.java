/*
 * Created on Aug 6, 2004
 *
 */
package com.anite.zebra.avalon.mocks;

import com.anite.zebra.core.definitions.api.IProcessDefinition;
import com.anite.zebra.core.definitions.api.IRoutingDefinitions;
import com.anite.zebra.core.definitions.api.ITaskDefinition;
import com.anite.zebra.core.definitions.api.ITaskDefinitions;

/**
 * @author Eric Pugh
 *
 */
public class MockProcessDefinition implements IProcessDefinition {

    /**
     * 
     */
    public MockProcessDefinition() {
        super();
        // TODO Auto-generated constructor stub
    }

    /* (non-Javadoc)
     * @see com.anite.zebra.core.definitions.api.IProcessDefinition#getTaskDefs()
     */
    public ITaskDefinitions getTaskDefs() {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see com.anite.zebra.core.definitions.api.IProcessDefinition#getRoutingDefs()
     */
    public IRoutingDefinitions getRoutingDefs() {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see com.anite.zebra.core.definitions.api.IProcessDefinition#getFirstTask()
     */
    public ITaskDefinition getFirstTask() {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see com.anite.zebra.core.definitions.api.IProcessDefinition#getClassConstruct()
     */
    public String getClassConstruct() {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see com.anite.zebra.core.definitions.api.IProcessDefinition#getClassDestruct()
     */
    public String getClassDestruct() {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see com.anite.zebra.core.definitions.api.IProcessDefinition#getVersion()
     */
    public Long getVersion() {
        // TODO Auto-generated method stub
        return null;
    }

}
