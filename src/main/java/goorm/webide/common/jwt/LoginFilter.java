package goorm.webide.common.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import goorm.webide.user.dto.UserDetailDto;
import goorm.webide.user.dto.UserDto;
import goorm.webide.user.dto.response.UserResponse;
import goorm.webide.user.util.enums.LoginResult;
import goorm.webide.user.util.exception.DuplicateException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import org.springframework.http.HttpStatus;
import org.springframework.lang.Nullable;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;


public class LoginFilter extends AbstractAuthenticationProcessingFilter {

    private final AuthenticationManager authenticationManager; // 검증담당 클래스
    private final JWTUtil jwtUtil;
    private static final AntPathRequestMatcher DEFAULT_ANT_PATH_REQUEST_MATCHER =
            new AntPathRequestMatcher("/user/login", "POST");
    private String usernameParameter = "username";
    private String passwordParameter = "password";


    public LoginFilter(AuthenticationManager authenticationManager, JWTUtil jwtUtil){
        super(DEFAULT_ANT_PATH_REQUEST_MATCHER, authenticationManager);
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
    }

    @Override
    public Authentication attemptAuthentication
            (HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        // 클라이언트 요청에서 username, password 추출
        String username = obtainUsername(request);
        String password = obtainPassword(request);

        System.out.println("password = " + password);
        System.out.println("username = " + username);

        UsernamePasswordAuthenticationToken authToken =
                new UsernamePasswordAuthenticationToken(username, password, null);

        return authenticationManager.authenticate(authToken);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
                                            Authentication authResult) throws IOException, ServletException {
        // JWT 반환코드
        UserDetailDto userDetailDto  = (UserDetailDto) authResult.getPrincipal();
        Long userNo = userDetailDto.getUserNo();
        String nickname = userDetailDto.getNickname();
        String email = userDetailDto.getEmail();

        String token = jwtUtil.createJwt(email, userNo, 30*60*1000L);

        response.addHeader("Authorization", "Bearer " + token);

        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        UserResponse userResponse = UserResponse.builder()
                .success(LoginResult.SUCCESS.getResult())
                .message(LoginResult.SUCCESS.getMessage())
                .user(new UserDto(userNo, email, nickname))
                .build();

        String json = new ObjectMapper().writeValueAsString(userResponse);
        response.getWriter().write(json);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                              AuthenticationException failed) throws IOException, ServletException {
        // 로그인 실패시 코드
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        UserResponse userResponse = UserResponse.builder()
                .success(LoginResult.FAIL.getResult())
                .message(LoginResult.FAIL.getMessage())
                .errors(Arrays.asList("유효하지 않은 사용자 또는 비밀번호입니다."))
                .build();

        String json = new ObjectMapper().writeValueAsString(userResponse);
        response.getWriter().write(json);

    }

    @Nullable
    protected String obtainPassword(HttpServletRequest request) {
        return request.getParameter(this.passwordParameter);
    }

    @Nullable
    protected String obtainUsername(HttpServletRequest request) {
        return request.getParameter(this.usernameParameter);
    }

}
