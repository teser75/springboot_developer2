package me.shinssunyoung.springbootdeveloper.config.jwt;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Comment;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties("jwt")
public class JwtProperties {
    private String secretKey;
    private String issuer;

}
