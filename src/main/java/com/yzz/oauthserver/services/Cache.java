package com.yzz.oauthserver.services;

import com.sun.javafx.collections.MappingChange.Map;
import com.yzz.oauthserver.bean.User;

public interface Cache {
	
	public User setUser(String code,User user);
	
	public User getUser(String code);
}
