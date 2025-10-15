package com.url.shortner.security;
import com.url.shortner.service.UserDetailsImpl;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Date;
import java.util.stream.Collectors;
@Component
public class JwtUtils {
    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.expiry}")
    private String jwtExpiration;

    public String getJwtFromHeader(HttpServletRequest request){
        String bearerToken = request.getHeader("Authorization");
        if(bearerToken != null && bearerToken.startsWith("Bearer ")){
            return bearerToken.substring(7);
        }
        return null;
    }
    public String generateToken(UserDetailsImpl userDetails){
            String username = userDetails.getUsername();
            long uid = userDetails.getUid();
            String roles = userDetails.getAuthorities().stream()
                    .map(authority->authority.getAuthority())
                    .collect(Collectors.joining(","));
            return Jwts.builder()
                    .subject(username)
                    .claim("uid" , uid)
                    .claim("roles",roles)
                    .issuedAt(new Date())
                    .expiration(new Date(new Date().getTime() + 172800000))
                    .signWith(key())
                    .compact();
    }

    public String getUserNameFromJwt(String token){
        return Jwts.parser()
                .verifyWith((SecretKey) key())
                .build().parseSignedClaims(token)
                .getPayload().getSubject();
    }
    private Key key(){
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
    }

    public Boolean validateToken(String authToken){
        try {
            Jwts.parser().verifyWith((SecretKey) key())
                    .build().parseSignedClaims(authToken);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            throw new RuntimeException(e);
        }
    }
}
