package com.example.jwt.filter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class MyFilter3 implements Filter {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        // 토큰 : cos 이걸 만들어줘야 함. id,pw 정상적으로 들어와서 로그인이 완료되면 토큰을 만들어주고 그걸 응답해 준다.
        // 요청할 떄마다 header에 Authorization에 value값으로 토큰을 가지고 온다.
        // 그때 토큰이 넘어오면 이 토큰이 내가 만든 토큰이 맞는지만 검증만 하면 된다. (RSA, HS256)

        //
        request.setCharacterEncoding("UTF-8");

        // token : cos
        /*
        if (request.getMethod().equals("POST")) {
            System.out.println("POST 요청됨");
            String authorization = request.getHeader("Authorization");
            System.out.println("authorization = " + authorization);

            if (authorization.equals("cos")) {
                filterChain.doFilter(request, response);
            } else {
                PrintWriter printWriter = response.getWriter();
                printWriter.println("인증 안됨");
            }
        }
        */
    }
}
