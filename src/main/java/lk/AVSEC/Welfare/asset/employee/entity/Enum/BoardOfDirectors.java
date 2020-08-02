package lk.AVSEC.Welfare.asset.employee.entity.Enum;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum BoardOfDirectors {
    HOSS("Head of Security Services"),
    DHOSS("Deputy Head of Security Services"),
    PRE("President"),
    VPRE("Vice President"),
    SCTY("Secretory"),
    VSCTY("Vice Secretory"),
    TRS("Treasure"),
    AGT("Agent"),
    MBR("Member"),
    OTR("Other");

    private final String boardOfDirectors;
}
