package lk.avsec_welfare.asset.report;

import lk.avsec_welfare.asset.censure.service.CensureService;
import lk.avsec_welfare.asset.common_asset.model.TwoDate;
import lk.avsec_welfare.asset.common_asset.model.enums.LiveDead;
import lk.avsec_welfare.asset.employee.entity.Employee;
import lk.avsec_welfare.asset.employee.service.EmployeeFilesService;
import lk.avsec_welfare.asset.employee.service.EmployeeService;
import lk.avsec_welfare.asset.death_donation.entity.DeathDonation;
import lk.avsec_welfare.asset.death_donation.service.DeathDonationService;
import lk.avsec_welfare.asset.instalment.service.InstalmentService;
import lk.avsec_welfare.asset.main_account.entity.Enum.OtherFundReceivingType;
import lk.avsec_welfare.asset.main_account.service.MainAccountService;
import lk.avsec_welfare.asset.other_expence.entity.OtherExpence;
import lk.avsec_welfare.asset.other_expence.entity.enums.OtherExpenseType;
import lk.avsec_welfare.asset.other_expence.service.OtherExpenceService;
import lk.avsec_welfare.asset.other_fund_receiving.entity.OtherFundReceiving;
import lk.avsec_welfare.asset.other_fund_receiving.service.OtherFundReceivingService;
import lk.avsec_welfare.asset.grievances.service.GrievancesService;
import lk.avsec_welfare.asset.offence.entity.Offence;

import lk.avsec_welfare.asset.offence.service.OffenceService;
import lk.avsec_welfare.asset.report.model.AgentTotalAmount;
import lk.avsec_welfare.asset.report.model.OtherExpenseCountAmount;
import lk.avsec_welfare.asset.report.model.OtherFundReceivingTypeAmount;
import lk.avsec_welfare.asset.userManagement.entity.Role;
import lk.avsec_welfare.asset.userManagement.entity.User;
import lk.avsec_welfare.asset.userManagement.service.RoleService;
import lk.avsec_welfare.asset.userManagement.service.UserService;
import lk.avsec_welfare.util.service.DateTimeAgeService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping( "/report" )
public class ReportController {
  private final MainAccountService mainAccountService;
  private final UserService userService;
  private final DeathDonationService deathDonationService;
  private final InstalmentService instalmentService;
  private final OtherExpenceService otherExpenceService;
  private final OtherFundReceivingService otherFundReceivingService;
  private final GrievancesService grievancesService;
  private final OffenceService offenceService;
  private final CensureService censureService;
  private final EmployeeFilesService employeeFilesService;
  private final DateTimeAgeService dateTimeAgeService;
  private final RoleService roleService;
  private final EmployeeService employeeService;

  public ReportController(MainAccountService mainAccountService, UserService userService,
                          DeathDonationService deathDonationService, InstalmentService instalmentService,
                          OtherExpenceService otherExpenceService,
                          OtherFundReceivingService otherFundReceivingService, GrievancesService grievancesService,
                          OffenceService offenceService, CensureService censureService, EmployeeFilesService employeeFilesService, DateTimeAgeService dateTimeAgeService, RoleService roleService,
                          EmployeeService employeeService) {
    this.mainAccountService = mainAccountService;
    this.userService = userService;
    this.deathDonationService = deathDonationService;
    this.instalmentService = instalmentService;
    this.otherExpenceService = otherExpenceService;
    this.otherFundReceivingService = otherFundReceivingService;
    this.grievancesService = grievancesService;
    this.offenceService = offenceService;
    this.censureService = censureService;
    this.employeeFilesService = employeeFilesService;

    this.dateTimeAgeService = dateTimeAgeService;
    this.roleService = roleService;
    this.employeeService = employeeService;
  }

  //1. main account service according to type
  @GetMapping( "/mainAccount" )
  public String mainAccountReport(Model model) {
    model.addAttribute("mainAccounts", mainAccountService.findAll());
    return "report/mainAccount";
  }

//2 other fund receiving for one date and date range
  @GetMapping( "/otherFundReceivingType" )
  public String donationReport(Model model) {
    LocalDate localDate = LocalDate.now();
    String message = "This report is belongs to " + localDate;
    model.addAttribute("message", message);
    return commonOtherFundReceivingType(localDate, localDate, model);
  }


  @PostMapping( "/otherFundReceivingType" )
  public String donationReportSearch(@ModelAttribute TwoDate twoDate, Model model) {
    String message = "This report is belongs from " + twoDate.getStartDate() + " to " + twoDate.getEndDate();
    model.addAttribute("message", message);
    return commonOtherFundReceivingType(twoDate.getStartDate(), twoDate.getEndDate(), model);
  }

