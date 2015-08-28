package com.softserve.edu.dto;

import java.util.List;

public class VerificationUpdateDTO {

	private List<String> idsOfVerifications;
	private Long organizationId;

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
