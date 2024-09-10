package com.example.moneytransferapi.securityconfig;

import com.example.moneytransferapi.exception.CustomAuthenticationException;
import com.example.moneytransferapi.service.RedisService;
import com.example.moneytransferapi.service.SessionService;
import com.example.moneytransferapi.utilitys.JwtUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.security.sasl.AuthenticationException;
import java.io.IOException;
import java.util.function.Function;

@Component
@AllArgsConstructor
public class JwtAuthFIlter extends OncePerRequestFilter {
    private final JwtUtil jwt;
    private final UserDetailsService userDetailsService;
    private final RedisService redisService;
    private final SessionService sessionService;
    private static final String TOKEN_PREFIX = "b:";
  ///    | | | | |
//    @Override
//    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
//
//        if (SecurityContextHolder.getContext().getAuthentication()!=null){
//            filterChain.doFilter(request,response);
//            return;
//        }
//
//        String authToken = request.getHeader("Authorization");
//        if ( authToken==null || !authToken.startsWith("Bearer ") ){
//            filterChain.doFilter(request,response);
//            return;
//        }
//        authToken = authToken.substring(7);
//        if (!jwt.isValid(authToken) || redisService.keyExists(authToken) ){
//            filterChain.doFilter(request,response);
//            return;
//        }
//        if (!sessionService.stillActive(authToken)) {
//            throw new AuthenticationException("Session has expired or is inactive.");
//        }
//
//        Function<Claims,String> extractEmail = Claims::getSubject;
//        String usermail = jwt.extractClaim(authToken,extractEmail);
//
//        UserDetails currentUser = userDetailsService.loadUserByUsername(usermail);
//        UsernamePasswordAuthenticationToken authenticationToken =
//                new UsernamePasswordAuthenticationToken(currentUser, null, currentUser.getAuthorities());
//        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
//
//        filterChain.doFilter(request, response);
//
//
//    }
  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

      if (SecurityContextHolder.getContext().getAuthentication() != null) {
          filterChain.doFilter(request, response);
          return;
      }

      String authToken = request.getHeader("Authorization");
      if (authToken == null || !authToken.startsWith("Bearer ")) {
          filterChain.doFilter(request, response);
          return;
      }

      authToken = authToken.substring(7);

      try {
          if (!jwt.isValid(authToken) || redisService.keyExists(authToken)) {
              throw new CustomAuthenticationException("Invalid or blacklisted token.");
          }
          if (!sessionService.stillActive(authToken)) {
              throw new CustomAuthenticationException("Session has expired or is inactive.");
          }

          Function<Claims, String> extractEmail = Claims::getSubject;
          String usermail = jwt.extractClaim(authToken, extractEmail);

          UserDetails currentUser = userDetailsService.loadUserByUsername(usermail);
          UsernamePasswordAuthenticationToken authenticationToken =
                  new UsernamePasswordAuthenticationToken(currentUser, null, currentUser.getAuthorities());
          SecurityContextHolder.getContext().setAuthentication(authenticationToken);

          filterChain.doFilter(request, response);

      } catch (CustomAuthenticationException e) {
          SecurityContextHolder.clearContext();
          response.setStatus(HttpStatus.UNAUTHORIZED.value());
          response.setContentType(MediaType.APPLICATION_JSON_VALUE);
          response.getWriter().write("{\"error\": \"" + e.getMessage() + "\"}");
      }
  }

}
