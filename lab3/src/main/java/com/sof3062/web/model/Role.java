package com.sof3062.web.model;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "roles")
public class Role {
    @Id
    @Column(name = "role_id")
    String id;
    @Column(name = "role_name")
    String name;
    @OneToMany(mappedBy = "role")
    List<UserRole> userRoles;
}