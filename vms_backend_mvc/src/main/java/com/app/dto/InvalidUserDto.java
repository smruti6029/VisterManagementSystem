package com.app.dto;

import java.util.ArrayList;
import java.util.List;

public class InvalidUserDto {
	
	
	 public String firstName;
	 public String lastName;
	 public String phone;
	 public String gender;
	 public String email;
	 public  String dob;
	 public String govt_id;
	 public String empCode;
	 public String pincode;
	 public String role;
	 public String state;
	 public String city;
	 public String department;
	
	 
	 public String permission;
	 
	 

    // Default constructor
    public InvalidUserDto() {
    }

    // Getter and Setter methods for each field
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getEmpCode() {
        return empCode;
    }

    public void setEmpCode(String empCode) {
        this.empCode = empCode;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }
    
    

    public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getGovt_id() {
		return govt_id;
	}

	public void setGovt_id(String govt_id) {
		this.govt_id = govt_id;
	}

	public String getPermission() {
		return permission;
	}

	public void setPermission(String permission) {
		this.permission = permission;
	}

	public static List<InvalidUserDto> convertToInvalidUserDtoList(List<UserDto> userDtoList) {
        List<InvalidUserDto> invalidUserDtoList = new ArrayList<>();

        for (UserDto userDto : userDtoList) {
            InvalidUserDto invalidUserDto = new InvalidUserDto();

            invalidUserDto.setFirstName(userDto.getFirstName() != null ? userDto.getFirstName() : null);
            invalidUserDto.setLastName(userDto.getLastName() != null ? userDto.getLastName() : null);
            invalidUserDto.setPhone(userDto.getPhone() != null ? userDto.getPhone() : null);
            invalidUserDto.setEmail(userDto.getEmail() != null ? userDto.getEmail() : null);
            invalidUserDto.setDob(userDto.getDob() != null ? userDto.getDob() : null);
            invalidUserDto.setEmpCode(userDto.getEmpCode() != null ? userDto.getEmpCode() : null);
            invalidUserDto.setPincode(userDto.getPincode() != null ? userDto.getPincode() : null);
            invalidUserDto.setRole(userDto.getRole() != null ? userDto.getRole().getName() : null);
            invalidUserDto.setState(userDto.getState() != null ? userDto.getState().getName() : null);
            invalidUserDto.setCity(userDto.getCity() != null ? userDto.getCity().getName() : null);
            invalidUserDto.setDepartment(userDto.getDepartmentDto() != null ? userDto.getDepartmentDto().getName() : null);
            invalidUserDto.setGovt_id(userDto.getGovtId());
            invalidUserDto.setGender(userDto.getGender());
            
            if (userDto != null) {
                Boolean isPermission = userDto.getIsPermission();

                if (isPermission != null) {
                    if (isPermission) {
                        invalidUserDto.setPermission("YES");
                    } else {
                        invalidUserDto.setPermission("NO");
                    }
                } else {
                    // Handle the case where isPermission is null
                    // For example, set a default value or perform some other action
                }}
            

            invalidUserDtoList.add(invalidUserDto);
        }

        return invalidUserDtoList;
    }


    

}