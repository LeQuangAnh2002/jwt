package com.example.usersmanagementsystem.config;

import com.example.usersmanagementsystem.service.JWTUtils;
import com.example.usersmanagementsystem.service.OurUserDetailService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.security.Security;

@Component
public class JWTAuthFilter extends OncePerRequestFilter {
    @Autowired
    private JWTUtils jwtUtils;
    @Autowired
    OurUserDetailService ourUserDetailService;
//1.Trước tiên, bộ lọc kiểm tra xem tiêu đề "Authorization" có tồn tại và không rỗng hay không. Nếu không tồn tại hoặc rỗng, bộ lọc chuyển tiếp yêu cầu cho bộ lọc tiếp theo trong chuỗi bộ lọc (nếu có) bằng filterChain.doFilter(request, response) và kết thúc phương thức.
//2.Nếu tiêu đề "Authorization" tồn tại và không rỗng, bộ lọc trích xuất phần JWT từ tiêu đề bằng cách loại bỏ tiền tố "Bearer" (7 ký tự đầu tiên). Phần JWT còn lại được lưu trong biến jwtToken.
// 3.Sau đó, bộ lọc sử dụng jwtUtils để trích xuất tên người dùng từ jwtToken bằng cách gọi jwtUtils.extractUsername(jwtToken). Kết quả được lưu trong biến userEmail.
// 4.Nếu userEmail không null và chưa có thông tin xác thực người dùng trong SecurityContextHolder, bộ lọc gọi ourUserDetailService.loadUserByUsername(userEmail) để tải thông tin chi tiết người dùng từ tên người dùng. Sau đó, bộ lọc sử dụng jwtUtils.isTokenValid(jwtToken,userDetails) để kiểm tra tính hợp lệ của token JWT.
// 5.Nếu token JWT hợp lệ, bộ lọc tạo một SecurityContext mới và tạo một UsernamePasswordAuthenticationToken với thông tin người dùng,
// không có mật khẩu và các quyền đã xác định. Thông tin xác thực yêu cầu (request) được thêm vào token bằng token.setDetails(new WebAuthenticationDetailsSource().buildDetails(request)).
// Cuối cùng, bộ lọc đặt Authentication vào SecurityContext bằng securityContext.setAuthentication(token) và SecurityContextHolder được cập nhật với securityContext.
// 6.Cuối cùng, bộ lọc chuyển tiếp yêu cầu cho bộ lọc tiếp theo trong chuỗi bộ lọc (nếu có) bằng filterChain.doFilter(request, response).
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        final String authenHeader = request.getHeader("Authorization");
        final String jwtToken;
        final String userEmail;
        if (authenHeader == null || authenHeader.isBlank()){
            filterChain.doFilter(request,response);
            return;
        }

        jwtToken = authenHeader.substring(7);
        userEmail = jwtUtils.extractUsername(jwtToken);

        if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null){
            UserDetails userDetails = ourUserDetailService.loadUserByUsername(userEmail);

            if (jwtUtils.isTokenValid(jwtToken,userDetails)){
                SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
                UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());

                token.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                securityContext.setAuthentication(token);
                SecurityContextHolder.setContext(securityContext);
            }
        }
        filterChain.doFilter(request,response);
    }
}
