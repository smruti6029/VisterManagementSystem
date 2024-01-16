package com.app.serviceImpl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.app.Dao.CrediantialDao;
import com.app.entity.CredentialMaster;
import com.app.exception.UserNotFoundException;


@Service
public class CustumUserDetalisService implements UserDetailsService {


	@Autowired
	private CrediantialDao dao;


	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
	    CredentialMaster credentialMaster = this.dao.getUsername(username);

	    if (credentialMaster != null  && credentialMaster.getUser().getCompany().isActive()==true) {
	        String roleName = credentialMaster.getUser().getRole().getName();
	        
	        List<GrantedAuthority> authorities = new ArrayList<>();
	        authorities.add(new SimpleGrantedAuthority("ROLE_" + roleName)); 
	        return new User(credentialMaster.getUsername(), credentialMaster.getPassword(), authorities);
	    } else {
	        throw new UserNotFoundException("User not found");
	 
	    }
	}
	}