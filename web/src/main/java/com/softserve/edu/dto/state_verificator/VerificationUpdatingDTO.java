package com.softserve.edu.dto.state_verificator;

import java.util.List;


import com.softserve.edu.entity.Organization;

public class VerificationUpdatingDTO {
	 private List<String> idsOfVerifications;
	    private Organization provider;

	    public List<String> getIdsOfVerifications() {
	        return idsOfVerifications;
	    }

	    public void setIdsOfVerifications(List<String> idsOfVerifications) {
	        this.idsOfVerifications = idsOfVerifications;
	    }

		public Organization getProvider() {
			return provider;
		}

		public void setProvider(Organization provider) {
			this.provider = provider;
		}

	  
}
