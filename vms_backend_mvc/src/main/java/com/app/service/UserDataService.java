package com.app.service;

import com.app.response.JwtRequest;
import com.app.response.JwtResponse;

public interface UserDataService {

	JwtResponse generateToken(JwtRequest jwtRequest) throws Exception;


}
