package com.example.usersmanagementsystem.controller;

import com.example.usersmanagementsystem.dto.AuthRequest;
import com.example.usersmanagementsystem.dto.JwtResponse;
import com.example.usersmanagementsystem.dto.RefreshTokenRequest;
import com.example.usersmanagementsystem.dto.ReqRes;
import com.example.usersmanagementsystem.entity.OurUsers;
import com.example.usersmanagementsystem.entity.RefreshToken;
import com.example.usersmanagementsystem.exception.AccessForbiddenException;
import com.example.usersmanagementsystem.exception.UnauthorizedAccessException;
import com.example.usersmanagementsystem.repository.UsersRepo;
import com.example.usersmanagementsystem.service.JWTUtils;
import com.example.usersmanagementsystem.service.RefreshTokenService;
import com.example.usersmanagementsystem.service.UserManagementService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserManagementController {
    @Autowired
    private UserManagementService userManagementService;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JWTUtils jwtUtils;
    @Autowired
    private UsersRepo usersRepo;

    @Autowired
    private RefreshTokenService refreshTokenService;


    @PostMapping("/auth/login")
    public ResponseEntity<JwtResponse> login(@RequestBody @Valid AuthRequest authRequest) throws Exception {
//                try {
//                    Authentication authentication =  authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getEmail(),authRequest.getPassword()));
//                    if(authentication.isAuthenticated()){
//                        OurUsers user = usersRepo.findByEmail(authRequest.getEmail()).orElseThrow(() -> new UserNotFoundException("Username không tồn tại trong database"));
//                        RefreshToken refreshToken = refreshTokenService.createRefreshToken(authRequest.getEmail());
//                        return ResponseEntity.ok(JwtResponse.builder().token(jwtUtils.generateToken(user)).refreshToken(refreshToken.getToken()).role(user.getRole()).build());
//                    }else {
//                        throw new UserNotFoundException("Username or password does not exist in the database");
//                    }
//                }catch (BadCredentialsException e){
//                    throw e;
//                }

        OurUsers user = usersRepo.findByEmail(authRequest.getEmail()).orElseThrow(() -> new UnauthorizedAccessException("Account does not exist"));
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        boolean authenticated = passwordEncoder.matches(authRequest.getPassword(),user.getPassword());

        if (!authenticated){
            throw new UnauthorizedAccessException("Đăng nhập sai, xin vui lòng thử lại");
        }
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(authRequest.getEmail());
        return ResponseEntity.ok(JwtResponse.builder().token(jwtUtils.generateToken(user)).refreshToken(refreshToken.getToken()).role(user.getRole()).build());
    }
    @PostMapping("/auth/refresh-token")
    public ResponseEntity<JwtResponse> refreshToken(@RequestBody RefreshTokenRequest refresh){
        return refreshTokenService.findByToken(refresh.getToken())
                .map(refreshTokenService::verifyExpriration)
                .map(RefreshToken::getOurUsers)
                .map(ourUsers -> {
                    String accessToken = jwtUtils.generateToken(ourUsers);
                    return ResponseEntity.ok(JwtResponse.builder().token(accessToken).refreshToken(refresh.getToken()).role(ourUsers.getRole()).build());
                }).orElseThrow(() -> new RuntimeException("Refresh token is not in database"));
    }

    @PostMapping("/auth/logout")
    public ResponseEntity<Void> logout(@RequestBody RefreshTokenRequest request){
        refreshTokenService.deleteByToken(request.getToken());
        return ResponseEntity.ok().build();
    }
    @PostMapping("/admin/register")
    public ResponseEntity<ReqRes> register(@RequestBody @Valid ReqRes register){
        return ResponseEntity.ok(userManagementService.register(register));
    }

    @GetMapping("/admin/get-all-users")
    public ResponseEntity<ReqRes> getAllUsers(){
        return ResponseEntity.ok(userManagementService.getAllUsers());
    }
    @GetMapping("/admin/get-users/{userId}")
    public ResponseEntity<ReqRes> getUserByID(@PathVariable Integer userId){
        return ResponseEntity.ok(userManagementService.getUsersById(userId));
    }
    @PutMapping("/admin/update/{userId}")
    public ResponseEntity<ReqRes> getUserByID(@PathVariable Integer userId, @RequestBody OurUsers reqres){
        return ResponseEntity.ok(userManagementService.updateUser(userId,reqres));
    }
    @GetMapping("/adminuser/get-profile")
    public ResponseEntity<ReqRes> getMyProfile(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        ReqRes response = userManagementService.getMyInfo(email);
        return  ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @DeleteMapping("/admin/delete/{userId}")
    public ResponseEntity<ReqRes> deleteUSer(@PathVariable Integer userId){
        return ResponseEntity.ok(userManagementService.deleteUser(userId));
    }
}
