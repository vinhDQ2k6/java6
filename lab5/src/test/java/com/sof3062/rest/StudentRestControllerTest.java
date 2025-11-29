package com.sof3062.rest;

import static org.junit.jupiter.api.Assertions.*;

import com.sof3062.model.Student;
import java.util.Arrays;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestPropertySource(properties = "spring.flyway.validate-on-migrate=false")
public class StudentRestControllerTest {

  @Autowired
  private TestRestTemplate restTemplate;

  // Test data
  private static final String STUDENT_ID_1 = "TEST001";
  private static final String STUDENT_ID_2 = "TEST002";

  @Test
  @Order(1)
  public void testCreateStudents() {
    System.out.println("--- Test 1: Create Students ---");
    // Create Student 1
    Student s1 = new Student(STUDENT_ID_1, "Test Student One", 8.5, true);
    ResponseEntity<Student> response1 = restTemplate.postForEntity(
      "/api/students",
      s1,
      Student.class
    );
    System.out.println("Created Student 1: " + response1.getBody());
    assertEquals(HttpStatus.OK, response1.getStatusCode());
    assertEquals(STUDENT_ID_1, response1.getBody().getId());

    // Create Student 2
    Student s2 = new Student(STUDENT_ID_2, "Test Student Two", 9.0, false);
    ResponseEntity<Student> response2 = restTemplate.postForEntity(
      "/api/students",
      s2,
      Student.class
    );
    System.out.println("Created Student 2: " + response2.getBody());
    assertEquals(HttpStatus.OK, response2.getStatusCode());
    assertEquals(STUDENT_ID_2, response2.getBody().getId());
  }

  @Test
  @Order(2)
  public void testFindAll() {
    System.out.println("--- Test 2: Find All Students ---");
    ResponseEntity<Student[]> response = restTemplate.getForEntity(
      "/api/students",
      Student[].class
    );
    System.out.println(
      "Found Students: " + Arrays.toString(response.getBody())
    );
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertTrue(response.getBody().length >= 2);
  }

  @Test
  @Order(3)
  public void testFindById() {
    System.out.println("--- Test 3: Find Student By ID ---");
    ResponseEntity<Student> response = restTemplate.getForEntity(
      "/api/students/" + STUDENT_ID_1,
      Student.class
    );
    System.out.println(
      "Found Student (" + STUDENT_ID_1 + "): " + response.getBody()
    );
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals("Test Student One", response.getBody().getName());
  }

  @Test
  @Order(4)
  public void testUpdateStudent() {
    System.out.println("--- Test 4: Update Student ---");
    Student updatedS1 = new Student(
      STUDENT_ID_1,
      "Updated Student One",
      9.5,
      true
    );

    // Use exchange for PUT as put() returns void
    ResponseEntity<Student> response = restTemplate.exchange(
      "/api/students/" + STUDENT_ID_1,
      HttpMethod.PUT,
      new HttpEntity<>(updatedS1),
      Student.class
    );
    System.out.println("Updated Student: " + response.getBody());

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals("Updated Student One", response.getBody().getName());
    assertEquals(9.5, response.getBody().getMark());
  }

  @Test
  @Order(5)
  public void testDeleteStudents() {
    System.out.println("--- Test 5: Delete Students ---");
    restTemplate.delete("/api/students/" + STUDENT_ID_1);
    System.out.println("Deleted Student 1: " + STUDENT_ID_1);

    ResponseEntity<Student> response1 = restTemplate.getForEntity(
      "/api/students/" + STUDENT_ID_1,
      Student.class
    );
    System.out.println(
      "Check Student 1 (Expected 404): " + response1.getStatusCode()
    );
    assertEquals(HttpStatus.NOT_FOUND, response1.getStatusCode());

    restTemplate.delete("/api/students/" + STUDENT_ID_2);
    System.out.println("Deleted Student 2: " + STUDENT_ID_2);

    ResponseEntity<Student> response2 = restTemplate.getForEntity(
      "/api/students/" + STUDENT_ID_2,
      Student.class
    );
    System.out.println(
      "Check Student 2 (Expected 404): " + response2.getStatusCode()
    );
    assertEquals(HttpStatus.NOT_FOUND, response2.getStatusCode());
  }
}
