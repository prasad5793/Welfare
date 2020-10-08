package lk.AVSEC.Welfare.asset.transfers.entity;


import com.fasterxml.jackson.annotation.JsonFilter;
import lk.AVSEC.Welfare.asset.transfers.entity.Enum.TranferReason;
import lk.AVSEC.Welfare.util.audit.AuditEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonFilter("Transfers")
public class Transfers extends AuditEntity {

    @DateTimeFormat( pattern = "yyyy-MM-dd" )
    private LocalDate dateFrom;

    @DateTimeFormat( pattern = "yyyy-MM-dd" )
    private LocalDate dateTo;

    private String description;

    @Enumerated(EnumType.STRING)
    private TransferReason transferReason;

//    @Transient
//    private String remark;

}
