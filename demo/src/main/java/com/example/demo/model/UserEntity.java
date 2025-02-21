package com.example.demo.model;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
@Table(name="user_entity")
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotNull(message="Username is require")
    @Size(min=5, max=100)
    private String username;

    @NotNull(message="Password is require")
    @Size(min=6, max=100)
    @Column(name="pass")
    private String password;

    @NotNull(message = "Email is required")
    @Email()
    @Size(min=6, max=100)
    @Column(name="email")
    private String email;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "user_roles",
            joinColumns = @JoinColumn(name="user_id",  referencedColumnName  = "id"),
            inverseJoinColumns = @JoinColumn(name="role_id", referencedColumnName ="id"))
    private List<Role> roles = new ArrayList<>();

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

    public UserEntity(@NotNull(message = "Username is require") @Size(min = 6, max = 100) String username,
                      @NotNull(message = "Password is require") @Size(min = 6, max = 100) String password,
                      @NotNull(message = "Email is required") String email,
                      List<Role> roles) {
        super();
        this.username = username;
        this.password = password;
        this.email = email;
        this.roles = roles;
    }

    public UserEntity() {
        super();
    }

}

