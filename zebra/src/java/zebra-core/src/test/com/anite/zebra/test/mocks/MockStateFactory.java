package com.anite.zebra.test.mocks;
/*
 * Copyright 2004 Anite - Central Government Division
 *    http://www.anite.com/publicsector
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
import junit.framework.Assert;

import com.anite.zebra.core.definitions.api.IProcessDefinition;
import com.anite.zebra.core.definitions.api.ITaskDefinition;
import com.anite.zebra.core.exceptions.LockException;
import com.anite.zebra.core.factory.api.IStateFactory;
import com.anite.zebra.core.factory.exceptions.CreateObjectException;
import com.anite.zebra.core.factory.exceptions.StateFailureException;
import com.anite.zebra.core.state.api.IFOE;
import com.anite.zebra.core.state.api.IProcessInstance;
import com.anite.zebra.core.state.api.IStateObject;
import com.anite.zebra.core.state.api.ITaskInstance;
import com.anite.zebra.core.state.api.ITransaction;

/**
 * @author Eric Pugh
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class MockStateFactory implements IStateFactory {



	/* (non-Javadoc)
	 * @see com.anite.zebra.core.factory.api.IStateFactory#beginTransaction()
	 */
	public ITransaction beginTransaction() throws StateFailureException {
		return new MockTransaction();
	}

	/* (non-Javadoc)
	 * @see com.anite.zebra.core.factory.api.IStateFactory#saveObject(com.anite.zebra.core.state.api.IStateObject)
	 */
	public void saveObject(IStateObject aso) throws StateFailureException {		

	}

	/* (non-Javadoc)
	 * @see com.anite.zebra.core.factory.api.IStateFactory#deleteObject(com.anite.zebra.core.state.api.IStateObject)
	 */
	public void deleteObject(IStateObject so) throws StateFailureException {
		if (so instanceof ITaskInstance){
			ITaskInstance taskInstance = (ITaskInstance)so;
			Assert.assertTrue(taskInstance.getProcessInstance().getTaskInstances().contains(taskInstance));
			taskInstance.getProcessInstance().getTaskInstances().remove(taskInstance);
			Assert.assertFalse(taskInstance.getProcessInstance().getTaskInstances().contains(taskInstance));
		}
		
	}

	/* (non-Javadoc)
	 * @see com.anite.zebra.core.factory.api.IStateFactory#createProcessInstance(com.anite.zebra.core.definitions.api.IProcessDefinition)
	 */
	public IProcessInstance createProcessInstance(IProcessDefinition processDef)
			throws CreateObjectException {
		return new MockProcessInstance(processDef);
	}

	/* (non-Javadoc)
	 * @see com.anite.zebra.core.factory.api.IStateFactory#createTaskInstance(com.anite.zebra.core.definitions.api.ITaskDefintions, com.anite.zebra.core.state.api.IProcessInstance, com.anite.zebra.core.state.api.IFOE)
	 */
	public ITaskInstance createTaskInstance(ITaskDefinition taskDef,
			IProcessInstance processInstance, IFOE foe)
			throws CreateObjectException {
		ITaskInstance taskInstance =  new MockTaskInstance(taskDef,processInstance,foe);
		processInstance.getTaskInstances().add(taskInstance);
		return taskInstance;
	}

	/* (non-Javadoc)
	 * @see com.anite.zebra.core.factory.api.IStateFactory#createFOE(com.anite.zebra.core.state.api.IProcessInstance)
	 */
	public IFOE createFOE(IProcessInstance processInstance)
			throws CreateObjectException {
		return new MockFOE(processInstance);
	}

    /* 
     * Does nothing!
     */
    public void acquireLock(IProcessInstance processInstance) throws LockException {
        
    }

    /* 
     * Does nothing
     */
    public void releaseLock(IProcessInstance processInstance) throws LockException {
        
    }
}
