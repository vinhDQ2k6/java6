package com.sof3062.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sof3062.web.model.UserRole;
import com.sof3062.web.model.UserRoleKey;

public interface UserRoleDAO extends JpaRepository<UserRole, UserRoleKey> {

}