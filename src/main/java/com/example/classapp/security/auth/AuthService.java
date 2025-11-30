package com.example.classapp.security.auth;


import com.example.classapp.shared.dtos.GlobalResponse;
import com.example.classapp.security.dtos.request.LoginRequest;
import com.example.classapp.security.dtos.request.RegisterRequest;
import com.example.classapp.security.dtos.response.AuthResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;

public interface AuthService {
    ResponseEntity<GlobalResponse<AuthResponse>> register(RegisterRequest registerRequest, HttpServletResponse httpResponse);
    ResponseEntity<GlobalResponse<AuthResponse>> login(LoginRequest loginRequest, HttpServletResponse httpResponse);
    ResponseEntity<GlobalResponse<Void>> logout(HttpServletResponse httpResponse);
    ResponseEntity<GlobalResponse<Void>> verifyToken(HttpServletRequest request);
}
