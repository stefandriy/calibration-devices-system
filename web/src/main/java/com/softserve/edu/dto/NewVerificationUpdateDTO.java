package com.softserve.edu.dto;

import java.util.List;

/**
 * Created by Lyubomyr on 17.11.2015.
 */
public class NewVerificationUpdateDTO {
    private List<String> idsOfVerifications;
    private Long organizationId = 1L;

    public List<String> getIdsOfVerifications() {
        return idsOfVerifications;
    }

    public void setIdsOfVerifications(List<String> idsOfVerifications) {
        this.idsOfVerifications = idsOfVerifications;
    }

    public Long getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Long organizationId) {
        this.organizationId = organizationId;
    }
}
