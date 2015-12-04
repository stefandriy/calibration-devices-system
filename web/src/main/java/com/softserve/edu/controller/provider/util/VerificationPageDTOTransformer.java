package com.softserve.edu.controller.provider.util;

import com.softserve.edu.controller.calibrator.util.DistrictAndStreetComparator;
import com.softserve.edu.dto.calibrator.VerificationPlanningTaskDTO;
import com.softserve.edu.dto.provider.VerificationPageDTO;
import com.softserve.edu.entity.device.CounterType;
import com.softserve.edu.entity.verification.calibration.CalibrationTest;
import com.softserve.edu.entity.verification.Verification;

import java.time.LocalTime;
import java.util.*;

public class VerificationPageDTOTransformer {

    public static List<VerificationPageDTO> toDtoFromList(List<Verification> list){

        List<VerificationPageDTO> resultList = new ArrayList<>();

        CalibrationTest calibrationTest;
        for (Verification verification : list) {
            boolean isCalibrationTests = verification.getCalibrationTests().iterator().hasNext();
            if (isCalibrationTests) {
                calibrationTest = verification.getCalibrationTests().iterator().next();
            } else {
                calibrationTest = null;
            }
            VerificationPageDTO verificationPageDTO = new VerificationPageDTO(
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
                            verification.getBbiProtocols()==null?false:true,
                            verification.getProcessTimeExceeding(),
                            calibrationTest,
                            verification.getDevice(),
                            null,
                            null,
                            verification.getClientData().getClientAddress().getAddress(),
                            verification.getClientData().getClientAddress().getBuilding(),
                            verification.getClientData().getClientAddress().getFlat()
            );
            if(verification.getProvider()!=null){verificationPageDTO.setNameProvider(verification.getProvider().getName());}
            if(verification.getCalibrator()!=null){verificationPageDTO.setNameCalibrator(verification.getCalibrator().getName());}
            Set<CounterType> set =(verification.getDevice().getCounterTypeSet());
            if(verification.getCounter()!=null && verification.getCounter().getCounterType() != null){
                verificationPageDTO.setSymbol(verification.getCounter().getCounterType().getSymbol());
                verificationPageDTO.setStandardSize(verification.getCounter().getCounterType().getStandardSize());
                verificationPageDTO.setRealiseYear(Integer.valueOf(verification.getCounter().getReleaseYear()));
                verificationPageDTO.setDismantled(verification.getDismantled());
            }else if(set!=null) {
                List<CounterType> listCounterType = new ArrayList<>(set);
                verificationPageDTO.setSymbol(listCounterType.get(0).getSymbol());
                verificationPageDTO.setStandardSize(listCounterType.get(0).getStandardSize());
                verificationPageDTO.setRealiseYear(listCounterType.get(0).getYearIntroduction());
                verificationPageDTO.setDismantled(verification.getDismantled());
            }
            resultList.add(verificationPageDTO);
        }
        return resultList;
    }

    public static List<VerificationPlanningTaskDTO> toDoFromPageContent(List<Verification> verifications){
        List<VerificationPlanningTaskDTO> taskDTOs = new ArrayList<VerificationPlanningTaskDTO>();
        for (Verification verification : verifications) {
            taskDTOs.add(new VerificationPlanningTaskDTO(verification.getSentToCalibratorDate(),
                    verification.getId(),
                    verification.getProvider().getName(),
                    verification.getClientData().getFullName(),
                    verification.getClientData().getClientAddress().getDistrict(),
                    verification.getClientData().getClientAddress().getStreet(),
                    verification.getClientData().getClientAddress().getBuilding(),
                    verification.getClientData().getClientAddress().getFlat(),
                    verification.getClientData().getPhone(),
                    verification.getClientData().getSecondPhone(),
                    (verification.getInfo() != null) ? verification.getInfo().getDateOfVerif() : null,
                    (verification.getInfo() != null) ? verification.getInfo().getTimeFrom() : null,
                    (verification.getInfo() != null) ? verification.getInfo().getTimeTo() : null,
                    (verification.getInfo() != null) ? verification.getInfo().isServiceability() : true,
                    (verification.getInfo() != null) ? verification.getInfo().getNoWaterToDate() : null,
                    verification.isSealPresence()
            ));
        }
        return taskDTOs;
    }

}
