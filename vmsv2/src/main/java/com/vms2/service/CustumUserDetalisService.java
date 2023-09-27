package com.vms2.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.vms2.dao.CrediantialDao;
import com.vms2.entity.CredentialMaster;


@Service
public class CustumUserDetalisService implements UserDetailsService {


	@Autowired
	private CrediantialDao dao;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
	    CredentialMaster credentialMaster = this.dao.getUsername(username);

	    if (credentialMaster != null) {
	        String roleName = credentialMaster.getUser().getRole().getName();
	        
	        List<GrantedAuthority> authorities = new ArrayList<>();
	        authorities.add(new SimpleGrantedAuthority("ROLE_" + roleName)); 
	        return new User(credentialMaster.getUsername(), credentialMaster.getPassword(), authorities);
	    } else {
	        throw new UsernameNotFoundException("User not found");
	    }
	}

	

}
