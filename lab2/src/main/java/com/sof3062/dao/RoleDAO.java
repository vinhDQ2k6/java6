package com.sof3062.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sof3062.web.model.Role;

public interface RoleDAO extends JpaRepository<Role, String> {

}