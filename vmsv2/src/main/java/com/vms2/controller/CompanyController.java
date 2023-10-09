package com.vms2.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.vms2.authorize.Authorization;
import com.vms2.dto.CompanyDTO;
import com.vms2.dto.IdIsactiveDTO;
import com.vms2.entity.Company;
import com.vms2.entity.User;
import com.vms2.exception.CompanyAlreadyExistsException;
import com.vms2.exception.ConstraintViolationException;
import com.vms2.helper.Fileservice;
import com.vms2.response.Response;
import com.vms2.service.CompanyService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/comapany/")
public class CompanyController {

	@Autowired
	private CompanyService companyService;

	@Autowired
	private Authorization authorization;

	@Value("${project.image}")
	private String path;

	@PostMapping("/add")
	public ResponseEntity<Response<Company>> createCompany(@ModelAttribute @Valid CompanyDTO companyDTO,
			BindingResult bindingResult, HttpServletRequest request) {

		String header = request.getHeader("Authorization");
		if (header == null) {
			return new ResponseEntity<>((new Response<>("Unauthorize",null,HttpStatus.UNAUTHORIZED.value())),HttpStatus.UNAUTHORIZED);
		}
		User user = authorization.authorizetoaddCompany(header.substring(7));
		if (user == null) {
			return new ResponseEntity<>((new Response<>("Unauthorize",null,HttpStatus.UNAUTHORIZED.value())),HttpStatus.UNAUTHORIZED);
		}

		if (bindingResult.hasErrors()) {
			Map<String, String> errorMap = new HashMap<>();
			for (FieldError error : bindingResult.getFieldErrors()) {
				errorMap.put(error.getField(), error.getDefaultMessage());
			}
			return new ResponseEntity<>(new Response<>("Validation error", errorMap, HttpStatus.BAD_REQUEST.value()),
					HttpStatus.BAD_REQUEST);
		}

		try {
			Company savedCompany = companyService.saveCompany(companyDTO);
			return new ResponseEntity<>(
					new Response<>("Company created successfully", savedCompany, HttpStatus.OK.value()), HttpStatus.OK);
		} catch (CompanyAlreadyExistsException ex) {
			return new ResponseEntity<>(new Response<>(ex.getMessage(), null, HttpStatus.CONFLICT.value()),
					HttpStatus.CONFLICT);
		} catch (ConstraintViolationException ex) {
			return new ResponseEntity<>(new Response<>(ex.getMessage(), null, HttpStatus.CONFLICT.value()),
					HttpStatus.CONFLICT);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(
					new Response<>("Failed to create company", null, HttpStatus.BAD_REQUEST.value()),
					HttpStatus.BAD_REQUEST);
		}
	}

