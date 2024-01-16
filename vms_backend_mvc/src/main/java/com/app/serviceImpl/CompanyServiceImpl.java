package com.app.serviceImpl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.app.Dao.BuildingDao;
import com.app.Dao.CityDao;
import com.app.Dao.CompanyDao;
import com.app.Dao.DepartmentDao;
import com.app.Dao.StateDao;
import com.app.dto.CompanyDTO;
import com.app.dto.CompanyPaginatedResponse;
import com.app.dto.IsActiveDto;
import com.app.dto.PaginationRequest;
import com.app.entity.Building;
import com.app.entity.Company;
import com.app.entity.Department;
import com.app.exception.CompanyAlreadyExistsException;
import com.app.response.Response;
import com.app.service.CompanyService;
import com.app.util.ExcelExport;

@Service
@Transactional
public class CompanyServiceImpl implements CompanyService {

	@Autowired
	private CompanyDao companyDao;

	@Autowired
	private StateDao stateRepo;

	@Autowired
	private CityDao cityRepo;

	@Autowired
	private FileServiceImpl fileService;

	@Autowired
	private BuildingDao buildingDao;

	@Autowired
	private DepartmentDao departmentDao;

	@Autowired
	private ExcelExport excelExport;

	@Value("${project.image}")
	private String path;

	@Override
	public CompanyDTO saveCompany(CompanyDTO companydto) {

		Company company = convertToEntity(companydto);

		Company saveCompany = companyDao.saveCompany(company);

		Company companyFromdb = companyDao.findByEmail(company.getEmail());

		Department department = new Department();
		department.setCompany(companyFromdb);
		department.setIsActive(true);
		department.setName("ADMIN");
		department.setUpdatedOn(new Date());
		department.setCreatedOn(new Date());

		departmentDao.save(department);

		return convertToDto(saveCompany);
	}

	@Override
	public CompanyDTO getCompanyById(Integer id) {

		Company companyById = companyDao.getCompanyById(id);

		return convertToDto(companyById);
	}

	@Override
	public CompanyPaginatedResponse getAllCompanies(PaginationRequest request) {

		Page<Company> allCompanies = companyDao.getAllCompanies(request);

		List<CompanyDTO> companies = allCompanies.getContent().stream().map(CompanyDTO::toCompanyDto) // Assuming you
				.collect(Collectors.toList());

//		try
//		{
//		ByteArrayInputStream datatoexcel = excelExport.companydataToExcel(companies);
//		}catch (Exception e) {
//			// TODO: handle exception
//		}

		CompanyPaginatedResponse obj = new CompanyPaginatedResponse(allCompanies.getSize(),
				allCompanies.getTotalElements(), allCompanies.getTotalPages(), companies);

		return obj;
	}

	@Override
	public List<Company> getAllCompanies() {

		List<Company> allCompanies = companyDao.getAllCompanies();

		System.out.println(allCompanies.size() + "Size");

//		List<CompanyDTO> companyDTOs = new ArrayList<>();
//
//		for (Company company : allCompanies) {
//			CompanyDTO companyDto = new CompanyDTO();
//			companyDto.setId(company.getId());
//			companyDto.setName(company.getName());
//			companyDto.setEmail(company.getEmail());
//			companyDto.setAddress(company.getAddress());
//			companyDto.setState(company.getState().getId());
//			companyDto.setCity(company.getCity().getId());
//			companyDto.setPincode(company.getPincode());
//			companyDto.setPhoneNumber(company.getPhoneNumber());
//			companyDto.setIndustry(company.getIndustry());
//			companyDto.setAboutUs(company.getAboutUs());
//			companyDTOs.add(companyDto);

		Collections.sort(allCompanies, Comparator.comparing(Company::getCreatedOn).reversed());

		return allCompanies;
	}

//	@Override
//	public Response<?> getAllCompaniesByBuildingId(Integer buildingId) {
//
//		// List<Company> companyByBuildingId =
//		// companyDao.getAllCompaniesByBuildingId(buildingId);
//
//		List<CompanyDTO> companyByBuildingId1 = companyDao.getAllCompaniesByBuildingId(buildingId).stream().
//
//				map(CompanyDTO::toCompanyDto).filter(Company -> Company.getIsActive()).collect(Collectors.toList());
//
//	
//		
//
//		if (companyByBuildingId1 != null) {
//
////			List<GetCompanyDto> dto = companyByBuildingId1.stream().map(x -> {
////
////				GetCompanyDto obj = new GetCompanyDto();
////				
////				obj.setName(x.getName());
////				obj.setLogo(x.getLogo());
////
////				return obj;
////			}).collect(Collectors.toList());
//			return new Response<>("All Companies By Building Id", companyByBuildingId1, HttpStatus.OK.value());
//		} else {
//			return new Response<>("No Companies  found By Building Id", null, HttpStatus.BAD_REQUEST.value());
//
//		}
//
//	}

//	@Override
//	public Response<?> getAllCompaniesByBuildingId(Integer buildingId) {
//		List<CompanyDTO> companyList = companyDao.getAllCompaniesByBuildingId(buildingId).stream()
//				.map(CompanyDTO::toCompanyDto).filter(Company -> Company.getIsActive()).collect(Collectors.toList());
//
//		List<CompanyDTO> uniqueCompanies = new ArrayList<>();
//
//		for (CompanyDTO company : companyList) {
//			boolean isDuplicate = false;
//
//			for (CompanyDTO uniqueCompany : uniqueCompanies) {
//				if ((company.getName().equals(uniqueCompany.getName()))) {
//					isDuplicate = true;
//					break;
//				}
//			}
//
//			if (!isDuplicate) {
//				uniqueCompanies.add(company);
//			}
//		}
//
//		if (!uniqueCompanies.isEmpty()) {
//			return new Response<>("All Companies By Building Id", uniqueCompanies, HttpStatus.OK.value());
//		} else {
//			return new Response<>("No Companies found By Building Id", null, HttpStatus.BAD_REQUEST.value());
//		}
//	}

