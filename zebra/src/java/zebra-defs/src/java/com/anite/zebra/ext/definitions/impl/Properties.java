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

package com.anite.zebra.ext.definitions.impl;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.convert1.ConvertRegistry;
import org.apache.commons.convert1.ConvertUtils;

import com.anite.zebra.ext.definitions.api.IProperties;

/**
 * 
 * @author Eric Pugh
 * @@hibernate.class  
 */

public class Properties implements IProperties {
    private Long id;
    /**
     * @return Returns the id.
     * @hibernate.id generator-class="native"
     */
    public Long getId() {
        return id;
    }
    /**
     * @param id The id to set.
     */
    protected void setId(Long id) {
        this.id = id;
    }
    private String name;

    /* (non-Javadoc)
     * @see com.anite.zebra.ext.definitions.api.IProperties#containsKey(java.lang.String)
     */
    public boolean containsKey(String key) {
        return properties.containsKey(key);
    }
    private Map properties = new HashMap();

    private static ConvertRegistry convertRegistry = new ConvertRegistry();

    static {
        ConvertUtils.registerStandardFromStringConverters(convertRegistry);
    }

    /**
     * @return Returns the name.
     * @hibernate.property
     */
    public String getName() {
        return name;
    }

    /**
     * @param name
     *            The name to set.
     */
    public void setName(String name) {
        this.name = name;
    }

    public Object get(Object key) {
        return properties.get(key);
    }

    public String getString(String key) {
        return (String) convertRegistry.convert(properties.get(key), String.class);
    }

    public Long getLongAsObj(String key) {
        return (Long) convertRegistry.convert(properties.get(key), Long.class);
    }

    public long getLong(String key) {
        return getLongAsObj(key).longValue();
    }

    public Integer getIntegerAsObj(String key) {
        return (Integer) convertRegistry.convert(properties.get(key), Integer.class);
    }

    public int getInteger(String key) {
        return getIntegerAsObj(key).intValue();
    }
    public Boolean getBooleanAsObj(String key) {
        return (Boolean) convertRegistry.convert(properties.get(key), Boolean.class);
    }

    public boolean getBoolean(String key) {
        return getBooleanAsObj(key).booleanValue();
    }

    public void add(String key, String value) {
        properties.put(key, value);
    }
    public void put(String key, String value) {
        properties.put(key, value);
    }

    /**
     * @return Returns the properties.
     * @hibernate.map cascade = "all" lazy = "false"
     * @hibernate.collection-key column ="propertyId" 
     * @hibernate.collection-index column="propName" type="string"
     * @hibernate.collection-element column="propValue" type="string"
     */
    protected Map getProperties() {
        return properties;
    }

    /**
     * @param properties
     *            The properties to set.
     */
    protected void setProperties(Map properties) {
        this.properties = properties;
    }
    
    public Iterator keys(){
        return properties.keySet().iterator();
    }
}