package com.platzi.market.web.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;


@Component
public class JWTUtil {
    private static final String KEY = "pl4tz1";

    // se le da una fecha de expiracion de 10 horas y se usa un modelo HS256 para firmar el documento.
    public String generateToken(UserDetails userDetails){
        return Jwts.builder().setSubject(userDetails.getUsername()).setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10))
                .signWith(SignatureAlgorithm.HS256, KEY).compact();
    }

    // metodo para validar el token y que se pueda consultar a los endpoints configurados con JWT
    public boolean validateToken(String token, UserDetails userDetails){
        return userDetails.getUsername().equals(extractUserName(token)) && !isTokenExpired(token);
    }

    public String extractUserName(String token){
        return getClaims(token).getSubject(); // subject es donde esta el usuario de la peticion
    }

    public boolean isTokenExpired(String token){
        return getClaims(token).getExpiration().before(new Date());
    }

    // Claims es un objeto de JWT
    private Claims getClaims(String token){
        return Jwts.parser().setSigningKey(KEY).parseClaimsJws(token).getBody();
    }
}
