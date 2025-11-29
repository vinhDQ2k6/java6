package com.sof3062.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sof3062.model.Student;
import com.sof3062.util.HttpClient;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class StudentService {

  private static final String BASE_URL =
    "https://java6lab4-eb61b-default-rtdb.asia-southeast1.firebasedatabase.app/students";
  private final ObjectMapper mapper = new ObjectMapper();

  public List<Student> findAll() {
    List<Student> list = new ArrayList<>();
    try {
      String url = BASE_URL + ".json";
      HttpURLConnection conn = HttpClient.openConnection(url, "GET");
      byte[] data = HttpClient.readData(conn);
      String json = new String(data);

      if (json.equals("null")) return list;

      JsonNode root = mapper.readTree(json);
      Iterator<Map.Entry<String, JsonNode>> fields = root.fields();
      while (fields.hasNext()) {
        Map.Entry<String, JsonNode> entry = fields.next();
        JsonNode node = entry.getValue();
        Student student = mapper.treeToValue(node, Student.class);
        // Ensure ID is set if it wasn't in the body (though we put it there)
        if (student.getId() == null) {
          student.setId(entry.getKey());
        }
        list.add(student);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return list;
  }

  public Student findById(String id) {
    try {
      String url = BASE_URL + "/" + id + ".json";
      HttpURLConnection conn = HttpClient.openConnection(url, "GET");
      byte[] data = HttpClient.readData(conn);
      String json = new String(data);

      if (json.equals("null")) return null;

      return mapper.readValue(json, Student.class);
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }

  public void create(Student student) {
    update(student); // Create and Update are the same with PUT
  }

  public void update(Student student) {
    try {
      String url = BASE_URL + "/" + student.getId() + ".json";
      String json = mapper.writeValueAsString(student);
      HttpURLConnection conn = HttpClient.openConnection(url, "PUT");
      HttpClient.postData(conn, json.getBytes());
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void delete(String id) {
    try {
      String url = BASE_URL + "/" + id + ".json";
      HttpURLConnection conn = HttpClient.openConnection(url, "DELETE");
      HttpClient.readData(conn);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
