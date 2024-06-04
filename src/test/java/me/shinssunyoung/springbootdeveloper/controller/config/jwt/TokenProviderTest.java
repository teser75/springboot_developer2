package me.shinssunyoung.springbootdeveloper.controller.config.jwt;

import io.jsonwebtoken.Jwts;
import me.shinssunyoung.springbootdeveloper.config.jwt.JwtProperties;
import me.shinssunyoung.springbootdeveloper.config.jwt.TokenProvider;
import me.shinssunyoung.springbootdeveloper.domain.User;
import me.shinssunyoung.springbootdeveloper.repository.UserRepository;
import me.shinssunyoung.springbootdeveloper.service.UserDetailService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.Duration;
import java.util.Date;
import java.util.Map;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
public class TokenProviderTest {

    @Autowired
    private TokenProvider tokenProvider;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtProperties jwtProperties;

    //GenerateToken
    @DisplayName("generateToken(): 유저 정보와 만료 기간을 전달해 토큰을 만들 수 있다.")
    @Test
    void generateToken(){
        //Given
        User testuser = userRepository.save(User.builder()
                .email("user@gmail.com")
                .password("test")
                .build());


        //When
        String token = tokenProvider.generateToken(testuser , Duration.ofDays(14));

        //then
        Long userId = Jwts.parser()
                .setSigningKey( jwtProperties.getSecretKey())
                .parseClaimsJws(token)
                .getBody()
                .get("id", Long.class);


        assertThat(userId).isEqualTo(testuser.getId());

    }


    @DisplayName("validToken(): 만료된 토큰인 때에 유효성 검증에 실패한다.")
    @Test
    void validToken_invalidToken(){

        //given
        String token = JwtFactory.builder()
                .expireation( new Date( new Date().getTime() - Duration.ofDays(7).toMillis() ))
                .build()
                .createToken(jwtProperties);

        //When
        boolean result = tokenProvider.validToken(token);

        //then
        assertThat(result).isFalse();

    }

    @DisplayName("validToken() : 유효한 토큰일 때에 유효성 검증에 성공한다.")
    @Test
    void validToken_validToken(){

        //given
        String token = JwtFactory.withDefaultValues().createToken(jwtProperties);

        //when
        boolean result = tokenProvider.validToken(token);

        //then
        assertThat(result).isTrue();
    }

    @DisplayName("getAuthentication(): 토큰 기반으로 인증 정보를 가져올 수 있다.")
    @Test
    void getAuthentication(){

        //given
        String userEmail = "user@gmail.com";
        String token = JwtFactory.builder()
                .subject(userEmail)
                .build()
                .createToken(jwtProperties);


        //When
        Authentication authentication = tokenProvider.getAuthentication(token);

        //then
        assertThat( ((UserDetails) authentication.getPrincipal() ).getUsername() ).isEqualTo(userEmail);

    }


    @DisplayName("getUserId(): 토큰으로 유저 ID를 가져울 수 있다.")
    @Test
    void getUserId(){

        //given
        Long userId = 1l;
        String token = JwtFactory.builder()
                .claims(Map.of("id", userId))
                .build()
                .createToken(jwtProperties);

        //when
        Long userIdByToken = tokenProvider.getUserId(token);


        //Then
        assertThat(userIdByToken).isEqualTo(userId);


    }









}
