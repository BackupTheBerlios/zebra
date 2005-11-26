/*
 * Copyright 2004 2005 Anite - Central Government Division
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

package com.anite.zebra.hivemind.om.state;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratorType;
import javax.persistence.Id;

/**
 * @author Matthew.Norris
 * @author Ben Gidley
 * @author John Rae
 */
@Entity
public class ZebraPropertySetEntry {

    private Integer propertySetId;

    private String value=null;
    private Serializable object=null;

    /** Version Flag for serialisation   */
    static final long serialVersionUID = 1L;

   
    public ZebraPropertySetEntry(){
    	//noop
    }
    
    /**
	 * Constructor taking String
	 * @param value
	 */
	public ZebraPropertySetEntry(String value) {
		setValue(value);
	}

	/**
	 * Constructor taking Object
	 * @param object
	 */
	public ZebraPropertySetEntry(Serializable object) {
		setObject(object);
	}
    
    /**
     * 
     */
	@Id(generate=GeneratorType.AUTO)
    public Integer getId() {
        return this.propertySetId;
    }

    public void setId(Integer propertySetId) {
        this.propertySetId = propertySetId;
    }

    /**
     * A string property set value
     * @hibernate.property length="4000"
     * @return Returns the value.
     * 
     */
    @Basic
    @Column(length=4000)
    public String getValue() {
        return this.value;
    }

    /**
     * @param value The value to set.
     */
    public void setValue(String value) {
        this.value = value;
    }

    /**
     * This is a serialized object. 100k is the max length here
     * but in many databases hibernate will pick a bigger type.
     * 
     * In oracle this is created as a long raw. Many DBA's will tell
     * you this is bad. They are right (Blobs could be better) and wrong 
     * (Oracle JDBC does not support this sufficently). Unfortunately Oracle
     * BLOB support is too different from other JDBC drivers for it
     * to work here. 
     * 
     * @hibernate.property type="serializable" length="1000000"
     * @return Returns the object.
     */
    @Basic
    public Serializable getObject() {
        return this.object;
    }

    /**
     * @param object The object to set.
     */
    public void setObject(Serializable object) {
        this.object = object;
    }
}