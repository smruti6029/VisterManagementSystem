package com.vms2.config;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vms2.response.Response;
import com.vms2.security.JwtUtil;
import com.vms2.serviceImp.CustumUserDetalisService;


@Component
public class JwtAunthicationFilter extends OncePerRequestFilter {

	@Autowired
	private CustumUserDetalisService custumUserDetalisService;

	@Autowired
    private JwtUtil jwtUtil;
    
    @Autowired
    private Jackson2ObjectMapperBuilder objectMapperBuilder;
    


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        if (request != null && request.getHeader("Authorization") != null) {
            String header = request.getHeader("Authorization");

            String username = null;
            String jwtToken = null;

            if (header != null && header.startsWith("Bearer ")) {
                jwtToken = header.substring(7);
                try {
                    username = this.jwtUtil.getUsernameFromToken(jwtToken);
                    
                } catch (Exception e) {
                	
                	Response<?> customResponse = new Response<>("Invalid Token",null , HttpStatus.FORBIDDEN.value());

                       sendJsonResponse(response, customResponse, HttpStatus.FORBIDDEN);

                       return;         
                      
                }
            }

            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                try {
                    UserDetails userDetails = this.custumUserDetalisService.loadUserByUsername(username);
                    UsernamePasswordAuthenticationToken authToken =
                            new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                } catch (UsernameNotFoundException e) {
                    // Handle user not found
              	  Response<?> customResponse = new Response<>("User Not Found",null, HttpStatus.UNAUTHORIZED.value());

                  sendJsonResponse(response, customResponse, HttpStatus.UNAUTHORIZED);
                  
                 return;
                }
            }
        }

        filterChain.doFilter(request, response);
    }
    
    private void sendJsonResponse(HttpServletResponse response, Response<?> customResponse, HttpStatus status) throws IOException {
        ObjectMapper objectMapper = objectMapperBuilder.build();
        response.setStatus(status.value());
        response.setContentType("application/json");
        response.getWriter().write(objectMapper.writeValueAsString(customResponse));
        response.getWriter().flush();
    }
    

}
