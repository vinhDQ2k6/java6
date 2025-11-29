package com.sof3062.dao;

import com.sof3062.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentDAO extends JpaRepository<Student, String> {}
