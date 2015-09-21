package com.softserve.edu.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.softserve.edu.entity.util.OrganizationTypeValue;
import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Embeddable
@EqualsAndHashCode
public class OrganizationType {

    @Enumerated(EnumType.STRING)
    private OrganizationTypeValue typeValue;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "organization_id")
    private Organization organization;

    public OrganizationType(OrganizationTypeValue typeValue) {
        this.typeValue = typeValue;
    }

    public String getTypeValue() {
        return typeValue.name();
    }
}
