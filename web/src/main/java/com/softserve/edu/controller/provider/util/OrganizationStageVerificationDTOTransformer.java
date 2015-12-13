package com.softserve.edu.controller.provider.util;


import com.softserve.edu.dto.provider.OrganizationStageVerificationDTO;
import com.softserve.edu.entity.Address;
import com.softserve.edu.entity.device.Counter;
import com.softserve.edu.entity.organization.Organization;
import com.softserve.edu.entity.verification.ClientData;
import com.softserve.edu.entity.verification.calibration.AdditionalInfo;

public class OrganizationStageVerificationDTOTransformer {

    public static OrganizationStageVerificationDTO toDtoFromVerification(ClientData clientData,
                                  Address address, String verificationId, Organization calibrator, String comment,
                                  AdditionalInfo info, Boolean dismantled, Counter counter) {

        String calibratorName = (calibrator != null) ? calibrator.getName() : null;

        String entrance = (info != null) ? "" + info.getEntrance() : null;
        String doorCode = (info != null) ? "" + info.getDoorCode() : null;
        String floor = (info != null) ? "" + info.getFloor() : null;
        Long dateOfVerif = (info != null && info.getDateOfVerif() != null) ? info.getDateOfVerif().getTime() : null;
        Boolean serviceability = (info != null) ? info.getServiceability() : null;
        Long noWaterToDate = (info != null && info.getNoWaterToDate() != null) ? info.getNoWaterToDate().getTime() : null;
        String notes = (info != null) ? info.getNotes() : null;
        String time = (info != null) ? info.getTimeFrom() + "-" + info.getTimeTo() : null;

        Long dateOfDismantled = ( counter != null && counter.getDateOfDismantled() != null) ?
                counter.getDateOfDismantled().getTime() : null;
        Long dateOfMounted = (counter != null && counter.getDateOfMounted() != null) ?
                counter.getDateOfMounted().getTime() : null;
        String numberCounter = (counter != null) ? counter.getNumberCounter() : null;
        String releaseYear = (counter != null) ? counter.getReleaseYear() : null;
        String symbol = (counter != null && counter.getCounterType() != null) ? counter.getCounterType().getSymbol() : null;
        String standardSize = (counter != null && counter.getCounterType() != null) ?
                counter.getCounterType().getStandardSize() : null;

        return new OrganizationStageVerificationDTO(clientData, address, verificationId, calibratorName, entrance, doorCode, floor, dateOfVerif,
                serviceability, noWaterToDate, notes, time, dismantled, dateOfDismantled, dateOfMounted, numberCounter,
                releaseYear, symbol, standardSize);
    }
}
