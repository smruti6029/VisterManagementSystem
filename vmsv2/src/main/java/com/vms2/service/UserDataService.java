package com.vms2.service;

import com.vms2.response.JwtRequest;
import com.vms2.response.JwtResponse;

public interface UserDataService {

	JwtResponse generateToken(JwtRequest jwtRequest) throws Exception;


}
