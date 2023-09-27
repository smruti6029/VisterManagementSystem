package com.vms2.config;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.vms2.security.JwtUtil;
import com.vms2.service.CustumUserDetalisService;


@Component
public class JwtAunthicationFilter extends OncePerRequestFilter {

	@Autowired
	private CustumUserDetalisService custumUserDetalisService;

	@Autowired
	private JwtUtil jwtUtil;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		if (request != null && request.getHeader("Authorization")!=null) {
			String header = request.getHeader("Authorization");

			String unsername = null;

			String Jwttoken = null;

			if (header != null && header.startsWith("Bearer ")) {
				Jwttoken = header.substring(7);
				try {

					unsername = this.jwtUtil.getUsernameFromToken(Jwttoken);

				} catch (Exception e) {

				}

				UserDetails userdetalis = this.custumUserDetalisService.loadUserByUsername(unsername);
				if (unsername != null && SecurityContextHolder.getContext().getAuthentication() == null) {
					UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
							userdetalis, null, userdetalis.getAuthorities());
					usernamePasswordAuthenticationToken
							.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
					SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
				} else {
					System.out.println("Token IS not VAlidited");
				}
			}
		}
		filterChain.doFilter(request, response);

	}

}
