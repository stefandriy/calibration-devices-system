package com.softserve.edu.service.admin;


import com.softserve.edu.entity.device.CounterType;
import com.softserve.edu.service.utils.ListToPageTransformer;

import java.util.List;
import java.util.Set;

public interface CounterTypeService {
    Set<CounterType> findAll();
    /**
     * Save counter type with params
     * @param name
     * @param symbol
     * @param standardSize
     * @param manufacturer
     * @param calibrationInterval
     * @param yearIntroduction
     * @param gost
     * @param deviceId
     */
    void addCounterType(String name, String symbol, String standardSize, String manufacturer,
                        Integer calibrationInterval, Integer yearIntroduction, String gost, Long deviceId);

    /**
     * Edit counter type with params
     * @param id
     * @param name
     * @param symbol
     * @param standardSize
     * @param manufacturer
     * @param calibrationInterval
     * @param yearIntroduction
     * @param gost
     * @param deviceId
     */
    void editCounterType(Long id, String name, String symbol, String standardSize, String manufacturer,
                         Integer calibrationInterval, Integer yearIntroduction, String gost, Long deviceId);

    /**
     * Delete counter type by his id
     * @param id
     */
    void removeCounterType(Long id);

    /**
     * Find counter type by id
     * @param id
     * @return
     */
    CounterType findById(Long id);

    /**
     * Service for building page by SortCriteria, SortOrder and Searching data
     * @param pageNumber
     * @param itemsPerPage
     * @param name
     * @param symbol
     * @param standardSize
     * @param manufacturer
     * @param calibrationInterval
     * @param yearIntroduction
     * @param gost
     * @param sortCriteria
     * @param sortOrder
     * @return
     */
    ListToPageTransformer<CounterType> getCounterTypeBySearchAndPagination(int pageNumber, int itemsPerPage, String name,
                                                                           String symbol, String standardSize,
                                                                           String manufacturer, Integer calibrationInterval,
                                                                           Integer yearIntroduction, String gost,
                                                                          String sortCriteria, String sortOrder);


}
