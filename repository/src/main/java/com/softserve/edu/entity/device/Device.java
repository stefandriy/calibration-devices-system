package com.softserve.edu.entity.device;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.softserve.edu.entity.organization.Organization;
import com.softserve.edu.entity.verification.Verification;
import com.softserve.edu.entity.enumeration.device.DeviceType;
import lombok.*;

import javax.persistence.*;
import java.util.Set;

@Entity
@Getter
@Setter
@EqualsAndHashCode(of = "id")
@Table(name = "DEVICE")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Device {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.PRIVATE)
    private Long id;

    @Column(nullable = false)
    private String deviceName;

    @Enumerated(EnumType.STRING)
    private DeviceType deviceType;

    private String deviceSign;

    private String number;

    @OneToMany(mappedBy = "device")
    @JsonBackReference
    private Set<Verification> verifications;

    @ManyToOne
    @JoinColumn(name = "providerId")
    @JsonManagedReference
    private Organization provider;

    @ManyToOne
    @JoinColumn(name = "manufacturerId")
    private Manufacturer manufacturer;

    public Device(String number, Set<Verification> verifications, Manufacturer manufacturer) {
        this.number = number;
        this.verifications = verifications;
        this.manufacturer = manufacturer;
    }

    public Device(String number, DeviceType deviceType, String deviceName) {
        this.number = number;
        this.deviceType = deviceType;
        this.deviceName = deviceName;
    }
}
