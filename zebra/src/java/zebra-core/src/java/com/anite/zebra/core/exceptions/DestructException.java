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

package com.anite.zebra.core.exceptions;

/**
 * @author Matthew.Norris
 */
public class DestructException extends BaseZebraException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	public DestructException() {
		super();
	}

	/**
	 * @param message
	 */
	public DestructException(String message) {
		super(message);
	}

	/**
	 * @param message
	 * @param nestedException
	 */
	public DestructException(
		String message,
		Throwable nestedException) {
		super(message, nestedException);
	}

	/**
	 * @param nestedException
	 */
	public DestructException(Throwable nestedException) {
		super(nestedException);
	}

}
