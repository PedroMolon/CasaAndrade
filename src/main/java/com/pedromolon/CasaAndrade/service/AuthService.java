package com.pedromolon.CasaAndrade.service;

import com.pedromolon.CasaAndrade.dto.request.LoginRequest;
import com.pedromolon.CasaAndrade.dto.request.RegisterRequest;
import com.pedromolon.CasaAndrade.dto.response.LoginResponse;
import com.pedromolon.CasaAndrade.dto.response.RegisterResponse;
import com.pedromolon.CasaAndrade.exception.ResourceNotFoundException;
import com.pedromolon.CasaAndrade.model.Role;
import com.pedromolon.CasaAndrade.model.User;
import com.pedromolon.CasaAndrade.repository.RoleRepository;
import com.pedromolon.CasaAndrade.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashSet;
import java.util.Set;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;

    public AuthService(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder, TokenService tokenService) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenService = tokenService;
    }

    @Transactional
    public RegisterResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.email())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User email is already registered");
        }

        User user = new User();
        user.setName(request.name());
        user.setEmail(request.email());
        user.setPassword(passwordEncoder.encode(request.password()));

        Role role = roleRepository.findByName("ROLE_SELLER")
                .orElseThrow(() -> new ResourceNotFoundException("Role not found with this name"));

        Set<Role> roles = new HashSet<>();
        roles.add(role);
        user.setRoles(roles);

        User savedUser = userRepository.save(user);
        return new RegisterResponse(savedUser.getId(), savedUser.getName(), savedUser.getEmail(), "User saved successfully");
    }

    @Transactional
    public LoginResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User email or password is invalid"));

        if (!passwordEncoder.matches(request.password(), user.getPassword())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User email or password is invalid");
        }

        String token = tokenService.generateToken(user);
        return new LoginResponse(token, 3600L);
    }

}
