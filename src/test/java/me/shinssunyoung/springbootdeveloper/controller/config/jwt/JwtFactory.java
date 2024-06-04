package me.shinssunyoung.springbootdeveloper.controller.config.jwt;

import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.Builder;
import lombok.Getter;
import me.shinssunyoung.springbootdeveloper.config.jwt.JwtProperties;

import java.time.Duration;
import java.util.Date;
import java.util.Map;

import static java.util.Collections.emptyMap;

@Getter
public class JwtFactory {
    private String subject = "test@email.com";
    private Date issuedAt = new Date();
    private Date expireation = new Date( new Date().getTime() + Duration.ofDays(14).toMillis()  );

    private Map<String, Object> claims = emptyMap();

    @Builder
    public JwtFactory(String subject, Date issuedAt, Date expireation, Map<String, Object> claims) {
        this.subject = subject != null ? subject : this.subject;
        this.issuedAt = issuedAt != null ? issuedAt : this.issuedAt;
        this.expireation = expireation != null ? expireation : this.expireation;
        this.claims = claims != null ? claims : this.claims;
    }


    public static JwtFactory withDefaultValues() {
        return JwtFactory.builder().build();
    }

    public String createToken( JwtProperties jwtProperites ) {
        return Jwts.builder()
                .setSubject(subject)
                .setHeaderParam(Header.TYPE , Header.JWT_TYPE)
                .setIssuer(jwtProperites.getIssuer())
                .setIssuedAt(issuedAt)
                .setExpiration(expireation)
                .addClaims(claims)
                .signWith(SignatureAlgorithm.HS256 , jwtProperites.getSecretKey())
                .compact();

    }




}
