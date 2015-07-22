package com.softserve.edu.dto.calibrator;

import java.util.List;

public class VerificationUpdatingDTO {

private List<String> idsOfVerifications;
private Long idsOfVerificators;

public List<String> getIdsOfVerifications() {
	return idsOfVerifications;
}
public void setIdsOfVerifications(List<String> idsOfVerifications) {
	this.idsOfVerifications = idsOfVerifications;
}
public Long getIdsOfVerificators() {
	return idsOfVerificators;
}
public void setIdsOfVerificators(Long idsOfVerificators) {
	this.idsOfVerificators = idsOfVerificators;
}


}
