package com.softserve.edu.dto.state_verificator;

import java.util.List;

import com.softserve.edu.entity.Provider;

public class VerificationUpdatingDTO {
	 private List<String> idsOfVerifications;
	    private Provider provider;

	    public List<String> getIdsOfVerifications() {
	        return idsOfVerifications;
	    }

	    public void setIdsOfVerifications(List<String> idsOfVerifications) {
	        this.idsOfVerifications = idsOfVerifications;
	    }

		public Provider getProvider() {
			return provider;
		}

		public void setProvider(Provider provider) {
			this.provider = provider;
		}

	  
}
