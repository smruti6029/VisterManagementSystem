package com.app.Dao;

import com.app.entity.CredentialMaster;

public interface CrediantialDao {

	Integer saveCrediantial(CredentialMaster credentialMaster);

	CredentialMaster getUsername(String username);

	Integer updatePassword(CredentialMaster user);

	void clearOtp();

}
