package com.app.serviceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.app.Dao.CrediantialDao;
import com.app.Dao.UserDao;
import com.app.entity.CredentialMaster;
import com.app.entity.User;
import com.app.response.JwtRequest;
import com.app.response.JwtResponse;
import com.app.security.JwtHelper;
import com.app.service.UserDataService;




@Service
public class UserDataServiceImpl implements UserDataService {

		@Autowired
		private CustumUserDetalisService custumUserDetalisService;

		@Autowired
		private AuthenticationManager authenticationManager;

		@Autowired
		private JwtHelper jwtUtil;
		
		@Autowired
		private UserDao userD;

		@Autowired
		private CrediantialDao userDao;

		@Override
		public JwtResponse generateToken(JwtRequest jwtRequest) throws Exception {
			
			
			System.out.println("Inside Generate Tokan");
			
			
			 CredentialMaster user = userDao.getUsername(jwtRequest.getUsername());

			 CredentialMaster userentity = new CredentialMaster();
			
			


			if (user != null) {

				if (userentity.passwordMatcher(jwtRequest.getPassword(), user.getPassword())) {

					JwtResponse jwtResponse = new JwtResponse();
					UserDetails userDetalis = this.custumUserDetalisService.loadUserByUsername(jwtRequest.getUsername());
					String token = this.jwtUtil.generateToken(userDetalis);
					System.out.println("tokan -" + token);
					String name=this.jwtUtil.getUsernameFromToken(token);
					User userbyPhone = userD.getUserbyPhone(name);
					
					jwtResponse.setId(userbyPhone.getId());
					
					jwtResponse.setName(userbyPhone.getFirstname() +" " +userbyPhone.getLastname());
					jwtResponse.setToken(token);
					jwtResponse.setRole(userbyPhone.getRole().getName());
					jwtResponse.setCompany_id(userbyPhone.getCompany().getId());
					jwtResponse.setUsername(name);
					jwtResponse.setCompany_name(userbyPhone.getCompany().getName());
					
//					System.out.println(userbyPhone.getCompany().getUserLimit()+  "User add limit ");
					
					if(userbyPhone.getRole().getName().equals("ADMIN"))
					{
						if(userbyPhone.getCompany().getUserLimit()!=null)
							
						jwtResponse.setLimit(userbyPhone.getCompany().getUserLimit());
						userbyPhone.getCompany().getBuilding().getId();
						
						jwtResponse.setBuildingId(userbyPhone.getCompany().getBuilding().getBuildingId());
					}
					
					if(userbyPhone.getRole().getName().equals("RECEPTIONIST"))
					{
						if(userbyPhone.getCompany().getBuilding().getBuildingId()!=null)
							
						jwtResponse.setBuildingId(userbyPhone.getCompany().getBuilding().getBuildingId());
						
					
					}
					
					
					
					return jwtResponse;
				} else {
					throw new Exception("User Not Found!!!");
				}

			} else {
				throw new Exception("User Not Found!!!");
			}
		}

		

	

}
