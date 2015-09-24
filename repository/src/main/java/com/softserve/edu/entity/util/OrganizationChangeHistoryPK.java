package com.softserve.edu.entity.util;

import com.softserve.edu.entity.Organization;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by vova on 22.09.15.
 */
@Getter
@Setter
@Embeddable
public class OrganizationChangeHistoryPK implements Serializable{

    private Date date;
    private Long orgId;

    public OrganizationChangeHistoryPK() {
    }

    public OrganizationChangeHistoryPK(Date date, Long organizationId) {
        this.date = date;
        this.orgId = organizationId;
    }
}
