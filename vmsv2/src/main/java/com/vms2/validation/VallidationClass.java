package com.vms2.validation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import com.vms2.dao.CrediantialDao;
import com.vms2.dao.RoleDao;
import com.vms2.dto.ForgotPasswordDTO;
import com.vms2.dto.RoleDTO;
import com.vms2.dto.UserDto;
import com.vms2.entity.CredentialMaster;
import com.vms2.entity.Role;
import com.vms2.response.Response;

@Component
public class VallidationClass {

	@Autowired
	private RoleDao roleDao;

	@Autowired
	private CrediantialDao crediantialDao;

	public Response<?> checkroleVallidOrnot(RoleDTO roleDTO) {
		if (roleDTO.getName() != null) {
			Role role = roleDao.getRole(roleDTO.getName());
			if (role == null) {
				return new Response<>("Vallid Role", roleDTO, HttpStatus.OK.value());
			} else {
				return new Response<>("Role Already Added", roleDTO, HttpStatus.BAD_REQUEST.value());
			}
		} else {
			return new Response<>("Add Role ", roleDTO, HttpStatus.BAD_REQUEST.value());
		}
	}

	public Response<?> checkUser(UserDto userDto) {
		if (userDto.getFirstname() == null && userDto.getLastname()==null) {
			return new Response<>("Provied Name ", userDto, HttpStatus.BAD_REQUEST.value());
		}  if (userDto.getPhone() == null || userDto.getPhone().trim().length() != 10) {
			return new Response<>("Provied Vallid Phone Number ", userDto, HttpStatus.BAD_REQUEST.value());

		}  if (userDto.getCompany().getId() == null) {
			return new Response<>("Provied Company ID  ", userDto, HttpStatus.BAD_REQUEST.value());
		} if (userDto.getEmail() == null) {
			return new Response<>("Provied  gmail  ", userDto, HttpStatus.BAD_REQUEST.value());
		} if (userDto.getCity().getId() == null) {
			return new Response<>("Provied City Id  ", userDto, HttpStatus.BAD_REQUEST.value());
		} if (userDto.getDob() == null) {
			return new Response<>("Provied DOB  ", userDto, HttpStatus.BAD_REQUEST.value());

		}  if (userDto.getRole().getId() == null) {
			return new Response<>("Provied Role  ", userDto, HttpStatus.BAD_REQUEST.value());
		} 
		 if (userDto.getPinCode() == null || userDto.getPinCode().length()!=6) {
				return new Response<>("Inalid Pin  ", userDto, HttpStatus.BAD_REQUEST.value());
			} 
		 if (userDto.getAddress() == null) {
				return new Response<>("Provied Address ", userDto, HttpStatus.BAD_REQUEST.value());
		 }
		 if (userDto.getEmpCode() == null) {
				return new Response<>("Provied Emp Code ", userDto, HttpStatus.BAD_REQUEST.value());
		 }
		 
		 
			return new Response<>("Vallid User", userDto, HttpStatus.OK.value());
		
	}

	public Response<?> checkPassword(ForgotPasswordDTO passwordDTO) {

		if (passwordDTO.getNewPassword().equals(passwordDTO.getNewPassword1())) {
			CredentialMaster user = crediantialDao.getUsername(passwordDTO.getUsername());
			if (user == null) {
				return new Response<>("User Not Found", "Try Again", HttpStatus.BAD_REQUEST.value());
			}
			CredentialMaster obj = new CredentialMaster();
			if (obj.passwordMatcher(passwordDTO.getOldpassword(), user.getPassword())) {
				if (obj.passwordMatcher(passwordDTO.getNewPassword1(), user.getPassword())) {
					return new Response<>("New Password Can't Be Same", "try again", HttpStatus.BAD_REQUEST.value());
				}
				return new Response<>("Success", user, HttpStatus.OK.value());
			} else {
				return new Response<>("Password Mismatch", "Try Again", HttpStatus.OK.value());
			}

		} else {
			return new Response<>("New Password Mismatch", "Try Again", HttpStatus.BAD_REQUEST.value());
		}
	}

}
