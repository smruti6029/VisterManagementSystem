package com.app.controller;

import java.io.File;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.app.authorize.Authorization;
import com.app.dto.ChangePasswordDto;
import com.app.dto.IsActiveDto;
import com.app.dto.UserDto;
import com.app.entity.User;
import com.app.response.Response;
import com.app.service.UserService;
import com.app.serviceImpl.ExcelImportService;
import com.app.validation.VallidationClass;

@RestController
@RequestMapping("/api/user/")
public class UserController {

	@Autowired
	private VallidationClass vallidationClass;

	@Autowired
	private UserService userService;

	@Autowired
	private Authorization authorization;

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Value("${project.excel}")
	private String excelDir;

//	@PostMapping("/adduser1")
//	public ResponseEntity<?> addUser1(@RequestBody @Valid UserDto userDto,HttpServletRequest request, HttpServletResponse response) {
//		
//		
//		Response<?> checkUser = vallidationClass.checkUser(userDto);
//		if (checkUser.getStatus() == HttpStatus.OK.value()) {
//			Response<?> saveUser = userService.saveUser(userDto);
//
//			return new ResponseEntity<Response>(saveUser, HttpStatus.OK);
//		} else {
//			return new ResponseEntity<Response>(checkUser, HttpStatus.BAD_REQUEST);
//		}
//		
//
//	}

//	@PostMapping("/adduser")
//	public ResponseEntity<?> addUser(@RequestBody @Valid UserDto userDto,HttpServletRequest request, HttpServletResponse response) {
//		
//		String header = request.getHeader("Authorization");
//		if(header==null)
//		{
//			return new ResponseEntity<Response>((new Response("Unauthorize",null,HttpStatus.BAD_REQUEST.value())),HttpStatus.BAD_REQUEST);
//		}
//		
//		Response<?> authorizetoAdduser = authorization.authorizetoAdduser(header.substring(7),userDto);
//		
//		if(authorizetoAdduser.getStatus()==HttpStatus.OK.value())
//		{
//		Response<?> checkUser = vallidationClass.checkUser(userDto);
//		if (checkUser.getStatus() == HttpStatus.OK.value()) {
//			Response<?> saveUser = userService.saveUser(userDto);
//
//			return new ResponseEntity<Response>(saveUser, HttpStatus.OK);
//		} else {
//			return new ResponseEntity<Response>(checkUser, HttpStatus.BAD_REQUEST);
//		}
//		}
//		return ResponseEntity.badRequest().body(authorizetoAdduser);
//
//	}

	@PostMapping("/adduser")
	public ResponseEntity<?> addUser(@RequestBody @Valid UserDto userDto, HttpServletRequest request,
			HttpServletResponse response) {

		String header = request.getHeader("Authorization");
		if (header == null) {
			return new ResponseEntity<>((new Response<>("Unauthorize", null, HttpStatus.UNAUTHORIZED.value())),
					HttpStatus.UNAUTHORIZED);
		}

		Response<?> authorizetoAdduser = authorization.authorizetoAdduser(header.substring(7), userDto);

		if (authorizetoAdduser.getStatus() == HttpStatus.OK.value()) {
			if (userDto.getId() == null) {
				Response<?> checkUser = vallidationClass.checkUser(userDto, authorizetoAdduser);
				if (checkUser.getStatus() == HttpStatus.OK.value()) {
					Response<?> saveUser = userService.saveUser(userDto);

					if (saveUser.getStatus() == HttpStatus.OK.value()) {
						return new ResponseEntity<>(saveUser, HttpStatus.OK);
					} else {
						return new ResponseEntity<>(saveUser, HttpStatus.BAD_REQUEST);
					}
				} else {
					return new ResponseEntity<>(checkUser, HttpStatus.BAD_REQUEST);
				}
			} else {
				Response<?> authorizeto = authorization.authorizetoAdduser(header.substring(7), userDto);

				if (authorizetoAdduser.getStatus() == HttpStatus.OK.value()) {
					Response<?> saveUser = userService.saveUser(userDto);
					return new ResponseEntity<>(saveUser, HttpStatus.valueOf(saveUser.getStatus()));
				} else {
					return new ResponseEntity<>(authorizeto, HttpStatus.BAD_REQUEST);
				}
			}
		}
		return ResponseEntity.badRequest().body(authorizetoAdduser);

	}

