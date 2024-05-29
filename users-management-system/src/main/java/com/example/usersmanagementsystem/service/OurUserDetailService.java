package com.example.usersmanagementsystem.service;

import com.example.usersmanagementsystem.repository.UsersRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class OurUserDetailService implements UserDetailsService {
    @Autowired
    private UsersRepo usersRepo;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return usersRepo.findByEmail(username).orElseThrow( () -> new UsernameNotFoundException("invalid user request !"));
    }
}
// interface UserDetailsService là một interface quan trọng trong Spring Security, dc sử dụng để cung cấp thông tin về người dùng (user) cho quá trình xác thực(authentication)
// khi một người dùng thực hiện đăng nhập, Spring Security sẽ gọi phương thức  loadUserByUsername() của UserDetailsService để lấy thông tin về người dùng đó.
// Phương thức loadUserByUsername() phải trả về một UserDetails object, chứa thông tin cần thiết để xác thực người dùng, như username, password, quyền, v.v.
// loadUserByUsername()  sử dụng UsersRepo (một repository) để tìm kiếm người dùng theo email.
//Nếu tìm thấy người dùng, phương thức sẽ trả về một UserDetails object chứa thông tin người dùng đó.
