package com.vms2.service;

import java.util.List;

import com.vms2.dto.ForgotPasswordDTO;
import com.vms2.dto.IdIsactiveDTO;
import com.vms2.dto.UserDto;
import com.vms2.entity.CredentialMaster;
import com.vms2.entity.User;
import com.vms2.response.Response;

public interface UserService {

	Response<?> saveUser(UserDto userDto, User object);

	List<UserDto> getallUser(User user);

	Response<?> deleteUser(IdIsactiveDTO idIsactiveDTO);

	Response<?> forgotPassword(Object  data, ForgotPasswordDTO passwordDTO);

	Response<?> getUserByid(Integer id);


	Response<?> getUsers();

}
