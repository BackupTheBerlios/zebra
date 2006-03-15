package com.anite.zebra.hivemind.api;

import com.anite.zebra.core.factory.api.IStateFactory;

public interface ZebraStateFactory extends IStateFactory {
    /**
     * Event listener for events (e.g. taskInstanceCreated)
     * @param listener
     */
    public abstract void addStateFactoryListener(StateFactoryListener listener);

    public abstract void removeStateFactoryListener(StateFactoryListener listener);

}