  private String commonOtherFundReceivingType(LocalDate startDate, LocalDate endDate, Model model) {
    LocalDateTime startDateTime = dateTimeAgeService.dateTimeToLocalDateStartInDay(startDate);
    LocalDateTime endDateTime = dateTimeAgeService.dateTimeToLocalDateEndInDay(endDate);

    List< OtherFundReceiving > otherFundReceivings = otherFundReceivingService.findByCreatedAtIsBetween(startDateTime
        , endDateTime);


    List< OtherFundReceivingTypeAmount > otherFundReceivingTypeAmounts = new ArrayList<>();

    for ( OtherFundReceivingType otherFundReceivingType : OtherFundReceivingType.values() ) {
      OtherFundReceivingTypeAmount otherFundReceivingTypeAmount = new OtherFundReceivingTypeAmount();
      List< BigDecimal > amounts = new ArrayList<>();

      List< OtherFundReceiving > otherFundReceivingAccordingsTo = otherFundReceivings
          .stream()
          .filter(x -> x.getOtherFundReceivingType().equals(otherFundReceivingType))
          .collect(Collectors.toList());

      otherFundReceivingTypeAmount.setOtherFundReceivingType(otherFundReceivingType);
      otherFundReceivingTypeAmount.setRecordCount(otherFundReceivingAccordingsTo.size());
      otherFundReceivingAccordingsTo.forEach(x -> amounts.add(x.getAmount()));
      otherFundReceivingTypeAmount.setAmount(amounts.stream().reduce(BigDecimal.ZERO, BigDecimal::add));

      otherFundReceivingTypeAmounts.add(otherFundReceivingTypeAmount);

    }
    model.addAttribute("otherFundReceivingTypeAmounts", otherFundReceivingTypeAmounts);

    return "report/otherFundReceivingType";
  }
//3. death donation for one day and date range
  @GetMapping( "/deathDonation" )
  public String deathDonation(Model model) {
    LocalDate localDate = LocalDate.now();
    String message = "This report is belongs to " + localDate;
    TwoDate twoDate = new TwoDate();
    twoDate.setEndDate(localDate);
    twoDate.setStartDate(localDate);

    return commonDeathDonation(twoDate, model, message);
  }

  @PostMapping( "/deathDonation" )
  public String deathDonationSearch(@ModelAttribute TwoDate twoDate, Model model) {
    String message = "This report is belongs from " + twoDate.getStartDate() + " to " + twoDate.getEndDate();
    model.addAttribute("message", message);
    return commonDeathDonation(twoDate, model, message);
  }

  private String commonDeathDonation(TwoDate twoDate, Model model, String message) {
    LocalDateTime startDateTime = dateTimeAgeService.dateTimeToLocalDateStartInDay(twoDate.getStartDate());
    LocalDateTime endDateTime = dateTimeAgeService.dateTimeToLocalDateEndInDay(twoDate.getEndDate());

    List< DeathDonation > deathDonations = deathDonationService.findByCreatedAtIsBetween(startDateTime, endDateTime);

    if ( twoDate.getId() == null ) {
      model.addAttribute("deathDonations", deathDonations);
    } else {
      Employee employee = employeeService.findById(twoDate.getId());
      model.addAttribute("deathDonations",
                         deathDonations.stream().filter(x -> x.getEmployee().equals(employee)).collect(Collectors.toList()));
      model.addAttribute("employeeDetail", employee);
      message =
          message + " \n This report is belongs to epf number " + employee.getEpf() + " name " + employee.getName();
    }
    model.addAttribute("message", message);
    model.addAttribute("employees", employeeService.findAll());
    return "report/otherFundReceivingType";
  }


  //4. other expences for one day and date range
  @GetMapping( "/otherExpense" )
  public String otherExpense(Model model) {
    LocalDate localDate = LocalDate.now();
    String message = "This report is belongs to " + localDate;
    model.addAttribute("message", message);
    TwoDate twoDate = new TwoDate();
    twoDate.setEndDate(localDate);
    twoDate.setStartDate(localDate);
    return commonOtherExpense(twoDate, model);
  }


  @PostMapping( "/otherExpense" )
  public String otherExpenseSearch(@ModelAttribute TwoDate twoDate, Model model) {
    String message = "This report is belongs from " + twoDate.getStartDate() + " to " + twoDate.getEndDate();
    model.addAttribute("message", message);
    return commonOtherExpense(twoDate, model);
  }

