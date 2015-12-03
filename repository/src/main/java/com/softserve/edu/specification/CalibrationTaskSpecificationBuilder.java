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
        searchCriteria.add(new SearchCriterion<>("phoneNumber", "module", SearchCriterion.Operator.LIKE,
                SearchCriterion.ValueType.STRING, "telephone"));
        searchCriteria.add(new SearchCriterion<>("employeeFullName", "module", SearchCriterion.Operator.LIKE,
                SearchCriterion.ValueType.STRING, "employeeFullName"));
        searchCriteria.add(new SearchCriterion<>("moduleNumber", "module", SearchCriterion.Operator.LIKE,
                SearchCriterion.ValueType.STRING, "moduleNumber"));
        searchCriteria.add(new SearchCriterion<>("moduleType", "module", SearchCriterion.Operator.EQUAL_BY_ENUM,
                CalibrationModule.ModuleType.class, null, null, "moduleType"));
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
