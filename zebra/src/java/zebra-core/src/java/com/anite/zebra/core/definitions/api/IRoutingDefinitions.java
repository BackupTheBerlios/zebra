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

package com.anite.zebra.core.definitions.api;

import java.util.Iterator;


/**
 * @author Matthew Norris
 */
public interface IRoutingDefinitions{
	
	/**
	 * @return returns the ProcessDef this RoutingDefs belongs to
	 * @todo check on this guy, I think it isn't required.  Checked all of CTMS
	 */
	//public IProcessDefinition getProcessDef();
	public Iterator iterator();
	
	
}
