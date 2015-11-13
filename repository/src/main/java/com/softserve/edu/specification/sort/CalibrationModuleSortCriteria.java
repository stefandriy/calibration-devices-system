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
                return new Sort(Sort.Direction.ASC, "moduleId");
            } else {
                return new Sort(Sort.Direction.DESC, "moduleId");
            }
        }
    },
    UNDEFINED {
        public Sort getSort(String sortOrder) {
            return new Sort(Sort.Direction.ASC, "condDesignation");
        }
    },
    ORGANIZATION_JOIN_NAME {
        public Sort getSort(String sortOrder) {
            if(sortOrder.equalsIgnoreCase("asc")) {
                return new Sort(Sort.Direction.ASC, "organization.name");
            } else {
                return new Sort(Sort.Direction.DESC, "organization.name");
            }
        }
    },
    DEVICE_TYPE {
        public Sort getSort(String sortOrder) {
            if(sortOrder.equalsIgnoreCase("asc")) {
                return new Sort(Sort.Direction.ASC, "deviceType");
            } else {
                return new Sort(Sort.Direction.DESC, "deviceType");
            }
        }
    },
    ORGANIZATION_CODE {
        public Sort getSort(String sortOrder) {
            if(sortOrder.equalsIgnoreCase("asc")) {
                return new Sort(Sort.Direction.ASC, "organizationCode");
            } else {
                return new Sort(Sort.Direction.DESC, "organizationCode");
            }
        }
    },
    COND_DESIGNATION {
        public Sort getSort(String sortOrder) {
            if(sortOrder.equalsIgnoreCase("asc")) {
                return new Sort(Sort.Direction.ASC, "condDesignation");
            } else {
                return new Sort(Sort.Direction.DESC, "condDesignation");
            }
        }
    },
    SERIAL_NUMBER {
        public Sort getSort(String sortOrder) {
            if(sortOrder.equalsIgnoreCase("asc")) {
                return new Sort(Sort.Direction.ASC, "serialNumber");
            } else {
                return new Sort(Sort.Direction.DESC, "serialNumber");
            }
        }
    },
    EMPLOYEE_FULL_NAME {
        public Sort getSort(String sortOrder) {
            if(sortOrder.equalsIgnoreCase("asc")) {
                return new Sort(Sort.Direction.ASC, "employeeFullName");
            } else {
                return new Sort(Sort.Direction.DESC, "employeeFullName");
            }
        }
    },
    PHONE_NUMBER {
        public Sort getSort(String sortOrder) {
            if(sortOrder.equalsIgnoreCase("asc")) {
                return new Sort(Sort.Direction.ASC, "telephone");
            } else {
                return new Sort(Sort.Direction.DESC, "telephone");
            }
        }
    },
    MODULE_TYPE {
        public Sort getSort(String sortOrder) {
            if(sortOrder.equalsIgnoreCase("asc")) {
                return new Sort(Sort.Direction.ASC, "moduleType");
            } else {
                return new Sort(Sort.Direction.DESC, "moduleType");
            }
        }
    },
    EMAIL {
        public Sort getSort(String sortOrder) {
            if(sortOrder.equalsIgnoreCase("asc")) {
                return new Sort(Sort.Direction.ASC, "email");
            } else {
                return new Sort(Sort.Direction.DESC, "email");
            }
        }
    },
    CALIBRATION_TYPE {
        public Sort getSort(String sortOrder) {
            if(sortOrder.equalsIgnoreCase("asc")) {
                return new Sort(Sort.Direction.ASC, "calibrationType");
            } else {
                return new Sort(Sort.Direction.DESC, "calibrationType");
            }
        }
    },
    MODULE_NUMBER {
        public Sort getSort(String sortOrder) {
            if(sortOrder.equalsIgnoreCase("asc")) {
                return new Sort(Sort.Direction.ASC, "moduleNumber");
            } else {
                return new Sort(Sort.Direction.DESC, "moduleNumber");
            }
        }
    },
    WORK_DATE {
        public Sort getSort(String sortOrder) {
            if(sortOrder.equalsIgnoreCase("asc")) {
                return new Sort(Sort.Direction.ASC, "workDate");
            } else {
                return new Sort(Sort.Direction.DESC, "workDate");
            }
        }
    }

}
