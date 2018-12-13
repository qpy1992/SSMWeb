package com.yzz.oauthserver.impl;

import org.apache.oltu.oauth2.as.issuer.MD5Generator;
import org.apache.oltu.oauth2.as.issuer.OAuthIssuerImpl;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.yzz.oauthserver.bean.User;
import com.yzz.oauthserver.services.Cache;
@Service

public class CacheImpl implements Cache{
	
	@Cacheable(value="userCache",key="#code")
	public User setUser(String code, User user) {
		// TODO Auto-generated method stub
		return user;
	}
	@Cacheable(value="userCache",key="#code")
	public User getUser(String code){
		
		return null;
	}
	

	
}
