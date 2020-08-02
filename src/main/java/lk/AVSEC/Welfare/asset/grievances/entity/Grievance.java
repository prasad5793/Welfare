package lk.AVSEC.Welfare.asset.grievances.entity;

import com.fasterxml.jackson.annotation.JsonFilter;
import lk.AVSEC.Welfare.asset.employee.entity.Employee;
import lk.AVSEC.Welfare.asset.grievances.entity.Enum.GrievancesStatus;
import lk.AVSEC.Welfare.asset.grievances.entity.Enum.Priority;
import lk.AVSEC.Welfare.asset.grievances.entity.Enum.SolutionType;
import lk.AVSEC.Welfare.util.audit.AuditEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
<<<<<<< HEAD:src/main/java/lk/AVSEC/Welfare/asset/grievances/entity/Grievance.java
@JsonFilter("Grievances")
public class Grievance extends AuditEntity {

    // @Size(min = 2, max = 60, message = "Your name length should be 13")
    private String name;
=======
@JsonFilter("Grievance")
public class Grievance extends AuditEntity {
>>>>>>> 991a9fa77174c9052d9120bd05ad1fd6ca31d6e1:src/main/java/lk/AVSEC/Welfare/asset/grievances/entity/Grievance.java

    private String description;

    @Enumerated(EnumType.STRING)
    private Priority priority;

    @Enumerated(EnumType.STRING)
    private GrievancesStatus grievancesStatus;
<<<<<<< HEAD:src/main/java/lk/AVSEC/Welfare/asset/grievances/entity/Grievance.java

    private String remark;

    @ManyToOne
    private Employee employee;
=======

    @Enumerated(EnumType.STRING)
    private SolutionType solutionType;

    @OneToMany(mappedBy = "grievance", cascade = {CascadeType.PERSIST,CascadeType.DETACH})
    private List<GrievanceStateChange> grievanceStateChange;

    @Transient
    private String remark;

>>>>>>> 991a9fa77174c9052d9120bd05ad1fd6ca31d6e1:src/main/java/lk/AVSEC/Welfare/asset/grievances/entity/Grievance.java
}
