package com.softserve.edu.dto.admin;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by Sonka on 23.11.2015.
 */
@Getter
@Setter
public class UnsuitabilityReasonDTO {
    private Long id;
    private String counterTypeName;
    private String name;
    private Long counterId;

    public UnsuitabilityReasonDTO() {}

    public UnsuitabilityReasonDTO(Long id, String name, Long counterId,String counterTypeName) {
        this.id = id;
        this.name = name;
        this.counterId = counterId;
        this.counterTypeName = counterTypeName;
    }
}
