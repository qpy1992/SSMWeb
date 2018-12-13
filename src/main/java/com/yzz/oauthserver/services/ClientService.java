package com.yzz.oauthserver.services;

public interface ClientService {
	
	public int check(String clientId,String clientSecret);
	
	public int checkClient(String clientId);
	
}
