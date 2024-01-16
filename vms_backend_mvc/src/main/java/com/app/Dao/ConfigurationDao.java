package com.app.Dao;

import com.app.entity.Configuration;

public interface ConfigurationDao {
	
	public Configuration getByKey(String schedulerKey);
}
