package me.shinssunyoung.springbootdeveloper.controller.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import me.shinssunyoung.springbootdeveloper.config.jwt.JwtProperties;
import me.shinssunyoung.springbootdeveloper.controller.config.jwt.JwtFactory;
import me.shinssunyoung.springbootdeveloper.domain.RefreshToken;
import me.shinssunyoung.springbootdeveloper.domain.User;
import me.shinssunyoung.springbootdeveloper.dto.CreateAccessTokenRequest;
import me.shinssunyoung.springbootdeveloper.repository.RefreshTokenRepository;
import me.shinssunyoung.springbootdeveloper.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MockMvcBuilder;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc

public class TokenApiConrollerTest {
    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    @Autowired
    protected WebApplicationContext webApplicationContext;

    @Autowired
    JwtProperties jwtProperties;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RefreshTokenRepository refreshTokenRepository;

    @BeforeEach
    public void mockMvcSetup(){
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        userRepository.deleteAll();
    }


    @DisplayName("createNewAccessToek: 새로운 액세스 토큰을 발급한다.")
    @Test
    public void createNewAccessToken() throws Exception {

        //given
        final String url = "/api/token";

        // testUser 새로운사용자를 등록한다.
        User testUser = userRepository.save( User.builder().email("user@gmail.com").password("test").build());

        //리프레쉬 토큰 내용을 생성한다.
        String refreshToken = JwtFactory.builder().claims(Map.of("id", testUser.getId())).build().createToken(jwtProperties);

        //리프레쉬 토큰 테이블에 해당 내용을 생성하여 저장한다.
        refreshTokenRepository.save(new RefreshToken(testUser.getId(), refreshToken));

        CreateAccessTokenRequest request = new CreateAccessTokenRequest();
        request.setRefreshToken(  refreshToken);

        final String requestBody = objectMapper.writeValueAsString(request);

        //when

        // when
        ResultActions resultActions = mockMvc.perform(post(url)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(requestBody));

        //then

        resultActions.andExpect(status().isCreated())
                .andExpect(jsonPath("$.accessToken").isNotEmpty());


    }



}
