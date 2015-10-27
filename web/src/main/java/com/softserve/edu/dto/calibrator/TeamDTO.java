package com.softserve.edu.dto.calibrator;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TeamDTO {

    private String teamName;

    private String teamNumber;

    public TeamDTO() {
    }

    public TeamDTO(String teamName, String teamNumber) {
        this.teamName = teamName;
        this.teamNumber = teamNumber;
    }
}
