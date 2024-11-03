//package com.example.task_for_VitaSoft.security;
//
//import com.example.task_for_VitaSoft.model.User;
//import com.example.task_for_VitaSoft.repository.UserRepository;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.AuthenticationException;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
//import org.springframework.web.bind.annotation.*;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.util.HashMap;
//import java.util.Map;
//
//@RestController
//@RequestMapping("/users/auth")
//public class AuthenticationRestController {
//
////    private AuthenticationManager authenticationManager;
//    private final UserRepository userRepository;
////    private final JwtTokenProvider jwtTokenProvider;
//
////    public AuthenticationRestController(AuthenticationManager authenticationManager,
////                                        UserRepository userRepository,
////                                        JwtTokenProvider jwtTokenProvider) {
////        this.authenticationManager = authenticationManager;
////        this.userRepository = userRepository;
////        this.jwtTokenProvider = jwtTokenProvider;
////    }
//
////    public AuthenticationRestController(AuthenticationManager authenticationManager,
////                                        UserRepository userRepository) {
////        this.authenticationManager = authenticationManager;
////        this.userRepository = userRepository;
////    }
//
//    public AuthenticationRestController(UserRepository userRepository) {
//        this.userRepository = userRepository;
//    }
//
////    @PostMapping("/login")
//////    @GetMapping("/login")
////    public ResponseEntity<?> authenticate(@RequestBody AuthenticationRequestDTO request) {
////        try {
//////            authenticationManager.authenticate(
//////                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
////            User user = userRepository.findByEmail(request.getEmail())
////                    .orElseThrow(() -> new UsernameNotFoundException("User doesn't exists"));
//////            String token = jwtTokenProvider.createToken(request.getEmail(), user.getRoles().toString());
////            Map<Object, Object> response = new HashMap<>();
////            response.put("email", request.getEmail());
//////            response.put("token", token);
////            return ResponseEntity.ok("Hey, user!");
////        } catch (AuthenticationException e) {
////            return new ResponseEntity<>("Invalid email/password combination", HttpStatus.FORBIDDEN);
////        }
////    }
//
////    @PostMapping("/login")
////        public String hello() {
////        return "<h2> Hey, user! </h2>";
////    }
//
////    @PostMapping("/logout")
//    @GetMapping("/logout")
//    public String logout(HttpServletRequest request, HttpServletResponse response) {
//        SecurityContextLogoutHandler securityContextLogoutHandler = new SecurityContextLogoutHandler();
//        securityContextLogoutHandler.logout(request, response, null);
//        return "<h2> Вы вышли из системы! </h2>";
//    }
//
//    @GetMapping("/login")
//    public String hello() {
//        return "<h2> Hey, user! </h2>";
//    }
//
////    @GetMapping("/login")
////    public ResponseEntity<?> authenticate(@RequestBody AuthenticationRequestDTO request) {
////        try {
////            authenticationManager.authenticate(
////                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
//////            User user = userRepository.findByEmail(request.getEmail())
//////                    .orElseThrow(() -> new UsernameNotFoundException("User doesn't exists"));
////            userRepository.findByEmail(request.getEmail())
////                    .orElseThrow(() -> new UsernameNotFoundException("User doesn't exists"));
//////            String token = jwtTokenProvider.createToken(request.getEmail(), user.getRoles().toString());
////            Map<Object, Object> response = new HashMap<>();
////            response.put("email", request.getEmail());
//////            response.put("token", token);
////            return ResponseEntity.ok("Вы вошли в систему!");
////        } catch (AuthenticationException e) {
////            return new ResponseEntity<>("Invalid email/password combination", HttpStatus.FORBIDDEN);
////        }
////    }
//}
