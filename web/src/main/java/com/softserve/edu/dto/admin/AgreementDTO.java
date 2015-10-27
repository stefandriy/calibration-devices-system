package com.softserve.edu.dto.admin;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class AgreementDTO {

    private Long id;
    private Long customerId;
    private Long executorId;
    private String customerName;
    private String executorName;
    private String number;
    private Long deviceCount;
    private String deviceType;
    private String customerType;
    private Date date;
    private String startDateToSearch;
    private String endDateToSearch;

    public AgreementDTO() {
    }

    public AgreementDTO(Long id, Long customerId, Long executorId, String customerName, String executorName,
                        String number, Long deviceCount, String deviceType, String customerType, Date date) {
        this.setId(id);
        this.setCustomerId(customerId);
        this.setExecutorId(executorId);
        this.setCustomerName(customerName);
        this.setExecutorName(executorName);
        this.setNumber(number);
        this.setDeviceCount(deviceCount);
        this.setDeviceType(deviceType);
        this.setCustomerType(customerType);
        this.setDate(date);
        this.setStartDateToSearch(null);
        this.setEndDateToSearch(null);
    }

    public AgreementDTO(Long id, Long customerId, Long executorId, String customerName, String executorName,
                        String number, Long deviceCount, String deviceType, Date date) {
        this(id, customerId, executorId, customerName, executorName, number, deviceCount, deviceType, null, date);
    }

    public AgreementDTO(Long customerId, Long executorId, String number, Long deviceCount, String deviceType) {
        this(null, customerId, executorId, null, null, number, deviceCount, deviceType, null);
    }

}