	@GetMapping("/getall")
	public ResponseEntity<?> getallUser(HttpServletRequest request, HttpServletResponse response) {
		String header = request.getHeader("Authorization");
		if (header == null) {
			return new ResponseEntity<>((new Response<>("Unauthorize", null, HttpStatus.BAD_REQUEST.value())),
					HttpStatus.BAD_REQUEST);
		}
		User authorize = authorization.authorizegetallUserdetalis(header.substring(7));

		if (authorize != null) {

//			List<UserDto> getallUser = userService.getallUser(authorize);
			Response<?> getallUserV2 = userService.getallUserV2(authorize);
//			return new ResponseEntity<List<UserDto>>(getallUser, HttpStatus.OK);
			return new ResponseEntity<>(getallUserV2, HttpStatus.valueOf(getallUserV2.getStatus()));

		} else {
			return new ResponseEntity<>((new Response<>("Unauthorize", null, HttpStatus.BAD_REQUEST.value())),
					HttpStatus.BAD_REQUEST);

		}
	}

//	
//	@GetMapping("/getallvis")
//	public ResponseEntity<?> getallUser1(HttpServletRequest request, HttpServletResponse response) {
//		
//		String header = request.getHeader("Authorization");
//		if(header==null)
//		{
//			return new ResponseEntity<Response>((new Response("Unauthorize",null,HttpStatus.BAD_REQUEST.value())),HttpStatus.BAD_REQUEST);
//		}
//		User authorize = authorization.authorizegetallUserdetalis(header.substring(7));
//
//		if (authorize!=null) {
//
//			List<UserDto> getallUser = userService.getallUser(authorize);
//			
//			return new ResponseEntity<List<UserDto>>(getallUser, HttpStatus.OK);
//		}
//		else {
//			return new ResponseEntity<Response>((new Response("Unauthorize",null,HttpStatus.BAD_REQUEST.value())),HttpStatus.BAD_REQUEST);
//
//		}
//	}

	@PostMapping("/delete")
	public ResponseEntity<?> deleteUserByid(@RequestBody IsActiveDto idIsactiveDTO) throws Exception {

		Response<?> user = userService.getUserByid(idIsactiveDTO.getId());

		UserDto data = (UserDto) user.getData();

		if (data.getRole().getName().equals("SUPERADMIN")) {
			return new ResponseEntity<>(
					new Response<>("You Can't Delete SuperAdmin", null, HttpStatus.BAD_REQUEST.value()),
					HttpStatus.BAD_REQUEST);
		}

		Response<?> deleteUser = userService.deleteUser(idIsactiveDTO);
		return ResponseEntity.ok(deleteUser);
	}

	@PostMapping("/change")
	public ResponseEntity<?> changeUserPassword(@RequestBody @Valid ChangePasswordDto passwordDTO) {

		Response<?> checkPassword = vallidationClass.checkPassword(passwordDTO);
		if (checkPassword.getStatus() == HttpStatus.OK.value()) {
			Response<?> forgotPassword = userService.changePassword(checkPassword.getData(), passwordDTO);
			if (forgotPassword.getStatus() == HttpStatus.OK.value()) {
				return ResponseEntity.ok(forgotPassword);
			}
			return ResponseEntity.badRequest().body(forgotPassword);
		}

		return ResponseEntity.badRequest().body(checkPassword);
	}

