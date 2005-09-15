/*
 * Copyright 2004/2005 Anite - Enforcement & Security
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

package com.anite.zebra.core.processdestruct;

import com.anite.zebra.core.api.IProcessDestruct;
import com.anite.zebra.core.definitions.api.IProcessDefinition;
import com.anite.zebra.core.definitions.processdef.ClassExceptionProcess;
import com.anite.zebra.core.exceptions.DestructException;
import com.anite.zebra.core.state.api.IProcessInstance;

/**
 * @author Matthew.Norris
 * Created on Aug 21, 2005
 */
public class MockProcessDestruct implements IProcessDestruct {

	private int runCount =0;
	/* (non-Javadoc)
	 * @see com.anite.zebra.core.api.IProcessDestruct#processDestruct(com.anite.zebra.core.state.api.IProcessInstance)
	 */
	public void processDestruct(IProcessInstance processInstance)
			throws DestructException {
		runCount++;
		IProcessDefinition pd;
		try {
			pd = processInstance.getProcessDef();
		} catch (Exception e) {
			throw new DestructException(e);
		}
		if (pd instanceof ClassExceptionProcess) {
			ClassExceptionProcess cep = (ClassExceptionProcess) pd;
			if (cep.failProcessDestruct) {
				throw new DestructException("Instructed to FAIL");
			}
		}

	}

	/**
	 * @return
	 *
	 * @author Matthew.Norris
	 * Created on Aug 21, 2005
	 */
	public int getRunCount() {
		return runCount;
	}

}