	@Override
	public Response<?> getAllCompaniesByBuildingId(Integer buildingId) {

		Building buildingById = buildingDao.getBuildingById(buildingId);
		if (buildingById != null) {

			List<CompanyDTO> companyList = companyDao.getAllCompaniesByBuildingId(buildingId).stream()
					.map(CompanyDTO::toCompanyDto).filter(CompanyDTO::getIsActive).collect(Collectors.toList());

			if (!companyList.isEmpty()) {
				List<CompanyDTO> uniqueCompanies = new ArrayList<>();

				for (CompanyDTO company : companyList) {
					boolean isDuplicate = false;

					for (CompanyDTO uniqueCompany : uniqueCompanies) {
						if ((company.getName().equals(uniqueCompany.getName()))) {
							isDuplicate = true;
							break;
						}
					}

					if (!isDuplicate) {
						uniqueCompanies.add(company);
					}
				}

				return new Response<>("All Companies By Building Id", uniqueCompanies, HttpStatus.OK.value());
			} else {
				return new Response<>("No Companies found By Building Id", null, HttpStatus.OK.value());
			}
		} else {
			return new Response<>("No Building found", null, 400);
		}
	}

	public Company convertToEntity(CompanyDTO companyDTO) {

		Company company = new Company();

//		State state = new State(companyDTO.getState());
//
//		City city = new City(companyDTO.getCity());
//
		List<Company> existingCompanies = companyDao.getCompanyByStateAndCity(companyDTO.getState(),
				companyDTO.getCity());

		if (existingCompanies != null && !existingCompanies.isEmpty()) {
			for (Company existingCompany : existingCompanies) {

				if (existingCompany.getName().equals(companyDTO.getName())) {

					throw new CompanyAlreadyExistsException(
							"A company with the same name, state, and city combination already exists.");
				}
			}
		}

		List<Company> existingCompaniesWithNameAndBuilding = companyDao
				.getCompanyByNameAndBuilding(companyDTO.getName(), companyDTO.getBuilding().getBuildingId());

		if (existingCompaniesWithNameAndBuilding != null && !existingCompaniesWithNameAndBuilding.isEmpty()) {
			throw new CompanyAlreadyExistsException(
					"A company with the same name and buildingId combination already exists.");
		}

		company.setName(companyDTO.getName().trim());
		company.setEmail(companyDTO.getEmail().trim());
		company.setAddress(companyDTO.getAddress().trim());
		company.setState(companyDTO.getState());
		company.setCity(companyDTO.getCity());
		company.setPincode(companyDTO.getPincode());
		company.setPhoneNumber(companyDTO.getPhoneNumber());
		company.setIndustry(companyDTO.getIndustry().trim());
		company.setAboutUs(companyDTO.getAboutUs().trim());
		company.setUserLimit(companyDTO.getUserLimit());
		company.setBuilding(companyDTO.getBuilding());

		if (!companyDTO.getImage().isEmpty()) {
			MultipartFile image = companyDTO.getImage();
			String fileName = null;

			try {
				fileName = fileService.uploadImage(image);

			} catch (Exception e) {

				e.printStackTrace();
			}

			company.setLogo(fileName);

		}

		return company;
	}

	private CompanyDTO convertToDto(Company company) {
		CompanyDTO companyDto = new CompanyDTO();

		if (company == null) {
			return null;
		}

		companyDto.setId(company.getId());
		companyDto.setName(company.getName().trim());
		companyDto.setEmail(company.getEmail().trim());
		companyDto.setAddress(company.getAddress().trim());
		companyDto.setState(company.getState());
		companyDto.setCity(company.getCity());
		companyDto.setPincode(company.getPincode());
		companyDto.setPhoneNumber(company.getPhoneNumber().trim());
		companyDto.setIndustry(company.getIndustry().trim());
		companyDto.setAboutUs(company.getAboutUs().trim());
		companyDto.setLogo(company.getLogo());
		companyDto.setUserLimit(company.getUserLimit());
		if (company.getBuilding().getBuildingId() != null) {
			companyDto.setBuilding(company.getBuilding());

		}
		return companyDto;
	}

