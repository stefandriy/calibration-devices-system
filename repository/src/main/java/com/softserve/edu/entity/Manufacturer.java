package com.softserve.edu.entity;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name="`MANUFACTURER`")
public class Manufacturer {
    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private String name;

    @OneToMany
    @JoinColumn(name = "manufacturer_id")
    private Set<Device> devices;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Device> getDevices() {
        return devices;
    }

    public void setDevices(Set<Device> devices) {
        this.devices = devices;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("id", id)
                .append("name", name)
                .toString();
    }

    @Override
    public int hashCode(){
        return new HashCodeBuilder()
                .append(id)
                .append(name)
                .toHashCode();
    }

    @Override
    public boolean equals(final Object obj){
        if(obj instanceof Manufacturer){
            final Manufacturer other = (Manufacturer) obj;
            return new EqualsBuilder()
                    .append(id, other.id)
                    .append(name, other.name)
                    .isEquals();
        } else{
            return false;
        }
    }


}
