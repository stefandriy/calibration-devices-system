package com.softserve.edu.entity.organization;

import com.softserve.edu.entity.enumeration.device.DeviceType;
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

    @Temporal(TemporalType.DATE)
    private Date date;

    @Enumerated(EnumType.STRING)
    private DeviceType deviceType;

    public Agreement(Organization customer, Organization executor, String number, Long deviceCount, Date date, DeviceType deviceType) {
        this.setCustomer(customer);
        this.setExecutor(executor);
        this.setNumber(number);
        this.setDeviceCount(deviceCount);
        this.setDate(date);
        this.setDeviceType(deviceType);
    }
}
