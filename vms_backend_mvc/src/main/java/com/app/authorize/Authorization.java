package com.app.authorize;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import com.app.Dao.RoleDao;
import com.app.Dao.UserDao;
import com.app.dto.UserDto;
import com.app.entity.Role;
import com.app.entity.User;
import com.app.response.Response;
import com.app.security.JwtHelper;

@Component
public class Authorization {

	@Autowired
	private JwtHelper jwtHelper;

	@Autowired
	private UserDao userDao;

	@Autowired
	private RoleDao role;

	public User authorizegetallUserdetalis(String username) {

		String usernameFromToken = jwtHelper.getUsernameFromToken(username);
		User user = userDao.getUserbyPhone(usernameFromToken);
		if (user != null && user.getRole().getName().equals("SUPERADMIN") || user.getRole().getName().equals("ADMIN")) {
			return user;

		} else {
			return null;
		}

	}

	public Response<?> authorizetoAdduser(String username, UserDto userDto) {

//		if (userDto.getId() != null) {
//			return new Response<>("Valid User To Update Detlis", userDto, HttpStatus.OK.value());
//		}

		Role roleByid = role.getroleByid(userDto.getRole().getId());

		String usernameFromToken = jwtHelper.getUsernameFromToken(username);

		User user = userDao.getUserbyPhone(usernameFromToken);

		if (!user.getRole().getName().equalsIgnoreCase("SUPERADMIN")) {
			Long countActiveUsers = userDao.countActiveUsers(user.getCompany().getId());

			if (userDto.getId() == null && countActiveUsers > user.getCompany().getUserLimit().longValue()) {
				return new Response<>(" User Limit Exceed ", null, HttpStatus.BAD_REQUEST.value());
			}
		}

		if (user.getRole().getName().equals("EMPLOYEE") || user.getRole().getName().equals("HR")) {
			return new Response<>("Valid User", user, HttpStatus.OK.value());
		}

		if (userDto.getId() != null) {
			User getuserByid = userDao.getuserByid(userDto.getId());
			if (getuserByid.getRole().getName().equalsIgnoreCase("SUPERADMIN")) {
				return new Response<>("You Can't Edit Or Add  Super Admin", userDto, HttpStatus.BAD_REQUEST.value());
			}
		}

		if (user.getRole().getName().equalsIgnoreCase("SUPERADMIN")) {
			return new Response<>("Valid User", user, HttpStatus.OK.value());
		} else if (user.getRole().getName().equalsIgnoreCase("ADMIN")) {

			if (roleByid.getName().equalsIgnoreCase("SUPERADMIN")) {
				return new Response<>("You Can't add Super Admin", userDto, HttpStatus.BAD_REQUEST.value());
			} else {
				return new Response<>("Valid User", user, HttpStatus.OK.value());
			}
		} else if (user.getRole().getName().equals("RECEPTIONIST")) {

			if (roleByid.getName().equalsIgnoreCase("SUPERADMIN") || roleByid.getName().equalsIgnoreCase("ADMIN")) {
				return new Response<>("You Cant't Add It ", userDto, HttpStatus.BAD_REQUEST.value());
			} else {
				return new Response<>("Valid User", user, HttpStatus.OK.value());
			}
		}
		return new Response<>("You Have Not Permission To add it  ", userDto, HttpStatus.BAD_REQUEST.value());

	}

	public User authorizationForCompany(String token) {
		String usernameFromToken = jwtHelper.getUsernameFromToken(token);
		User user = userDao.getUserbyPhone(usernameFromToken);
		if (user != null && user.getRole().getName().equals("SUPERADMIN")) {
			return user;

		} else {
			return null;
		}
	}

	public Response<?> authorizetoAdduser(String substring) {
		String usernameFromToken = jwtHelper.getUsernameFromToken(substring);
		User user = userDao.getUserbyPhone(usernameFromToken);
		if (user != null) {
			return new Response<>("Vallid User", user, HttpStatus.OK.value());
		} else {
			{
				return new Response<>("UNAUTHORIZED", user, HttpStatus.UNAUTHORIZED.value());
			}
		}

	}

	public Boolean checkValidUser(String username) {
		String usernameFromToken = jwtHelper.getUsernameFromToken(username);
		User user = userDao.getUserbyPhone(usernameFromToken);
		if (user != null && user.getRole().getName().equals("ADMIN")) {
			return true;
		}

		return false;
	}

	public User authorizeByReceptionist(String userName) {

		String usernameFromToken = jwtHelper.getUsernameFromToken(userName);

		if (usernameFromToken == null) {
			return null;
		}

		User user = this.userDao.getUserbyPhone(usernameFromToken);

		if (user != null && user.getRole().getName().equals("RECEPTIONIST")) {
			return user;

		} else {
			return null;

		}
	}

	public User checkRole(String token) {
		String usernameFromToken = jwtHelper.getUsernameFromToken(token);
		User user = this.userDao.getUserbyPhone(usernameFromToken);

		if (user != null && user.getRole().getName().equals("ADMIN")) {
			return user;
		} else {
			return null;
		}

	}

}
