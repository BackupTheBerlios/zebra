package com.anite.borris.services.security.utils;

import java.util.List;
import java.util.Map;

/**
 * User: Raymond.Offiah
 * Date: 26-May-2005
 * Time: 09:21:25
 */
public interface ConfigurationService {
    /**
     * returns the configuration table
     * @return
     */
    List getData();

    /**
     * Sets up the configuration service
     * @param list
     */
    void setData(List data);

    // Returns a map representation of the configuration
    Map getDataMap();

}
