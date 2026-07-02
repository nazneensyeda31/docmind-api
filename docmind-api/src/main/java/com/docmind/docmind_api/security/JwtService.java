package com.docmind.docmind_api.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import javax.crypto.SecretKey;
import java.util.Date;

@Service
public class JwtService {
    @Value("${jwt.secret}")
    private String jwtKey;
    @Value("${jwt.expiration}")
    private Long jwtExpiration;


    private SecretKey getSigningKey(){
        byte[] secretKey = Decoders.BASE64.decode(jwtKey);
        return Keys.hmacShaKeyFor(secretKey);
    }

    public String generateToken(String email){
        return Jwts.builder()
                .subject(email)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis()+jwtExpiration))
                .signWith(getSigningKey())
                .compact();

    }
    public String extractEmail(String token){
        return Jwts.parser()
                .verifyWith(getSigningKey()).build().parseSignedClaims(token).
                getPayload().getSubject();
    }

    public Date expirationDate(String token){
        return Jwts.parser()
                .verifyWith(getSigningKey()).build().parseSignedClaims(token)
                .getPayload().getExpiration();
    }
    public boolean isTokenValid(String token, UserDetails userDetails){
        final String email = extractEmail(token);

        return (email.equals(userDetails.getUsername()) && expirationDate(token).after(new Date(System.currentTimeMillis())) );
    }


}
