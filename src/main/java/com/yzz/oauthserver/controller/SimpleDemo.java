package com.yzz.oauthserver.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.yzz.oauthserver.tool.MD5Util;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.oltu.oauth2.as.issuer.MD5Generator;
import org.apache.oltu.oauth2.as.issuer.OAuthIssuer;
import org.apache.oltu.oauth2.as.issuer.OAuthIssuerImpl;
import org.apache.oltu.oauth2.as.request.OAuthAuthzRequest;
import org.apache.oltu.oauth2.as.request.OAuthTokenRequest;
import org.apache.oltu.oauth2.as.response.OAuthASResponse;
import org.apache.oltu.oauth2.common.error.OAuthError;
import org.apache.oltu.oauth2.common.exception.OAuthProblemException;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.apache.oltu.oauth2.common.message.OAuthResponse;
import org.apache.oltu.oauth2.common.message.types.GrantType;
import org.apache.oltu.oauth2.common.message.types.ParameterStyle;
import org.apache.oltu.oauth2.common.message.types.ResponseType;
import org.apache.oltu.oauth2.rs.request.OAuthAccessResourceRequest;
import org.apache.oltu.oauth2.rs.response.OAuthRSResponse;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import com.yzz.oauthserver.bean.User;
import com.yzz.oauthserver.services.Cache;
import com.yzz.oauthserver.services.ClientService;
import com.yzz.oauthserver.services.UserService;
import com.yzz.oauthserver.tool.Constants;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

@RequestMapping({"/simpleDemo"})
@Controller
public class SimpleDemo
{
 @Resource
  private ClientService client;
  @Resource	
  private UserService userService;
  
