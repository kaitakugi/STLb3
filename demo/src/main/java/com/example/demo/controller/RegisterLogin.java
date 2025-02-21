package com.example.demo.controller;

import com.example.demo.model.Role;
import com.example.demo.model.UserEntity;
import com.example.demo.repository.CustomRepository;
import com.example.demo.repository.RoleRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

@Transactional
@Controller
public class RegisterLogin {

    private RoleRepository rolerepository;
    private CustomRepository customrepository;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public RegisterLogin(RoleRepository roleRepository, CustomRepository customRepository, PasswordEncoder passwordEncoder) {
        this.rolerepository = roleRepository;
        this.customrepository = customRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/register/admin")
    public String registerAdmin(Model model){
        model.addAttribute("user", new UserEntity());
        return "register_admin";
    }

    @PostMapping("/register/admin")
    public String processRegisterAd(@Valid @ModelAttribute("user") UserEntity user, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return "register_admin";
        }

        if (customrepository.existsByUsername(user.getUsername())) {
            model.addAttribute("error", "username exists");
            return "register_admin";
        }
        Role admin_role = rolerepository.findByName("ADMIN").get();
        Role user_role = rolerepository.findByName("USER").get();
        System.out.println("role name: " + user_role.getName());
        UserEntity newUser = new UserEntity();
        newUser.setUsername(user.getUsername());
        newUser.setPassword(passwordEncoder.encode(user.getPassword()));
        newUser.setEmail(user.getEmail());
        newUser.getRoles().add(admin_role);
        newUser.getRoles().add(user_role);

        customrepository.save(newUser);

        UserEntity savedUser = customrepository.findByUsername(user.getUsername()).get();
        System.out.println("Roles assigned: ");
        savedUser.getRoles().forEach(role -> System.out.println("Role name: " + role.getName()));

        return "login"; // Chuyển hướng đến trang thành công
    }

    @GetMapping("/register/user")
    public String showRegisterForm(Model model) {
        // Tạo đối tượng User trống để liên kết với form
        model.addAttribute("user", new UserEntity());
        return "register"; // Tên file HTML
    }

    @PostMapping("/register/user")
    public String processRegister(@Valid @ModelAttribute("user") UserEntity user, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return "register";
        }

        if (customrepository.existsByUsername(user.getUsername())) {
            model.addAttribute("error", "username exists");
            return "register";
        }
        Role user_role = rolerepository.findByName("USER").get();
        System.out.println("role name: " + user_role.getName());
        UserEntity newUser = new UserEntity();
        newUser.setUsername(user.getUsername());
        newUser.setPassword(passwordEncoder.encode(user.getPassword()));
        newUser.setEmail(user.getEmail());
        newUser.getRoles().add(user_role);

        customrepository.save(newUser);
        return "login"; // Chuyển hướng đến trang thành công
    }

    @GetMapping("/login")
    public String showLoginForm() {
        return "login";
    }

}
