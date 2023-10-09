package com.vms2.service;

import com.vms2.response.JwtRequest;
import com.vms2.response.JwtResponse;
import com.vms2.response.Response;

public interface UserDataService {

	Response<?> generateToken(JwtRequest jwtRequest) throws Exception;


}
