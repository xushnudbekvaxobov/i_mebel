package imebel.imebel.jwt;

import imebel.imebel.dto.request.JwtDto;
import imebel.imebel.entity.UserEntity;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;


@Service
public class JwtService {
    private final String secretKey = "mySecretKeymySecretKeymySecretKeymySecretKey";
    private final long expirationTime = 86400000; // 1 day in milliseconds

    public String generateToken(UserEntity userEntity){
        return Jwts.builder()
                .subject(userEntity.getEmail())
                .claim("roles", "ROLE_" + userEntity.getRole().name())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(getSignKey())
                .compact();
    }

    public JwtDto decode(String token){
        Claims claims = Jwts.parser()
                .verifyWith(getSignKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
        String email = claims.getSubject();
        String roles = claims.get("roles", String.class);
        return new JwtDto(email, roles);
    }

    private SecretKey getSignKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
