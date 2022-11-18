package com.booking.appbookings.security.controller;

import com.booking.appbookings.security.dto.JwtDTO;
import com.booking.appbookings.security.dto.LoginUser;
import com.booking.appbookings.security.dto.NewUser;
import com.booking.appbookings.security.entity.Role;
import com.booking.appbookings.security.entity.User;
import com.booking.appbookings.security.enums.RoleName;
import com.booking.appbookings.security.jwt.JwtProvider;
import com.booking.appbookings.security.service.RoleService;
import com.booking.appbookings.security.service.UserService;
import com.booking.appbookings.util.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.naming.Binding;
import javax.validation.Valid;
import java.util.HashSet;
import java.util.Set;

@RestController
@RequestMapping("/auth")
@CrossOrigin
public class AuthController {
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    UserService userService;
    @Autowired
    RoleService roleService;
    @Autowired
    JwtProvider jwtProvider;

    @PostMapping("/newUser")
    public ResponseEntity<?> newUser (@Valid @RequestBody NewUser newUser, BindingResult bindingResult) {
        if(bindingResult.hasErrors())
            return new ResponseEntity(new Message("Invalid Credentials"), HttpStatus.BAD_REQUEST);
        if(userService.existsByUsername(newUser.getUserName()))
            return new ResponseEntity(new Message("Username already exists"), HttpStatus.BAD_REQUEST);
        if(userService.existsByEmail(newUser.getEmail()))
            return new ResponseEntity(new Message("Email already exists"), HttpStatus.BAD_REQUEST);
        User user = new User(newUser.getName(), newUser.getUserName(), newUser.getEmail(), passwordEncoder.encode(newUser.getPassword()));
        Set<Role> roles = new HashSet<>();
        roles.add(roleService.findByRoleName(RoleName.ROLE_USER).get());
        if(newUser.getRoles().contains("admin"))
            roles.add(roleService.findByRoleName(RoleName.ROLE_ADMIN).get());
        user.setRoles(roles);
        userService.save(user);
        return new ResponseEntity(new Message("User created"), HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login (@Valid @RequestBody LoginUser loginUser, BindingResult bindingResult) {
        if(bindingResult.hasErrors())
            return new ResponseEntity(new Message("Invalid Credentials"), HttpStatus.BAD_REQUEST);
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginUser.getUserName(), loginUser.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtProvider.generateToken(authentication);
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        JwtDTO jwtDTO = new JwtDTO(jwt, userDetails.getUsername(), userDetails.getAuthorities());
        return new ResponseEntity(jwtDTO, HttpStatus.OK);
    }
}
