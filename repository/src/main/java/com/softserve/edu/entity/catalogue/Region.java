package com.softserve.edu.entity.catalogue;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.persistence.*;

import static com.softserve.edu.entity.catalogue.util.Checker.checkForEmptyText;

@Entity
@Table(name="`REGION`")
public class Region extends AbstractCatalogue {
    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private String designation;

    protected Region() {}

    public Region(String name) {
        this.designation = name;
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

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("id", id)
                .append("designation", designation)

                .toString();
    }

    @Override
    public int hashCode(){
        return new HashCodeBuilder()
                .append(id)
                .append(designation)

                .toHashCode();
    }

    @Override
    public boolean equals(final Object obj){
        if(obj instanceof Region){
            final Region other = (Region) obj;
            return new EqualsBuilder()
                    .append(id, other.id)
                    .append(designation, other.designation)
                   .isEquals();
        } else{
            return false;
        }
    }
}
