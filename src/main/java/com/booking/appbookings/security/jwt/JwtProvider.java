package com.booking.appbookings.security.jwt;

import com.booking.appbookings.security.entity.MainUser;
import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtProvider {
    private final static Logger logger = LoggerFactory.getLogger(JwtProvider.class);

    @Value("secret")
    private String secret;
    @Value("360000")
    private int expiration;

    public String generateToken(Authentication authentication){
        MainUser minUser = (MainUser) authentication.getPrincipal();
        return Jwts.builder()
                .setSubject(minUser.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(new Date(new Date().getTime() + expiration))
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }

    public String getUsernameFromToken(String token){
        return Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public boolean validateToken(String token){
        try{
            Jwts.parser().setSigningKey(secret).parseClaimsJws(token);
            return true;
        }catch (MalformedJwtException e){
            logger.error("Malformed token");
        } catch (UnsupportedJwtException e){
            logger.error("Unsupported token");
        } catch (ExpiredJwtException e){
            logger.error("Expired token");
        } catch (IllegalArgumentException e){
            logger.error("Empty token");
        } catch (SignatureException e){
            logger.error("Invalid signature");
        }
        return false;
    }
}
