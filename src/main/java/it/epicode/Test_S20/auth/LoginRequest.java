package it.epicode.Test_S20.auth;

import lombok.Data;

@Data
public class LoginRequest {
    private String username;
    private String password;
}
