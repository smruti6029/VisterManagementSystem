package com.app.serviceImpl;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponentsBuilder;

import com.app.Dao.ConfigurationDao;
import com.app.Dao.CrediantialDao;
import com.app.Dao.RoleDao;
import com.app.Dao.UserDao;
import com.app.dto.AlluserBycompanyId;
import com.app.dto.ChangePasswordDto;
import com.app.dto.ExcelExportDtoForUser;
import com.app.dto.ImportExcelDTO;
import com.app.dto.InvalidUserDto;
import com.app.dto.IsActiveDto;
import com.app.dto.UserDto;
import com.app.dto.UsersFroMettingDto;
import com.app.entity.City;
import com.app.entity.Company;
import com.app.entity.CredentialMaster;
import com.app.entity.Department;
import com.app.entity.Role;
import com.app.entity.State;
import com.app.entity.User;
import com.app.mailservice.EmailSendService;
import com.app.response.Response;
import com.app.service.CredentialService;
import com.app.service.UserService;
import com.app.util.ExcelExport;

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

	@Autowired
	private ExcelImportService excelImportService;

	@Autowired
	private ExcelExport excelExport;

	@Autowired
	private ConfigurationDao configurationDao;

	@Value("${project.excel}")
	private String fileuploadPath;

	@Override
	public Response<?> saveUser(UserDto userDto) {

		if (userDto.getId() == null) {

			User vallidUser = userDao.getUserbyPhone(userDto.getPhone().trim());
			User userByEmpCode = userDao.getUserByEmpCode(userDto.getEmpCode().trim());

			if (userByEmpCode != null) {
				return new Response<>("Employee Code cant't be Duplicate", null, HttpStatus.BAD_REQUEST.value());
			}

			User userByemail = userDao.getUserByEmail(userDto.getEmail().trim());
			if (userByemail != null) {
				return new Response<>("Employee Email cant't be Duplicate", null, HttpStatus.BAD_REQUEST.value());
			}

			User userByGoveID = userDao.getUserByGoveID(userDto.getGovtId().trim());
			if (userByGoveID != null) {
				if (userDto.getGovtId().length() == 10) {
					return new Response<>("Pan Card Number Can't be Duplicate", null, HttpStatus.BAD_REQUEST.value());
				} else {
					return new Response<>("Aadhar Number Can't be Duplicate", null, HttpStatus.BAD_REQUEST.value());
				}
			}

			Integer saveUser;
			if (vallidUser == null) {

				User user = User.fromDTO(userDto);
				String dobString = userDto.getDob().trim();
				try {
					LocalDate dob = LocalDate.parse(dobString);

					if (dob.isAfter(LocalDate.now())) {
						return new Response<>("Date of birth cannot be in the future", null,
								HttpStatus.BAD_REQUEST.value());
					}

					user.setDob(dobString);
				} catch (DateTimeParseException e) {
					return new Response<>("Invalid date of birth format", null, HttpStatus.BAD_REQUEST.value());
				}
//				user.setCreated_by(userfromtoken.getRole().getName());
				user.setIsActive(true);
				user.setUpdatedOn(new Date());
				user.setCreatedOn(new Date());
				user.setIsPresent(true);
				user.setIsPermission(userDto.getIsPermission() ? true : false);

//				user.setUpdatedBy(userfromtoken.getId());
				saveUser = userDao.saveUser(user);

			}

			else {
				return new Response<>("Mobile Number Already Exists", null, HttpStatus.BAD_REQUEST.value());
			}

			return credentialService.adduserInCrediantial(userDto);

		}
		// update User
		else

		{

			User user = userDao.getuserByid(userDto.getId());

			if (user == null) {
				return new Response<>("User Not Found", null, HttpStatus.BAD_REQUEST.value());
			}

			User vallidUser = userDao.getUserbyPhone(userDto.getPhone().trim());

			if (vallidUser != null && !user.getId().equals(vallidUser.getId())) {
				return new Response<>("Mobile Number Already Exists", null, HttpStatus.BAD_REQUEST.value());
			}
			User userByEmpCode = userDao.getUserByEmpCode(userDto.getEmpCode());
			if (userByEmpCode != null && !user.getId().equals(userByEmpCode.getId())) {
				return new Response<>("Employee Code  Can't Be Duplicate", null, HttpStatus.BAD_REQUEST.value());
			}

			User userByemail = userDao.getUserByEmail(userDto.getEmail().trim());

			if (userByemail != null && !user.getId().equals(userByemail.getId())) {
				return new Response<>("Employee Email Cant't be Duplicate", null, HttpStatus.BAD_REQUEST.value());
			}

//			User userByGoveID = userDao.getUserByGoveID(userDto.getGovtId().trim());
//			if (userByGoveID != null && !user.getId().equals(userByGoveID.getId())) {
//				if (userDto.getGovtId().length() == 10) {
//					return new Response<>("Pan Card Number Can't be Duplicate", null, HttpStatus.BAD_REQUEST.value());
//				} else {
//					return new Response<>("Aadhar Number Can't be Duplicate", null, HttpStatus.BAD_REQUEST.value());
//				}
//			}

			if (userDto.getDepartmentDto() != null) {
				Department dept = new Department();
				dept.setId(userDto.getDepartmentDto().getId());
				user.setDepartment(dept);
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
			if (userDto.getPincode() != null) {
				user.setPinCode(userDto.getPincode());
			}
			if (userDto.getFirstName() != null) {
				user.setFirstname(userDto.getFirstName());
			}
			if (userDto.getLastName() != null) {
				user.setLastname(userDto.getLastName());
			}
			if (userDto.getImage() != null) {
				user.setImage(userDto.getImage());
			}

			if (userDto.getIsPermission() != null) {
				user.setIsPermission(userDto.getIsPermission());
			}

//			if (userDto.get() != null) {
//				user.setGovtId(userDto.getGovtId());
//			}
//			if (userDto() != null) {
//				user.setEmpCode(userDto.getEmpCode());
//			}
//			if (userDto.() != null) {
//				user.setGovtId(userDto.getAddress());
//			}
//			if (userDto.getPinCode() != null) {
//				user.setPinCode(userDto.getPinCode());
//				;
//			}
			if (userDto.getPhone() != null) {

				CredentialMaster userCredential = crediantialDao.getUsername(user.getPhone());
				userCredential.setUsername(userDto.getPhone());
				crediantialDao.updatePassword(userCredential);
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
			user.setUpdatedOn(new Date());
//			user.set(userfromtoken.getId());
			Integer updateUser = userDao.updateUser(user);
			return new Response<>("Updated Succesfully", userDto, HttpStatus.OK.value());
		}
	}

	@Override
	public List<UserDto> getallUser(User user) {
		List<User> getallUser = new ArrayList<>();

		if (user.getRole().getName().equals("SUPERADMIN")) {
			getallUser = userDao.getallUsers();

		}

		else {
			getallUser = userDao.getallUser(user.getCompany().getId());
		}

		List<UserDto> allUser = new ArrayList<>();
		getallUser.forEach(x -> {

			if (user.getRole().getName().equals("SUPERADMIN")) {

				if (x.getRole().getName().equals("ADMIN")) {

					if (x.getIsActive()) {

						try {
							allUser.add(UserDto.convertUserToDTO(x));
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
			}
			if (user.getRole().getName().equals("ADMIN")) {
				if (!x.getRole().getName().equals("SUPERADMIN") && !x.getRole().getName().equals("ADMIN")) {
					if (x.getIsActive()) {
						try {
							allUser.add(UserDto.convertUserToDTO(x));
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
			}
			if (user.getRole().getName().equals("EMPLOYEE")) {
				if (!x.getRole().getName().equals("SUPERADMIN") || user.getRole().getName().equals("ADMIN")) {

					if (x.getIsActive()) {
						try {
							allUser.add(UserDto.convertUserToDTO(x));
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
			}

		});
		Collections.sort(allUser, Comparator.comparing(UserDto::getCreatedOn).reversed());

		return allUser;
	}

	@Override
	public Response<?> getallUserV2(User user) {

		String baseUrl = configurationDao.getByKey("BASE_URL").getValue();
		List<User> getallUser = new ArrayList<>();

		if (user.getRole().getName().equals("SUPERADMIN")) {
			getallUser = userDao.getallUsers();
		} else {
			getallUser = userDao.getallUser(user.getCompany().getId());
		}

		AtomicInteger countUser = new AtomicInteger(0);

		List<UserDto> allUser = new ArrayList<>();

		System.out.println(getallUser.size() + " size ");
		getallUser.forEach(x -> {

			if (user.getRole().getName().equals("SUPERADMIN")) {

				if (x.getRole().getName().equals("ADMIN")) {

					if (x.getIsActive()) {
						try {
							countUser.incrementAndGet();
							allUser.add(UserDto.convertUserToDTO(x));
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
			}

			if (user.getRole().getName().equals("ADMIN")) {
				if (!x.getRole().getName().equals("SUPERADMIN") && !x.getRole().getName().equals("ADMIN")) {
					if (x.getIsActive()) {
						try {
//	                        System.out.println(x.getRole().getName() + " Role NAme           ");
							allUser.add(UserDto.convertUserToDTO(x));
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
			}

			if (user.getRole().getName().equals("EMPLOYEE")) {
				if (!x.getRole().getName().equals("SUPERADMIN") || user.getRole().getName().equals("ADMIN")) {
					if (x.getIsActive()) {
						try {
							allUser.add(UserDto.convertUserToDTO(x));
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
			}

		});

		if (user.getRole().getName().equals("ADMIN")) {
			countUser.set(getallUser.size());
		}

//		String filename = "user.xlsx";
		String allUserExcell = null;

		ExcelExportDtoForUser obj = new ExcelExportDtoForUser();
		List<ExcelExportDtoForUser> convertUsersToExcelExportDto = obj.convertUsersToExcelExportDto(allUser);

//		try {
//			ByteArrayInputStream datatoexcel = excelExport.datatoexcelforUser(convertUsersToExcelExportDto);
//
//			String filename = UUID.randomUUID().toString() + ".xlsx";
//
//			String tempFilePath = fileuploadPath + filename;
//			File excelFile = new File(tempFilePath);
//			FileUtils.copyInputStreamToFile(datatoexcel, excelFile);
//
//			allUserExcell = UriComponentsBuilder.fromHttpUrl(baseUrl).path("/api/meeting/download/excel")
//					.queryParam("filename", filename).toUriString();
//		} catch (Exception e) {
//
//		}

		AlluserBycompanyId alluserBycompanyId = new AlluserBycompanyId(countUser.get(), allUser);

		alluserBycompanyId.setExcelUrl(allUserExcell);

		return new Response<>("Success", alluserBycompanyId, HttpStatus.OK.value());
	}
	
	@Override
	public Response<?> getUsers2(Integer companyId , Integer buildingId) {

		List<User> getallUsers = userDao.getallUser2(companyId,buildingId);

		List<UsersFroMettingDto> allUsers = new ArrayList<>();

		if (!getallUsers.isEmpty()) {

			getallUsers.forEach(x -> {
				if (!x.getRole().getName().equals("RECEPTIONIST")) {

					if (x.getIsActive() == true) {
						UsersFroMettingDto convertTodto = UsersFroMettingDto.convertTodto(x);
						allUsers.add(convertTodto);
					}
				}

			});
			return new Response<>("Success", allUsers, HttpStatus.OK.value());
		}
		return new Response<>("No Record", null, HttpStatus.NO_CONTENT.value());
	}

	@Override
	public Response<?> deleteUser(IsActiveDto idIsactiveDTO) {

		User user = userDao.getuserByid(idIsactiveDTO.getId());
		if (user != null) {
			user.setIsActive(idIsactiveDTO.getIsActive());
			Integer updateUser = userDao.updateUser(user);
			if (updateUser != 0) {
				UserDto convertUserToDTO = null;
				try {
					convertUserToDTO = UserDto.convertUserToDTO(user);
				} catch (Exception e) {
					// TODO: handle exception
				}

				return new Response<>("Delete User Succesfully", convertUserToDTO, HttpStatus.OK.value());
			} else {
				return new Response<>("try again", "Bad Request", HttpStatus.BAD_REQUEST.value());
			}
		} else {
			return new Response<>("User Not Found", null, HttpStatus.BAD_REQUEST.value());
		}

	}

	@Override
	public Response<?> changePassword(Object data, ChangePasswordDto passwordDTO) {
		CredentialMaster obj = new CredentialMaster();
		CredentialMaster user = (CredentialMaster) data;
		user.setPassword(obj.passwordEncoder(passwordDTO.getNewPassword()));
		user.setupdatedOn(new Date());
		String subject = "Your Password Change Successfully";
		String gmail = user.getUser().getEmail();

		Integer updatePassword = crediantialDao.updatePassword(user);

		if (updatePassword > 0) {
			String emailContent = "<html>" + "<body>" + "<div style=\"font-family: Arial, sans-serif; margin: 20px;\">"
					+ "<h2>Hello " + user.getUser().getFirstname() + " " + user.getUser().getLastname() + ",</h2>"
					+ "<p>Your password has been changed successfully. Below are your updated login details:</p>"
					+ "<ul>" + "<li><strong>Username:</strong> " + user.getUsername() + "</li>"
					+ "<li><strong>New Password:</strong> " + passwordDTO.getNewPassword() + "</li>" + "</ul>"
					+ "<p>Please keep your credentials secure. If this change was unauthorized, "
					+ "contact our support team immediately.</p>" + "<p>Thank you for using our service!</p>"
					+ "<p>Best regards,<br>Your Application Team</p>" + "</div>" + "</body>" + "</html>";

			Thread emailThread = new Thread(() -> {

				try {
					emailSendService.sendEmail(gmail, subject, emailContent);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			});

			emailThread.start();

			return new Response<>("Password Change Succesfully", "Success", HttpStatus.OK.value());

		}
		return new Response<>("Password Change Fallid", "Fallid", HttpStatus.BAD_REQUEST.value());
	}

	@Override
	public Response<?> getUserByid(Integer id) throws Exception {
		User user = userDao.getuserByid(id);
		if (user != null) {
			return new Response<>("User Detalis", UserDto.convertUserToDTO(user), HttpStatus.OK.value());
		} else {
			return new Response<>("User Not Found", null, HttpStatus.BAD_REQUEST.value());
		}
	}

	@Override
	public User getUserById(Integer id) {

		return userDao.getuserByid(id);
	}

	@Override
	public Response<?> getUsers(Integer companyId) {

		List<User> getallUsers = userDao.getallUser(companyId);

		List<UsersFroMettingDto> allUsers = new ArrayList<>();

		if (!getallUsers.isEmpty()) {

			getallUsers.forEach(x -> {
				if (!x.getRole().getName().equals("RECEPTIONIST")) {

					if (x.getIsActive() == true) {
						UsersFroMettingDto convertTodto = UsersFroMettingDto.convertTodto(x);
						allUsers.add(convertTodto);
					}
				}

			});
			return new Response<>("Success", allUsers, HttpStatus.OK.value());
		}
		return new Response<>("No Record", null, HttpStatus.NO_CONTENT.value());
	}

	@Override
	public Response<?> genarateOtp(String userName) {

		CredentialMaster user = crediantialDao.getUsername(userName);

		if (user == null) {
			return new Response<>("User Not found", null, HttpStatus.BAD_REQUEST.value());
		}

		Random random = new Random();
		int min = 100000;
		int max = 999999;
		int randomSixDigitNumber = random.nextInt((max - min) + 1) + min;
		long otpTimestamp = System.currentTimeMillis();
		user.setOtpTimestamp(otpTimestamp);
		user.setOtp(randomSixDigitNumber);

		crediantialDao.updatePassword(user);

		String subject = "Your One Time OTP Valid For 10 Minutes";
		String Email = user.getUser().getEmail();

		String content = "Hello, " + user.getUser().getFirstname() + ",\n\n"
				+ "Here is your one-time OTP (One Time Password) for authentication: <strong>" + randomSixDigitNumber
				+ "</strong>\n\n" + "Please use this OTP within the next 10 minutes.\n\n"
				+ "If you didn't request this OTP, please disregard this message.\n\n" + "Best regards, "
				+ user.getUser().getCompany().getName();

		Thread emailThread = new Thread(() -> {

			try {
				emailSendService.sendEmail(Email, subject, content);
			} catch (Exception e) {
				e.printStackTrace();
			}
		});

		emailThread.start();

		return new Response<>("Otp Sent To Your Mail", randomSixDigitNumber, HttpStatus.OK.value());
	}

	@Override
	public Response<?> forgotPassword(ChangePasswordDto changePasswordDto) {
		CredentialMaster user = crediantialDao.getUsername(changePasswordDto.getUsername());

		if (user == null) {
			return new Response<>("User Not found", null, HttpStatus.BAD_REQUEST.value());
		}

		try {
			long currentTime = System.currentTimeMillis();
			long otpTimestamp = user.getOtpTimestamp();

			if (currentTime - otpTimestamp > 600000) {
				user.setOtp(null);
				crediantialDao.updatePassword(user);
				return new Response<>("OTP Expired", null, HttpStatus.BAD_REQUEST.value());
			}

			if (!user.getOtp().equals(changePasswordDto.getOtp())) {
				return new Response<>("Incorrect OTP", null, HttpStatus.BAD_REQUEST.value());
			}

			user.setOtp(null);
			user.setOtpTimestamp(null);
			CredentialMaster obj = new CredentialMaster();
			user.setPassword(obj.passwordEncoder(changePasswordDto.getNewPassword()));
			crediantialDao.updatePassword(user);
			return new Response<>("Password Upadte Succesfully", null, HttpStatus.OK.value());

		} catch (Exception e) {
			return new Response<>("Incorrect OTP", null, HttpStatus.BAD_REQUEST.value());
		}

	}

	@Override
	public Response<?> saveUsersByexcell(MultipartFile file, Integer companyId) {
		try {
			Response<?> convertExceltoList = excelImportService.convertExceltoList(file.getInputStream(), companyId);
			if (convertExceltoList.getStatus() == HttpStatus.BAD_REQUEST.value()) {
				return convertExceltoList;
			}
			String faildDataDownloadLink = null;
			String duplicateDataDownloadLink = null;
			String baseUrl = configurationDao.getByKey("BASE_URL").getValue();

			if (convertExceltoList != null && convertExceltoList.getStatus() == HttpStatus.OK.value()) {
				List<UserDto> validUser = null;
				List<UserDto> inValidUser = null;
				Integer totalElement = 0;
				Integer successfullyAdded = 0;
				Integer unSuccessfullyAdded = 0;
				Integer duplicateData = 0;

				Map<String, List<UserDto>> data = (Map<String, List<UserDto>>) convertExceltoList.getData();

				for (String user : data.keySet()) {
					if (user.equals("Success")) {
						validUser = data.get(user);

						System.out.println(validUser + "vallid user");
					} else {
						inValidUser = data.get(user);
						System.out.println("Invallid user");
					}
				}

				if (validUser != null && !validUser.isEmpty()) {
					totalElement += validUser.size();
				}
				if (inValidUser != null && !inValidUser.isEmpty()) {
					totalElement += inValidUser.size();
					unSuccessfullyAdded = inValidUser.size();
				}

				List<UserDto> duplicateDataInExcell = new ArrayList<>();

				for (UserDto user : validUser) {
					Response<?> saveUser = saveUser(user);
					if (saveUser.getStatus() == HttpStatus.OK.value()) {
						successfullyAdded++;
					} else {
						duplicateData++;
						duplicateDataInExcell.add(user);
					}
				}

				if (!inValidUser.isEmpty()) {

					List<InvalidUserDto> convertToInvalidUserDtoList = InvalidUserDto
							.convertToInvalidUserDtoList(inValidUser);
//					String filename = "unSuccessfully.xlsx";
					ByteArrayInputStream datatoexcel = excelExport.datatoexcel(convertToInvalidUserDtoList);

					String filename = UUID.randomUUID().toString() + ".xlsx";

					String tempFilePath = fileuploadPath + filename;
					File excelFile = new File(tempFilePath);
					FileUtils.copyInputStreamToFile(datatoexcel, excelFile);

//					falidDataDawnloadLink = ServletUriComponentsBuilder.fromCurrentContextPath()
//							.path("/api/meeting/download/excel").queryParam("filename", randomFilename).toUriString();

					// Use baseUrl and chain path and queryParam
					faildDataDownloadLink = UriComponentsBuilder.fromHttpUrl(baseUrl)
							.path("/api/meeting/download/excel").queryParam("filename", filename).toUriString();
				}

				if (!duplicateDataInExcell.isEmpty()) {

					List<InvalidUserDto> duplicate = InvalidUserDto.convertToInvalidUserDtoList(duplicateDataInExcell);
//					String filename = "duplicateData.xlsx";
					ByteArrayInputStream datatoexcel = excelExport.datatoexcel(duplicate);

					String filename = UUID.randomUUID().toString() + ".xlsx";

					String tempFilePath = fileuploadPath + filename;
					File excelFile = new File(tempFilePath);
					FileUtils.copyInputStreamToFile(datatoexcel, excelFile);

					duplicateDataDownloadLink = UriComponentsBuilder.fromHttpUrl(baseUrl)
							.path("/api/meeting/download/excel").queryParam("filename", filename).toUriString();
				}

				System.out.println(duplicateDataInExcell);
//				String downloadLink = "/api/user/download/excel?filename=" + randomFilename;

				// Create a Response object with the Excel data
				ImportExcelDTO importExcelDTO = new ImportExcelDTO(totalElement, successfullyAdded, unSuccessfullyAdded,
						duplicateData, faildDataDownloadLink, duplicateDataDownloadLink);

				System.out.println(successfullyAdded + " Success data added");
				System.out.println(totalElement + " total Element");
				System.out.println(unSuccessfullyAdded + " total unsuccessfully");

				return new Response<>("Success", importExcelDTO, HttpStatus.OK.value());
			} else {
				return convertExceltoList;
			}
		} catch (IOException e) {
			return new Response<>("Try again", null, HttpStatus.BAD_REQUEST.value());
		}
	}

	@Override
	public Response<?> saveUsersByexcellV2(MultipartFile file, Integer companyId) {
		try {
			Response<?> convertExceltoList = excelImportService.convertExceltoListV2(file.getInputStream(), companyId);
			if (convertExceltoList.getStatus() == HttpStatus.BAD_REQUEST.value()) {
				return convertExceltoList;
			}
			String faildDataDownloadLink = null;
			String duplicateDataDownloadLink = null;
			String baseUrl = configurationDao.getByKey("BASE_URL").getValue();

			if (convertExceltoList != null && convertExceltoList.getStatus() == HttpStatus.OK.value()) {
				List<UserDto> validUser = null;
				List<UserDto> inValidUser = null;
				Integer totalElement = 0;
				Integer successfullyAdded = 0;
				Integer unSuccessfullyAdded = 0;
				Integer duplicateData = 0;

				Map<String, List<UserDto>> data = (Map<String, List<UserDto>>) convertExceltoList.getData();

				for (String user : data.keySet()) {
					if (user.equals("Success")) {
						validUser = data.get(user);

						System.out.println(validUser + "vallid user");
					} else {
						inValidUser = data.get(user);
						System.out.println("Invallid user");
					}
				}

				if (validUser != null && !validUser.isEmpty()) {
					totalElement += validUser.size();
				}
				if (inValidUser != null && !inValidUser.isEmpty()) {
					totalElement += inValidUser.size();
					unSuccessfullyAdded = inValidUser.size();
				}

				List<UserDto> duplicateDataInExcell = new ArrayList<>();

				for (UserDto user : validUser) {
					Response<?> saveUser = saveUser(user);
					if (saveUser.getStatus() == HttpStatus.OK.value()) {
						successfullyAdded++;
					} else {
						duplicateData++;
						duplicateDataInExcell.add(user);
					}
				}
				ImportExcelDTO importExcelDTO = new ImportExcelDTO();

				if (!inValidUser.isEmpty()) {

					List<InvalidUserDto> convertToInvalidUserDtoList = InvalidUserDto
							.convertToInvalidUserDtoList(inValidUser);
					importExcelDTO.setFailedElement(convertToInvalidUserDtoList);
//					String filename = "unSuccessfully.xlsx";
//					ByteArrayInputStream datatoexcel = excelExport.datatoexcel(convertToInvalidUserDtoList);
//
//					String filename = UUID.randomUUID().toString() + ".xlsx";
//
//					String tempFilePath = fileuploadPath + filename;
//					File excelFile = new File(tempFilePath);
//					FileUtils.copyInputStreamToFile(datatoexcel, excelFile);

//					falidDataDawnloadLink = ServletUriComponentsBuilder.fromCurrentContextPath()
//							.path("/api/meeting/download/excel").queryParam("filename", randomFilename).toUriString();

					// Use baseUrl and chain path and queryParam
//					faildDataDownloadLink = UriComponentsBuilder.fromHttpUrl(baseUrl)
//							.path("/api/meeting/download/excel").queryParam("filename", filename).toUriString();
				}

				if (!duplicateDataInExcell.isEmpty()) {

					List<InvalidUserDto> duplicate = InvalidUserDto.convertToInvalidUserDtoList(duplicateDataInExcell);
//					String filename = "duplicateData.xlsx";
					importExcelDTO.setDuplicateElement(duplicate);
//					ByteArrayInputStream datatoexcel = excelExport.datatoexcel(duplicate);
//
//					String filename = UUID.randomUUID().toString() + ".xlsx";
//
//					String tempFilePath = fileuploadPath + filename;
//					File excelFile = new File(tempFilePath);
//					FileUtils.copyInputStreamToFile(datatoexcel, excelFile);
//
//					duplicateDataDownloadLink = UriComponentsBuilder.fromHttpUrl(baseUrl)
//							.path("/api/meeting/download/excel").queryParam("filename", filename).toUriString();
				}

				System.out.println(duplicateDataInExcell);
//				String downloadLink = "/api/user/download/excel?filename=" + randomFilename;

				importExcelDTO.setDuplicateData(duplicateData);
				importExcelDTO.setDuplicateDataLink(duplicateDataDownloadLink);
				importExcelDTO.setFailedData(unSuccessfullyAdded);
				importExcelDTO.setFailedDataLink(faildDataDownloadLink);
				importExcelDTO.setSuccessfullyAdded(successfullyAdded);
				importExcelDTO.setTotalElement(totalElement);

				System.out.println(successfullyAdded + " Success data added");
				System.out.println(totalElement + " total Element");
				System.out.println(unSuccessfullyAdded + " total unsuccessfully");

				return new Response<>("Success", importExcelDTO, HttpStatus.OK.value());
			} else {
				return convertExceltoList;
			}
		} catch (IOException e) {
			return new Response<>("Try again", null, HttpStatus.BAD_REQUEST.value());
		}
	}

	@Override
	public Response<?> presentUser(IsActiveDto activeDto) {

		User user = userDao.getuserByid(activeDto.getId());
		user.setIsPresent(activeDto.getIsActive());
		userDao.updateUser(user);
		return new Response<>("Status Updated Succesfully", activeDto, HttpStatus.OK.value());

	}

	@Override
	public Response<?> acessUser(IsActiveDto activeDto) {

		User user = userDao.getuserByid(activeDto.getId());
		user.setIsPermission(activeDto.getIsActive());
		userDao.updateUser(user);
		return new Response<>("Status Updated Succesfully", activeDto, HttpStatus.OK.value());

	}
}
