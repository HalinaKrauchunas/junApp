package com.example.junapp.model;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashSet;

@Entity
@Data
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Поле не заполнено")
    private String username;

    @Size(min = 7, max = 80, message = "Пароль должен содержать не менее 7 символов")
    private String password;

    @NotBlank(message = "Поле не заполнено")
    private String email;

    private boolean active;

    @Column(name = "activation_code", columnDefinition = "TEXT")
    private String activationCode;

    @Column(name = "last_visit")
    private LocalDateTime lastVisit;

    private String locale;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "users_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Collection<Role> roles;

    public Collection<Role> getRoles() {
        if (roles == null) {
            roles = new HashSet<>();
        }
        return roles;
    }
}