package com.example.demo.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

@Configuration
public class SecurityConfig {
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .headers(headers -> headers.frameOptions(frameOptions -> frameOptions.disable())) // Allow H2 Console
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/register/**", "/login/**","/companies/**","/h2-console/**").permitAll()
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .requestMatchers("/user/**").hasRole("USER")
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .successHandler(customAuthenticationSuccessHandler())
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login")
                        .permitAll()
                );

        return http.build();
    }


//    public AuthenticationSuccessHandler customAuthenticationSuccessHandler() {
//        return (request, response, authentication) -> {
//            // Kiểm tra role của người dùng
//            String firstAuthority = authentication.getAuthorities().stream()
//                    .map(grantedAuthority -> grantedAuthority.getAuthority())
//                    .findFirst()
//                    .orElse(null); // Hoặc một giá trị mặc định nếu không có quyền nào
//            System.out.println("firstAuthority: " + firstAuthority);
//            if ("ROLE_ADMIN".equals(firstAuthority)) {
//                response.sendRedirect("/companies");
//            } else {
//                response.sendRedirect("");
//            }
//
//        };
//    }
    @Bean
    public AuthenticationSuccessHandler customAuthenticationSuccessHandler() {
        return (request, response, authentication) -> {
            // In ra danh sách quyền của người dùng
            System.out.println("Granted Authorities: " + authentication.getAuthorities());

            // Kiểm tra role của người dùng
            boolean isAdmin = authentication.getAuthorities().stream()
                    .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ADMIN"));

            if (isAdmin) {
                response.sendRedirect("/companies");
            } else {
                response.sendRedirect("/user/home");
            }
        };
    }



    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}

