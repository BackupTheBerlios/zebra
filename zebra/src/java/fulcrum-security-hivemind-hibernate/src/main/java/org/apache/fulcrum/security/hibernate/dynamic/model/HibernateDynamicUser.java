package org.apache.fulcrum.security.hibernate.dynamic.model;

import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.GeneratorType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.TemporalType;

import org.apache.fulcrum.security.model.dynamic.entity.DynamicUser;
import org.hibernate.annotations.CollectionOfElements;
import org.hibernate.annotations.Type;

@Entity
public class HibernateDynamicUser extends DynamicUser {

    @Override
    @ManyToMany(mappedBy="delegators")
    public Set<HibernateDynamicUser> getDelegatees() {
        return super.getDelegatees();
    }

    @Override
    @ManyToMany
    @JoinTable(table = @Table(name = "HIBUSER_DELEGATES"), joinColumns = { @JoinColumn(name = "DELEGATOR_ID") }, inverseJoinColumns = { @JoinColumn(name = "DELEGATEE_ID") })
    public Set<HibernateDynamicUser> getDelegators() {
        return super.getDelegators();
    }

    @Override
    @Basic
    public String getPassword() {
        return super.getPassword();
    }

    @Override
    @Id(generate = GeneratorType.AUTO)
    @Type(type = "long")
    public Object getId() {
        return super.getId();
    }

    @Override
    @Basic
    public String getName() {

        return super.getName();
    }

    @Override
    @ManyToMany
    public Set<HibernateDynamicGroup> getGroupsAsSet() {
        return super.getGroupsAsSet();
    }
    
    @Override
    @Basic(temporalType = TemporalType.DATE)
    public Date getPasswordExpiryDate() {
    	return super.getPasswordExpiryDate();
    }
    
    @Override
    @Basic
    public long getLockTime() {
    	return super.getLockTime();
    }
    
    @Override
    @Basic
    public int getLoginAttempts() {
    	return super.getLoginAttempts();
    }
    
    @Override
    @CollectionOfElements
    public List<String> getPasswordHistory() {
    	return super.getPasswordHistory();
    }

}
