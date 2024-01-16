package com.app.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.app.dto.ChangePasswordDto;
import com.app.dto.IsActiveDto;
import com.app.dto.UserDto;
import com.app.entity.User;
import com.app.response.Response;



public interface UserService {

	Response<?> saveUser(UserDto userDto);

	List<UserDto> getallUser(User user);

	Response<?> deleteUser(IsActiveDto idIsactiveDTO);

	Response<?> changePassword(Object  data, ChangePasswordDto passwordDTO);

	Response<?> getUserByid(Integer id) throws Exception;

	User getUserById(Integer id);

	Response<?> getUsers(Integer companyId);

	Response<?> genarateOtp(String username);

	Response<?> forgotPassword(ChangePasswordDto changePasswordDto);

	Response<?> saveUsersByexcell(MultipartFile file, Integer companyId);

	Response<?> presentUser(IsActiveDto activeDto);

	Response<?> acessUser(IsActiveDto activeDto);

	Response<?> getallUserV2(User authorize);

	Response<?> saveUsersByexcellV2(MultipartFile file, Integer companyId);

	Response<?> getUsers2(Integer companyId, Integer buildingId);

}