	@PostMapping("/forgot")
	public ResponseEntity<?> forgotUserPassword(@RequestBody @Valid ChangePasswordDto changePasswordDto) {
		Response<?> response = vallidationClass.vallidOtp(changePasswordDto);

		if (response.getStatus() == HttpStatus.OK.value()) {
			Response<?> forgotPassword = userService.forgotPassword(changePasswordDto);
			return new ResponseEntity<>(forgotPassword, HttpStatus.valueOf(forgotPassword.getStatus()));

		}
		return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatus()));
	}

	@GetMapping("/getotp")
	public ResponseEntity<?> getOtpByUserforPasswordForgot(@RequestParam String username) {

		Response<?> response = userService.genarateOtp(username);

		return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatus()));
	}

	@GetMapping("/getbyid/{id}")
	public ResponseEntity<?> getUserByid(@PathVariable Integer id) throws Exception {
		Response<?> user = userService.getUserByid(id);
		if (user.getStatus() == HttpStatus.OK.value()) {
			return new ResponseEntity<>(new Response<>("Success", user, HttpStatus.OK.value()), HttpStatus.OK);
		} else {
			return new ResponseEntity<>(new Response<>("User Not Found ", null, HttpStatus.BAD_REQUEST.value()),
					HttpStatus.BAD_REQUEST);
		}
	}

	@GetMapping("/alluser")
	public ResponseEntity<?> getUserforVister(@RequestParam Integer companyId) {

		if (companyId == null) {
			return new ResponseEntity<>(new Response<>("Provide Company Id", null, HttpStatus.BAD_REQUEST.value()),
					HttpStatus.BAD_REQUEST);
		}
		Response<?> users = userService.getUsers(companyId);
		if (users.getStatus() == HttpStatus.OK.value()) {
			return ResponseEntity.ok(users);
		}

		return new ResponseEntity<>(users, HttpStatus.NO_CONTENT);
	}

	@GetMapping("/alluserbuildingId")
	public ResponseEntity<?> getUserforVister2(@RequestParam(required = false) Integer companyId,
			@RequestParam(required = false) Integer buildingId, HttpServletRequest request) {

		String header = request.getHeader("Authorization");
		if (header != null) {
			User checkRole = authorization.checkRole(header.substring(7));
			if (checkRole != null) {

				Response<?> users = userService.getUsers2(checkRole.getCompany().getId(), buildingId);
				if (users.getStatus() == HttpStatus.OK.value()) {
					return ResponseEntity.ok(users);
				}

				return new ResponseEntity<>(users, HttpStatus.NO_CONTENT);
			}
		}

		Response<?> users = userService.getUsers2(companyId, buildingId);
		if (users.getStatus() == HttpStatus.OK.value()) {
			return ResponseEntity.ok(users);
		}

		return new ResponseEntity<>(users, HttpStatus.NO_CONTENT);
	}

	@PostMapping("/excel/upload")
	public ResponseEntity<?> upload(@RequestParam MultipartFile file, @RequestParam Integer companyId,
			HttpServletRequest request) throws IOException {

		String header = request.getHeader("Authorization");
		if (header == null) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("You Are Not Authorize");
		}

		Boolean checkVallidUSer = authorization.checkValidUser(header.substring(7));

		if (checkVallidUSer) {
//		
			System.out.println(file.getContentType() + "Content type ");

			if (file != null && ExcelImportService.checkExcelFormat(file)) {
				// true
//				Response<?> saveUsersByexcell = userService.saveUsersByexcell(file, companyId);
				Response<?> saveUsersByexcell = userService.saveUsersByexcellV2(file, companyId);
				return new ResponseEntity<>(saveUsersByexcell, HttpStatus.valueOf(saveUsersByexcell.getStatus()));
			} else {
				return new ResponseEntity<>(new Response<>("Please Upload a excel Sheet", null, 400),
						HttpStatus.BAD_REQUEST);
//				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Please Upload a excel Sheet");
			}
		}
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("You Are Not Authorize");
	}

	@GetMapping("/download/excel")
	public ResponseEntity<Resource> downloadExcel(@RequestParam("filename") String filename) {

		try {

			String filePath = excelDir + filename;

			File file = new File(filePath);
			if (!file.exists()) {

				System.out.println("file not found");

				return ResponseEntity.notFound().build();
			}

			FileSystemResource resource = new FileSystemResource(file);

			HttpHeaders headers = new HttpHeaders();

			headers.add("Content-Disposition", "attachment; filename=" + filename);

			return ResponseEntity.ok().headers(headers).contentType(MediaType.APPLICATION_OCTET_STREAM).body(resource);

		} catch (Exception e) {

			e.printStackTrace();

			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		}
	}

	@PostMapping("/present")
	public ResponseEntity<?> presentUser(@RequestBody IsActiveDto activeDto, HttpServletRequest request) {

		Response<?> response = userService.presentUser(activeDto);
		return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatus()));

	}

	@PostMapping("/access")
	public ResponseEntity<?> acessUser(@RequestBody IsActiveDto activeDto, HttpServletRequest request) {

		Response<?> response = userService.acessUser(activeDto);
		return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatus()));

	}

}
