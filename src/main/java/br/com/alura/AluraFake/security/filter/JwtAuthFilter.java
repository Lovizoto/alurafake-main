package br.com.alura.AluraFake.security.filter;

import br.com.alura.AluraFake.exception.InvalidJwtAuthenticationException;
import br.com.alura.AluraFake.security.service.JwtTokenService;
import br.com.alura.AluraFake.security.service.UserDetailServiceImpl;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;

//Padrão de Projeto pronto que utilizo de outros códigos meus

@Component
public class JwtAuthFilter  extends OncePerRequestFilter {

    private final JwtTokenService jwtTokenService;
    private final UserDetailServiceImpl userDetailService;
    private final HandlerExceptionResolver handlerExceptionResolver;

    public JwtAuthFilter(JwtTokenService jwtTokenService,
                         UserDetailServiceImpl userDetailService,
                         @Qualifier("handlerExceptionResolver")  HandlerExceptionResolver handlerExceptionResolver) {
        this.jwtTokenService = jwtTokenService;
        this.userDetailService = userDetailService;
        this.handlerExceptionResolver = handlerExceptionResolver;
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {

        final String authorizationHeader = request.getHeader("Authorization");

        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            final String jwtToken = authorizationHeader.substring(7);
            final String userEmail = jwtTokenService.extractUsername(jwtToken); //a exceção pode ocorrer aqui

            if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = userDetailService.loadUserByUsername(userEmail);
                if(jwtTokenService.validateToken(jwtToken, userDetails)) {
                    UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.getAuthorities());
                    authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                }
            }
            filterChain.doFilter(request, response);
        } catch (JwtException | IllegalArgumentException e) {
            handlerExceptionResolver.resolveException(request, response, null, new InvalidJwtAuthenticationException("Invalid or expired JWT token."));
        }
    }
}
