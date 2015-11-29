package com.softserve.edu.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by Lyubomyr on 28.11.2015.
 */

@Getter
@Setter
public class VerificationPlanningTaskFilterSearch {
    private String verficationId;
    private String providerName;
    private String clientFullName;
    private String district;
    private String street;
}
