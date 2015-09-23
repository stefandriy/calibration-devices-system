package com.softserve.edu.entity.util;

import com.softserve.edu.entity.Organization;

import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by vova on 22.09.15.
 */
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

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Long getOrganizationId() {
        return orgId;
    }

    public void setOrganizationId(Long organizationId) {
        this.orgId = organizationId;
    }
}
