package com.softserve.edu.dto.calibrator;

import com.softserve.edu.entity.device.Device;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;


@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DisassemblyTeamPageItem {

    private String id;
    private String name;
    private Date effectiveTo;
    private Device.DeviceType specialization;
    private String leaderFullName;
    private String leaderPhone;
    private String leaderEmail;

    public DisassemblyTeamPageItem(String id, String name, Date effectiveTo, Device.DeviceType specialization, String leaderFullName,
                           String leaderPhone, String leaderEmail) {
        this.id = id;
        this.name = name;
        this.effectiveTo = effectiveTo;
        this.specialization = specialization;
        this.leaderFullName = leaderFullName;
        this.leaderPhone = leaderPhone;
        this.leaderEmail = leaderEmail;
    }

}
