package com.vms2.service;

import com.vms2.dto.UserDto;
import com.vms2.response.Response;

public interface CredentialService {

	Response<?> adduserInCrediantial(UserDto userDto);

}
