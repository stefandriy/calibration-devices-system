package com.softserve.edu.entity.catalogue.Team;


import com.softserve.edu.entity.device.Device;
import com.softserve.edu.entity.organization.Organization;
import lombok.*;

import javax.persistence.*;
import java.util.Date;

/**
 * Disassembly team entity. Contain information about teams.
 */
@Entity
@Getter
@Setter
@EqualsAndHashCode(of = "id")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "DISASSEMBLY_TEAM")
public class DisassemblyTeam {

    @Setter(AccessLevel.PRIVATE)
    @Id
    private String id;

    private String name;

    @Temporal(TemporalType.DATE)
    private Date effectiveTo;

    @Enumerated(EnumType.STRING)
    private Device.DeviceType specialization;

    private String leaderFullName;

    private String leaderPhone;

    private String leaderEmail;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "calibratorId")
    private Organization organization;

    public DisassemblyTeam(String id, String name, Date effectiveTo, Device.DeviceType specialization, String leaderFullName,
                           String leaderPhone, String leaderEmail) {
        this.id = id;
        this.name = name;
        this.effectiveTo = effectiveTo;
        this.specialization = specialization;
        this.leaderFullName = leaderFullName;
        this.leaderPhone = leaderPhone;
        this.leaderEmail = leaderEmail;
    }

    @Override
    public String toString() {
        return "DisassemblyTeam{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", effectiveTo=" + effectiveTo +
                ", specialization=" + specialization +
                ", leaderFullName='" + leaderFullName + '\'' +
                ", leaderPhone='" + leaderPhone + '\'' +
                ", leaderEmail='" + leaderEmail + '\'' +
                '}';
    }
}
