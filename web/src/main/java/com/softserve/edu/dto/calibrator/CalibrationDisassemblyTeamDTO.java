package com.softserve.edu.dto.calibrator;


import com.softserve.edu.entity.catalogue.Team.DisassemblyTeam;
import com.softserve.edu.entity.device.Device;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class CalibrationDisassemblyTeamDTO {

    private String id;
    private String name;
    private Date effectiveTo;
    private Device.DeviceType specialization;
    private String leaderFullName;
    private String leaderPhone;
    private String leaderEmail;

    public DisassemblyTeam saveTeam() {
        return new DisassemblyTeam(id, name, effectiveTo, specialization, leaderFullName, leaderPhone, leaderEmail);
    }
}
