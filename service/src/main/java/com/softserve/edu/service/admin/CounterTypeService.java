package com.softserve.edu.service.admin;


import com.softserve.edu.entity.device.CounterType;
import com.softserve.edu.service.utils.ListToPageTransformer;

import java.util.List;

public interface CounterTypeService {

    void addCounterType(String name, String symbol, String standardSize, String manufacturer,
                        Integer calibrationInterval, String yearIntroduction, String gost, Long deviceId);
    void editCounterType(Long id, String name, String symbol, String standardSize, String manufacturer,
                         Integer calibrationInterval, String yearIntroduction, String gost, Long deviceId);
    void removeCounterType(Long id);

    CounterType findById(Long id);

    ListToPageTransformer<CounterType> getCounterTypeBySearchAndPagination(int pageNumber, int itemsPerPage, String name,
                                                                           String symbol, String standardSize,
                                                                           String manufacturer, Integer calibrationInterval,
                                                                           String yearIntroduction, String gost,
                                                                          String sortCriteria, String sortOrder);


}
