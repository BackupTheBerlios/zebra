package com.anite.zebra.hivemind.impl;

import java.util.List;
import java.util.Map;

import org.apache.fulcrum.security.entity.User;

import com.anite.zebra.core.exceptions.DefinitionNotFoundException;
import com.anite.zebra.core.exceptions.StartProcessException;
import com.anite.zebra.core.exceptions.TransitionException;

public class Zebra<PD, TD, PI, TI>{

	public Map getAllProcessDefinitions() {
		// TODO Auto-generated method stub
		return null;
	}

	public PD getProcessDefinitions() throws DefinitionNotFoundException {
		// TODO Auto-generated method stub
		return null;
	}

	public PI createProcessPaused(String processName) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<TI> getTaskList(User user) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<TI> getOnlyOwnedTaskList(User user) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<TI> getOnlyDelegatedTaskList(User user) {
		// TODO Auto-generated method stub
		return null;
	}

	public void startProcess(PI processInstance)
			throws StartProcessException {
		// TODO Auto-generated method stub

	}

	public void transitionTask(TI taskInstance) throws TransitionException {
		// TODO Auto-generated method stub

	}

}
