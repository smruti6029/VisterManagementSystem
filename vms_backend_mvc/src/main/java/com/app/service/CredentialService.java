package com.app.service;

import com.app.dto.UserDto;
import com.app.response.Response;

public interface CredentialService {

	Response<?> adduserInCrediantial(UserDto userDto);

}
