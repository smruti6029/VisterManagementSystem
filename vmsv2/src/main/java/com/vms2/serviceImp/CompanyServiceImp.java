package com.vms2.serviceImp;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.vms2.dao.CityDao;
import com.vms2.dao.CompanyRepo;
import com.vms2.dao.StateDao;
import com.vms2.dto.CompanyDTO;
import com.vms2.dto.IdIsactiveDTO;
import com.vms2.entity.City;
import com.vms2.entity.Company;
import com.vms2.entity.State;
import com.vms2.exception.CompanyAlreadyExistsException;
import com.vms2.helper.Fileservice;
import com.vms2.service.CompanyService;

@Service
public class CompanyServiceImp implements CompanyService {

	@Autowired
	private CompanyRepo companyRepo;

	@Autowired
	private StateDao stateRepo;

	@Autowired
	private CityDao cityRepo;

	@Value("${project.image}")
	private String path;

	public List<Company> getAllCompanies() {

		return companyRepo.getAllCompanies();
	}

	@Override

	public Company getCompanyById(Integer id) {

		return companyRepo.getCompanyById(id);
	}

	@Override
	public Company saveCompany(CompanyDTO companydto) {

		Company company = convertToEntity(companydto);

		return companyRepo.saveCompany(company);
	}

	@Override
	public Company updateCompany(Integer companyId, Company updatedCompany) {

		return companyRepo.updateCompany(companyId, updatedCompany);
	}

	@Override
	public Company updateCompany(Integer companyId) {

		return companyRepo.updateCompany(companyId);
	}

	@Override
	public Integer updateCompany(IdIsactiveDTO activeDto) {

		return companyRepo.updateActiveCompany(activeDto);

	}

	@Override
	public Company convertToEntity(CompanyDTO companyDTO) {

		Company company = new Company();

		State state = stateRepo.getStateById(companyDTO.getState());

		City city = cityRepo.getCityById(companyDTO.getCity());

		List<Company> existingCompanies = companyRepo.getCompanyByStateAndCity(state, city);

		if (existingCompanies != null && !existingCompanies.isEmpty()) {
			for (Company existingCompany : existingCompanies) {

				if (existingCompany.getName().equals(companyDTO.getName())) {

					throw new CompanyAlreadyExistsException(
							"A company with the same name, state, and city combination already exists.");
				}
			}
		}

		company.setName(companyDTO.getName());
		company.setEmail(companyDTO.getEmail());
		company.setAddress(companyDTO.getAddress());
		company.setCity(city);
		company.setState(state);
		company.setPincode(companyDTO.getPincode());
		company.setPhoneNumber(companyDTO.getPhoneNumber());
		company.setIndustry(companyDTO.getIndustry());
		company.setAboutUs(companyDTO.getAboutUs());

		MultipartFile image = companyDTO.getImage();

		if (!image.isEmpty()) {

			String fileName = null;

			try {
				fileName = Fileservice.uploadImage(path, image);
			} catch (IOException e) {

				e.printStackTrace();
			}

			company.setLogo(fileName);

		}

		return company;
	}

	@Override
	public void isActiveCompany(Integer id) {

		Company companyById = companyRepo.getCompanyById(id);

		if (companyById != null) {
			companyById.setActive(false);
			companyRepo.saveCompany(companyById);

		}

	}

	@Override
	public List<Company> serachCompany(Integer stateId, Integer cityId, String companyName, Boolean isActive) {

		return companyRepo.serachCompany(stateId, cityId, companyName, isActive);
	}

	@Override
	public boolean isEmailAlreadyInUse(String email) {

		Company existingCompany = companyRepo.findByEmail(email);

		return existingCompany != null;
	}

}
