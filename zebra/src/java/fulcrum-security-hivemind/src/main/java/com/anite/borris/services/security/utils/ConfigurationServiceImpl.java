/**
 * User: Raymond.Offiah
 * Date: 11-Apr-2005
 * Time: 12:13:36
 */
package com.anite.borris.services.security.utils;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * This class reads and stores the configuration information from
 * the hivemindmodule.xml file
 */
public class ConfigurationServiceImpl implements ConfigurationService {

    private List data;

    private Map dataMap;

    public List getData() {
        return data;
    }

    public void setData(List data) {
        this.data = data;
    }

    // Returns a map representation of the configuration
    public Map getDataMap() {

        dataMap = new HashMap();

        for (Iterator iterator = getData().iterator(); iterator.hasNext();) {

            Configuration configuration = (Configuration) iterator.next();
            dataMap.put(configuration.getKey(), configuration.getValue());
        }

        return dataMap;
    }
}
