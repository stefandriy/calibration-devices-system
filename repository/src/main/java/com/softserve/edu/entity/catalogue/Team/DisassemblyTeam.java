package com.softserve.edu.entity.catalogue.Team;

import com.softserve.edu.entity.enumeration.device.DeviceType;
import com.softserve.edu.entity.organization.Organization;
import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Entity
@Getter
@Setter
@EqualsAndHashCode(of = "number")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "DisassemblyTeam")
public class DisassemblyTeam {

    @Setter(AccessLevel.PRIVATE)
    @Id
    private String number;

    private String name;

    @Temporal(TemporalType.DATE)
    private Date effectiveTo;

    @Enumerated(EnumType.STRING)
    private DeviceType specialization;

    private String leaderFullName;

    private String leaderPhone;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "calibratorId")
    private Organization organization;

    @Override
    public String toString() {
        return "DisassemblyTeam{" +
                "number='" + number + '\'' +
                ", name='" + name + '\'' +
                ", effectiveTo=" + effectiveTo +
                ", specialization=" + specialization +
                ", leaderFullName='" + leaderFullName + '\'' +
                ", leaderPhone='" + leaderPhone + '\'' +
                '}';
    }
}
