package com.yzz.oauthserver.services;



import com.yzz.oauthserver.bean.User;

public interface UserService {
	
	public User Login(String userName,String password);
}
