package com.example.classapp.security.auth.implement;
import com.example.classapp.core.daos.StudentsDao;
import com.example.classapp.core.daos.TeachersDao;
import com.example.classapp.core.daos.UserDao;
import com.example.classapp.shared.dtos.GlobalResponse;
import com.example.classapp.core.entity.StudentEntity;
import com.example.classapp.core.entity.TeacherEntity;
import com.example.classapp.core.entity.UserEntity;
import com.example.classapp.core.enums.UserType;
import com.example.classapp.shared.exceptions.GlobalException;
import com.example.classapp.security.auth.AuthService;
import com.example.classapp.security.dtos.request.LoginRequest;
import com.example.classapp.security.dtos.request.RegisterRequest;
import com.example.classapp.security.dtos.response.AuthResponse;
import com.example.classapp.security.jwtUtils.JwtTokenProvider;
import com.example.classapp.shared.utils.ResponseGenerator;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private StudentsDao studentsDao;

    @Autowired
    private TeachersDao teachersDao;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;


    @Transactional
    public ResponseEntity<GlobalResponse<AuthResponse>> register(RegisterRequest registerRequest,HttpServletResponse httpResponse) {

       if (userDao.existsByEmail(registerRequest.getEmail())) {
           throw new GlobalException(HttpStatus.OK, "The email is already in");
       }

        UserEntity user = new UserEntity();
        user.setName(registerRequest.getName());
        user.setEmail(registerRequest.getEmail());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        user.setUserType(registerRequest.getUserType() != null ? registerRequest.getUserType() : UserType.STUDENT);
        user = userDao.save(user);
        createUserStudentOrTeacher(user);

        String token = jwtTokenProvider.generateTokenFromEmail(
                user.getEmail(),
                user.getId(),
                user.getName(),
                user.getUserType().name()
        );
        saveCookie(token,httpResponse);
        AuthResponse response = AuthResponse.builder()
                    .token(token)
                    .username(user.getName())
                    .userType(String.valueOf(user.getUserType()))
                    .build();

    return ResponseGenerator.generateResponse("User Registered Succesfully",response, HttpStatus.CREATED);

    }


    @Transactional
    public ResponseEntity<GlobalResponse<AuthResponse>> login(LoginRequest loginRequest, HttpServletResponse httpResponse) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword())
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserEntity user = userDao.findByEmail(loginRequest.getEmail())
                .orElseThrow(() -> new GlobalException(HttpStatus.NOT_FOUND, "User Not Found"));

        String token = jwtTokenProvider.generateToken(
                authentication,
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getUserType().name()
        );
        saveCookie(token,httpResponse);
        AuthResponse response = AuthResponse.builder()
                .token(token)
                .username(user.getName())
                .userType(String.valueOf(user.getUserType()))
                .build();
       return ResponseGenerator.generateResponse("User Logged Succesfully",response, HttpStatus.OK);
    };


    public ResponseEntity<GlobalResponse<Void>> logout(HttpServletResponse httpResponse) {
        clearCookie(httpResponse);
        SecurityContextHolder.clearContext();
        return ResponseGenerator.generateResponse("Logout Succesfully",null, HttpStatus.OK);
    }

    public ResponseEntity<GlobalResponse<Void>> verifyToken(HttpServletRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated() || authentication instanceof AnonymousAuthenticationToken) {
            return ResponseGenerator.generateResponse("Invalid Token", HttpStatus.UNAUTHORIZED);
        }

        return ResponseGenerator.generateResponse("Verified",HttpStatus.OK);
    }


    public void  createUserStudentOrTeacher(UserEntity user) {
        if(user.getUserType()==UserType.STUDENT) {
            StudentEntity student = new StudentEntity();
            student.setUser(user);
            studentsDao.save(student);
        }else if(user.getUserType()==UserType.TEACHER) {
            TeacherEntity teacher = new TeacherEntity();
            teacher.setUser(user);
            teachersDao.save(teacher);
        }
    }

    private void saveCookie(String token, HttpServletResponse response) {
        Cookie cookie = new Cookie("access_token", token);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setMaxAge(259200);
        cookie.setSecure(true);
        response.addCookie(cookie);
    }

    private void clearCookie(HttpServletResponse response) {
        Cookie cookie = new Cookie("access_token", null);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setMaxAge(1);
        cookie.setSecure(true);
        response.addCookie(cookie);
    }


}
