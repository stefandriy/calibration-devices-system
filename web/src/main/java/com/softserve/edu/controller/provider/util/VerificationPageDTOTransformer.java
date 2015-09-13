package com.softserve.edu.controller.provider.util;

import com.softserve.edu.dto.provider.VerificationPageDTO;
import com.softserve.edu.entity.CalibrationTest;
import com.softserve.edu.entity.Verification;

import java.util.ArrayList;
import java.util.List;

public class VerificationPageDTOTransformer {

    public static List<VerificationPageDTO> toDtoFromList(List<Verification> list){
        List<VerificationPageDTO> resultList = new ArrayList<VerificationPageDTO>();
        CalibrationTest calibrationTest = new CalibrationTest();
        for (Verification verification : list) {
            boolean isCalibrationTests = verification.getCalibrationTests().iterator().hasNext();
            if (isCalibrationTests) {
                calibrationTest = verification.getCalibrationTests().iterator().next();
            } else {
                calibrationTest = null;
            }
            resultList.add(new VerificationPageDTO(
                            verification.getId(),
                            verification.getInitialDate(),
                            verification.getClientData().getLastName(),
                            verification.getClientData().getClientAddress().getStreet(),
                            verification.getClientData().getClientAddress().getRegion(),
                            verification.getStatus(),
                            verification.getReadStatus(),
                            verification.getProviderEmployee(),
                            verification.getCalibratorEmployee(),
                            verification.getStateVerificatorEmployee(),
                            verification.getClientData().getFirstName(),
                            verification.getClientData().getFullName(),
                            verification.getClientData().getClientAddress().getDistrict(),
                            verification.getClientData().getClientAddress().getLocality(),
                            verification.getClientData().getPhone(),
                            verification.getBbiProtocol()==null?false:true,
                            verification.getProcessTimeExceeding(),
                            calibrationTest,
                            verification.getDevice(),
                            null,
                            null)
            );
        }
        return resultList;
    }

}
