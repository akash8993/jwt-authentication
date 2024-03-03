package com.jwt.jwt.authentication.configuration;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;

import static com.jwt.jwt.authentication.utils.JwtUtils.getUserNameFromToken;
import static com.jwt.jwt.authentication.utils.JwtUtils.validateToken;

@Component
@RequiredArgsConstructor
public class JWTAuthenticationFilter extends OncePerRequestFilter {
    private final UserDetailsService userDetailsService;


    // We will write code for authenticating JWT

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        //Fetch Token from request
        var jwtTokenOptional= getTokenFromRequest(request);

        //Validate JWT Token and we will use JWT Utils

        jwtTokenOptional.ifPresent(jwtToken-> {
            if(validateToken(jwtToken))
            {
                //Get Username from token
                var username= getUserNameFromToken(jwtToken);

                //Fetch User details with help of username

               var userDetails= userDetailsService.loadUserByUsername(username.get());

               //Create Authentication Token
                var authenticationToken=
                        new UsernamePasswordAuthenticationToken(userDetails,null, userDetails.getAuthorities());

                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

              //Set Authentication Token to security Context
                SecurityContextHolder.getContext()
                        .setAuthentication(authenticationToken);
            }
        });

        //Pass request and response to next filter
        filterChain.doFilter(request,response);

    }

    private Optional<String> getTokenFromRequest(HttpServletRequest request) {
        //Extract Authentication Header
       var authHeader= request.getHeader(HttpHeaders.AUTHORIZATION);

       //Token starts with Bearer so remove that
        if(StringUtils.hasText(authHeader) && authHeader.startsWith("Bearer"))
        {
            return Optional.of(authHeader.substring(7));
        }
        return Optional.empty();

    }
}
