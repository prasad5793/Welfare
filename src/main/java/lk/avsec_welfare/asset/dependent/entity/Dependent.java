package lk.avsec_welfare.asset.dependent.entity;

import com.fasterxml.jackson.annotation.JsonFilter;
import lk.avsec_welfare.asset.common_asset.model.enums.LiveDead;
import lk.avsec_welfare.asset.dependent.entity.Enum.CurrentStatus;
import lk.avsec_welfare.asset.dependent.entity.Enum.Relationship;
import lk.avsec_welfare.asset.employee.entity.Employee;
import lk.avsec_welfare.util.audit.AuditEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonFilter("Dependent")
public class Dependent extends AuditEntity {

    private String name;

    private String nic;

    private String remark;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dob;

    @Enumerated(EnumType.STRING)
    private CurrentStatus currentStatus;

    @Enumerated(EnumType.STRING)
    private LiveDead liveDead;


    @OneToMany(mappedBy = "dependent", fetch = FetchType.EAGER)
    private List<DependentEmployee> dependentEmployees;

    @Transient
    @Enumerated(EnumType.STRING)
    private Relationship relationship;

    @Transient
    private String epfNumber;

    @Transient
    private Employee employee;
}
