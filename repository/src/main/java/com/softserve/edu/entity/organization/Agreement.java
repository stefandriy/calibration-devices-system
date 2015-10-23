package com.softserve.edu.entity.organization;

import com.softserve.edu.entity.device.Device;
import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(of = "id")
@Entity
@Table(name = "AGREEMENT")
public class Agreement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.PRIVATE)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customerId")
    private Organization customer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "executorId")
    private Organization executor;

    private String number;

    private Long deviceCount;

    private Boolean isAvailable = false;

    @Temporal(TemporalType.DATE)
    private Date date;

    @Enumerated(EnumType.STRING)
    private Device.DeviceType deviceType;

    public Agreement(Organization customer, Organization executor, String number, Long deviceCount, Date date, Device.DeviceType deviceType) {
        this(customer, executor, number, deviceCount, date, deviceType, true);
    }

    public Agreement(Organization customer, Organization executor, String number, Long deviceCount, Date date, Device.DeviceType deviceType, Boolean isAvailable) {
        this.setCustomer(customer);
        this.setExecutor(executor);
        this.setNumber(number);
        this.setDeviceCount(deviceCount);
        this.setDate(date);
        this.setDeviceType(deviceType);
        this.setIsAvailable(true);
        this.setIsAvailable(isAvailable);
    }
}
