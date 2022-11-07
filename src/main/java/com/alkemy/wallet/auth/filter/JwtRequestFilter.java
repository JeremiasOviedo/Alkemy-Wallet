package com.alkemy.wallet.auth.filter;


import com.alkemy.wallet.auth.service.JwtUtils;
import com.alkemy.wallet.auth.service.UserDetailsCustomService;
import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {

  @Autowired
  private UserDetailsCustomService userDetailsCustomService;
  @Autowired
  private JwtUtils jwtUtils;
  @Autowired
  private AuthenticationManager authenticationManager;


  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
      FilterChain filterChain)
      throws ServletException, IOException {

    final String authorizationHeader = request.getHeader("Authorization");

    String email = null;
    String jwt = null;

    if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
      jwt = authorizationHeader.substring(7);
      email = jwtUtils.extractUsername(jwt);
    }

    if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {

      UserDetails userDetails = this.userDetailsCustomService.loadUserByUsername(email);

      if (jwtUtils.validateToken(jwt, userDetails)) {
        UsernamePasswordAuthenticationToken authReq =
            new UsernamePasswordAuthenticationToken(userDetails, null,userDetails.getAuthorities());
        authReq.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authReq);
      }
    }
    filterChain.doFilter(request, response);
  }
}

