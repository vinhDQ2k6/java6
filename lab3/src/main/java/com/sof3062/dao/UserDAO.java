package com.sof3062.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sof3062.web.model.User;

public interface UserDAO extends JpaRepository<User, String> {

}