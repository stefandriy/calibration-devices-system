package com.softserve.edu.entity.device;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name = "MEASURING_EQUIPMENT")
@Getter
@Setter
@EqualsAndHashCode(of = "id")
@NoArgsConstructor
public class MeasuringEquipment {

    @Setter(AccessLevel.PRIVATE)
    @Id
    @GeneratedValue
    private Long id;

    private String name;
    private String deviceType;
    private String manufacturer;
    private String verificationInterval;

    public MeasuringEquipment(Long id, String name, String manufacturer) {
        super();
        this.id = id;
        this.name = name;
        this.manufacturer = manufacturer;
    }
}
