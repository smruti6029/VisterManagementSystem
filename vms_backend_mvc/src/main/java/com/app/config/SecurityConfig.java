package com.app.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.app.security.JwtAunthicationFilter;
import com.app.serviceImpl.CustumUserDetalisService;




@EnableWebSecurity(debug = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Autowired
	private CustumUserDetalisService custumUserDetalisService;
	
	@Autowired
	private JwtAunthicationFilter aunthicationFilter;
	
	
	 @Override
		protected void configure(HttpSecurity http) throws Exception {
			http.csrf()
			.disable()
			.cors()
			.disable()
			.authorizeRequests().antMatchers("/api/user/adduser1").permitAll()
		    .antMatchers("/").permitAll()
		    .anyRequest().permitAll()
			.and()
			.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
			http.addFilterBefore(aunthicationFilter,UsernamePasswordAuthenticationFilter.class);
		}
	
	
	 @Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(custumUserDetalisService);
	}
	 
	 @Bean	
	public PasswordEncoder  passwordEncoder()
	{
		 		 return new BCryptPasswordEncoder();
	}
	
	 
	 @Bean
	 public AuthenticationManager authenticationManagerBean() throws Exception
	 {
		 return super.authenticationManagerBean();
	 }
	

	
}
