package com.example.demo.controller;

import com.example.demo.model.Company;
import com.example.demo.service.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/companies")
public class CompanyController {

    @Autowired
    private CompanyService companyService;

    @GetMapping
    public String listCompanies(Model model) {
        model.addAttribute("companies", companyService.getAllCompanies());
        return "company_list";
    }

    @GetMapping("/new")
    public String showNewCompanyForm(Model model) {
        model.addAttribute("company", new Company());
        return "company_form";
    }

    @PostMapping
    public String saveCompany(@ModelAttribute("company") Company company, Model model) {
        try {
            if (company.getName() == null || company.getName().isEmpty()) {
                model.addAttribute("error", "Tên công ty không được để trống");
                return "company_form"; // Trả về form nhập lại nếu có lỗi
            }

            if (company.getId() != null) {
                companyService.updateCompany(company.getId(), company);
            } else {
                companyService.createCompany(company);
            }

            return "redirect:/companies";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "company_form"; // Trả về form với lỗi
        }
    }

    @GetMapping("/{id}/edit")
    public String showEditCompanyForm(@PathVariable("id") Long id, Model model) {
        Company company = companyService.getCompanyById(id);
        if (company != null) {
            model.addAttribute("company", company);
            return "company_form";
        } else {
            return "redirect:/companies";
        }
    }

    @PutMapping("/update")
    public String updateCompany(@ModelAttribute("company") Company company) {
        companyService.updateCompany(company.getId(), company);
        return "redirect:/companies";
    }

    @GetMapping("/{id}/delete")
    public String deleteCompany(@PathVariable("id") Long id) {
        companyService.deleteCompany(id);
        return "redirect:/companies";
    }
}