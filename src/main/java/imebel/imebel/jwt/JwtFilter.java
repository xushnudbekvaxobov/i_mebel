package imebel.imebel.jwt;

import imebel.imebel.dto.request.JwtDto;
import imebel.imebel.exception.UnauthorizedException;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;
import java.util.List;

@Component
public class JwtFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final HandlerExceptionResolver handlerExceptionResolver;

    public JwtFilter(JwtService jwtService, HandlerExceptionResolver handlerExceptionResolver) {
        this.jwtService = jwtService;
        this.handlerExceptionResolver = handlerExceptionResolver;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String path = request.getRequestURI();

        if (path.equals("/auth/register") ||
                path.equals("/auth/login") ||
                path.startsWith("/auth/verify-email") ||
                path.startsWith("/auth/forgot-password") ||
                path.startsWith("/auth/reset-password") ||
        path.equals("/categories/all")) {
            filterChain.doFilter(request, response);
            return;
        }
            String header = request.getHeader("Authorization");
            if(header == null || !header.startsWith("Bearer ")){
                filterChain.doFilter(request, response);
                return;
            }
            final String token = header.substring(7).trim();
            try{
                JwtDto jwtDto = jwtService.decode(token);
                String email = jwtDto.getEmail();
                String role = jwtDto.getRole();
                if (SecurityContextHolder.getContext().getAuthentication() == null) {
                    UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(email, null, List.of(new SimpleGrantedAuthority(role)));
                    authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                }
                filterChain.doFilter(request, response);
            }catch (ExpiredJwtException e){
                handlerExceptionResolver.resolveException(request, response, null, new UnauthorizedException("Authentication token has expired"));
            }catch (JwtException e){
                handlerExceptionResolver.resolveException(request, response, null,  new UnauthorizedException("Authentication token is invalid"));
            }
    }
}