  private String commonOtherExpense(TwoDate twoDate, Model model) {
    LocalDateTime startDateTime = dateTimeAgeService.dateTimeToLocalDateStartInDay(twoDate.getStartDate());
    LocalDateTime endDateTime = dateTimeAgeService.dateTimeToLocalDateEndInDay(twoDate.getEndDate());


    List< OtherExpence > otherExpenses = otherExpenceService.findByCreatedAtIsBetween(startDateTime, endDateTime);
    List< OtherExpenseCountAmount > otherExpenseCountAmounts = new ArrayList<>();
    for ( OtherExpenseType otherExpenseType : OtherExpenseType.values() ) {
      OtherExpenseCountAmount otherExpenseCountAmount = new OtherExpenseCountAmount();
      otherExpenseCountAmount.setOtherExpenseType(otherExpenseType);
      List< OtherExpence > otherExpenseByOtherExpenseType =
          otherExpenses.stream().filter(x -> x.getOtherExpenseType().equals(otherExpenseType)).collect(Collectors.toList());
      otherExpenseCountAmount.setRecordCounter(otherExpenseByOtherExpenseType.size());
      List< BigDecimal > otherExpenceByOtherExpenseTypeAmount = new ArrayList<>();

      otherExpenseByOtherExpenseType.forEach(x -> otherExpenceByOtherExpenseTypeAmount.add(x.getAmount()));
      otherExpenseCountAmount.setAmount(otherExpenceByOtherExpenseTypeAmount.stream().reduce(BigDecimal.ZERO,
                                                                                             BigDecimal::add));
      otherExpenseCountAmounts.add(otherExpenseCountAmount);

    }


    model.addAttribute("otherExpenseCountAmounts", otherExpenseCountAmounts);

    return "report/otherExpense";
  }


  //5. agent vise collection reporting date and date range

  @GetMapping( "/agentVise" )
  public String agentVise(Model model) {
    LocalDate localDate = LocalDate.now();
    String message = "This report is belongs to " + localDate;
    model.addAttribute("message", message);
    return commonAgentVise(localDate, localDate, model);
  }


  @PostMapping( "/agentVise" )
  public String agentViseSearch(@ModelAttribute TwoDate twoDate, Model model) {
    String message = "This report is belongs from " + twoDate.getStartDate() + " to " + twoDate.getEndDate();
    model.addAttribute("message", message);
    return commonAgentVise(twoDate.getStartDate(), twoDate.getEndDate(), model);
  }

  private String commonAgentVise(LocalDate startDate, LocalDate endDate, Model model) {
    LocalDateTime startDateTime = dateTimeAgeService.dateTimeToLocalDateStartInDay(startDate);
    LocalDateTime endDateTime = dateTimeAgeService.dateTimeToLocalDateEndInDay(endDate);

    Role role = roleService.findByRoleName("AGENT");
    List< User > users = role.getUsers();

    List< AgentTotalAmount > agentTotalAmounts = new ArrayList<>();

    users.forEach(x -> {
      AgentTotalAmount agentTotalAmount = new AgentTotalAmount();
      List< BigDecimal > agentAmount = new ArrayList<>();
      Employee employee = userService.findByUserName(x.getUsername()).getEmployee();

      instalmentService.findByCreatedAtIsBetweenAndCreatedBy(startDateTime, endDateTime, x.getUsername()).forEach(y -> {
        agentAmount.add(y.getAmount());
      });

      agentTotalAmount.setEmployee(employee);
      agentTotalAmount.setCount(agentAmount.size());
      agentTotalAmount.setAmount(agentAmount.stream().reduce(BigDecimal.ZERO, BigDecimal::add));

      agentTotalAmounts.add(agentTotalAmount);
    });


    model.addAttribute("agentTotalAmounts", agentTotalAmounts);

    return "report/agent";
  }


  //1.total students up to now
  @GetMapping("/employee")
  public String allEmplyeeReport( Model model) {
    model.addAttribute("allEmplyees", employeeService.findAll());
    return "report/allEmployee";
  }


  @PostMapping( "/employeeAllCount" )
  public String employeeAllCountSearch(@ModelAttribute TwoDate twoDate, Model model) {
    return commonEmployeeAllCount(model,
            employeeService.findByCreatedAtBetween(dateTimeAgeService.dateTimeToLocalDateStartInDay(twoDate.getStartDate()), dateTimeAgeService.dateTimeToLocalDateEndInDay(twoDate.getEndDate())));
  }

  private String commonEmployeeAllCount(Model model, List< Employee > employeesRequest) {
    model.addAttribute("twoDate", new TwoDate());
    List< Employee > employees = new ArrayList<>();
    for ( Employee employee : employeesRequest
            .stream()
            .filter(x -> LiveDead.ACTIVE.equals(x.getLiveDead()))
            .collect(Collectors.toList())
    ) {
      employee.setFileInfo(employeeFilesService.employeeFileDownloadLinks(employee));
      employees.add(employee);
    }
    model.addAttribute("employees", employees);
    return "report/employeeAllCount";
  }


//  offence and employee count
  @GetMapping( "/offenceCount" )
  public String offenceEmployee(Model model) {
    model.addAttribute("offences", offenceService.findAll());
    return "report/offenceReport";
  }

  @GetMapping( "/offenceCount/{id}" )
  public String offenceEmployeeSearch(@PathVariable( "id" ) Integer id, Model model) {
    List< Employee > employees = new ArrayList<>();
    Offence offence = offenceService.findById(id);
    model.addAttribute("offenceDetail", offence);
    censureService.findByOffence(offence).forEach(x -> employees.add(x.getEmployee()));
    model.addAttribute("employees", employees);
    return "report/offenceEmployee";
  }

}
