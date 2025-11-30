package com.sof3062.dto;

import lombok.Data;

/**
 * Data Transfer Object for login requests.
 */
@Data
public class LoginRequest {
    private String username;
    private String password;
}
