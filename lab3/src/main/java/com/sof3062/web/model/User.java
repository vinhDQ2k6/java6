package com.sof3062.web.model;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "users")
public class User {
    @Id
    String username;
    String password;
    boolean enabled;
    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER)
    List<UserRole> userRoles;
}