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


/**
 * @author Matthew Norris
 */
public interface IRoutingDefinition {
	/**
	 * @return Returns the guid.
	 */
	public Long getId();
	/**
	 * @return Returns the name.
	 */
	public String getName();
	/**
	 * @return Returns the parallel.
	 */
	public boolean getParallel();
	/**
	 * @return Returns the conditionClass.
	 */
	public String getConditionClass();
	/**
	 * @return Returns the Originating TaskDef
	 */
	public ITaskDefinition getOriginatingTaskDefinition();
	
	/**
	 * @return Returns the Desintation TaskDef
	 */
	
	public ITaskDefinition getDestinationTaskDefinition();
}