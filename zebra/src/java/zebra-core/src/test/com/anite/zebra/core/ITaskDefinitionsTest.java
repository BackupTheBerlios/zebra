package com.anite.zebra.core;

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

import java.util.Iterator;

import junit.framework.TestCase;

import com.anite.zebra.test.mocks.AutoRunTaskDef;
import com.anite.zebra.test.mocks.ManualRunTaskDef;
import com.anite.zebra.test.mocks.MockProcessDef;
import com.anite.zebra.test.mocks.MockTaskDefinitions;

/**
 * @author Eric.Pugh
 */
public class ITaskDefinitionsTest extends TestCase {

	public void testIterator() throws Exception {

	    MockTaskDefinitions td = new MockTaskDefinitions(new MockProcessDef());
	    td.add(new AutoRunTaskDef());
	    td.add(new ManualRunTaskDef());
	    Iterator i =td.iterator();
	    assertNotNull(i);
	    assertTrue(i.hasNext());
	    i.next();
	    assertTrue(i.hasNext());
	    i.next();
	    assertFalse(i.hasNext());

	}
}