package com.vms2.authorize;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import com.vms2.dao.RoleDao;
import com.vms2.dao.UserDao;
import com.vms2.dto.UserDto;
import com.vms2.entity.Role;
import com.vms2.entity.User;
import com.vms2.response.Response;
import com.vms2.security.JwtUtil;

@Component
public class Authorization {

	
	@Autowired
	private JwtUtil jwtUtil;
	
	
	@Autowired
	private UserDao userDao;
	
	@Autowired
	private RoleDao role;
	
	
	public boolean authorizegetallUserdetalis(String username) {
		
		String usernameFromToken = jwtUtil.getUsernameFromToken(username);
		User user = userDao.getUserbyPhone(usernameFromToken);
		if(user!=null && user.getRole().getName().equals("SUPERADMIN") || user.getRole().getName().equals("ADMIN"))
		{
			return true;	
		}
		else
		{
			return false;
		}
		
		
	}


	public Response<?> authorizetoAdduser(String username,  UserDto userDto) {
		
		if(userDto.getId()!=null)
		{
			return new Response("Valid User To Update Detlis",userDto,HttpStatus.OK.value());
		}
		
		String usernameFromToken = jwtUtil.getUsernameFromToken(username);
		User user = userDao.getUserbyPhone(usernameFromToken);
		
		if(user.getRole().getName().equalsIgnoreCase("SUPERADMIN") )
		{
			return new Response("Valid User",userDto,HttpStatus.OK.value());
		}
		else if(user.getRole().getName().equalsIgnoreCase("ADMIN"))
		{
			Role roleByid = role.getroleByid(userDto.getRole().getId());
			if(roleByid.getName().equalsIgnoreCase("SUPERADMIN"))
			{
				return new Response("You Can't add Super Admin",userDto,HttpStatus.BAD_REQUEST.value());
			}
			else
			{
				return new Response("Valid User",userDto,HttpStatus.OK.value());
			}
		}
		else if(user.getRole().getName().equals("RECEPTIONIST"))
		{
			Role roleByid = role.getroleByid(userDto.getRole().getId());
			if(roleByid.getName().equalsIgnoreCase("SUPERADMIN") || roleByid.getName().equalsIgnoreCase("ADMIN") )
			{
				return new Response("You Cant't Add It ",userDto,HttpStatus.BAD_REQUEST.value());
			}
			else
			{
				return new Response("Valid User",userDto,HttpStatus.OK.value());
			}
		}
		 return new Response("You Have Not Permission To add it  ",userDto,HttpStatus.BAD_REQUEST.value());
		
		
		
		
		
		
		
		
		
		
		
		
		
	}

}
