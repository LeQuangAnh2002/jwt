package com.example.usersmanagementsystem.service;

import com.example.usersmanagementsystem.dto.AuthRequest;
import com.example.usersmanagementsystem.dto.JwtResponse;
import com.example.usersmanagementsystem.dto.ReqRes;
import com.example.usersmanagementsystem.entity.OurUsers;
import com.example.usersmanagementsystem.repository.UsersRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Service
public class UserManagementService {
    @Autowired
    private UsersRepo usersRepo;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public ReqRes register(ReqRes register){
        ReqRes reqsp = new ReqRes();
        try {
            OurUsers ourUser = new OurUsers();
            ourUser.setEmail(register.getEmail());
            ourUser.setCity(register.getCity());
            ourUser.setRole(register.getRole());
            ourUser.setName(register.getName());
            ourUser.setPassword(passwordEncoder.encode(register.getPassword()));
            ourUser.setImg(register.getImg());
            OurUsers ourUsersResult = usersRepo.save(ourUser);
            if (ourUsersResult.getId() > 0){
                reqsp.setStatusCode(200);
                reqsp.setOurUsers(ourUsersResult);
                reqsp.setMessage("User saved successfully");
            }
        }catch (Exception e){
            reqsp.setStatusCode(500);
            reqsp.setError(e.getMessage());
        }
        return reqsp;
    }

//    public JwtResponse login (AuthRequest authRequest){
//        JwtResponse response = new JwtResponse();
//        try {
//            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getEmail(),authRequest.getPassword()));
//            var user = usersRepo.findByEmail(authRequest.getEmail()).orElseThrow();
//            var jwt = jwtUtils.generateToken(user);
//            var refreshToken = refreshTokenService.createRefreshToken(authRequest.getEmail());
//            response.setStatusCode(200);
//            response.setToken(jwt);
//            response.setRole(user.getRole());
//            response.setRefreshToken(refreshToken.getToken());
//            response.setExpirationTime("24Hrs");
//            response.setMessage("Successfully Logged In");
//        }catch (Exception e){
//            response.setStatusCode(500);
//            response.setError("Username or password not found");
//        }
//        return response;
//    }

//    public ReqRes refreshToken(ReqRes refreshTokenReqiest){
//        ReqRes response = new ReqRes();
//        try{
//            String ourEmail = jwtUtils.extractUsername(refreshTokenReqiest.getToken());
//            OurUsers users = usersRepo.findByEmail(ourEmail).orElseThrow();
//            if (jwtUtils.isTokenValid(refreshTokenReqiest.getToken(), users)) {
//                var jwt = jwtUtils.generateToken(users);
//                response.setStatusCode(200);
//                response.setToken(jwt);
//                response.setRefreshToken(refreshTokenReqiest.getToken());
//                response.setExpirationTime("24Hr");
//                response.setMessage("Successfully Refreshed Token");
//            }
//            response.setStatusCode(200);
//            return response;
//
//        }catch (Exception e){
//            response.setStatusCode(500);
//            response.setMessage(e.getMessage());
//            return response;
//        }
//    }
    public ReqRes getAllUsers() {
        ReqRes reqRes = new ReqRes();

        try {
            List<OurUsers> result = usersRepo.findAll();
            if (!result.isEmpty()) {
                reqRes.setOurUsersList(result);
                reqRes.setStatusCode(200);
                reqRes.setMessage("Successful");
            } else {
                reqRes.setStatusCode(404);
                reqRes.setMessage("No users found");
            }
            return reqRes;
        } catch (Exception e) {
            reqRes.setStatusCode(500);
            reqRes.setMessage("Error occurred: " + e.getMessage());
            return reqRes;
        }
    }


    public ReqRes getUsersById(Integer id) {
        ReqRes reqRes = new ReqRes();
        try {
            OurUsers usersById = usersRepo.findById(id).orElseThrow(() -> new RuntimeException("User Not found"));
            reqRes.setOurUsers(usersById);
            reqRes.setStatusCode(200);
            reqRes.setMessage("Users with id '" + id + "' found successfully");
        } catch (Exception e) {
            reqRes.setStatusCode(500);
            reqRes.setMessage("Error occurred: " + e.getMessage());
        }
        return reqRes;
    }


    public ReqRes deleteUser(Integer userId) {
        ReqRes reqRes = new ReqRes();
        try {
            Optional<OurUsers> userOptional = usersRepo.findById(userId);
            if (userOptional.isPresent()) {
                usersRepo.deleteById(userId);
                reqRes.setStatusCode(200);
                reqRes.setMessage("User deleted successfully");
            } else {
                reqRes.setStatusCode(404);
                reqRes.setMessage("User not found for deletion");
            }
        } catch (Exception e) {
            reqRes.setStatusCode(500);
            reqRes.setMessage("Error occurred while deleting user: " + e.getMessage());
        }
        return reqRes;
    }

    public ReqRes updateUser(Integer userId, OurUsers updatedUser) {
        ReqRes reqRes = new ReqRes();
        try {
            Optional<OurUsers> userOptional = usersRepo.findById(userId);
            if (userOptional.isPresent()) {
                OurUsers existingUser = userOptional.get();
                existingUser.setEmail(updatedUser.getEmail());
                existingUser.setName(updatedUser.getName());
                existingUser.setCity(updatedUser.getCity());
                existingUser.setRole(updatedUser.getRole());
                existingUser.setImg(updatedUser.getImg());

                // Check if password is present in the request
                if (updatedUser.getPassword() != null && !updatedUser.getPassword().isEmpty()) {
                    // Encode the password and update it
                    existingUser.setPassword(passwordEncoder.encode(updatedUser.getPassword()));
                }

                OurUsers savedUser = usersRepo.save(existingUser);
                reqRes.setOurUsers(savedUser);
                reqRes.setStatusCode(200);
                reqRes.setMessage("User updated successfully");
            } else {
                reqRes.setStatusCode(404);
                reqRes.setMessage("User not found for update");
            }
        } catch (Exception e) {
            reqRes.setStatusCode(500);
            reqRes.setMessage("Error occurred while updating user: " + e.getMessage());
        }
        return reqRes;
    }


    public ReqRes getMyInfo(String email){
        ReqRes reqRes = new ReqRes();
        try {
            Optional<OurUsers> userOptional = usersRepo.findByEmail(email);
            if (userOptional.isPresent()) {
                reqRes.setOurUsers(userOptional.get());
                reqRes.setStatusCode(200);
                reqRes.setMessage("successful");
            } else {
                reqRes.setStatusCode(404);
                reqRes.setMessage("User not found for update");
            }

        }catch (Exception e){
            reqRes.setStatusCode(500);
            reqRes.setMessage("Error occurred while getting user info: " + e.getMessage());
        }
        return reqRes;

    }
}
