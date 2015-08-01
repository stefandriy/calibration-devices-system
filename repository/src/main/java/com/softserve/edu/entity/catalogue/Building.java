package com.softserve.edu.entity.catalogue;

import static com.softserve.edu.entity.catalogue.util.Checker.checkForEmptyText;
import static com.softserve.edu.entity.catalogue.util.Checker.checkForNull;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

@Entity
@Table(name="`BUILDING`")
public class Building extends AbstractCatalogue {
    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private String designation;

    @ManyToOne
    @JoinColumn(name = "street_id", nullable = false)
    private Street street;

    protected Building() {}

    public Building(Street street, String designation) {
        setStreet(street);
        setDesignation(designation);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDesignation() {
        return designation;
    }

    private void setDesignation(String designation) {
        checkForEmptyText(designation);
        this.designation = designation;
    }

    public Street getStreet() {
        return street;
    }

    public void setStreet(Street street) {
        checkForNull(street);
        this.street = street;
    }
    
    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("id", id)
                .append("designation", designation)
                .append("street", street)
                .toString();
    }
    
    @Override
    public int hashCode(){
        return new HashCodeBuilder()
        		.append(id)
                .append(designation)
                .append(street)
                .toHashCode();
    }

    @Override
    public boolean equals(final Object obj){
        if(obj instanceof Building){
            final Building other = (Building) obj;
            return new EqualsBuilder()
            		.append(id, other.id)
                    .append(designation, other.designation)
                    .append(street, other.street)
                    .isEquals();
        } else{
            return false;
        }
    }
}
