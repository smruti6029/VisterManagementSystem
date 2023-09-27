package com.vms2.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.vms2.authorize.Authorization;
import com.vms2.dto.ForgotPasswordDTO;
import com.vms2.dto.UserDto;
import com.vms2.response.Response;
import com.vms2.security.JwtUtil;
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
			return ResponseEntity.badRequest().body("Unauthorize");
		}
		
		Response<?> authorizetoAdduser = authorization.authorizetoAdduser(header.substring(7),userDto);
		
		if(authorizetoAdduser.getStatus()==HttpStatus.OK.value())
		{
		Response<?> checkUser = vallidationClass.checkUser(userDto);
		if (checkUser.getStatus() == HttpStatus.OK.value()) {
			Response<?> saveUser = userService.saveUser(userDto);

			return new ResponseEntity<Response>(saveUser, HttpStatus.OK);
		} else {
			return new ResponseEntity<Response>(checkUser, HttpStatus.BAD_REQUEST);
		}
		}
		return ResponseEntity.badRequest().body(authorizetoAdduser);

	}

	@GetMapping("/getall")
	public ResponseEntity<?> getallUser(HttpServletRequest request, HttpServletResponse response) {
		String header = request.getHeader("Authorization");
		boolean authorizegetallUserdetalis = authorization.authorizegetallUserdetalis(header.substring(7));

		if (authorizegetallUserdetalis) {

			List<UserDto> getallUser = userService.getallUser();
			return new ResponseEntity<List<UserDto>>(getallUser, HttpStatus.OK);
		}
		else {
			return ResponseEntity.badRequest().body("Unauthorize");

		}
	}

	@DeleteMapping("/delete/{id}")
	public ResponseEntity<?> deleteUserByid(@PathVariable Integer id) {
		Response<?> deleteUser = userService.deleteUser(id);
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
	
}
