package com.app.validation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import com.app.Dao.CrediantialDao;
import com.app.Dao.RoleDao;
import com.app.dto.ChangePasswordDto;
import com.app.dto.MeetingDto;
import com.app.dto.PaginationRequest;
import com.app.dto.RoleDTO;
import com.app.dto.UserDto;
import com.app.entity.CredentialMaster;
import com.app.entity.Role;
import com.app.entity.User;
import com.app.response.Response;



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

	public Response<?> checkUser(UserDto userDto, Response<?> authorizetoAdduser) {

		User data = (User) authorizetoAdduser.getData();
		
		String firstName = userDto.getFirstName().trim();

		String lastName = userDto.getLastName().trim();

		if (!data.getRole().getName().equals("SUPERADMIN")) {
			if (userDto.getDepartmentDto() == null || userDto.getDepartmentDto().getId() == null) {
				return new Response<>("Provied Department ", userDto, HttpStatus.BAD_REQUEST.value());
			}
		}

		if ((userDto.getFirstName() == null && userDto.getLastName() == null)
				&& (userDto.getFirstName().isEmpty() && userDto.getLastName().isEmpty())) {
			return new Response<>("Provide Name ", userDto, HttpStatus.BAD_REQUEST.value());
		}

	

		if (!(firstName.length() > 0)) {

			return new Response<>("First Name cannot be Empty or Contain only Spaces.", null,
					HttpStatus.BAD_REQUEST.value());

		}
		
		if (!data.getRole().getName().equals("SUPERADMIN")) {
			if (userDto.getDepartmentDto() == null || userDto.getDepartmentDto().getId() == null) {
				return new Response<>("Provied Department ", userDto, HttpStatus.BAD_REQUEST.value());
			}
			
			if (userDto.getEmail() == null || !userDto.getEmail().matches("^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$")) {
			    return new Response<>("Provide a Valid Email", userDto, HttpStatus.BAD_REQUEST.value());
			}

		}
		if (userDto.getEmail() == null || !userDto.getEmail().matches("^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$")) {
		    return new Response<>("Provide a Valid Email", userDto, HttpStatus.BAD_REQUEST.value());
		}

		if (!(lastName.length() > 0)) {
			return new Response<>("Last Name cannot be Empty or Contain only Spaces.", null,
					HttpStatus.BAD_REQUEST.value());

		}
		if (userDto.getPhone() == null || userDto.getPhone().trim().length() != 10) {
			return new Response<>("Provide Valid Phone Number ", userDto, HttpStatus.BAD_REQUEST.value());

		}
		if (userDto.getCompany().getId() == null) {
			return new Response<>("Provide Company ID  ", userDto, HttpStatus.BAD_REQUEST.value());
		}
		if (userDto.getGender() == null) {
			return new Response<>("Provide Gender   ", userDto, HttpStatus.BAD_REQUEST.value());
		}
		if (userDto.getIsPermission() == null) {
			return new Response<>("Provide Permission   ", userDto, HttpStatus.BAD_REQUEST.value());
		}

		if (userDto.getEmail() == null) {
			return new Response<>("Provide  gmail  ", userDto, HttpStatus.BAD_REQUEST.value());
		}
		if (userDto.getCity().getId() == null) {
			return new Response<>("Provide City Id  ", userDto, HttpStatus.BAD_REQUEST.value());
		}
		if (userDto.getDob() == null) {
			return new Response<>("Provide DOB  ", userDto, HttpStatus.BAD_REQUEST.value());

		}
		if (userDto.getEmpCode() == null || userDto.getEmpCode().isEmpty()) {
			return new Response<>("Provide Employee Code  ", userDto, HttpStatus.BAD_REQUEST.value());
		}
		if (userDto.getRole().getId() == null) {
			return new Response<>("Provide Role  ", userDto, HttpStatus.BAD_REQUEST.value());
		}
		{
			return new Response<>("Valid User", userDto, HttpStatus.OK.value());
		}
	}


	public Response<?> checkPassword(ChangePasswordDto passwordDTO) {

			
			if(passwordDTO.getOldpassword()==null)
			{
				return new Response<>("Provide Old Password", null, HttpStatus.BAD_REQUEST.value());
			}
		
			CredentialMaster obj = new CredentialMaster();
			CredentialMaster user = crediantialDao.getUsername(passwordDTO.getUsername());
			if (user == null) {
				return new Response<>("User Not Found", null, HttpStatus.BAD_REQUEST.value());
			}
			if(obj.passwordMatcher(passwordDTO.getNewPassword(), user.getPassword()))
			{
				return new Response<>("New  Password Can't be Same As Old Password", null, HttpStatus.BAD_REQUEST.value());
			}
	
			if (obj.passwordMatcher(passwordDTO.getOldpassword(), user.getPassword())) {	
				return new Response<>("Success", user, HttpStatus.OK.value());
			} else {
				return new Response<>("Password Mismatch", null, HttpStatus.BAD_REQUEST.value());
			}
	}
	
	public Response<?> checkMeeting(MeetingDto meetingDto) {
		try {
			if(meetingDto.getUser().getId() == null || meetingDto.getUser() == null) {
				return new Response<>("User / Employee is required", null, HttpStatus.BAD_REQUEST.value());
			}
//			if(meetingDto.getVisitor().getId() == null || meetingDto.getVisitor() == null) {
//				return new Response<>("Visitor is required", null, HttpStatus.BAD_REQUEST.value());
//			}
//			if(meetingDto.getRoom().getId() == null || meetingDto.getRoom() == null) {
//				return new Response<>("Room is required", null, HttpStatus.BAD_REQUEST.value());
//			}
			return new Response<>("Success", null, HttpStatus.OK.value());
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return new Response<>("Fail", e.getMessage(), HttpStatus.BAD_REQUEST.value());
		}
		
	}
	
	
	public Response<?> checkPageVallid(PaginationRequest request) {
		if(request.getSize()==null)
		{
			return new Response<>("Size is required", null, HttpStatus.BAD_REQUEST.value());
		}
		if(request.getPage()==null)
		{
			return new Response<>("Page is required", null, HttpStatus.BAD_REQUEST.value());
		}
		 return new Response<>("Vallid", request, HttpStatus.OK.value());
	}

	public Response<?> vallidOtp(ChangePasswordDto changePasswordDto) {

		
		if(changePasswordDto.getNewPassword()==null)
		{
			return new Response<>("Provide New Password First", null, HttpStatus.BAD_REQUEST.value());
		}
		
		if(changePasswordDto.getUsername()==null)
		{
			return new Response<>("Provide UserName First", null, HttpStatus.BAD_REQUEST.value());
		}
		if(changePasswordDto.getOtp()==null)
		{
			return new Response<>("Provide  Otp", null, HttpStatus.BAD_REQUEST.value());
		}
		return new Response<>("Success", null, HttpStatus.OK.value());
	}

}
