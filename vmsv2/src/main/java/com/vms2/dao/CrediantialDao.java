package com.vms2.dao;

import com.vms2.entity.CredentialMaster;
import com.vms2.entity.User;

public interface CrediantialDao {

	Integer saveCrediantial(CredentialMaster credentialMaster);

	CredentialMaster getUsername(String username);

	Integer updatePassword(CredentialMaster user);

}
