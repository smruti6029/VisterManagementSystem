package com.vms2.serviceImp;

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
import com.vms2.dto.IdIsactiveDTO;
import com.vms2.dto.UserDto;
import com.vms2.dto.UsersFroMettingDTO;
import com.vms2.entity.City;
import com.vms2.entity.Company;
import com.vms2.entity.CredentialMaster;
import com.vms2.entity.Role;
import com.vms2.entity.State;
import com.vms2.entity.User;
import com.vms2.mailservice.EmailSendService;
import com.vms2.response.Response;
import com.vms2.service.CredentialService;
import com.vms2.service.UserService;

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
	public Response<?> saveUser(UserDto userDto,User userfromtoken) {

		// save New User
		if (userDto.getId() == null) {
			User vallidUser = userDao.getUserbyPhone(userDto.getPhone().trim());
			Integer saveUser;
			if (vallidUser == null) {

				User user = User.fromDTO(userDto);
				user.setCreated_by(userfromtoken.getRole().getName());
				user.setIsActive(true);
				user.setupdatedOn(new Date());
				user.setCreatedOn(new Date());
				user.setUpdatedBy(userfromtoken.getId());
				saveUser = userDao.saveUser(user);

			}

			else {
				return new Response<>("Mobile Number Already Exit", null, HttpStatus.BAD_REQUEST.value());
			}

			return credentialService.adduserInCrediantial(userDto);

		}
		// update User
		else

		{
			User user = userDao.getuserByid(userDto.getId());

//			if (!user.getPhone().equals(userDto.getPhone())) {
//				return new Response("Username Can't  be Changed", userDto, HttpStatus.BAD_REQUEST.value());
//			}

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
			if (userDto.getFirstname() != null) {
				user.setFirstname(userDto.getFirstname());
			}
			if (userDto.getLastname() != null) {
				user.setLastname(userDto.getLastname());
			}
			if (userDto.getImage() != null) {
				user.setImage(userDto.getImage());
			}
			
			if (userDto.getGovtId() != null) {
				user.setGovtId(userDto.getGovtId());
			}
			if (userDto.getEmpCode() != null) {
				user.setEmpCode(userDto.getEmpCode());
			}
			if (userDto.getAddress() != null) {
				user.setGovtId(userDto.getAddress());
			}
			if (userDto.getPinCode() != null) {
				user.setPinCode(userDto.getPinCode());;
			}
			if (userDto.getPhone() != null) {
				user.setPhone(userDto.getPhone());
			}
			
//			if (userDto.getPhone() != null) {
//				user.setPhone(userDto.getPhone());
//			}
			if (userDto.getRole() != null) {
				Role role = new Role();
				role.setId(userDto.getRole().getId());
				user.setRole(role);
			}
			user.setupdatedOn(new Date());
			user.setUpdatedBy(userfromtoken.getId());
			Integer updateUser = userDao.updateUser(user);
			return new Response("Updated Succesfully", userDto, HttpStatus.OK.value());
		}
	}

	@Override
	public List<UserDto> getallUser(User user) {

		List<User> getallUser = userDao.getallUser(user.getCompany().getId());

		List<UserDto> allUser = new ArrayList<>();
		getallUser.forEach(x -> {

			if(x.getIsActive()==true)
			{

			if (user.getRole().getName().equals("SUPERADMIN")) {

				allUser.add(UserDto.convertUserToDTO(x));
			}
			if (user.getRole().getName().equals("ADMIN")) {
				if (!x.getRole().getName().equals("SUPERADMIN")) {
					allUser.add(UserDto.convertUserToDTO(x));
				}
			}
			if (user.getRole().getName().equals("EMPLOYEE")) {
				if (!x.getRole().getName().equals("SUPERADMIN") || user.getRole().getName().equals("ADMIN")) {
					allUser.add(UserDto.convertUserToDTO(x));
				}
			}
			}
		});

		return allUser;
	}

	@Override
	public Response<?> deleteUser(IdIsactiveDTO idIsactiveDTO) {

		User user = userDao.getuserByid(idIsactiveDTO.getId());
		if (user != null) {
			user.setIsActive(idIsactiveDTO.getIsActive());
			Integer updateUser = userDao.updateUser(user);
			
			CredentialMaster getcrediantialByuser = crediantialDao.getcrediantialByuser(idIsactiveDTO.getId());
			getcrediantialByuser.setIsActive(false);
			crediantialDao.update(getcrediantialByuser);
			
			
			if (updateUser != 0) {
				return new Response<>("Delete User Succesfully", user, HttpStatus.OK.value());
			} else {
				return new Response<>("try again", "Bad Request", HttpStatus.BAD_REQUEST.value());
			}
		} else {
			return new Response<>("User Not Found", null, HttpStatus.BAD_REQUEST.value());
		}

	}

	@Override
	public Response<?> forgotPassword(Object data, ForgotPasswordDTO passwordDTO) {
		CredentialMaster obj = new CredentialMaster();
		CredentialMaster user = (CredentialMaster) data;
		user.setPassword(obj.passwordEncoder(passwordDTO.getNewPassword()));
		user.setupdatedOn(new Date());
		String subject = "Your Password Change Successfully";
		String gmail = user.getUser().getEmail();

		Integer updatePassword = crediantialDao.update(user);

		if (updatePassword > 0) {
			String emailContent = "<html>" + "<body>" + "<div style=\"font-family: Arial, sans-serif; margin: 20px;\">"
					+ "<h2>Hello " + user.getUser().getFirstname() + " " + user.getUser().getLastname() + ",</h2>"
					+ "<p>Your password has been changed successfully. Below are your updated login details:</p>"
					+ "<ul>" + "<li><strong>Username:</strong> " + user.getUsername() + "</li>"
					+ "<li><strong>New Password:</strong> " + passwordDTO.getNewPassword() + "</li>" + "</ul>"
					+ "<p>Please keep your credentials secure. If this change was unauthorized, "
					+ "contact our support team immediately.</p>" + "<p>Thank you for using our service!</p>"
					+ "<p>Best regards,<br>Your Application Team</p>" + "</div>" + "</body>" + "</html>";

			try {
				emailSendService.sendEmail(gmail, subject, emailContent);
				return new Response<>("Password Change Succesfully", "Success", HttpStatus.OK.value());
			} catch (Exception e) {
				return new Response<>("Fallid To Send Mail", "Fallid", HttpStatus.BAD_REQUEST.value());
			}
		}

		return new Response<>("Password Change Fallid", "Fallid", HttpStatus.BAD_REQUEST.value());
	}

	@Override
	public Response<?> getUserByid(Integer id) {
		User user = userDao.getuserByid(id);
		if (user != null) {
			return new Response<>("User Detalis", user, HttpStatus.OK.value());
		} else {
			return new Response<>("User Not Found", null, HttpStatus.BAD_REQUEST.value());
		}
	}

	@Override
	public Response<?> getUsers() {
		
		List<User> getallUsers = userDao.getallUsers();
		
		List<UsersFroMettingDTO> allUsers = new ArrayList<>();
		
		if(!getallUsers.isEmpty())
		{
		getallUsers.forEach(x -> {

			
			if (x.getIsActive()==true) 
			{
				UsersFroMettingDTO convertTodto = UsersFroMettingDTO.convertTodto(x);
				allUsers.add(convertTodto);

				
			}
		});
		return new Response<>("Success",allUsers,HttpStatus.OK.value());
		}
		return new Response<>("No Record",null,HttpStatus.NO_CONTENT.value());
	}

	

}
