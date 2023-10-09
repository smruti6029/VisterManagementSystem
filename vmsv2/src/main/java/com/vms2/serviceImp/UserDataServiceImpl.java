package com.vms2.serviceImp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.vms2.dao.CrediantialDao;
import com.vms2.entity.CredentialMaster;
import com.vms2.response.JwtRequest;
import com.vms2.response.JwtResponse;
import com.vms2.response.Response;
import com.vms2.security.JwtUtil;
import com.vms2.service.UserDataService;



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
	public Response<?> generateToken(JwtRequest jwtRequest) throws Exception {
		
		
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
				
				
				jwtResponse.setUser_id(user.getId());
				jwtResponse.setName(user.getUser().getFirstname()+ " "+user.getUser().getLastname());
				jwtResponse.setToken(token);
				jwtResponse.setRole_name(user.getUser().getRole().getName());
				jwtResponse.setCompany_id(user.getUser().getCompany().getId());
				
				return 	new Response<>("Success",jwtResponse,HttpStatus.OK.value());
			} else {
				new Response<>("User Not Found",null,HttpStatus.BAD_REQUEST.value());
			}

		} else {
			return new Response<>("User Not Found",null,HttpStatus.BAD_REQUEST.value());
		}
		return new Response<>("User Not Found",null,HttpStatus.BAD_REQUEST.value());
	}

	

}
