package com.softserve.edu.entity.catalogue;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.persistence.*;

import static com.softserve.edu.entity.catalogue.util.Checker.checkForEmptyText;
import static com.softserve.edu.entity.catalogue.util.Checker.checkForNull;

@Entity
@Table(name="`STREET`")
public class Street extends AbstractCatalogue {
    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private String designation;

    @ManyToOne(optional = false)
    @JoinColumn(name = "locality_id", nullable = false)
    private Locality locality;
    @ManyToOne
    @JoinColumn(name = "streetType")
    private StreetType streetType;


    protected Street() {}


    public Street(String designation,Locality locality, StreetType streetType) {
        setDesignation(designation);
        setLocality(locality);
        setStreetType(streetType);
    }

    @Override
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String getDesignation() {
        return designation;
    }

    private void setDesignation(String designation) {
        checkForEmptyText(designation);
        this.designation = designation;
    }

    public Locality getLocality() {
        return locality;
    }

    private void setLocality(Locality locality) {
        checkForNull(locality);
        this.locality = locality;
    }
    public StreetType getStreetType() {
        return streetType;
    }

    public void setStreetType(StreetType streetType) {

        this.streetType = streetType;
    }


    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("id", id)
                .append("designation", designation)
                .append("locality", locality)
                .append("streetType", streetType)
                .toString();
    }

    @Override
    public int hashCode(){
        return new HashCodeBuilder()
                .append(id)
                .append(designation)
                .append(locality)
                .append(streetType)
                .toHashCode();
    }

    @Override
    public boolean equals(final Object obj){
        if(obj instanceof Street){
            final Street other = (Street) obj;
            return new EqualsBuilder()
                    .append(id, other.id)
                    .append(designation, other.designation)
                    .append(locality, other.locality)
                    .append(streetType, other.streetType)
                    .isEquals();
        } else{
            return false;
        }
    }
}
