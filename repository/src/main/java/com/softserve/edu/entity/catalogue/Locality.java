package com.softserve.edu.entity.catalogue;

import com.softserve.edu.entity.Organization;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.persistence.*;

import java.util.HashSet;
import java.util.Set;

import static com.softserve.edu.entity.catalogue.util.Checker.checkForEmptyText;
import static com.softserve.edu.entity.catalogue.util.Checker.checkForNull;

@Entity
@Table(name="`LOCALITY`")
@Getter
@Setter
public class Locality extends AbstractCatalogue {
    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private String designation;

    @ManyToOne
    @JoinColumn(name = "district_id", nullable = false)
    private District district;
    
    @Column
    private String mailIndex;

    @ManyToMany(mappedBy = "localities")
    private Set<Organization> organizations;
    
    protected Locality() {}

    public Locality(District district, String designation) {
        this.district = district;
        this.designation = designation;
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

    public District getDistrict() {
        return district;
    }

    private void setDistrict(District district) {
        checkForNull(district);
        this.district = district;
    }



	public String getMailIndex() {
		return mailIndex;
	}

	public void setMailIndex(String mailIndex) {
		this.mailIndex = mailIndex;
	}

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("id", id)
                .append("designation", designation)
                .append("district", district)
                .append("mailIndex", mailIndex)
                .toString();
    }

    @Override
    public int hashCode(){
        return new HashCodeBuilder()
                .append(id)
                .append(designation)
                .append(district)
                .append(mailIndex)
                .toHashCode();
    }

    @Override
    public boolean equals(final Object obj){
        if(obj instanceof Locality){
            final Locality other = (Locality) obj;
            return new EqualsBuilder()
                    .append(id, other.id)
                    .append(designation, other.designation)
                    .append(district, other.district)
                    .append(mailIndex, other.mailIndex)
                    .isEquals();
        } else{
            return false;
        }
    }


	
	
}
