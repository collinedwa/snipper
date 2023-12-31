package io.snipper.snippet.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.snipper.snippet.model.User;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.security.oauth2.resource.OAuth2ResourceServerProperties;
import org.springframework.stereotype.Service;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class AuthorizationService {
    private final Map<String, String> tokens;
    private final EncryptionService encryptionService;
    private final byte[] authKey;

    @Autowired
    public AuthorizationService(final EncryptionService encryptionService) throws Exception {
        this.encryptionService = encryptionService;
        this.tokens = new HashMap<>();
        this.authKey = encryptionService.encryptAndEncodeBase64("secret-key", encryptionService.secretKey);
    }

    public void generateToken(final User user) {
        final Instant timestamp = Instant.now();
        final Instant expiration = timestamp.plusSeconds(86400);
        final String jwtToken = Jwts.builder()
                .setSubject(user.getEmail())
                .signWith(SignatureAlgorithm.HS256, authKey)
                .setExpiration(Date.from(expiration))
                .compact();
        tokens.put(user.getEmail(), jwtToken);
    }

    public boolean validateToken(final User user) {
        final String jwtToken = tokens.get(user.getEmail());
        if (jwtToken != null) {
            final Claims claims = Jwts.parser().setSigningKey(authKey).parseClaimsJws(jwtToken).getBody();
            return claims.getExpiration().after(Date.from(Instant.now()));
        }
        return false;
    }
 }
