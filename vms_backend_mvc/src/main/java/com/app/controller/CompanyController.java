package com.app.controller;

import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.poi.util.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.app.Dao.CityDao;
import com.app.Dao.CompanyDao;
import com.app.Dao.ConfigurationDao;
import com.app.Dao.StateDao;
import com.app.authorize.Authorization;
import com.app.dto.CompanyDTO;
import com.app.dto.CompanyPaginatedResponse;
import com.app.dto.CustomResponseDTO;
import com.app.dto.IsActiveDto;
import com.app.dto.PaginationRequest;
import com.app.dto.SerachCompnyRequest;
import com.app.entity.Company;
import com.app.entity.User;
import com.app.exception.CompanyAlreadyExistsException;
import com.app.exception.ConstraintViolationException;
import com.app.response.Response;
import com.app.service.CompanyService;
import com.app.service.FileService;
import com.app.util.ExcelExport;

@RestController
@RequestMapping("/com")
public class CompanyController {

	@Autowired
	private StateDao stateRepo;

	@Autowired
	private CityDao cityRepo;

	@Autowired
	private CompanyDao companyDao;

	@Autowired
	private ExcelExport excelExport;

	@Autowired
	private ConfigurationDao configurationDao;

	private final CompanyService companyService;
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private Authorization authorization;

	@Value("${project.excel}")
	private String excelDir;

	public CompanyController(CompanyService companyService) {
		this.companyService = companyService;
	}

	@Autowired
	private FileService fileService;

	@Value("${project.image}")
	private String path;

	@PostMapping("/add")
	public ResponseEntity<?> createCompany(@ModelAttribute @Valid CompanyDTO companyDTO, BindingResult bindingResult,
			HttpServletRequest request) {

		String header = request.getHeader("Authorization");
		System.out.println(header + "header outer ");
		if (header == null) {
			System.out.println(header + "header         ");
			return new ResponseEntity<>((new Response<>("Unauthorize", null, HttpStatus.UNAUTHORIZED.value())),
					HttpStatus.UNAUTHORIZED);
		}
		User user = authorization.authorizationForCompany(header.substring(7));
		if (user == null) {
			return new ResponseEntity<>((new Response<>("Unauthorize", null, HttpStatus.UNAUTHORIZED.value())),
					HttpStatus.UNAUTHORIZED);
		}

		if (bindingResult.hasErrors()) {

			Map<String, String> errorMap = new HashMap<>();

			for (FieldError error : bindingResult.getFieldErrors()) {

				errorMap.put("error", error.getDefaultMessage());

			}
			errorMap.put("status", "400");
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMap);
		}

