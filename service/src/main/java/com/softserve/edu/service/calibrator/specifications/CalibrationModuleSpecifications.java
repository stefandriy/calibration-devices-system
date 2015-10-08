package com.softserve.edu.service.calibrator.specifications;

import com.softserve.edu.entity.device.CalibrationModule;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.Date;


/**
 * Created by Vasyl on 08.10.2015.
 */
public class CalibrationModuleSpecifications {

    public static Specification<CalibrationModule> moduleHasType(String moduleType){
        return new Specification<CalibrationModule>() {
            @Override
            public Predicate toPredicate(Root<CalibrationModule> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                return criteriaBuilder.equal(root.get("moduleType"), moduleType);
            }
        };
    }

    public static Specification<CalibrationModule> moduleHasWorkDate(Date workDate){
        return new Specification<CalibrationModule>() {
            @Override
            public Predicate toPredicate(Root<CalibrationModule> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                return criteriaBuilder.lessThan(root.get("workDate"), workDate);
            }
        };
    }

    public static Specification<CalibrationModule> moduleHasCalibratorId(Long calibratorId){
        return new Specification<CalibrationModule>() {
            @Override
            public Predicate toPredicate(Root<CalibrationModule> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                return criteriaBuilder.lessThan(root.get("calibratorId"), calibratorId);
            }
        };
    }
}
