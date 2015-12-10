package com.softserve.edu.specification;

import com.softserve.edu.entity.device.CalibrationModule;
import com.softserve.edu.entity.verification.calibration.CalibrationTask;
import org.apache.log4j.Logger;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public final class CalibrationTaskSpecificationBuilder extends SpecificationBuilder<CalibrationTask> {

    static Logger logger = Logger.getLogger(AgreementSpecificationBuilder.class);

    private static final String PHONE_NUMBER = "phoneNumber";
    private static final String EMPLOYEE_NAME = "employeeFullName";
    private static final String MODULE_NUMBER = "moduleNumber";
    private static final String MODULE_TYPE = "moduleType";
    private static final String ORGANIZATION = "organizationCode";
    private static final String IS_FOR_STATION = "isForStation";
    public static final String START_DATE_TO_SEARCH = "startDateToSearch";
    public static final String END_DATE_TO_SEARCH = "endDateToSearch";

    public CalibrationTaskSpecificationBuilder(Map<String, String> searchValues) {
        super(searchValues);
    }

    /**
     * Initialize criteria for CalibrationTask Specification
     * @return search criteria
     */
    @Override
    protected List<SearchCriterion> initCriteria() {
        List<SearchCriterion> searchCriteria = new ArrayList<>();
        searchCriteria.add(new SearchCriterion<>(PHONE_NUMBER, "module", SearchCriterion.Operator.LIKE,
                SearchCriterion.ValueType.STRING, "telephone"));
        searchCriteria.add(new SearchCriterion<>(EMPLOYEE_NAME, "module", SearchCriterion.Operator.LIKE,
                SearchCriterion.ValueType.STRING, "employeeFullName"));
        searchCriteria.add(new SearchCriterion<>(MODULE_NUMBER, "module", SearchCriterion.Operator.LIKE,
                SearchCriterion.ValueType.STRING, "moduleNumber"));
        searchCriteria.add(new SearchCriterion<>(MODULE_TYPE, "module", SearchCriterion.Operator.EQUAL_BY_ENUM,
                CalibrationModule.ModuleType.class, null, null, "moduleType"));
        searchCriteria.add(new SearchCriterion<>(ORGANIZATION, "module", SearchCriterion.Operator.EQUAL,
                SearchCriterion.ValueType.STRING, "organizationCode"));
        searchCriteria.add(new SearchCriterion<>(IS_FOR_STATION, "module", SearchCriterion.Operator.NOT_NULL));
        searchCriteria.add(new SearchCriterion<>(START_DATE_TO_SEARCH, "dateOfTask",
                SearchCriterion.Operator.BETWEEN_DATE, END_DATE_TO_SEARCH ));
        return searchCriteria;
    }

    @Override
    public Sort getSort(String sortCriteria, String sortOrder) {
        return null;
    }

    @Override
    public Pageable constructPageSpecification(int pageNumber, int itemsPerPage,
                                               String sortCriteria, String sortOrder) {
        return null;
    }



}