	private CompanyDTO convertToDto1(Company company) {
		CompanyDTO companyDto = new CompanyDTO();
		companyDto.setId(company.getId());
		companyDto.setName(company.getName());
		companyDto.setEmail(company.getEmail());
		companyDto.setAddress(company.getAddress());
		companyDto.setState(company.getState());
		companyDto.setCity(company.getCity());
		companyDto.setPincode(company.getPincode());
		companyDto.setPhoneNumber(company.getPhoneNumber());
		companyDto.setIndustry(company.getIndustry());
		companyDto.setAboutUs(company.getAboutUs());
		companyDto.setLogo(company.getLogo());
		companyDto.setUserLimit(company.getUserLimit());
		companyDto.setBuilding(company.getBuilding());
		return companyDto;
	}

	@Override
	public CompanyDTO updateCompany(Integer id, CompanyDTO companyDTO) {

		Company company = companyDao.getCompanyById(id);

		Company company2 = companyDao.findByEmail(companyDTO.getEmail());

		if (company != null) {
			if (!Objects.equals(company.getName(), companyDTO.getName())) {
				company.setName(companyDTO.getName());
			}

			if (!Objects.equals(company.getEmail(), companyDTO.getEmail())) {
				company.setEmail(companyDTO.getEmail());
			}
			if (!Objects.equals(company.getAddress(), companyDTO.getAddress())) {
				company.setAddress(companyDTO.getAddress());
			}

			if (!Objects.equals(company.getState(), companyDTO.getState())) {
				company.setState(companyDTO.getState());
			}
			if (!Objects.equals(company.getCity(), companyDTO.getCity())) {
				company.setCity(companyDTO.getCity());
			}
			if (!Objects.equals(company.getPincode(), companyDTO.getPincode())) {
				company.setPincode(companyDTO.getPincode());
			}
			if (!Objects.equals(company.getPhoneNumber(), companyDTO.getPhoneNumber())) {
				company.setPhoneNumber(companyDTO.getPhoneNumber());
			}

			if (!Objects.equals(company.getBuilding(), companyDTO.getBuilding())) {
				company.setBuilding(companyDTO.getBuilding());
			}
			if (!Objects.equals(company.getUserLimit(), companyDTO.getUserLimit())) {
				company.setUserLimit(companyDTO.getUserLimit());
			}

			MultipartFile image = companyDTO.getImage();
			if (image != null) {
				try {
					String fileName = fileService.uploadImage(image);
					company.setLogo(fileName);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			Company updateCompany = companyDao.updateCompany(company);

			return convertToDto(updateCompany);
		}

		return null;
	}

	@Override
	public void isActiveCompany(Integer id) {

		Company companyById = companyDao.getCompanyById(id);

		if (companyById != null) {
			companyById.setActive(false);
			companyDao.saveCompany(companyById);

		}

	}

	@Override
	public List<CompanyDTO> searchCompany(Integer stateId, String companyName, Boolean isActive) {

		List<Company> searchCompany = companyDao.searchCompany(stateId, companyName, isActive);

		List<CompanyDTO> companyDTOs = new ArrayList<>();

		searchCompany.forEach(x -> {

			try {

				CompanyDTO companyDto = CompanyDTO.toCompanyDto(x);

				companyDTOs.add(companyDto);

			} catch (Exception e) {
				e.printStackTrace();

			}

		});

		return companyDTOs;

	}

	@Override
	public boolean isEmailAlreadyInUse(String email) {

		Company existingCompany = companyDao.findByEmail(email);

		return existingCompany != null;
	}

	@Override
	public boolean isPhoneNumberInUser(String phone) {

		Company existingCompany = companyDao.findByPhone(phone);

		return existingCompany != null;
	}

	@Override
	public boolean isCompanyNameInUse(String Name) {

		Company existingCompany = companyDao.findByCompanyName(Name);

		return existingCompany != null;

	}

	@Override
	public Integer updateCompany(IsActiveDto activeDto) {

		return companyDao.updateActiveCompany(activeDto);

	}

	@Override
	public List<IsActiveDto> getAllCompaniesStatus() {

		List<Object[]> results = companyDao.findAllCompaniesStatus();
		List<IsActiveDto> isActiveDtos = new ArrayList<>();

		for (Object[] result : results) {
			IsActiveDto isActiveDto = new IsActiveDto();
			isActiveDto.setId((Integer) result[0]);
			isActiveDto.setIsActive((Boolean) result[1]);
			isActiveDtos.add(isActiveDto);
		}

		return isActiveDtos;
	}

	@Override
	public boolean existsByNameAndStateIdAndCityId(String name, Integer state, Integer city) {

		return companyDao.existsByNameAndStateIdAndCityId(name, state, city);
	}

	@Override
	public Response<?> getCompanyByphone(CompanyDTO companyDTO) {
		// Assuming getcompanyByphoneWithBuildingID returns some data
		Company companyData = companyDao.getcompanyByphoneWithBuildingID(companyDTO);

		if (companyData != null) {

			System.out.println("Inside Company Data ");
			// If data is found, return a success response
			return new Response<>("Mobile Number Already exists", null, HttpStatus.CONFLICT.value());
		} else {
			return new Response<>("No Data Found", null, HttpStatus.OK.value());
		}
	}

}