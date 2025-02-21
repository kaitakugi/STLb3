package com.example.demo.security;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.security.core.userdetails.UserDetailsService;
import com.example.demo.repository.CustomRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.GrantedAuthority;
import com.example.demo.model.Role;
import com.example.demo.model.UserEntity;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CustomerUserDetailsService implements UserDetailsService {

    private CustomRepository customrepository;

    @Autowired
    public CustomerUserDetailsService(CustomRepository customrepository) {
        super();
        this.customrepository = customrepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity user = customrepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("username not found"));
        System.out.println("Roles for user: " + user.getRoles());
        return new User(user.getUsername(), user.getPassword(), mapToAuthorities(user.getRoles()));
    }

    private Collection<GrantedAuthority> mapToAuthorities(List<Role> roles) {
        return roles.stream().map(role -> new SimpleGrantedAuthority("ROLE_" + role.getName())).collect(Collectors.toList());
    }

}

