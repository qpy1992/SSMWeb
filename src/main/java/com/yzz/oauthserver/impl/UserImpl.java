package com.yzz.oauthserver.impl;

import javax.annotation.Resource;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.yzz.oauthserver.bean.User;
import com.yzz.oauthserver.dao.UserDao;
import com.yzz.oauthserver.services.Cache;
import com.yzz.oauthserver.services.UserService;
@Service("userService")
public class UserImpl implements UserService{
	private Cache cache;
	@Resource
	private UserDao userDao;
	
	public User Login(String userName, String userPassword) {
		// TODO Auto-generated method stub
		
		return userDao.login(userName, userPassword);
	}

}
