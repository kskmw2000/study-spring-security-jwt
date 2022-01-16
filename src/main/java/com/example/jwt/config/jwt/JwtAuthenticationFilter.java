package com.example.jwt.config.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.example.jwt.config.auth.PrincipalDetails;
import com.example.jwt.model.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

// 스프링 시큐리티에서 UsernamePasswordAuthenticationFilter 가 존재함.
// /login 요청해서 username, password를 post로 전송하면
// UsernamePasswordAuthenticationFilter 동작을 함
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    // /login 요청을 하면 로그인 시도를 위해서 실행되는 함수
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        System.out.println("JwtAuthenticationFilter : attemptAuthentication 함수로 로그인 시도중...");

        // 1. username, password를 받아서
        /*
        try {
//            System.out.println("request.getInputStream().toString() = " + request.getInputStream().toString());

            BufferedReader br = request.getReader();
            String input = null;
            while ((input = br.readLine()) != null) {
                System.out.println("input = " + input);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("========================");
        */

        // Json 으로 로그인하기
        ObjectMapper om = new ObjectMapper();
        User user;
        try {
            user = om.readValue(request.getInputStream(), User.class);
            System.out.println("user.toString() = " + user.toString());

            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword());

            // PrincipalDetailsService.loadUserByUsername()가 호출된 후 정상이면 authentication이 리턴됨.
            // 데이터베이스에 존재하는 아이디와 비밀번호가 일치한다.
            // authentication 내 로그인한 정보가 존재함
            Authentication authentication =
                    authenticationManager.authenticate(authenticationToken);

            //  => 로그인이 되었다라는 의미임.
            PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();

            // 로그인이 정상적으로 되었다는 로그
            System.out.println("principalDetails = " + principalDetails.getUser().getUsername());

            // authentication 객체가 session 영역에 저장됨.
            // 그 이유는 권한 관리를 security 가 대신 해주기 떄문에 편하려고 하는 것임
            // 굳이 JWT 토큰을 사용하면서 세션을 만들 이유는 없으. 근데, 단지 권한 처리 떄문에 session에 넣어주는 것임.
            return authentication;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    // attemptAuthentication 실행후, 인증이 정상적으로 successfulAuthentication 함수가 실행됨
    // JWT 토큰을 만들어서 request를 요청한 사용자에게 JWT토큰을 response 하면 됨.
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        System.out.println("successfulAuthentication 함수가 실행됨 : 즉, 인증이 완료되었다라는 의미임. ");

        PrincipalDetails principalDetails = (PrincipalDetails) authResult.getPrincipal();

        // 토큰을 생성한다.
        // RSA 방식은 아닌 Hash 암호화 방식임.
        String jwtToken = JWT.create()
                .withSubject(principalDetails.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + (1000*60*10)))
                .withClaim("id", principalDetails.getUser().getId())
                .withClaim("username", principalDetails.getUser().getUsername())
                .sign(Algorithm.HMAC512("cos"));

        response.addHeader("Authorization", "Bearer " + jwtToken);
    }
}
