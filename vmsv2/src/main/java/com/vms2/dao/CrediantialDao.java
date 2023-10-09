package com.vms2.dao;

import com.vms2.entity.CredentialMaster;

public interface CrediantialDao {

	Integer saveCrediantial(CredentialMaster credentialMaster);

	CredentialMaster getUsername(String username);

	Integer update(CredentialMaster user);

	CredentialMaster getcrediantialByuser(Integer id);

}
