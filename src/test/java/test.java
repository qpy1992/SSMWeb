import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.yzz.oauthserver.bean.User;
import com.yzz.oauthserver.services.ClientService;
import com.yzz.oauthserver.services.UserService;



public class test {
	public static void main(String[] args) {
		ApplicationContext a=new ClassPathXmlApplicationContext("spring-mybatis.xml"); 
    	ClientService client=(ClientService) a.getBean("clientService");
    	UserService userService=(UserService) a.getBean("userService");
    			
    	//UserInfo user = uService.getUserInfoById(1);
        //System.out.println(user.getUsername());
    	//System.out.println(client.checkClient("test_id1"));
    	System.out.println(client.check("test_id1", "test_secret1"));
    	//User u=userService.Login("admin", "12345");
    	//System.out.println(u.getUserName());
	}
}
