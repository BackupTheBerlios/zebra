/*
 * Created on Aug 8, 2004
 *
 */
package com.anite.zebra.avalon.mocks;

import java.util.Set;

import com.anite.zebra.core.definitions.api.IProcessDefinition;
import com.anite.zebra.ext.definitions.api.IProcessVersion;
import com.anite.zebra.ext.definitions.api.IProcessVersions;

/**
 * @author Eric Pugh
 *
 */
public class MockProcessVersions implements IProcessVersions {

    /* (non-Javadoc)
     * @see com.anite.zebra.ext.definitions.api.IProcessVersions#getLatestProcessVersion()
     */
    public IProcessVersion getLatestProcessVersion() {
        return null;
    }
    /* (non-Javadoc)
     * @see com.anite.zebra.ext.definitions.api.IProcessVersions#getName()
     */
    public String getName() {
        return null;
    }
    /* (non-Javadoc)
     * @see com.anite.zebra.ext.definitions.api.IProcessVersions#getProcessVersions()
     */
    public Set getProcessVersions() {
        return null;
    }
    public MockProcessVersions() {
        super();
    }

    public Set getProcessDefs() {
        return null;
    }

    public void addProcessDef(IProcessDefinition arg0) {

    }

    public IProcessDefinition getLatestProcessDef() {
        return null;
    }

}
