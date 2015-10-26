package com.softserve.edu.dto.admin;

import lombok.Getter;
import lombok.Setter;

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

    public AgreementDTO(){}
    public AgreementDTO(Long id, Long customerId, Long executorId, String customerName, String executorName, String number, Long deviceCount, String deviceType) {
        this.setId(id);
        this.setCustomerId(customerId);
        this.setExecutorId(executorId);
        this.setCustomerName(customerName);
        this.setExecutorName(executorName);
        this.setNumber(number);
        this.setDeviceCount(deviceCount);
        this.setDeviceType(deviceType);
    }

    public AgreementDTO(Long customerId, Long executorId, String number, Long deviceCount, String deviceType) {
        this(null, customerId, executorId, null, null, number, deviceCount, deviceType);
    }

}
