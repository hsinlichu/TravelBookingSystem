/**
 * 
 */
package travel.booking.controller;

import java.util.Enumeration;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.bind.annotation.SessionAttributes;

import travel.booking.container.LoginInfo;


/**
 * @author root
 *
 */
@Controller
@SessionAttributes("loginInfo")
public class HomePageController {
	//@Resource(name = "loginInfoSession")
	//LoginInfo loginInfo;
	
	@ModelAttribute("loginInfo")
	public LoginInfo addLoginInfo() {
		System.out.println("LoginInfo @ModelAttribute");
		return new LoginInfo();
	}
	
	@RequestMapping(value={"", "/", "index.html", "index"}, method=RequestMethod.GET, params = {})
    public String getHomePage(HttpSession session) {
		System.out.println("--- Session data ---");
		Enumeration<String> e = session.getAttributeNames();
		  while (e.hasMoreElements()){
			String s = e.nextElement();
			System.out.println(s + " -> " + session.getAttribute(s));
		  }
		
			/*
			 * System.out.println("--- Model data ---"); Map modelMap = model.asMap(); for
			 * (Object modelKey : modelMap.keySet()) { Object modelValue =
			 * modelMap.get(modelKey); System.out.println(modelKey + " -- " + modelValue); }
			 */
		
    	System.out.println("Home Page");
    	//System.out.println("New Status: " + loginInfo.islogin);
        return "index";
    }
	
	@RequestMapping(value={"", "/", "index.html", "index"}, method=RequestMethod.GET, params = {"msg"})
    public String getHomePage(@RequestParam String msg, HttpSession session) {
		/*
		 * System.out.println(msg); model.addAttribute("loginInfo", loginInfo);
		 * model.addAttribute("msg", msg); System.out.println("Home Page MSG");
		 * System.out.println("New Status: " + loginInfo.islogin);
		 * 
		 * System.out.println(loginInfo.account);
		 */
        return "index";
    }
}