package test.ey.user.security.jwt;

import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import test.ey.user.exception.UserException;
import test.ey.user.security.CustomerDetailsService;
import test.ey.user.security.UserRequestInfo;

import javax.annotation.Resource;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private CustomerDetailsService customerDetailsService;

    @Resource(name = "requestScopedBean")
    private UserRequestInfo userRequestInfo;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if(request.getServletPath().matches("/user/login|/user/create|/swagger-ui/*|/api-docs/*|/h2-console/*")) {
            filterChain.doFilter(request, response);
        } else {
            String authorizationHeader = request.getHeader("Authorization");
            String token = null;

            if(authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
                token = authorizationHeader.substring(7);
                userRequestInfo.setSubject(jwtUtil.extractSubject(token));
                userRequestInfo.setClaims(jwtUtil.extractAllClaims(token));
                userRequestInfo.setIssuedAt(jwtUtil.extractIssuedAt(token));
                userRequestInfo.setToken(token);
            }

            if(userRequestInfo.getSubject() != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = customerDetailsService.loadUserByUsername(userRequestInfo.getSubject());
                if(validateId(request) && jwtUtil.validateToken(token, userDetails)) {
                    UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                            new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    new WebAuthenticationDetailsSource().buildDetails(request);
                    SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
                }
            }

            filterChain.doFilter(request, response);
        }
    }

    public Boolean validateId(HttpServletRequest request){
        if(request.getServletPath().matches("/user/\\d+.*")){
            String[] urlArray = request.getServletPath().split("/");
            if(Long.parseLong(urlArray[2]) != userRequestInfo.getUser().getUsrId().longValue()){
                return false;
            }
        }
        return true;
    }

    public Boolean isAdmin() {
        return "admin".equalsIgnoreCase((String) userRequestInfo.getClaims().get("role"));
    }

    public Boolean isUser() {
        return "user".equalsIgnoreCase((String) userRequestInfo.getClaims().get("role"));
    }

    public String getCurrentUser() {
        return userRequestInfo.getSubject();
    }

}
