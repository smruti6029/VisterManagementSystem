package com.vms2.service;

import java.util.List;

import com.vms2.dto.ForgotPasswordDTO;
import com.vms2.dto.UserDto;
import com.vms2.entity.CredentialMaster;
import com.vms2.response.Response;

public interface UserService {

	Response<?> saveUser(UserDto userDto);

	List<UserDto> getallUser();

	Response<?> deleteUser(Integer id);

	Response<?> forgotPassword(Object  data, ForgotPasswordDTO passwordDTO);

}
