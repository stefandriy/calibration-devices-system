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
@Table(name="`DISTRICT`")
public class District extends AbstractCatalogue {
    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private String designation;

    @ManyToOne
    @JoinColumn(name = "region_id", nullable = false)
    private Region region;

    protected District() {}

    public District(String name, Region region) {
        this.designation = name;
        this.region = region;
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

    public Region getRegion() {
        return region;
    }

    private void setRegion(Region region) {
        checkForNull(region);
        this.region = region;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("id", id)
                .append("designation", designation)
                .append("region", region)
                .toString();
    }
    
    @Override
    public int hashCode(){
        return new HashCodeBuilder()
        		.append(id)
                .append(designation)
                .append(region)
                .toHashCode();
    }

    @Override
    public boolean equals(final Object obj){
        if(obj instanceof District){
            final District other = (District) obj;
            return new EqualsBuilder()
            		.append(id, other.id)
                    .append(designation, other.designation)
                    .append(region, other.region)
                    .isEquals();
        } else{
            return false;
        }
    }
}
