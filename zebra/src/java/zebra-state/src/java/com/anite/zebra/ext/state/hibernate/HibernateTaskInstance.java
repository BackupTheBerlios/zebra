package com.anite.zebra.ext.state.hibernate;

import com.anite.zebra.core.definitions.api.ITaskDefinition;
import com.anite.zebra.core.exceptions.DefinitionNotFoundException;
import com.anite.zebra.core.state.api.IFOE;
import com.anite.zebra.core.state.api.IProcessInstance;
import com.anite.zebra.core.state.api.ITaskInstance;

/**
 *
 * @author Eric Pugh
 *
 * @hibernate.class
 *  table="TASK_INSTANCE"
 */

public class HibernateTaskInstance implements ITaskInstance {
    private Long taskInstanceId;
	private IProcessInstance processInstance;
	private IFOE foe;
	private ITaskDefinition taskDef;
	private long state;

    /**
     * @hibernate.id generator-class="native" column="taskInstanceId"
     *
     * @return Returns the id.
     */
    public Long getTaskInstanceId() {
        return taskInstanceId;
    }

    /**
     *
     * @param id
     *            The id to set.
     */
    public void setTaskInstanceId(Long id) {
        this.taskInstanceId = id;
    }
    
	/**
	 * @hibernate.many-to-one
	 * column="processInstanceId" not-null="true"
	 * class="com.anite.zebra.ext.state.hibernate.HibernateProcessInstance"
	 * cascade="save-update"
	 * @return
	 */    
    public IProcessInstance getProcessInstance() {
		return processInstance;
	}

	public IFOE getFOE() {
		return foe;
	}

	public ITaskDefinition getTaskDefinition() throws DefinitionNotFoundException {
		return taskDef;
	}

	public long getState() {
		return state;
	}
	
	public void setState(long state) {
		this.state = state;

	}

	public void setFOE(IFOE foe) {
		this.foe = foe;
	}

	public void setProcessInstance(IProcessInstance processInstance) {
		this.processInstance = processInstance;
	}

	public void setTaskDefinition(ITaskDefinition taskDef) {
		this.taskDef = taskDef;
	}


}