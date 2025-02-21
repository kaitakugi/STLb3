package com.example.demo.controller;

import com.example.demo.model.Company;
import com.example.demo.model.User;
import com.example.demo.service.UserService;
import com.example.demo.service.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/companies/{companyId}/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private CompanyService companyService;

    @GetMapping("")
    public String listUsers(@PathVariable("companyId") Long companyId, Model model) {
        Company company = companyService.getCompanyById(companyId);
        model.addAttribute("users", company.getUsers());
        model.addAttribute("company", company);
        return "user_list";
    }

    @GetMapping("/new")
    public String showNewUserForm(@PathVariable("companyId") Long companyId, Model model) {
        User user = new User();
        Company company = companyService.getCompanyById(companyId);
        user.setCompany(company);
        model.addAttribute("user", user);
        model.addAttribute("company", company);
        return "user_form";
    }

    @PostMapping("/save")
    public String saveUser(@PathVariable("companyId") Long companyId, @ModelAttribute User user) {
        Company company = companyService.getCompanyById(companyId);
        user.setCompany(company);
        userService.createUser(user);
        return "redirect:/companies/" + companyId + "/users";
    }

    @GetMapping("/{id}/edit")
    public String showEditUserForm(@PathVariable("companyId") Long companyId, @PathVariable("id") Long id, Model model) {
        User user = userService.getUserById(id);
        Company company = companyService.findById(companyId);
        if (user == null || !user.getCompany().getId().equals(companyId)) {
            return "redirect:/companies/" + companyId + "/users";
        }

        model.addAttribute("user", user);
        model.addAttribute("company", company);
        return "user_form";
    }

    @PutMapping("/update")
    public String updateUser(@PathVariable("companyId") Long companyId, @ModelAttribute("user") User user) {
        userService.updateUser(user.getId(), user);
        return "redirect:/companies/" + companyId + "/users";
    }

    @PostMapping("/{id}/delete")
    public String deleteUser(@PathVariable("companyId") Long companyId, @PathVariable("id") Long id) {
        User user = userService.getUserById(id);
        if (user != null && user.getCompany().getId().equals(companyId)) {
            userService.deleteUser(id);
        }
        return "redirect:/companies/" + companyId + "/users";
    }


}
