package com.softserve.edu.controller.provider.util;

import java.util.ArrayList;
import java.util.List;

import com.softserve.edu.dto.provider.VerificationPageDTO;
import com.softserve.edu.entity.Verification;

import org.springframework.data.domain.Page;

	public class VerificationPageDTOTransformer {
    public static Page<VerificationPageDTO> toDTO(Page<Verification> verificationPage) {
        return verificationPage
                .map(verification -> new VerificationPageDTO(
                                verification.getId(),
                                verification.getInitialDate(),
                                verification.getClientData().getLastName(),
                                verification.getClientData().getClientAddress().getStreet(),
                                verification.getStatus(),
                                verification.getReadStatus(), verification.getProviderEmployee(), verification.getCalibratorEmployee(), verification.getStateVerificatorEmployee(),
                                verification.getClientData().getFirstName(), verification.getClientData().getClientAddress().getDistrict(),
                                verification.getClientData().getClientAddress().getLocality(),
                                verification.getClientData().getPhone())
                );
    }

    public static Long getCount(Long countId){
    	return countId ;
    }

    public static List<VerificationPageDTO> toDtoFromList(List<Verification> list){
    	
    	List<VerificationPageDTO> resultList = new ArrayList<VerificationPageDTO>();
			    	for (Verification verification : list) {
			    		 resultList.add(new VerificationPageDTO(
			                     verification.getId(),
			                     verification.getInitialDate(),
			                     verification.getClientData().getLastName(),
			                     verification.getClientData().getClientAddress().getStreet(),
			                     verification.getStatus(),
                                         verification.getReadStatus(), verification.getProviderEmployee(), verification.getCalibratorEmployee(), verification.getStateVerificatorEmployee(),
                                         verification.getClientData().getFirstName(), verification.getClientData().getClientAddress().getDistrict(),
                                         verification.getClientData().getClientAddress().getLocality(),
                                         verification.getClientData().getPhone())
                         );
                    }
			return resultList;
		}

}
