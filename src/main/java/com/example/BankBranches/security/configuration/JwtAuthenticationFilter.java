package com.example.BankBranches.security.configuration;
import com.example.BankBranches.dto.GenericResponse;
import com.example.BankBranches.entity.AppUser;
import com.example.BankBranches.repository.UserRepo;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;

public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private JwtTokenUtils jwtTokenUtil;

    private UserRepo userRepo;

    JwtAuthenticationFilter(JwtTokenUtils jwtTokenUtil, UserRepo usersRepo) {
        this.jwtTokenUtil = jwtTokenUtil;
        this.userRepo = usersRepo;
    }
    private static Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        try {
            //String authToken = extractAuthToken(request.getHeader("Authorization"));
            String authToken = (request.getHeader("Authorization"));
            String username = jwtTokenUtil.parseToken(authToken);
            AppUser users = userRepo.findByEmail(username);
            if (users != null) {
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(users, null, Arrays.asList(
                        new SimpleGrantedAuthority(users.getRole().name())));
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                logger.info("authenticated user " + username + ", setting security context");
                SecurityContextHolder.getContext().setAuthentication(authentication);
                filterChain.doFilter(request, response);
            } else {
                generateUnauthorisedAccess(response);
            }
        } catch (Exception e) {
            System.out.println(e);
            generateUnauthorisedAccess(response);
        }
    }
    private String extractAuthToken(String authorizationHeader) {
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer")) {
            return authorizationHeader.substring(7); // Extract token without "Bearer " prefix
        }
        return null; // Token not found or header value format is incorrect
    }
    public void generateUnauthorisedAccess(HttpServletResponse res) throws IOException {
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        GenericResponse resp = new GenericResponse(HttpStatus.UNAUTHORIZED.value(), "UNAUTHORISED");
        String jsonRespString = ow.writeValueAsString(resp);
        res.setContentType(MediaType.APPLICATION_JSON_VALUE);
        PrintWriter writer = res.getWriter();
        writer.write(jsonRespString);
        System.out.println("===============================");
    }
}
