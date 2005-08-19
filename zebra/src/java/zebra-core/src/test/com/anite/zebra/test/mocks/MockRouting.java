/**
 * @author matt
 * Created on 19-Aug-2005
 */
package com.anite.zebra.test.mocks;

import junit.framework.Assert;

import com.anite.zebra.core.definitions.api.IRoutingDefinition;
import com.anite.zebra.core.definitions.api.ITaskDefinition;
import com.anite.zebra.test.mocks.taskdefs.MockTaskDef;

/**
 * @author matt
 * Created on 19-Aug-2005
 *
 */
public class MockRouting implements IRoutingDefinition {
	private static long idCounter = 1;
	private ITaskDefinition destinationTaskDefinition;
	private ITaskDefinition originatingTaskDefinition;
	private String conditionClass;
	private boolean parallel;
	private String name;
	private Long id;

	public MockRouting(MockTaskDef source, MockTaskDef dest) {
		MockProcessDef pd = source.getProcessDef();
		Assert.assertTrue(pd.equals(dest.getProcessDef()));
		this.id = new Long(idCounter++);
		pd.getMockRoutingDefs().add(this);
		source.getRoutingOut().add(this);
		dest.getRoutingIn().add(this);
		this.destinationTaskDefinition = dest;
		this.originatingTaskDefinition = source;
	}
	
	/* (non-Javadoc)
	 * @see com.anite.zebra.core.definitions.api.IRoutingDefinition#getId()
	 */
	public Long getId() {
		return id;
	}

	/* (non-Javadoc)
	 * @see com.anite.zebra.core.definitions.api.IRoutingDefinition#getName()
	 */
	public String getName() {
		return name;
	}

	/* (non-Javadoc)
	 * @see com.anite.zebra.core.definitions.api.IRoutingDefinition#getParallel()
	 */
	public boolean getParallel() {
		return parallel;
	}

	/* (non-Javadoc)
	 * @see com.anite.zebra.core.definitions.api.IRoutingDefinition#getConditionClass()
	 */
	public String getConditionClass() {
		return conditionClass;
	}

	/* (non-Javadoc)
	 * @see com.anite.zebra.core.definitions.api.IRoutingDefinition#getOriginatingTaskDefinition()
	 */
	public ITaskDefinition getOriginatingTaskDefinition() {
		return originatingTaskDefinition;
	}

	/* (non-Javadoc)
	 * @see com.anite.zebra.core.definitions.api.IRoutingDefinition#getDestinationTaskDefinition()
	 */
	public ITaskDefinition getDestinationTaskDefinition() {
		return destinationTaskDefinition;
	}

	/**
	 * @author matt
	 * Created on 19-Aug-2005
	 *
	 * @param b
	 */
	public void setParallel(boolean parallel) {
		this.parallel = parallel;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setConditionClass(String conditionClass) {
		this.conditionClass = conditionClass;
	}

}
