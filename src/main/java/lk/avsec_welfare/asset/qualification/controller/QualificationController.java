package lk.avsec_welfare.asset.qualification.controller;

import lk.avsec_welfare.asset.common_asset.model.enums.Province;
import lk.avsec_welfare.asset.course.service.CourseService;
import lk.avsec_welfare.asset.dependent.service.DependentEmployeeService;
import lk.avsec_welfare.asset.employee.entity.Employee;
import lk.avsec_welfare.asset.employee.service.EmployeeFilesService;
import lk.avsec_welfare.asset.employee.service.EmployeeService;
import lk.avsec_welfare.asset.employee_working_place.service.EmployeeWorkingPlaceService;
import lk.avsec_welfare.asset.qualification.entity.Qualification;
import lk.avsec_welfare.asset.qualification.service.QualificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

@Controller
@RequestMapping( "/qualification" )
public class QualificationController {

  private final QualificationService qualificationService;
  private final EmployeeService employeeService;
  private final CourseService courseService;
  private final EmployeeFilesService employeeFilesService;
  private final EmployeeWorkingPlaceService employeeWorkingPlaceService;
  private final DependentEmployeeService dependentEmployeeService;

  @Autowired
  public QualificationController(QualificationService qualificationService,
                                 EmployeeService employeeService, CourseService courseService, EmployeeFilesService employeeFilesService,
                                 EmployeeWorkingPlaceService employeeWorkingPlaceService,
                                 DependentEmployeeService dependentEmployeeService) {
    this.qualificationService = qualificationService;
    this.employeeService = employeeService;
    this.courseService = courseService;
    this.employeeFilesService = employeeFilesService;
    this.employeeWorkingPlaceService = employeeWorkingPlaceService;
    this.dependentEmployeeService = dependentEmployeeService;
  }

  private String commonThing(Model model, Boolean booleanValue, Qualification qualificationObject) {
    model.addAttribute("provinces", Province.values());
    model.addAttribute("addStatus", booleanValue);
    model.addAttribute("qualification", qualificationObject);
    model.addAttribute("courses", courseService.findAll());


    Employee employee = employeeService.findById(qualificationObject.getEmployee().getId());
    model.addAttribute("files", employeeFilesService.employeeFileDownloadLinks(employee));
    model.addAttribute("employeeDetail", employee);
    model.addAttribute("employeeWorkingPlaces", employeeWorkingPlaceService.findByEmployee(employee));
    model.addAttribute("dependentEmployees", dependentEmployeeService.findByEmployee(employee));
    model.addAttribute("contendHeader", "Employee View Details");
    return "qualification/addQualification";
  }

  @GetMapping
  public String findAll(Model model) {
    model.addAttribute("qualifications", qualificationService.findAll());
    return "qualification/qualification";
  }

  @GetMapping( "/add/{id}" )
  public String form(@PathVariable Integer id, Model model) {
    Qualification newQualification = new Qualification();
    newQualification.setEmployee(employeeService.findById(id));
    return commonThing(model, false, newQualification);
  }

  @GetMapping( "/{id}" )
  public String findById(@PathVariable Integer id, Model model) {
    model.addAttribute("qualificationDetail", qualificationService.findById(id));
    return "qualification/qualification-detail";
  }

  @GetMapping( "/edit/{id}" )
  public String edit(@PathVariable Integer id, Model model) {
    return commonThing(model, true, qualificationService.findById(id));
  }

  @PostMapping( value = {"/save", "/update"} )
  public String persist(@Valid @ModelAttribute Qualification qualification, BindingResult bindingResult,
                        RedirectAttributes redirectAttributes, Model model) {
    if ( bindingResult.hasErrors() ) {
      return commonThing(model, false, qualification);
    }
    qualificationService.persist(qualification);
    redirectAttributes.addFlashAttribute("employees", employeeService.findAll());
    return "redirect:/employee";
  }

  @GetMapping( "/delete/{id}" )
  public String delete(@PathVariable Integer id, Model model) {
    qualificationService.delete(id);
    return "redirect:/qualification";
  }


}
