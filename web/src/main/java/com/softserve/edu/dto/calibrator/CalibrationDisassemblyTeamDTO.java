package com.softserve.edu.dto.calibrator;


import com.softserve.edu.entity.catalogue.Team.DisassemblyTeam;
import com.softserve.edu.entity.device.Device;
import com.softserve.edu.entity.organization.Organization;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class CalibrationDisassemblyTeamDTO {

    private String teamNumber;
    private String teamName;
    private Date effectiveTo;
    private Device.DeviceType specialization;
    private String leaderFullName;
    private String leaderPhone;
    private String leaderEmail;

    public DisassemblyTeam saveTeam(Organization organization) {
        return new DisassemblyTeam(teamNumber, teamName, effectiveTo, specialization, leaderFullName, leaderPhone, leaderEmail, organization);

    }
}
