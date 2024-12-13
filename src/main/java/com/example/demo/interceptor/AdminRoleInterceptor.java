package com.example.demo.interceptor;


import com.example.demo.constants.GlobalConstants;
import com.example.demo.dto.Authentication;
import com.example.demo.entity.Role;
import com.example.demo.exception.UnauthorizedException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class AdminRoleInterceptor implements HandlerInterceptor {


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handle)
            throws UnauthorizedException{

        // 세션 가져오기
        HttpSession session = request.getSession(false);
        if(session == null){
            throw new UnauthorizedException(HttpStatus.UNAUTHORIZED, "세션이 끊어졌습니다.");
        }

        // 세션에서 사용자 인증 정보 가져오기
        Authentication authentication = (Authentication) session.getAttribute(GlobalConstants.USER_AUTH);
        if(authentication == null) {
            throw new UnauthorizedException(HttpStatus.UNAUTHORIZED, "로그인이 필요합니다.");
        }

        // 사용자 권한 확인
        Role role = authentication.getRole();
        if(role == null) {
            throw new UnauthorizedException(HttpStatus.UNAUTHORIZED, "ADMIN 권한이 필요합니다.");
        }

        // ADMIN 권한 확인 성공
        return true;

    }
}