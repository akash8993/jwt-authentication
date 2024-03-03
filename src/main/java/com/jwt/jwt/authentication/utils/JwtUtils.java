package com.jwt.jwt.authentication.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.apache.commons.lang3.time.DateUtils;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

public class JwtUtils {

    private JwtUtils()
    {}

    // IS private key se hum apne JWT token to sign krenge and usse verify krenge
    private static final SecretKey secretKey= Jwts.SIG.HS256.key().build();


    private static final String ISSUER = "akash_gupta";
    public static boolean validateToken(String jwtToken) {
        return parseToken(jwtToken).isPresent();
    }

    private static Optional<Claims> parseToken(String jwtToken) {
        var jwtParser= Jwts.parser()
                .verifyWith(secretKey)
                .build();

        //Here with below we get all the details from username
        return Optional.of(jwtParser.parseSignedClaims(jwtToken).getPayload());
    }

    public static Optional<String> getUserNameFromToken(String jwtToken) {
       var claimsOptional= parseToken(jwtToken);
      if(claimsOptional.isPresent()) {

          //claims mein jo subject hai vo apna username hai
          return Optional.of(claimsOptional.get().getSubject());
      }
      return Optional.empty();
    }


    ///To Generate new token
    public static String generateToken(String username) {

        var currentDate= new Date();
        var expireDate=DateUtils.addMinutes(currentDate, 10);
        return Jwts.builder().id(UUID.randomUUID().toString())
                .issuer(ISSUER)
                .subject(username)
                .signWith(secretKey)
                .issuedAt(currentDate)
                .expiration(expireDate)
                .compact();

    }
}
