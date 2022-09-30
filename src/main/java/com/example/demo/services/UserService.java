package com.example.demo.services;

import com.example.demo.converters.UserConverter;
import com.example.demo.dto.LoginRequestDTO;
import com.example.demo.dto.LoginResponseDTO;
import com.example.demo.entities.User;
import com.example.demo.exceptions.GeneralServiceException;
import com.example.demo.exceptions.NoDataFoundException;
import com.example.demo.exceptions.ValidateServiceException;
import com.example.demo.repository.UserRepository;
import com.example.demo.validators.UserValidator;
import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Slf4j
@Service
public class UserService {
    @Value("${jwt.password}")
    private String jwtSecret;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserConverter userConverter;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Transactional
    public User createUser(User user) {
        try {
            UserValidator.validate(user);
            User existUser=userRepository.findByUsername(user.getUsername())
                    .orElse(null);
            if(existUser!=null)
                throw new ValidateServiceException("El nombre usuario ya existe");

            String encoder=passwordEncoder.encode(user.getPassword());
            user.setPassword(encoder);

            return userRepository.save(user);
        } catch (ValidateServiceException | NoDataFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new GeneralServiceException(e.getMessage(), e);
        }
    }

    public List<User> findAll(){
        try {
            List<User> users = userRepository.findAll();
            return users;
        } catch (ValidateServiceException | NoDataFoundException e) {
            log.info(e.getMessage(), e);
            throw e;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new GeneralServiceException(e.getMessage(), e);
        }
    }

    public LoginResponseDTO login(LoginRequestDTO request){
        try {
            User user=userRepository.findByUsername(request.getUsername())
                    .orElseThrow(()->new ValidateServiceException("Usuario o password incorrecto"));

            if(!passwordEncoder.matches(request.getPassword(),user.getPassword()))
                throw new ValidateServiceException("Usuario o password incorrectos");

            String token =createToken(user);

            return new LoginResponseDTO(userConverter.fromEntity(user),token);

        } catch (ValidateServiceException | NoDataFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new GeneralServiceException(e.getMessage(), e);
        }
    }

    public String createToken(User user){
        Date now =new Date();
        Date expiryDate=new Date(now.getTime()+ (1000*60*60));

        return Jwts.builder()
                .setSubject(user.getUsername())
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS512,jwtSecret).compact();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token);
            return true;
        }catch (UnsupportedJwtException e) {
            log.error("JWT in a particular format/configuration that does not match the format expected");
        }catch (MalformedJwtException e) {
            log.error(" JWT was not correctly constructed and should be rejected");
        }catch (SignatureException e) {
            log.error("Signature or verifying an existing signature of a JWT failed");
        }catch (ExpiredJwtException e) {
            log.error("JWT was accepted after it expired and must be rejected");
        }
        return false;
    }

    public String getUsernameFromToken(String jwt) {
        try {
            return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(jwt).getBody().getSubject();
        } catch (Exception e) {
            throw new ValidateServiceException("Invalid Token");
        }

    }
}
