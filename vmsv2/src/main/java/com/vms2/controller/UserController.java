package com.vms2.controller;

import java.awt.print.Pageable;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.vms2.authorize.Authorization;
import com.vms2.dto.ForgotPasswordDTO;
import com.vms2.dto.IdIsactiveDTO;
import com.vms2.dto.UserDto;
import com.vms2.entity.User;
import com.vms2.response.Response;
import com.vms2.service.UserService;
import com.vms2.validation.VallidationClass;

@RestController
@RequestMapping("/api/user/")
public class UserController {

	@Autowired
	private VallidationClass vallidationClass;

	@Autowired
	private UserService userService;

	@Autowired
	private Authorization authorization;

	@PostMapping("/adduser")
	public ResponseEntity<?> addUser(@RequestBody @Valid UserDto userDto,HttpServletRequest request, HttpServletResponse response) {
		
		String header = request.getHeader("Authorization");
		if(header==null)
		{
			return new ResponseEntity<>((new Response<>("Unauthorize",null,HttpStatus.UNAUTHORIZED.value())),HttpStatus.UNAUTHORIZED);
		}
		
		Response<?> authorizetoAdduser = authorization.authorizetoAdduser(header.substring(7),userDto);
		
		if(authorizetoAdduser.getStatus()==HttpStatus.OK.value())
		{
			if(userDto.getId()==null)
			{
		Response<?> checkUser = vallidationClass.checkUser(userDto);
		if (checkUser.getStatus() == HttpStatus.OK.value()) {
			Response<?> saveUser = userService.saveUser(userDto,(User)authorizetoAdduser.getData());

			if(saveUser.getStatus()==HttpStatus.OK.value())
			{
			return new ResponseEntity<>(saveUser, HttpStatus.OK);
			}
			else
			{
				return new ResponseEntity<>(saveUser, HttpStatus.BAD_REQUEST);	
			}
		} else {
			return new ResponseEntity<>(checkUser, HttpStatus.BAD_REQUEST);
		}
		}
			else
			{
				Response<?> saveUser = userService.saveUser(userDto,(User)authorizetoAdduser.getData());
				return new ResponseEntity<>(saveUser, HttpStatus.OK);
			}
		}
		return ResponseEntity.badRequest().body(authorizetoAdduser);

	}

	@GetMapping("/getall")
	public ResponseEntity<?> getallUser(HttpServletRequest request, HttpServletResponse response) {
		String header = request.getHeader("Authorization");
		if(header==null)
		{
			return new ResponseEntity<>((new Response<>("Unauthorize",null,HttpStatus.UNAUTHORIZED.value())),HttpStatus.UNAUTHORIZED);		}
		User authorize = authorization.authorizegetallUserdetalis(header.substring(7));

		if (authorize!=null) {

			List<UserDto> getallUser = userService.getallUser(authorize);
			return new ResponseEntity<>((new Response<>("Success",getallUser,HttpStatus.OK.value())),HttpStatus.OK);

		}
		else {
			return new ResponseEntity<>((new Response<>("Unauthorize",null,HttpStatus.BAD_REQUEST.value())),HttpStatus.BAD_REQUEST);

		}
	}

	@PostMapping("/delete")
	public ResponseEntity<?> deleteUserByid(@RequestBody IdIsactiveDTO idIsactiveDTO) {
		
		Response<?> user = userService.getUserByid(idIsactiveDTO.getId());
		
		User data =(User) user.getData();
		
		if(data.getRole().getName().equals("SUPERADMIN"))
		{
			return new ResponseEntity<>(new Response<>("You Can't Delete SuperAdmin",null,HttpStatus.BAD_REQUEST.value()),HttpStatus.BAD_REQUEST);
		}
		
		Response<?> deleteUser = userService.deleteUser(idIsactiveDTO);
		return ResponseEntity.ok(deleteUser);
	}

	@PostMapping("/forgot")
	public ResponseEntity<?> forgotUserPassword(@RequestBody ForgotPasswordDTO passwordDTO)
	{
		
		Response<?> checkPassword = vallidationClass.checkPassword(passwordDTO);
		if(checkPassword.getStatus()==HttpStatus.OK.value())
		{
			Response<?> forgotPassword = userService.forgotPassword(checkPassword.getData(),passwordDTO);
			if(forgotPassword.getStatus()==HttpStatus.OK.value())
			{
			return ResponseEntity.ok(forgotPassword);
			}
			return ResponseEntity.badRequest().body(forgotPassword);
		}
		
		return ResponseEntity.badRequest().body(checkPassword);
	}
	
	
	@GetMapping("/getbyid/{id}")
	public ResponseEntity<?> getUserByid(@PathVariable Integer id) {
		Response<?> user = userService.getUserByid(id);
		if(user!=null)
		{
		return new ResponseEntity<>(user,HttpStatus.OK);
	}
		return new ResponseEntity<>(user,HttpStatus.BAD_REQUEST);
	}
	
	
	
	
	@GetMapping("/alluser")
	public ResponseEntity<?> getUserforVister()
	{
		Response<?> users = userService.getUsers();
		if(users.getStatus()==HttpStatus.OK.value())
		{
			return ResponseEntity.ok(users);
		}
	
		return new ResponseEntity<>(users,HttpStatus.NO_CONTENT);
	}
}
