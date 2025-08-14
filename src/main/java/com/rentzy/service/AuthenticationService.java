package com.rentzy.service;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import com.rentzy.controller.auth.dto.request.AuthenticationRequest;
import com.rentzy.controller.auth.dto.request.IntrospectRequest;
import com.rentzy.controller.auth.dto.request.RefreshRequest;
import com.rentzy.controller.auth.dto.response.AuthenticationResponse;
import com.rentzy.controller.auth.dto.response.IntrospectResponse;
import com.rentzy.entity.InvalidatedToken;
import com.rentzy.enums.UserRole;
import com.rentzy.entity.UserEntity;
import com.rentzy.controller.auth.dto.request.logoutRequest;
import com.rentzy.repository.InvalidatedTokenRepository;
import com.rentzy.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationService {
    static final Logger log = LoggerFactory.getLogger(AuthenticationService.class);
    UserRepository userRepository;
    PasswordEncoder passwordEncoder;
    InvalidatedTokenRepository invalidatedTokenRepository;

    @NonFinal
    @Value("${jwt.secret}")
    protected String SIGNER_KEY;

    @NonFinal
    @Value("${jwt.valid-duration}")
    protected long VALID_DURATION;

    @NonFinal
    @Value("${jwt.refreshable-duration}")
    protected long REFRESHABLE_DURATION;

    public AuthenticationResponse authenticate(AuthenticationRequest authenticationRequest) {
        var user = userRepository.findById(authenticationRequest.getUserId()).orElseThrow(() -> new UsernameNotFoundException(authenticationRequest.getUserId()));

        boolean authenticated = passwordEncoder.matches(authenticationRequest.getPassword(), user.getPassword());

        if (!authenticated) throw new UsernameNotFoundException(authenticationRequest.getUserId());

        var token  = generateToken(user);

        return AuthenticationResponse.builder()
                .token(token)
                .authenticated(true)
                .build();
    }

    public IntrospectResponse introspect(IntrospectRequest introspectRequest) throws ParseException, JOSEException {
        var token = introspectRequest.getToken();

        boolean isValid = true;

        try {
            verifyToken(token, false);
        }
        catch (Exception e) {
            isValid = false;
        }

        return IntrospectResponse.builder()
                .valid(isValid)
                .build();
    }

    public void logout(logoutRequest request) throws ParseException, JOSEException {
        var signedToken = verifyToken(request.getToken(), true);

        String Jit = signedToken.getJWTClaimsSet().getJWTID();
        Date exprireTime = signedToken.getJWTClaimsSet().getExpirationTime();

        InvalidatedToken invalidatedToken = new InvalidatedToken();
        invalidatedToken.setId(Jit);
        invalidatedToken.setExpiryTime(exprireTime);

        invalidatedTokenRepository.save(invalidatedToken);
    }

    public AuthenticationResponse refreshToken(RefreshRequest request) throws ParseException, JOSEException{
        var signedJWT = verifyToken(request.getToken(), true);

        var jit = signedJWT.getJWTClaimsSet().getJWTID();
        var expiryTime = signedJWT.getJWTClaimsSet().getExpirationTime();

        InvalidatedToken invalidatedToken  = new InvalidatedToken();
        invalidatedToken.setId(jit);
        invalidatedToken.setExpiryTime(expiryTime);

        invalidatedTokenRepository.save(invalidatedToken);

        var userId = signedJWT.getJWTClaimsSet().getSubject();

        var user = userRepository.findById(userId).orElseThrow(() -> new UsernameNotFoundException(userId));

        var token = generateToken(user);

        return AuthenticationResponse.builder().token(token).authenticated(true).build();
    }

    String generateToken(UserEntity user) {
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS512);

        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(user.getId())
                .issuer("rentzy.com")
                .issueTime(new Date())
                .expirationTime(new Date(
                        Instant.now().plus(VALID_DURATION, ChronoUnit.SECONDS).toEpochMilli()
                ))
                .jwtID(UUID.randomUUID().toString())
                .claim("scope", buildScope(user))
                .build();

        SignedJWT signedJWT  = new SignedJWT(header, jwtClaimsSet);

        try {
            MACSigner signer = new MACSigner(SIGNER_KEY.getBytes(StandardCharsets.UTF_8));
            signedJWT.sign(signer);
            return signedJWT.serialize();
        } catch (JOSEException e) {
            log.error("Cannot create token", e);
            throw new RuntimeException(e);
        }
    }

    private SignedJWT verifyToken(String token, boolean isRefresh) throws JOSEException, ParseException {
        SignedJWT signedJWT = SignedJWT.parse(token);

        if (!JWSAlgorithm.HS512.equals(signedJWT.getHeader().getAlgorithm())) {
            throw new JOSEException("Unexpected JWS algorithm");
        }

        Date expiredTime = (isRefresh) ?
        new Date(signedJWT
                .getJWTClaimsSet()
                .getIssueTime()
                .toInstant()
                .plus(REFRESHABLE_DURATION, ChronoUnit.SECONDS)
                .toEpochMilli())
        : signedJWT.getJWTClaimsSet().getExpirationTime();

        JWSVerifier verifier = new MACVerifier(SIGNER_KEY.getBytes());

        var verified = signedJWT.verify(verifier);

        if (!(verified && expiredTime.after(new Date()))) throw new ParseException("Expired JWT", 0);

        if (invalidatedTokenRepository.existsById(signedJWT.getJWTClaimsSet().getJWTID())) {
            throw new IllegalArgumentException("Invalid JWT");
        }

        return signedJWT;
    }

    private String buildScope(UserEntity user){
        UserRole role = user.getRole();
        return (role != null)
                ? "ROLE_" + role.name()
                : "";
    }
}
