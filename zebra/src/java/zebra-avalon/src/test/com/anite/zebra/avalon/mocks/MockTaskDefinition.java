/*
 * Created on Aug 6, 2004
 *
 */
package com.anite.zebra.avalon.mocks;

import java.util.Set;

import com.anite.zebra.core.definitions.api.ITaskDefinition;

/**
 * @author Eric Pugh
 *
 */
public class MockTaskDefinition implements ITaskDefinition {

    /**
     * 
     */
    public MockTaskDefinition() {
        super();
        // TODO Auto-generated constructor stub
    }

    /* (non-Javadoc)
     * @see com.anite.zebra.core.definitions.api.ITaskDefinition#getId()
     */
    public Long getId() {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see com.anite.zebra.core.definitions.api.ITaskDefinition#isAuto()
     */
    public boolean isAuto() {
        // TODO Auto-generated method stub
        return false;
    }

    /* (non-Javadoc)
     * @see com.anite.zebra.core.definitions.api.ITaskDefinition#getClassName()
     */
    public String getClassName() {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see com.anite.zebra.core.definitions.api.ITaskDefinition#isSynchronised()
     */
    public boolean isSynchronised() {
        // TODO Auto-generated method stub
        return false;
    }

    /* (non-Javadoc)
     * @see com.anite.zebra.core.definitions.api.ITaskDefinition#getRoutingOut()
     */
    public Set getRoutingOut() {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see com.anite.zebra.core.definitions.api.ITaskDefinition#getRoutingIn()
     */
    public Set getRoutingIn() {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see com.anite.zebra.core.definitions.api.ITaskDefinition#getClassConstruct()
     */
    public String getClassConstruct() {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see com.anite.zebra.core.definitions.api.ITaskDefinition#getClassDestruct()
     */
    public String getClassDestruct() {
        // TODO Auto-generated method stub
        return null;
    }

}
