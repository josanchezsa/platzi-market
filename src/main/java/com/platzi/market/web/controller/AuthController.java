package com.platzi.market.web.controller;

import com.platzi.market.domain.dto.AuthenticationRequest;
import com.platzi.market.domain.dto.AuthenticationResponse;
import com.platzi.market.domain.service.PlatziUserDetailService;
import com.platzi.market.web.security.JWTUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    // este authenticationManager es propio de Spring
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private PlatziUserDetailService platziUserDetailService;

    @Autowired
    private JWTUtil jwtUtil;

    // metodo para responder con JWT cuando alguien trate de iniciar sesion
    // aca tambien se verifica que el user y pass sean correctos
    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> createToken(@RequestBody AuthenticationRequest request){
        try{
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(),
                    request.getPassword()));
            UserDetails userDetails = platziUserDetailService.loadUserByUsername(request.getUsername());
            String jwt = jwtUtil.generateToken(userDetails);
            return new ResponseEntity<>(new AuthenticationResponse(jwt), HttpStatus.OK);

        }catch (BadCredentialsException e){ // en caso de que no estén correctos el user y el pass
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }
}
