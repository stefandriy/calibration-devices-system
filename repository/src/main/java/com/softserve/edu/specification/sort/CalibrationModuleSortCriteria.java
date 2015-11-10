package com.softserve.edu.specification.sort;

import com.softserve.edu.specification.AgreementSpecificationBuilder;
import com.softserve.edu.specification.CalibrationModuleSpecification;
import org.springframework.data.domain.Sort;

/**
 * Created by roman on 08.11.15.
 *
 */


public enum CalibrationModuleSortCriteria implements SortCriteria {

    ID {
        public Sort getSort(String sortOrder) {
            if(sortOrder.equalsIgnoreCase("asc")) {
                return new Sort(Sort.Direction.ASC, CalibrationModuleSpecification.ID);
            } else {
                return new Sort(Sort.Direction.DESC, CalibrationModuleSpecification.ID);
            }
        }
    },
    UNDEFINED {
        public Sort getSort(String sortOrder) {
            /*if(sortOrder != null && sortOrder.equalsIgnoreCase("asc")) {
                return new Sort(Sort.Direction.ASC, CalibrationModuleSpecification.MODULE_NUMBER);
            } else {*/
                return new Sort(Sort.Direction.DESC, CalibrationModuleSpecification.MODULE_NUMBER);
            //}
        }
    },
    ORGANIZATION_JOIN_NAME {
        public Sort getSort(String sortOrder) {
            if(sortOrder.equalsIgnoreCase("asc")) {
                return new Sort(Sort.Direction.ASC, CalibrationModuleSpecification.ORGANIZATION_JOIN_NAME);
            } else {
                return new Sort(Sort.Direction.DESC, CalibrationModuleSpecification.ORGANIZATION_JOIN_NAME);
            }
        }
    },
    DEVICE_TYPE {
        public Sort getSort(String sortOrder) {
            if(sortOrder.equalsIgnoreCase("asc")) {
                return new Sort(Sort.Direction.ASC, CalibrationModuleSpecification.DEVICE_TYPE);
            } else {
                return new Sort(Sort.Direction.DESC, CalibrationModuleSpecification.DEVICE_TYPE);
            }
        }
    },
    ORGANIZATION_CODE {
        public Sort getSort(String sortOrder) {
            if(sortOrder.equalsIgnoreCase("asc")) {
                return new Sort(Sort.Direction.ASC, CalibrationModuleSpecification.ORGANIZATION_CODE);
            } else {
                return new Sort(Sort.Direction.DESC, CalibrationModuleSpecification.ORGANIZATION_CODE);
            }
        }
    },
    COND_DESIGNATION {
        public Sort getSort(String sortOrder) {
            if(sortOrder.equalsIgnoreCase("asc")) {
                return new Sort(Sort.Direction.ASC, CalibrationModuleSpecification.COND_DESIGNATION);
            } else {
                return new Sort(Sort.Direction.DESC, CalibrationModuleSpecification.COND_DESIGNATION);
            }
        }
    },
    SERIAL_NUMBER {
        public Sort getSort(String sortOrder) {
            if(sortOrder.equalsIgnoreCase("asc")) {
                return new Sort(Sort.Direction.ASC, CalibrationModuleSpecification.SERIAL_NUMBER);
            } else {
                return new Sort(Sort.Direction.DESC, CalibrationModuleSpecification.SERIAL_NUMBER);
            }
        }
    },
    EMPLOYEE_FULL_NAME {
        public Sort getSort(String sortOrder) {
            if(sortOrder.equalsIgnoreCase("asc")) {
                return new Sort(Sort.Direction.ASC, CalibrationModuleSpecification.EMPLOYEE_FULL_NAME);
            } else {
                return new Sort(Sort.Direction.DESC, CalibrationModuleSpecification.EMPLOYEE_FULL_NAME);
            }
        }
    },
    TELEPHONE {
        public Sort getSort(String sortOrder) {
            if(sortOrder.equalsIgnoreCase("asc")) {
                return new Sort(Sort.Direction.ASC, CalibrationModuleSpecification.TELEPHONE);
            } else {
                return new Sort(Sort.Direction.DESC, CalibrationModuleSpecification.TELEPHONE);
            }
        }
    },
    MODULE_TYPE {
        public Sort getSort(String sortOrder) {
            if(sortOrder.equalsIgnoreCase("asc")) {
                return new Sort(Sort.Direction.ASC, CalibrationModuleSpecification.MODULE_TYPE);
            } else {
                return new Sort(Sort.Direction.DESC, CalibrationModuleSpecification.MODULE_TYPE);
            }
        }
    },
    EMAIL {
        public Sort getSort(String sortOrder) {
            if(sortOrder.equalsIgnoreCase("asc")) {
                return new Sort(Sort.Direction.ASC, CalibrationModuleSpecification.EMAIL);
            } else {
                return new Sort(Sort.Direction.DESC, CalibrationModuleSpecification.EMAIL);
            }
        }
    },
    CALIBRATION_TYPE {
        public Sort getSort(String sortOrder) {
            if(sortOrder.equalsIgnoreCase("asc")) {
                return new Sort(Sort.Direction.ASC, CalibrationModuleSpecification.CALIBRATION_TYPE);
            } else {
                return new Sort(Sort.Direction.DESC, CalibrationModuleSpecification.CALIBRATION_TYPE);
            }
        }
    },
    MODULE_NUMBER {
        public Sort getSort(String sortOrder) {
            if(sortOrder.equalsIgnoreCase("asc")) {
                return new Sort(Sort.Direction.ASC, CalibrationModuleSpecification.MODULE_NUMBER);
            } else {
                return new Sort(Sort.Direction.DESC, CalibrationModuleSpecification.MODULE_NUMBER);
            }
        }
    },
    WORK_DATE {
        public Sort getSort(String sortOrder) {
            if(sortOrder.equalsIgnoreCase("asc")) {
                return new Sort(Sort.Direction.ASC, CalibrationModuleSpecification.WORK_DATE);
            } else {
                return new Sort(Sort.Direction.DESC, CalibrationModuleSpecification.WORK_DATE);
            }
        }
    }

}
