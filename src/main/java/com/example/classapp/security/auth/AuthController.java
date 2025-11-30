package com.example.classapp.security.auth;


import com.example.classapp.shared.dtos.GlobalResponse;
import com.example.classapp.security.dtos.request.LoginRequest;
import com.example.classapp.security.dtos.request.RegisterRequest;
import com.example.classapp.security.dtos.response.AuthResponse;
import com.example.classapp.shared.constants.Api;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(Api.API_V1)
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;


    @PostMapping(Api.AUTH + "/login")
    public ResponseEntity<GlobalResponse<AuthResponse>> login(@RequestBody LoginRequest loginRequest, HttpServletResponse response) {
        return authService.login(loginRequest,response);
    }

    @PostMapping(Api.AUTH+"/register")
    public ResponseEntity<GlobalResponse<AuthResponse>> register(@RequestBody RegisterRequest registerRequest,HttpServletResponse response) {
        return authService.register(registerRequest,response);
    }

    @PostMapping(Api.AUTH+"/logout")
    public ResponseEntity<GlobalResponse<Void>> logout(HttpServletResponse response) {
        return authService.logout(response);
    }

    @GetMapping(Api.AUTH+"/verify")
    public ResponseEntity<GlobalResponse<Void>> verify(HttpServletRequest request) {
        return authService.verifyToken(request);
    }

}
