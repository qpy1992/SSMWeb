package com.yzz.oauthserver.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.yzz.oauthserver.bean.User;
import com.yzz.oauthserver.services.UserService;

@Controller
public class UserController {
	
	private UserService userDao;
	@RequestMapping("/Login")
	public String Login(){
		
		return "";
	}
}
