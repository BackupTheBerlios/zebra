/*
 * Copyright 2005 Anite - Central Government Division
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
package com.anite.zebra.test.mocks.taskdefs;

import com.anite.zebra.test.mocks.MockProcessDef;

/**
 * @author Matthew Norris
 * Created on 19-Aug-2005
 *
 */
public class JoinTaskDef extends AutoRunTaskDef {

	/**
	 * @author Matthew Norris
	 * Created on 19-Aug-2005
	 *
	 * @param pd
	 * @param taskName
	 */
	public JoinTaskDef(MockProcessDef pd, String taskName) {
		super(pd, taskName);
		this.setSynchronised(true);
	}

}