		try {

			Response<?> companyByphone = companyService.getCompanyByphone(companyDTO);
			if (companyByphone.getStatus() != HttpStatus.OK.value()) {
				throw new CompanyAlreadyExistsException(companyByphone.getMessage());
			}

			if (companyService.isCompanyNameInUse(companyDTO.getName())) {

				throw new CompanyAlreadyExistsException("Company with the same NAME already exists");
			}

			if (companyService.isEmailAlreadyInUse(companyDTO.getEmail())) {

				throw new CompanyAlreadyExistsException("Company with the same email already exists");
			}

			if (companyService.isPhoneNumberInUser(companyDTO.getPhoneNumber())) {

				throw new CompanyAlreadyExistsException("Company with the same PhoneNumber already exists");
			}

			if (companyService.existsByNameAndStateIdAndCityId(

					companyDTO.getName(), companyDTO.getState().getId(), companyDTO.getCity().getId())) {

				throw new CompanyAlreadyExistsException(
						"Company with the same name already exists in the specified state and city");
			}

//			System.out.println("company  image " + companyDTO.getImage());

			if (companyDTO.getImage().isEmpty()) {

				return new ResponseEntity<>(
						new CustomResponseDTO("Comapany Logo must not be null", HttpStatus.BAD_REQUEST.value()),
						HttpStatus.BAD_REQUEST);
			}

			CompanyDTO saveCompany = companyService.saveCompany(companyDTO);

			CustomResponseDTO response = new CustomResponseDTO(saveCompany, HttpStatus.OK.name(),
					HttpStatus.OK.value());

			return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatusCode()));

		} catch (CompanyAlreadyExistsException ex) {

			return new ResponseEntity<>(new CustomResponseDTO(ex.getMessage(), HttpStatus.CONFLICT.value()),
					HttpStatus.CONFLICT);

		} catch (ConstraintViolationException ex) {

			return new ResponseEntity<>(new CustomResponseDTO(ex.getMessage(), HttpStatus.CONFLICT.value()),
					HttpStatus.CONFLICT);

		} catch (Exception e) {

			e.printStackTrace();

			return new ResponseEntity<>(
					new CustomResponseDTO(HttpStatus.BAD_REQUEST.name(), HttpStatus.BAD_REQUEST.value()),
					HttpStatus.BAD_REQUEST);
		}

	}

	@GetMapping("/get")
	public ResponseEntity<?> getCompanyById(@RequestParam Integer id, HttpServletRequest request) {

//		String header = request.getHeader("Authorization");
//		if (header == null) {
//			return new ResponseEntity<>((new Response<>("Unauthorize",null,HttpStatus.UNAUTHORIZED.value())),HttpStatus.UNAUTHORIZED);
//		}
//		User user = authorization.authorizetoaddCompany(header.substring(7));
//		if (user == null) {
//			return new ResponseEntity<>((new Response<>("Unauthorize",null,HttpStatus.UNAUTHORIZED.value())),HttpStatus.UNAUTHORIZED);
//		}

		try {

			CompanyDTO company = companyService.getCompanyById(id);

			if (company != null) {
				return new ResponseEntity<>(new CustomResponseDTO(company, HttpStatus.OK.name(), HttpStatus.OK.value()),
						HttpStatus.OK);
			} else {
				return new ResponseEntity<>(new CustomResponseDTO("Company not found", HttpStatus.NOT_FOUND.value()),
						HttpStatus.NOT_FOUND);
			}
		} catch (Exception e) {
			return new ResponseEntity<>(
					new CustomResponseDTO("Failed to fetch company", HttpStatus.INTERNAL_SERVER_ERROR.value()),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/all")
	public ResponseEntity<?> getAllCompanies(HttpServletRequest request) {

		String header = request.getHeader("Authorization");
		if (header == null) {
			return new ResponseEntity<>((new Response<>("Unauthorize", null, HttpStatus.UNAUTHORIZED.value())),
					HttpStatus.UNAUTHORIZED);
		}
		User user = authorization.authorizationForCompany(header.substring(7));
		if (user == null) {
			return new ResponseEntity<>((new Response<>("Unauthorize", null, HttpStatus.UNAUTHORIZED.value())),
					HttpStatus.UNAUTHORIZED);
		}

		try {

			List<Company> companies = companyService.getAllCompanies();

			return new ResponseEntity<>(new CustomResponseDTO(companies, "Companies found", HttpStatus.OK.value()),
					HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();

			return new ResponseEntity<>(
					new CustomResponseDTO("Failed to fetch companies", HttpStatus.INTERNAL_SERVER_ERROR.value()),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/getByBuildingId")
	public ResponseEntity<?> getAllCompaniesByBuildingId(@RequestParam Integer buildingId) {

		try {

			Response<?> companies = this.companyService.getAllCompaniesByBuildingId(buildingId);

			return new ResponseEntity<>(companies, HttpStatus.valueOf(companies.getStatus()));

		} catch (Exception e) {

			e.printStackTrace();

			return new ResponseEntity<>(

					new CustomResponseDTO("Failed to fetch companies", HttpStatus.INTERNAL_SERVER_ERROR.value()),
					HttpStatus.INTERNAL_SERVER_ERROR);

		}

	}

	@PostMapping("/search")
	public ResponseEntity<?> searchCompany(@RequestBody SerachCompnyRequest searchResuest, HttpServletRequest request) {

		try {

			List<CompanyDTO> companies = companyService.searchCompany(searchResuest.getStateId().getId(),
					searchResuest.getCompanyName(), searchResuest.getIsActive());

			return new ResponseEntity<>(new CustomResponseDTO(companies, "Companies found", HttpStatus.OK.value()),
					HttpStatus.OK);

		} catch (Exception e) {

			return new ResponseEntity<>(
					new CustomResponseDTO("No companies Found", HttpStatus.INTERNAL_SERVER_ERROR.value()),
					HttpStatus.INTERNAL_SERVER_ERROR);

		}

	}

	@PostMapping("/update")
	public ResponseEntity<?> updateCompany(@RequestParam Integer id, @ModelAttribute @Valid CompanyDTO companyDTO,
			BindingResult bindingResult) {

		if (bindingResult.hasErrors()) {

			Map<String, String> errorMap = new HashMap<>();

			for (FieldError error : bindingResult.getFieldErrors()) {

				errorMap.put("error", error.getDefaultMessage());

			}
			errorMap.put("status", "400");

			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMap);

		}

		try {

			CompanyDTO existingCompany = this.companyService.getCompanyById(id);

			if (existingCompany != null) {

				Company companyByPhone = companyDao.findByPhone(companyDTO.getPhoneNumber());
				if (companyByPhone != null && !companyByPhone.getId().equals(existingCompany.getId())) {
//					Response<?> response = new Response<>("Company Phone already exists", null,
//							HttpStatus.BAD_REQUEST.value());
//					return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatus()));

					if (companyService.isPhoneNumberInUser(companyDTO.getPhoneNumber())) {

						throw new CompanyAlreadyExistsException("Company with the same PhoneNumber already exists");
					}
				}

				Company companyByEmail = companyDao.findByEmail(companyDTO.getEmail());
				if (companyByEmail != null && !companyByEmail.getId().equals(existingCompany.getId())) {
//					Response<?> response = new Response<>("Company Email already exists", null,
//							HttpStatus.BAD_REQUEST.value());
//					return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatus()));

					if (companyService.isEmailAlreadyInUse(companyDTO.getEmail())) {

						throw new CompanyAlreadyExistsException("Company with the same email already exists");
					}
				}

				Company companyByName = companyDao.findByCompanyName(companyDTO.getName());

				if (companyByName != null && !companyByName.getId().equals(existingCompany.getId())) {

					if (companyService.isCompanyNameInUse(companyDTO.getName())) {

						throw new CompanyAlreadyExistsException("Company with the same NAME already exists");
					}
				}

				CompanyDTO updatedCompany = this.companyService.updateCompany(id, companyDTO);

				CustomResponseDTO response = new CustomResponseDTO(updatedCompany, HttpStatus.OK.name(),
						HttpStatus.OK.value());

				return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatusCode()));
			} else {
				return new ResponseEntity<>(new CustomResponseDTO("Company not found", HttpStatus.NOT_FOUND.value()),
						HttpStatus.NOT_FOUND);
			}

		} catch (CompanyAlreadyExistsException ex) {

			return new ResponseEntity<>(new CustomResponseDTO(ex.getMessage(), HttpStatus.CONFLICT.value()),
					HttpStatus.CONFLICT);
		}

		catch (Exception e) {

			return new ResponseEntity<>(
					new CustomResponseDTO("Internal server error", HttpStatus.INTERNAL_SERVER_ERROR.value()),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@PostMapping("/active")
	public ResponseEntity<?> deactivateCompany(@RequestBody IsActiveDto activeDto, HttpServletRequest request) {

		String header = request.getHeader("Authorization");
		if (header == null) {
			return new ResponseEntity<>((new Response<>("Unauthorize", null, HttpStatus.UNAUTHORIZED.value())),
					HttpStatus.UNAUTHORIZED);
		}
		User user = authorization.authorizationForCompany(header.substring(7));
		if (user == null) {
			return new ResponseEntity<>((new Response<>("Unauthorize", null, HttpStatus.UNAUTHORIZED.value())),
					HttpStatus.UNAUTHORIZED);
		}

		Integer id = null;
		if (activeDto.getId() != null && activeDto.getIsActive() != null) {
			id = companyService.updateCompany(activeDto);
		}
		if (id != null && activeDto.getIsActive() == true) {

			return new ResponseEntity<>(new CustomResponseDTO("Company Activated ", HttpStatus.OK.value()),
					HttpStatus.OK);

		} else if (id != null)
			return new ResponseEntity<>(new CustomResponseDTO("Company Deactivated", HttpStatus.OK.value()),
					HttpStatus.OK);
		else {
			return new ResponseEntity<>(new CustomResponseDTO("Company Not found", HttpStatus.BAD_REQUEST.value()),
					HttpStatus.BAD_REQUEST);
		}

	}

	@GetMapping("/allStatus")
	public ResponseEntity<?> getAllStatusCompanies(HttpServletRequest request) {

		try {

			List<IsActiveDto> statusCompanies = companyService.getAllCompaniesStatus();

			return new ResponseEntity<>(
					new CustomResponseDTO(statusCompanies, "Companies found", HttpStatus.OK.value()), HttpStatus.OK);

		} catch (Exception e) {

			return new ResponseEntity<>(
					new CustomResponseDTO("Failed to Status of companies", HttpStatus.INTERNAL_SERVER_ERROR.value()),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@PostMapping("/upload")
	public ResponseEntity<?> uploadPostImage(@RequestParam("image") MultipartFile image,

			HttpServletRequest request) {

		String header = request.getHeader("Authorization");
		if (header == null) {
			return new ResponseEntity<>((new Response<>("Unauthorize", null, HttpStatus.UNAUTHORIZED.value())),
					HttpStatus.UNAUTHORIZED);
		}
		User user = authorization.authorizationForCompany(header.substring(7));
		if (user == null) {
			return new ResponseEntity<>((new Response<>("Unauthorize", null, HttpStatus.UNAUTHORIZED.value())),
					HttpStatus.UNAUTHORIZED);
		}

		try {

//			String fileName = this.fileService.uploadImage(path, image);
			String fileName = this.fileService.uploadImage(image);

			return new ResponseEntity<>(
					new CustomResponseDTO(fileName, "Image uploaded successfully", HttpStatus.OK.value()),
					HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(
					new CustomResponseDTO("Failed to upload image", HttpStatus.INTERNAL_SERVER_ERROR.value()),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping(value = "/image/{imageName}", produces = MediaType.IMAGE_JPEG_VALUE)
	public void downloadImage(@PathVariable("imageName") String imageName, HttpServletResponse response)
			throws IOException {

		try {
			InputStream resource = this.fileService.getResource(path, imageName);

			if (resource != null) {
				response.setContentType(MediaType.IMAGE_JPEG_VALUE);
				StreamUtils.copy(resource, response.getOutputStream());
			} else {
				response.setStatus(HttpServletResponse.SC_NOT_FOUND);
				response.getWriter().write("Image not found");
			}
		} catch (FileNotFoundException e) {
			// Handle the FileNotFoundException
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			response.getWriter().write("File Not Found Exception");
		} catch (Exception e) {
			e.printStackTrace();
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			response.getWriter().write("Error occurred while fetching the image");
		}

	}

	@PostMapping("/paginated")
	public ResponseEntity<?> getPaginatedCompanies(@RequestBody PaginationRequest paginationRequest) {

		try {
			CompanyPaginatedResponse allCompanies = companyService.getAllCompanies(paginationRequest);

			return new ResponseEntity<>(new CustomResponseDTO(allCompanies, "Companies found", HttpStatus.OK.value()),
					HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();

			return new ResponseEntity<>(
					new CustomResponseDTO("Failed to fetch companies", HttpStatus.INTERNAL_SERVER_ERROR.value()),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping("/exportcompanydata")
	public ResponseEntity<?> exportDataToExcel(@RequestBody PaginationRequest paginationRequest) {
	    try {
	        // Assuming excelExport is an instance of the class responsible for exporting data to Excel
	        Page<Company> allCompanies = companyDao.getAllCompanies(paginationRequest);

	        List<CompanyDTO> companies = allCompanies.getContent().stream().map(CompanyDTO::toCompanyDto)
	                .collect(Collectors.toList());

	        // Get the ByteArrayInputStream from the Excel export
	        ByteArrayInputStream excelStream = excelExport.companydataToExcel(companies);

	        // Convert to byte array
	        byte[] byteArray = IOUtils.toByteArray(excelStream);

	        // Close the ByteArrayInputStream
	        excelStream.close();

	        HttpHeaders headers = new HttpHeaders();
	        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
	        headers.setContentDispositionFormData("attachment", "company_data.xlsx");

	        return new ResponseEntity<>(byteArray, headers, HttpStatus.OK);

	    } catch (Exception e) {
	        e.printStackTrace();
	        return new ResponseEntity<>(
	                new CustomResponseDTO("Error in exporting Excel data", HttpStatus.BAD_REQUEST.value()),
	                HttpStatus.BAD_REQUEST);
	    }
	}


}