	@GetMapping("/get/{id}")
	public ResponseEntity<Response<Company>> getCompanyById(@PathVariable Integer id, HttpServletRequest request) {

		String header = request.getHeader("Authorization");
		if (header == null) {
			return new ResponseEntity<>((new Response<>("Unauthorize",null,HttpStatus.UNAUTHORIZED.value())),HttpStatus.UNAUTHORIZED);
		}
		User user = authorization.authorizetoaddCompany(header.substring(7));
		if (user == null) {
			return new ResponseEntity<>((new Response<>("Unauthorize",null,HttpStatus.UNAUTHORIZED.value())),HttpStatus.UNAUTHORIZED);
		}

		try {
			Company company = companyService.getCompanyById(id);
			if (company != null) {
				return new ResponseEntity<>(new Response<>("Company found", company, HttpStatus.OK.value()),
						HttpStatus.OK);
			} else {
				return new ResponseEntity<>(new Response<>("Company not found", null, HttpStatus.NOT_FOUND.value()),
						HttpStatus.NOT_FOUND);
			}
		} catch (Exception e) {
			return new ResponseEntity<>(
					new Response<>("Failed to fetch company", null, HttpStatus.INTERNAL_SERVER_ERROR.value()),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/all")
	public ResponseEntity<Response<List<Company>>> getAllCompanies(HttpServletRequest request) {

		String header = request.getHeader("Authorization");
		if (header == null) {
			return new ResponseEntity<>((new Response<>("Unauthorize",null,HttpStatus.UNAUTHORIZED.value())),HttpStatus.UNAUTHORIZED);
		}
		User user = authorization.authorizetoaddCompany(header.substring(7));
		if (user == null) {
			return new ResponseEntity<>((new Response<>("Unauthorize",null,HttpStatus.UNAUTHORIZED.value())),HttpStatus.UNAUTHORIZED);
		}

		try {
			List<Company> companies = companyService.getAllCompanies();
			return new ResponseEntity<>(new Response<>("Companies found", companies, HttpStatus.OK.value()),
					HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(
					new Response<>("Failed to fetch companies", null, HttpStatus.INTERNAL_SERVER_ERROR.value()),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	// Additional controller methods...

	@GetMapping("/search")
	public ResponseEntity<Response<List<Company>>> searchCompany(
			@RequestParam(value = "stateId", required = false) Integer stateId,
			@RequestParam(value = "cityId", required = false) Integer cityId,
			@RequestParam(required = false) String companyName, @RequestParam(required = false) Boolean isActive) {

		try {
			List<Company> companies = companyService.serachCompany(stateId, cityId, companyName, isActive);
			if (companies != null && !companies.isEmpty()) {
				return new ResponseEntity<>(new Response<>("Companies found", companies, HttpStatus.OK.value()),
						HttpStatus.OK);
			} else {
				return new ResponseEntity<>(new Response<>("No companies found", null, HttpStatus.NOT_FOUND.value()),
						HttpStatus.NOT_FOUND);
			}
		} catch (Exception e) {
			return new ResponseEntity<>(
					new Response<>("Failed to search companies", null, HttpStatus.INTERNAL_SERVER_ERROR.value()),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping("/update/{id}")
	public ResponseEntity<Response<Company>> updateCompany(@PathVariable Integer id,
			@ModelAttribute @Valid CompanyDTO companyDTO, BindingResult bindingResult, HttpServletRequest request) {

		String header = request.getHeader("Authorization");
		if (header == null) {
			return new ResponseEntity<>((new Response<>("Unauthorize",null,HttpStatus.UNAUTHORIZED.value())),HttpStatus.UNAUTHORIZED);
		}
		User user = authorization.authorizetoaddCompany(header.substring(7));
		if (user == null) {
			return new ResponseEntity<>((new Response<>("Unauthorize",null,HttpStatus.UNAUTHORIZED.value())),HttpStatus.UNAUTHORIZED);
		}

		if (bindingResult.hasErrors()) {
			Map<String, String> errorMap = new HashMap<>();
			for (FieldError error : bindingResult.getFieldErrors()) {
				errorMap.put(error.getField(), error.getDefaultMessage());
			}
			return new ResponseEntity<>(new Response<>("Validation error", errorMap, HttpStatus.BAD_REQUEST.value()),
					HttpStatus.BAD_REQUEST);
		}

		Company existingCompany = companyService.getCompanyById(id);

		Company company = companyService.convertToEntity(companyDTO);

		if (existingCompany != null) {
			Company updatedCompany = companyService.updateCompany(id, company);
			if (updatedCompany != null) {
				return new ResponseEntity<>(
						new Response<>("Company updated successfully", updatedCompany, HttpStatus.OK.value()),
						HttpStatus.OK);
			} else {
				return new ResponseEntity<>(
						new Response<>("Failed to update company", null, HttpStatus.INTERNAL_SERVER_ERROR.value()),
						HttpStatus.INTERNAL_SERVER_ERROR);
			}
		} else {
			return new ResponseEntity<>(new Response<>("Company not found", null, HttpStatus.BAD_REQUEST.value()),
					HttpStatus.BAD_REQUEST);
		}
	}

	@PostMapping("/active")
	public ResponseEntity<Response<String>> deactivateCompany(@RequestBody IdIsactiveDTO activeDto,
			HttpServletRequest request) {

		String header = request.getHeader("Authorization");
		if (header == null) {
			return new ResponseEntity<>((new Response<>("Unauthorize",null,HttpStatus.UNAUTHORIZED.value())),HttpStatus.UNAUTHORIZED);
		}
		User user = authorization.authorizetoaddCompany(header.substring(7));
		if (user == null) {
			return new ResponseEntity<>((new Response<>("Unauthorize",null,HttpStatus.UNAUTHORIZED.value())),HttpStatus.UNAUTHORIZED);
		}

		Integer id = null;
		if (activeDto.getId() != null && activeDto.getIsActive() != null) {
			id = companyService.updateCompany(activeDto);
			if (id != null && activeDto.getIsActive()) {
				return new ResponseEntity<>(new Response<>("Company Activated", null, HttpStatus.OK.value()),
						HttpStatus.OK);
			} else if (id != null) {
				return new ResponseEntity<>(new Response<>("Company Deactivated", null, HttpStatus.OK.value()),
						HttpStatus.OK);
			}
		}
		return new ResponseEntity<>(new Response<>("Company Not found", null, HttpStatus.BAD_REQUEST.value()),
				HttpStatus.BAD_REQUEST);
	}

//	@PostMapping("/upload")
//	public ResponseEntity<Response<String>> uploadPostImage(@RequestParam("image") MultipartFile image) {
//		try {
//			String fileName = Fileservice.uploadImage(path, image);
//			if (fileName != null) {
//				return new ResponseEntity<>(
//						new Response<>("Image uploaded successfully", fileName, HttpStatus.OK.value()), HttpStatus.OK);
//			} else {
//				return new ResponseEntity<>(
//						new Response<>("Failed to upload image", null, HttpStatus.INTERNAL_SERVER_ERROR.value()),
//						HttpStatus.INTERNAL_SERVER_ERROR);
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//			return new ResponseEntity<>(
//					new Response<>("Failed to upload image", null, HttpStatus.INTERNAL_SERVER_ERROR.value()),
//					HttpStatus.INTERNAL_SERVER_ERROR);
//		}
//	}
//
//	@GetMapping(value = "/image/{imageName}", produces = MediaType.IMAGE_JPEG_VALUE)
//	public void downloadImage(@PathVariable("imageName") String imageName, HttpServletResponse response)
//			throws IOException {
//
//		try {
//			InputStream resource = Fileservice.getResource(path, imageName);
//
//			if (resource != null) {
//				response.setContentType(MediaType.IMAGE_JPEG_VALUE);
//
//				StreamUtils.copy(resource, response.getOutputStream());
//
//			} else {
//
//				response.setStatus(HttpServletResponse.SC_NOT_FOUND);
//
//				response.getWriter().write("Image not found");
//
//			}
//
//		} catch (Exception e) {
//
//			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
//
//			response.getWriter().write("Error occurred while fetching the image");
//		}
//
//	}
//
}
