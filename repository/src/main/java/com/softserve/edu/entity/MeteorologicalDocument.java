package com.softserve.edu.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Embeddable;

/**
 * Document that contains the meteorological requirements for testing a device
 */
@Embeddable
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MeteorologicalDocument {
    private String name;
    private String sign;
}
