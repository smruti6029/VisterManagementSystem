package com.vms2.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.vms2.dao.CrediantialDao;
import com.vms2.dao.RoleDao;
import com.vms2.dao.UserDao;
import com.vms2.dto.ForgotPasswordDTO;
import com.vms2.dto.UserDto;
import com.vms2.entity.City;
import com.vms2.entity.Company;
import com.vms2.entity.CredentialMaster;
import com.vms2.entity.Role;
import com.vms2.entity.State;
import com.vms2.entity.User;
import com.vms2.mailservice.EmailSendService;
import com.vms2.response.Response;

@Service
public class UserServiceImp implements UserService {

	@Autowired
	private UserDao userDao;

	@Autowired
	private CredentialService credentialService;

	@Autowired
	private RoleDao roleDao;
	
	@Autowired
	private EmailSendService emailSendService;
	
	@Autowired
	private CrediantialDao crediantialDao;

	@Override
	public Response<?> saveUser(UserDto userDto) {

		// save New User
		if (userDto.getId() == null) {
			User vallidUser = userDao.getUserbyPhone(userDto.getPhone().trim());
			Integer saveUser;
			if (vallidUser == null) {

				User user = User.fromDTO(userDto);
				Role getroleByid = roleDao.getroleByid(userDto.getRole().getId());
				user.setCreated_by(getroleByid.getName());
				user.setIsActive(true);
				user.setUpdatedBY(new Date());
				user.setCreatedOn(new Date());
				saveUser = userDao.saveUser(user);

			}

			else {
				return new Response("Mobile Number Already Exit", userDto, HttpStatus.BAD_REQUEST.value());
			}

			return credentialService.adduserInCrediantial(userDto);

		}
		// update User
		else

		{
			 User user = userDao.getuserByid(userDto.getId());
			 
			 if(!user.getPhone().equals(userDto.getPhone()))
			 {
				 return new Response("Username Can't  be Changed", userDto, HttpStatus.BAD_REQUEST.value());
			 }

			

			if (userDto.getCity() != null) {
				City city = new City();
				city.setId(userDto.getCity().getId());
				user.setCity(city);
			}
			if (userDto.getCompany() != null) {
				Company company = new Company();
				company.setId(userDto.getCompany().getId());
				user.setCompany(company);
			}
			if (userDto.getDob() != null) {
				user.setDob(userDto.getDob());
			}
			if (userDto.getEmail() != null) {
				user.setEmail(userDto.getEmail());
			}
			if (userDto.getIsActive() != null) {
				user.setIsActive(userDto.getIsActive());
			}
			if (userDto.getState() != null) {
				State state = new State();
				state.setId(userDto.getState().getId());
				user.setState(state);
			}
			if (userDto.getPinCode() != null) {
				user.setPinCode(userDto.getPinCode());
			}
			if (userDto.getName() != null) {
				user.setName(userDto.getName());
			}
//			if (userDto.getPhone() != null) {
//				user.setPhone(userDto.getPhone());
//			}
			if (userDto.getRole() != null) {
				Role role = new Role();
				role.setId(userDto.getRole().getId());
				user.setRole(role);
			}
			user.setUpdatedBY(new Date());
			Integer updateUser = userDao.updateUser(user);
			return new Response("Updated Succesfully", userDto, HttpStatus.OK.value());
		}
	}

	@Override
	public List<UserDto> getallUser() {

		List<User> getallUser = userDao.getallUser();
		List<UserDto> allUser = new ArrayList<>();
		getallUser.forEach(x -> {
			UserDto obj = new UserDto();
			UserDto convertUserToDTO = obj.convertUserToDTO(x);
			allUser.add(convertUserToDTO);

		});

		return allUser;
	}

	@Override
	public Response<?> deleteUser(Integer id) {

		User user = userDao.getuserByid(id);
		if (user != null) {
			user.setIsActive(false);
			Integer updateUser = userDao.updateUser(user);
			if (updateUser != 0) {
				return new Response("Delete User Succesfully", user, HttpStatus.OK.value());
			} else {
				return new Response("try again", "Bad Request", HttpStatus.BAD_REQUEST.value());
			}
		} else {
			return new Response("User Not Found", id, HttpStatus.BAD_REQUEST.value());
		}

	}

	@Override
	public Response<?> forgotPassword(Object data, ForgotPasswordDTO passwordDTO) {
		CredentialMaster obj=new CredentialMaster();
		CredentialMaster user =(CredentialMaster) data;
		user.setPassword(obj.passwordEncoder(passwordDTO.getNewPassword()));
		String subject = "Your Password Change Successfully";
		String gmail = user.getUser().getEmail();
		
		Integer updatePassword = crediantialDao.updatePassword(user);
		
		
		if(updatePassword>0)
		{
		String emailContent = "<html>"
                + "<body>"
                + "<div style=\"font-family: Arial, sans-serif; margin: 20px;\">"
                + "<h2>Hello " + user.getUser().getName() + ",</h2>"
                + "<p>Your password has been changed successfully. Below are your updated login details:</p>"
                + "<ul>"
                + "<li><strong>Username:</strong> " + user.getUsername() + "</li>"
                + "<li><strong>New Password:</strong> " + passwordDTO.getNewPassword() + "</li>"
                + "</ul>"
                + "<p>Please keep your credentials secure. If this change was unauthorized, "
                + "contact our support team immediately.</p>"
                + "<p>Thank you for using our service!</p>"
                + "<p>Best regards,<br>Your Application Team</p>"
                + "</div>"
                + "</body>"
                + "</html>";



		try
		{
			emailSendService.sendEmail(gmail, subject, emailContent);
			return new Response<>("Password Change Succesfully","Success",HttpStatus.OK.value());
		}catch (Exception e) {
			return new Response<>("Fallid To Send Mail","Fallid",HttpStatus.BAD_REQUEST.value());
		}
		}
		
		return new Response<>("Password Change Fallid","Fallid",HttpStatus.BAD_REQUEST.value());
	}

}
