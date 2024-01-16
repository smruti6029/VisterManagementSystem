package com.app.security;

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

import com.app.dto.CustomResponseDTO;
import com.app.serviceImpl.CustumUserDetalisService;
import com.fasterxml.jackson.databind.ObjectMapper;






@Component
public class JwtAunthicationFilter extends OncePerRequestFilter {

    @Autowired
    private CustumUserDetalisService custumUserDetalisService;

    @Autowired
    private JwtHelper jwtUtil;
    
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
                	
                	   CustomResponseDTO customResponse = new CustomResponseDTO(null, "Invalid Token", HttpStatus.FORBIDDEN.value());

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
                	
                	  CustomResponseDTO customResponse = new CustomResponseDTO(null, "User Not Found", HttpStatus.UNAUTHORIZED.value());

                      sendJsonResponse(response, customResponse, HttpStatus.UNAUTHORIZED);
                      
                     return;
                }
            }
        }

        filterChain.doFilter(request, response);
    }
    
    private void sendJsonResponse(HttpServletResponse response, CustomResponseDTO customResponse, HttpStatus status) throws IOException {
        ObjectMapper objectMapper = objectMapperBuilder.build();
        response.setStatus(status.value());
        response.setContentType("application/json");
        response.getWriter().write(objectMapper.writeValueAsString(customResponse));
        response.getWriter().flush();
    }
    

	
	
//	   @Autowired
//	    private CustumUserDetalisService customUserDetailsService;
//
//	    @Autowired
//	    private JwtHelper jwtUtil;
//
//	    @Autowired
//	    private Jackson2ObjectMapperBuilder objectMapperBuilder;
//
//	    // Map to store the latest token's "iat" for each user
//	    private Map<String, Long> latestTokenIatMap = new HashMap<>();
//
//	    @Override
//	    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
//	            throws ServletException, IOException {
//
//	        String jwtToken = extractJwtToken(request);
//
//	        if (jwtToken != null) {
//	            try {
//	                String username = jwtUtil.getUsernameFromToken(jwtToken);
//
//	                if (username != null) {
//
//	                    long iatClaim = jwtUtil.getIatClaim(jwtToken);
//
//	                    // Retrieve the latest token's "iat" from the map
//	                    long latestTokenIat = latestTokenIatMap.getOrDefault(username, 0L);
//
//	                    if (iatClaim < latestTokenIat) {
//	                        CustomResponseDTO customResponse = new CustomResponseDTO(null, "Invalid Token",
//	                                HttpStatus.FORBIDDEN.value());
//	                        sendJsonResponse(response, customResponse, HttpStatus.FORBIDDEN);
//	                        return;
//	                    } else {
//	                        // Update the latest token's "iat" for this user
//	                        latestTokenIatMap.put(username, iatClaim);
//	                    }
//	                }
//
//	            } catch (Exception e) {
//	                CustomResponseDTO customResponse = new CustomResponseDTO(null, "Invalid Token",
//	                        HttpStatus.FORBIDDEN.value());
//	                sendJsonResponse(response, customResponse, HttpStatus.FORBIDDEN);
//	                return;
//	            }
//	        }
//
//	        filterChain.doFilter(request, response);
//	    }
//
//	    private String extractJwtToken(HttpServletRequest request) {
//	        String header = request.getHeader("Authorization");
//
//	        if (header != null && header.startsWith("Bearer ")) {
//	            return header.substring(7);
//	        }
//
//	        return null;
//	    }
//
//	    private void sendJsonResponse(HttpServletResponse response, CustomResponseDTO customResponse, HttpStatus status)
//	            throws IOException {
//	        ObjectMapper objectMapper = objectMapperBuilder.build();
//	        response.setStatus(status.value());
//	        response.setContentType("application/json");
//	        response.getWriter().write(objectMapper.writeValueAsString(customResponse));
//	        response.getWriter().flush();
//	    }
//	
//	
//	
	
	
	
}
	
	
	
	


