package com.softserve.edu.dto.state_verificator;

import java.util.List;


import com.softserve.edu.entity.Organization;

public class VerificationUpdatingDTO {
	 private List<String> idsOfVerifications;
	 private Long idsOfProviders;

	public Long getIdsOfProviders() {
		return idsOfProviders;
	}

	public void setIdsOfProviders(Long idsOfProviders) {
		this.idsOfProviders = idsOfProviders;
	}

	public List<String> getIdsOfVerifications() {
		return idsOfVerifications;
	}

	public void setIdsOfVerifications(List<String> idsOfVerifications) {
		this.idsOfVerifications = idsOfVerifications;
	}
}
