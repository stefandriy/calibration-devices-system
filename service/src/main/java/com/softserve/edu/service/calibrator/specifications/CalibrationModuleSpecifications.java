package com.softserve.edu.service.calibrator.specifications;

import com.softserve.edu.entity.device.CalibrationModule;
import com.softserve.edu.entity.device.Device;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;
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

    public static Specification<CalibrationModule> moduleDeviceType(String applicationFiled){
        return new Specification<CalibrationModule>() {
            @Override
            public Predicate toPredicate(Root<CalibrationModule> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                Predicate predicate = null;
                if (applicationFiled.equals("WATER")) {
                    predicate = criteriaBuilder.equal(root.get("deviceType"), Device.DeviceType.WATER);
                } else if (applicationFiled.equals("THERMAL")) {
                    predicate = criteriaBuilder.equal(root.get("deviceType"), Device.DeviceType.THERMAL);
                }
                return predicate;
            }
        };
    }

    public static Specification<CalibrationModule> moduleHasCalibratorId(Long calibratorId){
        return new Specification<CalibrationModule>() {
            @Override
            public Predicate toPredicate(Root<CalibrationModule> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                return criteriaBuilder.equal(root.get("organization").get("id"), calibratorId);
            }
        };
    }

    public static Specification<CalibrationModule> moduleIsAvaliable(){
        return new Specification<CalibrationModule>() {
            @Override
            public Predicate toPredicate(Root<CalibrationModule> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                return criteriaBuilder.equal(root.get("isAvaliable"), true);
            }
        };
    }
}
