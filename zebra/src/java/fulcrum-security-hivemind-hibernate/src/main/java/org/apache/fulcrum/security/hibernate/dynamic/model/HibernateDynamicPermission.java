package org.apache.fulcrum.security.hibernate.dynamic.model;

import java.util.Set;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.GeneratorType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

import org.apache.fulcrum.security.model.dynamic.entity.DynamicPermission;
import org.hibernate.annotations.Type;

@Entity
public class HibernateDynamicPermission extends DynamicPermission {

    @Override
    @ManyToMany
    public Set<HibernateDynamicRole> getRolesAsSet() {

        return super.getRolesAsSet();
    }

    @Override
    @Id(generate=GeneratorType.AUTO)
    @Type(type="long")
    public Object getId() {
        // TODO Auto-generated method stub
        return super.getId();
    }

    @Override
    @Basic
    public String getName() {
        // TODO Auto-generated method stub
        return super.getName();
    }
    

}
