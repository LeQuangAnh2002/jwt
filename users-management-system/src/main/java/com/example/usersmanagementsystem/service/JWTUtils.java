package com.example.usersmanagementsystem.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.function.Function;

@Component
public class JWTUtils {
    private SecretKey Key;
    private static  final long EXPIRATION_TIME = 86400000; //24 hours

// phương thức khởi tạo JWTUtils dc sử dụng để khởi tạo một đối tượng JWTUtils và thiết lập giá trị cho Key.
// chuỗi bí mật được giải mã từ base64 bằng cách sử dụng Base64.getDecoder().decode(secreteString.getBytes(StandardCharsets.UTF_8)). Kết quả là một mảng byte (keyBytes) chứa dữ liệu đã giải mã.
// ối tượng Key được khởi tạo bằng cách sử dụng SecretKeySpec và truyền vào mảng byte (keyBytes) và thuật toán mã hóa (HmacSHA256). SecretKeySpec là một triển khai của SecretKey và được sử dụng để đại diện cho khóa bí mật trong mã hóa HMAC (Hash-based Message Authentication Code).
    public JWTUtils(){
        String secreteString = "843567893696976453275974432697R634976R738467TR678T34865R6834R8763T478378637664538745673865783678548735687R3";
        byte[] keyBytes = Base64.getDecoder().decode(secreteString.getBytes(StandardCharsets.UTF_8));
        this.Key = new SecretKeySpec(keyBytes,"HmacSHA256");
    }
//    generateToken nhận vào một UserDetails và tạo ra 1 token jwt mới. token này chứa tên người dung, thời gian
//    gian phát hành và thời gian hết hạn. Nó dc ký bằng Key để đảm bảo tính hợp lệ

    public String generateToken(UserDetails userDetails){
        return Jwts.builder().subject(userDetails.getUsername())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + 1000*60*2))
                .signWith(Key)
                .compact();
    }
//    generateRefreshToken: Tương tự như generateToken, nhưng cho phép chú thích (claims) tùy chỉnh được thêm vào token.
//    Điều này hữu ích khi bạn muốn lưu trữ thông tin bổ sung trong token.
    public String generateRefreshToken(HashMap<String,Object> claims,UserDetails userDetails){
        return Jwts.builder()
                .claims(claims)
                .subject(userDetails.getUsername())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(Key)
                .compact();
    }
// extractUsername: Trích xuất tên người dùng từ một token JWT.
    public String extractUsername(String token){
        return extractClaims(token,Claims::getSubject);
    }
//    extractClaims: Phương thức tổng quát để trích xuất thông tin từ token. Nó nhận vào 1 token và 1 đối tuượng
//    Function<Claims,T> claimsTFunction để chỉ định loại thông tin cần trích xuất( ví dụ : tên người dùng,thời gian hết hạn).
//    Sử dụng Jwts.parser().verifyWith(Key).build().parseSignedClaims(token).getPayload() để giải mã token và lấy thông tin từ payload.
    private <T> T extractClaims(String token, Function<Claims,T> claimsTFunction){
        return claimsTFunction.apply(Jwts.parser().verifyWith(Key).build().parseSignedClaims(token).getPayload());
    }
// Xác minh tính hợp lệ của token bằng cách so sánh tên người dùng trong token với tên người dùng từ đối tượng UserDetails.
// Nó cũng kiểm tra xem token có hết hạn hay không bằng phương thức isTokenExpired.
    public boolean isTokenValid(String token, UserDetails userDetails){
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpried(token));
    }
//    isTokenExpired: Kiểm tra xem token đã hết hạn hay chưa bằng cách so sánh thời gian hết hạn trong token với thời gian hiện tại.
    public boolean isTokenExpried(String token){
        return extractClaims(token,Claims::getExpiration).before(new Date());
    }
}
