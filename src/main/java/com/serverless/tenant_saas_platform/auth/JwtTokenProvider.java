package com.serverless.tenant_saas_platform.auth;

import com.serverless.tenant_saas_platform.models.Role;
import com.serverless.tenant_saas_platform.models.RoleType;
import io.jsonwebtoken.*;
import org.springframework.stereotype.Component;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class JwtTokenProvider {

    private SecretKey secretKey;
    private long validityInMilliseconds = 360000; // 1 hour

    public JwtTokenProvider(){
        try{
            // Generate a new secretkey in HMSC
            KeyGenerator keyGenerator = KeyGenerator.getInstance("HmacSHA256");
            keyGenerator.init(256);
            this.secretKey = keyGenerator.generateKey();
        } catch(Exception e){
            throw new RuntimeException("Error generating secret key", e);
        }
    }

    // Generate a JWT token for the user
    public String createToken(String username, Set<RoleType> roles){
        Claims claims = Jwts.claims().setSubject(username);
        claims.put("roles", roles.stream()
                .map(Enum::name)
                .collect(Collectors.toSet()));

        Date now = new Date();
        Date validity = new Date(now.getTime() + validityInMilliseconds);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
    }

    //Validity the JWT Token
    public boolean  validateToken(String token){
        try {
            Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token);
            return true;
        }
        catch (JwtException | IllegalArgumentException e){
            return false;
        }
    }

    public String getUsernameFromToken(String token){
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claims.getSubject();
    }

    public Set<String> extractRoles(String token){
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token).getBody();
        Object rolesClaim = claims.get("roles");
        if(rolesClaim instanceof Collection<?>){
            //Convert Collection<?> to Set<String> safely
            Collection<?> rolesCollection = (Collection<?>) rolesClaim;
            return rolesCollection.stream()
                    .filter(Objects::nonNull)
                    .map(Object::toString)
                    .collect(Collectors.toSet());
        }

        return Collections.emptySet();
    }
}
