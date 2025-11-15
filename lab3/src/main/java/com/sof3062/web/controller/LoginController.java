package com.sof3062.web.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class LoginController {
    @RequestMapping("/login/{action}")
    public String login(Model model, @PathVariable("action") String action) {
        Map<String, String> msgs = new HashMap<String, String>() {
            {
                put("form", "Please enter your credentials");
                put("success", "Login successful!");
                put("failure", "Login failed. Please try again.");
                put("logout", "You have been logged out.");
            }
        };
        model.addAttribute("message", msgs.containsKey(action) ? msgs.get(action) : "");

        return "page";
    }
}