  @Resource
  private Cache cache = null;

  
//1、接收客户端的请求，对部分参数进行验证，验证通过后产生Code,回调客户端请求中的回调路径
  @RequestMapping("/checkClientId")
	public ModelAndView checkClientId(HttpServletRequest request){
	  ModelAndView m=new ModelAndView();
		OAuthAuthzRequest oauthRequest;
		String responseType=null,redirectUri=null,clientId=null;
		try {
			oauthRequest = new OAuthAuthzRequest(request);
			
			responseType=oauthRequest.getResponseType();
			redirectUri=oauthRequest.getRedirectURI();
			System.out.println(redirectUri);
			clientId=oauthRequest.getClientId();
			
		
			//数据库查询client_id判断。
			if (client.checkClient(clientId)<1) {
				
	            OAuthResponse response =OAuthASResponse.errorResponse(HttpServletResponse.SC_BAD_REQUEST)
	                            .setError(OAuthError.TokenResponse.INVALID_CLIENT)
	                            .setErrorDescription(Constants.INVALID_CLIENT_DESCRIPTION)
	                            .buildJSONMessage();
	            //定向到客户端的错误页面，待补充
	            return new ModelAndView("https://open.bot.tmall.com/oauth/callback");
			}
			
		} catch (OAuthSystemException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (OAuthProblemException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//跳转到用户登录页面
        ModelAndView mv = new ModelAndView("login1","url","createCode?response_type="+responseType+"&client_id="+clientId);
		mv.addObject("redirect_uri",redirectUri);
		return mv;
		
	}
  @RequestMapping(value="/createCode",produces = "text/html;charset=UTF-8")
  public String createCode(HttpServletRequest request) {
    
    try
    {
    	String userName=request.getParameter("userName");
    	String userPassword= MD5Util.getMd5(request.getParameter("userPassword"));
    	System.out.println(request.getParameter("redirect_uri"));
        String redirectURI = request.getParameter("redirect_uri");
    	User user=userService.Login(userName, userPassword);
    	if(user!=null){
    		//登录成功
    		OAuthAuthzRequest oauthRequest = new OAuthAuthzRequest(request);
        	String authorizationCode = null;
        	String responseType = oauthRequest.getParam("response_type");
            if (responseType.equals(ResponseType.CODE.toString())){
            	OAuthIssuerImpl oauthIssuerImpl = new OAuthIssuerImpl(new MD5Generator());
            	authorizationCode = oauthIssuerImpl.authorizationCode();
            	System.out.println("生成的code是:" + authorizationCode);
              
              //将用户信息缓存到code中
              this.cache.setUser(authorizationCode, user);
            }else{
            	System.out.println(user.getUserName());
            	//类型错误
            	return "error";
            }

            OAuthASResponse.OAuthAuthorizationResponseBuilder builder = OAuthASResponse.authorizationResponse(request, 302);

            builder.setCode(authorizationCode);

//            String redirectURI = oauthRequest.getParam("redirect_uri");

            System.out.println("构建响应,回调地址是:"+redirectURI);

            OAuthResponse response = builder.location(redirectURI).buildQueryMessage();

            String responseUri = response.getLocationUri();
            System.out.println("服务端重定向到客户端的路径：" + responseUri);
            return "redirect:" + responseUri;
    	}else{
    		System.out.println("账号密码错误");
    		request.setAttribute("msg", "账号密码错误");
    		//账号密码错误
    		return "error";
    	}
    }
    catch (OAuthSystemException e) {
      e.printStackTrace();
    }
    catch (OAuthProblemException e) {
      e.printStackTrace();
    }
    return null;
  }

//2、接收客户端的请求，创建资源请求许可
  @RequestMapping({"/createAccessToken"})
  public HttpEntity createAccessToken(HttpServletRequest request)
  {
    System.out.println("进入创建createAccessToken部分");
    try
    {
      OAuthTokenRequest oauthRequest = new OAuthTokenRequest(request);
      OAuthResponse response;
     
      if (client.check(oauthRequest.getClientId(),oauthRequest.getClientSecret())<1) {
        System.out.println("clientId或ClientSecret不对，挂了");
        response = 
          OAuthASResponse.errorResponse(401)
          .setError("unauthorized_client")
          .setErrorDescription("客户端验证失败，如错误的client_id/client_secret。")
          .buildJSONMessage();
        return new ResponseEntity(response.getBody(), HttpStatus.valueOf(response.getResponseStatus()));
      }

      String authCode = oauthRequest.getParam("code");

      System.out.println("参数输出开始==========");
      System.out.println("客户端传递过来的authCode:" + authCode);
      System.out.println("传递过来 的  AUTHORIZATION_CODE:" + oauthRequest.getParam("grant_type"));
      System.out.println("GrantType 的 AUTHORIZATION_CODE:" + GrantType.AUTHORIZATION_CODE.toString());
      System.out.println("服务器缓存中的用户信息:" + this.cache.getUser(authCode).getUserName());
      System.out.println("参数输出结束==========");

      if ((oauthRequest.getParam("grant_type").equals(GrantType.AUTHORIZATION_CODE.toString())) && 
        (this.cache.getUser(authCode) ==null)) {
    	  System.out.println("缓存为空,挂了:" + authCode);
    	  OAuthResponse response1 =OAuthASResponse.errorResponse(400)
          .setError("invalid_grant")
          .setErrorDescription("错误的授权码")
          .buildJSONMessage();
        return new ResponseEntity(response1.getBody(), HttpStatus.valueOf(response1.getResponseStatus()));
      }

     

      OAuthIssuer oauthIssuerImpl = new OAuthIssuerImpl(new MD5Generator());
      String accessToken = oauthIssuerImpl.accessToken();
      String refreshToken = oauthIssuerImpl.refreshToken();
      System.out.println("服务端生成的accessToken:" + accessToken);
      System.out.println("服务端生成的refreshToken:" + refreshToken);

      //将用户信息缓存到令牌中
      this.cache.setUser(accessToken, this.cache.getUser(authCode));
      
      
      OAuthResponse response1 = 
        OAuthASResponse.tokenResponse(200)
        .setAccessToken(accessToken).setRefreshToken(refreshToken).setExpiresIn("17600000")
        .buildJSONMessage();

      System.out.println("返回体:" + response1.getBody());

      return new ResponseEntity(response1.getBody(), HttpStatus.valueOf(response1.getResponseStatus()));
    }
    catch (OAuthSystemException e)
    {
      e.printStackTrace();
    }
    catch (OAuthProblemException e) {
      e.printStackTrace();
    }
    return null;
  }

  //3、资源管理部分对客户端的请求进行许可验证，通过后才返回对应的资源信息
  @RequestMapping({"/userInfo"})
  public HttpEntity getUserInfo(HttpServletRequest request)
  {
    System.out.println("进入获取资源部分");
    try
    {
      OAuthAccessResourceRequest oauthRequest = new OAuthAccessResourceRequest(request, new ParameterStyle[] { ParameterStyle.QUERY });

      String accessToken = oauthRequest.getAccessToken();

      System.out.println("服务端收到客户端传递的accessToken:" + accessToken);
      System.out.println("缓存中的用户信息:" + this.cache.getUser(accessToken).getUserName());
      if (this.cache.getUser(accessToken)==null){
    	  System.out.println("accessToken缓存中不存在用户信息，挂了");
    	  OAuthResponse oauthResponse =OAuthRSResponse.errorResponse(401)
    			  .setRealm(Constants.RESOURCE_SERVER_NAME)
    			  .setError("invalid_token")
    			  .buildHeaderMessage();

        HttpHeaders headers = new HttpHeaders();
        headers.add("WWW-Authenticate", oauthResponse.getHeader("WWW-Authenticate"));
        return new ResponseEntity(headers, HttpStatus.UNAUTHORIZED);
      }
      
      //得到缓存在令牌中的用户信息
     String userName=this.cache.getUser(accessToken).getUserName();
      System.out.println("返回给客户端的用户名称:" + userName);
      return new ResponseEntity(userName, HttpStatus.OK);
    }
    catch (OAuthSystemException e) {
      e.printStackTrace();
    }
    catch (OAuthProblemException e) {
      e.printStackTrace();
    }

    return null;
  }

    @RequestMapping(value ="/getMessage",method = RequestMethod.POST)
    @ResponseBody
    public JSONObject getMessage(HttpServletRequest request, HttpServletResponse response, BufferedReader br) {

        //System.out.println("hi");
        //Header部分
        JSONObject MerchineList = new JSONObject();
        JSONArray jSONArray = new JSONArray();
        List<JSONObject> devices = new ArrayList();
        List<JSONObject> properties = new ArrayList();
        List actions = new ArrayList();
        JSONObject extentions = new JSONObject();
        System.out.print(request.getHeaderNames());
        Enumeration<?> enum1 = request.getHeaderNames();
        while (enum1.hasMoreElements()) {
            String key = (String) enum1.nextElement();
            String value = request.getHeader(key);
            System.out.println(key + "\t" + value);
        }
        //body部分
        String inputLine;
        String str = "";
        try {
            while ((inputLine = br.readLine()) != null) {
                str += inputLine;
            }
            br.close();
        } catch (IOException e) {
            System.out.println("IOException: " + e);
        }
        System.out.println("str:" + str);
        JSONObject recieveHeader = new JSONObject();
        recieveHeader = JSONObject.fromObject(str);
        String str1 = recieveHeader.getString("header");
        System.out.println("header:" + recieveHeader.getString("header"));
        JSONObject recieveMessageId = new JSONObject();
        recieveMessageId = JSONObject.fromObject(str1);
        System.out.println("messageId:" + recieveMessageId.getString("messageId"));

        JSONObject header = new JSONObject();
        header.put("namespace", "AliGenie.Iot.Device.Discovery");
        header.put("name", "DiscoveryDevicesResponse");
        header.put("messageId", recieveMessageId.getString("messageId"));
        header.put("payLoadVersion", "1");


        JSONObject device = new JSONObject();
        JSONObject propertie = new JSONObject();


        device.put("deviceId", "0311800001");
        device.put("deviceName", "开关");
        device.put("deviceType", "switch");
        device.put("zone", "");
        device.put("brand", "");
        device.put("model", "");
        device.put("icon", "http://112.90.178.68:8083/upFiles/upload/files/20181019/Lighthouse.jpg");


        propertie.put("name", "powerstate");
        propertie.put("value", "off");
        properties.add(propertie);
        device.put("properties", properties);

        actions.add("TurnOn");
        actions.add("TurnOff");
        device.put("actions", actions);


        extentions.put("extension1", "");
        extentions.put("extension2", "");
        device.put("extentions", extentions);

        devices.add(device);
        JSONObject payload = new JSONObject();
        payload.put("devices", devices);

        MerchineList.put("header", header);
        MerchineList.put("payload", payload);
        System.out.println(MerchineList.toString());
        return MerchineList;
    }
}
