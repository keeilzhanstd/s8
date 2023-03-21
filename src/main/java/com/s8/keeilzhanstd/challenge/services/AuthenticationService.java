package com.s8.keeilzhanstd.challenge.services;

import com.s8.keeilzhanstd.challenge.config.JwTokenService;
import com.s8.keeilzhanstd.challenge.exceptions.UserAlreadyExistAuthenticationException;
import com.s8.keeilzhanstd.challenge.models.auth.AuthenticationRequest;
import com.s8.keeilzhanstd.challenge.models.auth.AuthenticationResponse;
import com.s8.keeilzhanstd.challenge.models.auth.RegisterRequest;
import com.s8.keeilzhanstd.challenge.models.user.Role;
import com.s8.keeilzhanstd.challenge.models.user.User;
import com.s8.keeilzhanstd.challenge.models.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JwTokenService jwTokenService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationResponse register(RegisterRequest request) {

        // Check if user already exists
        if(repository.findByUsername(request.getUsername()).isPresent()){

            //Throw custom exception
            throw new UserAlreadyExistAuthenticationException("User with username: " + request.getUsername() + " already exists");
        }

        // Create new user
        var user = User.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
                .build();

        // Save user
        repository.save(user);

        // Generate token
        var jwtToken = jwTokenService.generateToken(user);

        // Return token
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        // Authenticate user
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );

        // Get user
        var user = repository.findByUsername(request.getUsername())
                .orElseThrow();

        // Generate token
        var jwtToken = jwTokenService.generateToken(user);

        // Return token
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }
}
