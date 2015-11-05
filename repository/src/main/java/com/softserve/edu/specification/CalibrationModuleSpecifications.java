package com.softserve.edu.specification;

import com.softserve.edu.entity.device.CalibrationModule;
import com.softserve.edu.entity.device.Device;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;
import java.util.Date;


public class CalibrationModuleSpecifications {


    public static Specification<CalibrationModule> moduleHasType(String moduleType){
        return (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.equal(root.get("moduleType"), moduleType);
    }

    public static Specification<CalibrationModule> moduleHasWorkDate(Date workDate){
        return (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.lessThan(root.get("workDate"), workDate);
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
        return (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.equal(root.get("organization").get("id"), calibratorId);
    }

}
