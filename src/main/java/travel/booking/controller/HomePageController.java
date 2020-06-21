/**
 * 
 */
package travel.booking.controller;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;

import travel.booking.container.LoginInfo;
import travel.booking.container.Utility;

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
	
	@RequestMapping(value={"", "/", "index.html", "index"})
    public String getHomePage(HttpSession session, Model model) {
		Utility.printSession(session);
		Utility.printModel(model);
		
    	System.out.println("Home Page");
    	//System.out.println("New Status: " + loginInfo.islogin);
    	
    	String msg = (String) model.getAttribute("msg");
    	if(msg != null) {
    		System.out.println("Home Page MSG: " + msg);
    	    model.addAttribute("msg", msg); 
    	}
        return "index";
    }
}