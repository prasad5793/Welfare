package lk.avsec_welfare.asset.promotion.entity;


import com.fasterxml.jackson.annotation.JsonFilter;
import lk.avsec_welfare.asset.common_asset.model.enums.LiveDead;
import lk.avsec_welfare.asset.designation.entity.Designation;
import lk.avsec_welfare.asset.employee.entity.Employee;
import lk.avsec_welfare.util.audit.AuditEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.time.LocalDate;


@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonFilter("Promotion")
public class Promotion extends AuditEntity {

    private String remark;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate withEffect;

    @Enumerated(EnumType.STRING)
    private LiveDead liveDead;

    @ManyToOne
    private Employee employee;

    @ManyToOne
    private Designation designation;


}
