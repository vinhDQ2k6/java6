package com.sof3062.lab7.infrastructure.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class SpaController {

  // Chuyển hướng mọi request không có đuôi mở rộng (không phải file .js, .css, .png...)
  // về index.html để Vue Router xử lý.
  // Ví dụ: /login, /catalog
  // Lưu ý: Pattern này chỉ hỗ trợ route 1 cấp (VD: /catalog). Nếu có route lồng nhau (VD: /catalog/1), cần thêm pattern khác.
  @RequestMapping(value = "/{path:[^\\.]*}")
  public String forward() {
    return "forward:/index.html";
  }
}
