package com.sof3062.rest;

import com.sof3062.dao.StudentDAO;
import com.sof3062.model.Student;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/students")
public class StudentRestController {

  @Autowired
  private StudentDAO dao;

  @GetMapping
  public List<Student> getAll() {
    return dao.findAll();
  }

  @GetMapping("/{id}")
  public ResponseEntity<Student> getOne(@PathVariable("id") String id) {
    if (!dao.existsById(id)) {
      return ResponseEntity.notFound().build();
    }
    return ResponseEntity.ok(dao.findById(id).get());
  }

  @PostMapping
  public Student post(@RequestBody Student student) {
    return dao.save(student);
  }

  @PutMapping("/{id}")
  public ResponseEntity<Student> put(
    @PathVariable("id") String id,
    @RequestBody Student student
  ) {
    if (!dao.existsById(id)) {
      return ResponseEntity.notFound().build();
    }
    student.setId(id); // Ensure ID matches path
    return ResponseEntity.ok(dao.save(student));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable("id") String id) {
    if (!dao.existsById(id)) {
      return ResponseEntity.notFound().build();
    }
    dao.deleteById(id);
    return ResponseEntity.ok().build();
  }
}
