package com.yzz.oauthserver.impl;

import javax.annotation.Resource;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.yzz.oauthserver.dao.ClientDao;
import com.yzz.oauthserver.services.ClientService;
@Service("clientService")
public class ClientImpl implements ClientService{
	@Resource
	private ClientDao clientDao;
	
	//@Cacheable(value="check")
	public int check(String clientId,String clientSecret) {
		// TODO Auto-generated method stub
		
		return clientDao.check(clientId,clientSecret);
	}
	
	//@Cacheable(value="cacheClient")
	public int checkClient(String clientId) {
		// TODO Auto-generated method stub
		return clientDao.checkClient(clientId);
	}
	

}
