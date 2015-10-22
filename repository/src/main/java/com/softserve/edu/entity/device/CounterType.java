package com.softserve.edu.entity.device;

import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Entity
@Getter
@Setter
@EqualsAndHashCode(of = "id")
@Table(name = "COUNTER_TYPE")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CounterType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.PRIVATE)
    private Long id;

    private String name;

    private String symbol;

    private String standardSize;

    private String manufacturer;

    private Integer calibrationInterval;

    private String yearIntroduction;

    private String gost;

    @ManyToOne
    @JoinColumn(name = "deviceId")
    private Device device;

    public String getName() {
        return name;
    }

    public void setName() {
        this.name = device.getDeviceName();
    }
}
