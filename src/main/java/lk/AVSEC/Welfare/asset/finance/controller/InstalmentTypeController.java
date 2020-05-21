package lk.AVSEC.Welfare.asset.finance.controller;

import lk.AVSEC.Welfare.asset.finance.entity.Enum.CollectionType;
import lk.AVSEC.Welfare.asset.finance.service.InstalmentTypeService;
import lk.AVSEC.Welfare.asset.finance.entity.InstalmentType;
import lk.AVSEC.Welfare.util.interfaces.AbstractController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

@Controller
@RequestMapping("/instalmentType")
public class InstalmentTypeController implements AbstractController<InstalmentType, Integer> {

    private final InstalmentTypeService instalmentTypeService;

    @Autowired
    public InstalmentTypeController(InstalmentTypeService instalmentTypeService) {
        this.instalmentTypeService = instalmentTypeService;
    }

    private String commonThing(Model model, Boolean booleanValue, InstalmentType instalmentType) {
        model.addAttribute("collectionTypes", CollectionType.values());
        model.addAttribute("addStatus", booleanValue);
        model.addAttribute("instalmentType", instalmentType);
        return "instalmentType/addInstalmentType";
    }

    @Override
    public String findAll(Model model) {
        model.addAttribute("instalmentTypeis", instalmentTypeService.findAll());
        return "instalmentType/instalmentType";
    }

    @Override
    public String form(Model model) {
        return commonThing(model, false, new InstalmentType());
    }

    @Override
    public String findById(Integer id, Model model) {
        model.addAttribute("instalmentTypeDetail", instalmentTypeService.findById(id));
        return "instalmentType/instalmentType-detail";
    }

    @Override
    public String edit(Integer id, Model model) {
        return commonThing(model, true, instalmentTypeService.findById(id));
    }

    @Override
    public String persist(@Valid @ModelAttribute InstalmentType instalmentType, BindingResult bindingResult, RedirectAttributes redirectAttributes, Model model) throws Exception {
        if (bindingResult.hasErrors()) {
            return commonThing(model, false, instalmentType);
        }
        redirectAttributes.addFlashAttribute("instalmentTypeDetail", instalmentTypeService.persist(instalmentType));
        return "redirect:/instalmentType";
    }

    @Override
    public String delete(Integer id, Model model) {
        // there is no permission to delete
        return "redirect:/instalmentType";
    }
}
