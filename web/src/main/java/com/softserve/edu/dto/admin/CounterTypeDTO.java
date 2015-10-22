package com.softserve.edu.dto.admin;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CounterTypeDTO {

    private Long id;
    private String name;
    private String symbol;
    private String standardSize;
    private String manufacturer;
    private Integer calibrationInterval;
    private String yearIntroduction;
    private String gost;
    private Long deviceId;

    public CounterTypeDTO() {
    }

    public CounterTypeDTO(Long id, String name, String symbol, String standardSize, String manufacturer,
                          Integer calibrationInterval, String yearIntroduction, String gost, Long deviceId) {
        this.id = id;
        this.name = name;
        this.symbol = symbol;
        this.standardSize = standardSize;
        this.manufacturer = manufacturer;
        this.calibrationInterval = calibrationInterval;
        this.yearIntroduction = yearIntroduction;
        this.gost = gost;
        this.deviceId = deviceId;
    }
}
