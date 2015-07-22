package com.softserve.edu.dto.calibrator;

import java.util.List;

public class VerificationUpdatingDTO {

	private List<String> idsOfVerifications;
	private Long verificatorId;
	
	public List<String> getIdsOfVerifications() {
		return idsOfVerifications;
	}
	public void setIdsOfVerifications(List<String> idsOfVerifications) {
		this.idsOfVerifications = idsOfVerifications;
	}
	public Long getVerificatorId() {
		return verificatorId;
	}
	public void setVerificatorId(Long verificatorId) {
		this.verificatorId = verificatorId;
	}

}
