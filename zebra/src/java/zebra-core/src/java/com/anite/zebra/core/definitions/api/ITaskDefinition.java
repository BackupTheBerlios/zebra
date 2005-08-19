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

import java.util.Set;


/**
 * @author Matthew Norris
 */
public interface ITaskDefinition {
	/**
	 * @return Returns the guid.
	 */
	public Long getId();
	/**
	 * indicates if the task runs automatically as soon as it is created by the engine
	 * @return 
	 */
	public boolean isAuto();
	/**
	 * 
	 * This is the class that must conform to the ITaskAction interface that is called when the task is transitioned by the Engine. Can be null.
	 * 
	 * @return Returns the className.
	 */
	public String getClassName();
	/**
	 * indicates whether this task is a Synchronise task
	 * @return Returns the synchronise.
	 */
	public boolean isSynchronised();
	
	/**
	 * @return Returns the ProcessDef this TaskDef belongs to
	 * @todo check and see if this method is ever used.  I searched through
	 * CTMS and never saw a usage.  Except in one Avalon componetn that loaded it up
	 * and could have done it directly because it had the process def id.
	 */	
	//public IProcessDefinition getProcessDef();

	/**
	 * @return returns the Outbound RoutingDefs from this TaskDef
	 */
	public Set getRoutingOut();
	
	/**
	 * returns the inbound routingdefs leading to this TaskDef 
	 * @return
	 */
	public Set getRoutingIn(); 
	
	/**
	 * returns the class name to run when the task is first created
	 * @return
	 */
	public String getClassConstruct();
	/**
	 * returns the name of the class to run when the task has completed
	 * @return
	 */
	public String getClassDestruct();
}