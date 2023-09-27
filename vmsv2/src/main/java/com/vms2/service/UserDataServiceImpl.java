package com.vms2.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.vms2.dao.CrediantialDao;
import com.vms2.dao.UserDao;
import com.vms2.entity.CredentialMaster;
import com.vms2.entity.User;
import com.vms2.response.JwtRequest;
import com.vms2.response.JwtResponse;
import com.vms2.security.JwtUtil;



@Service
public class UserDataServiceImpl implements UserDataService {

	@Autowired
	private CustumUserDetalisService custumUserDetalisService;

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private JwtUtil jwtUtil;

	@Autowired
	private CrediantialDao userDao;

	@Override
	public JwtResponse generateToken(JwtRequest jwtRequest) throws Exception {
		
		
		System.out.println("Inside Generate Tokan");
		
		
		 CredentialMaster user = userDao.getUsername(jwtRequest.getUsername());

		 CredentialMaster userentity = new CredentialMaster();
		
		

//		System.out.println(userEntity.getUsername_gmail());

		if (user != null) {

			if (userentity.passwordMatcher(jwtRequest.getPassword(), user.getPassword())) {

				JwtResponse jwtResponse = new JwtResponse();
				UserDetails userDetalis = this.custumUserDetalisService.loadUserByUsername(jwtRequest.getUsername());
				String token = this.jwtUtil.generateToken(userDetalis);
				System.out.println("tokan -" + token);
				String name=this.jwtUtil.getUsernameFromToken(token);
				
				
				jwtResponse.setId(user.getId());
				jwtResponse.setName(name);
				jwtResponse.setToken(token);
				jwtResponse.setRole(user.getUser().getRole().getName());
				return jwtResponse;
			} else {
				throw new Exception("User Not Found!!!");
			}

		} else {
			throw new Exception("User Not Found!!!");
		}
	}

	

}
