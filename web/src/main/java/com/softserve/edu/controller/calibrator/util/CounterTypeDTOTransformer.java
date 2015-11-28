package com.softserve.edu.controller.calibrator.util;


import com.softserve.edu.entity.device.CounterType;
import com.softserve.edu.dto.admin.CounterTypeDTO;

import java.util.ArrayList;
import java.util.List;

public class CounterTypeDTOTransformer {

    public static List<CounterTypeDTO> toDtofromList(List<CounterType> list) {
        List<CounterTypeDTO> resultList = new ArrayList<>();
        for (CounterType counterType : list) {
            resultList.add(new com.softserve.edu.dto.admin.CounterTypeDTO(
                    counterType.getId(),
                    counterType.getName(),
                    counterType.getSymbol(),
                    counterType.getStandardSize(),
                    counterType.getManufacturer(),
                    counterType.getCalibrationInterval(),
                    counterType.getYearIntroduction(),
                    counterType.getGost(),
                    counterType.getDevice().getId()
            ));
        }
        return resultList;
    }
}
