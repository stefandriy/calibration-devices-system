package com.softserve.edu.entity.verification.calibration;

import com.softserve.edu.entity.device.CalibrationModule;
import com.softserve.edu.entity.organization.Organization;
import com.softserve.edu.entity.verification.Verification;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

@Entity
@Table(name = "CALIBRATION_TASK")
@Getter
@Setter
@EqualsAndHashCode(of = "taskId")
@NoArgsConstructor
public class CalibrationTask {

    @Id
    @GeneratedValue
    private Long taskId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "moduleId")
    private CalibrationModule module;

    private String crewName;

    @Temporal(TemporalType.DATE)
    private Date createTaskDate;

    @Temporal(TemporalType.DATE)
    private Date dateOfTask;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "calibratorId")
    private Organization organization;

    @OneToMany(mappedBy = "task")
    private Set<Verification> verifications;

    public CalibrationTask(CalibrationModule module, String crewName, Date createTaskDate, Date dateOfTask, Organization organization, Set<Verification> verifications) {
        this.module = module;
        this.crewName = crewName;
        this.createTaskDate = createTaskDate;
        this.dateOfTask = dateOfTask;
        this.organization = organization;
        this.verifications = verifications;
    }
}
