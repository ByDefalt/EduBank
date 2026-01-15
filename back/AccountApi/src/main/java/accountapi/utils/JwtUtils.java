package accountapi.utils;

import io.jsonwebtoken.Jwts;
import javax.crypto.SecretKey;
import java.util.Date;

public class JwtUtils {
    private final SecretKey secretKey = Jwts.SIG.HS256.key().build();

    public String generateKey(String id, String role) {
        return Jwts.builder()
                .subject(id)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 60 * 60 * 1000))
                .claims()
                .add("id", id)
                .add("role", role)
                .and()
                .signWith(secretKey)
                .compact();
    }

    public String validateToken(String jwt) {
        try {
            var claims = Jwts.parser()
                    .verifyWith(secretKey)
                    .build()
                    .parseSignedClaims(jwt)
                    .getPayload();
            return claims.getSubject();
        }catch (Exception e){
            System.out.println("Invalid JWT: " + e.getMessage());
            return null;
        }
    }
}
