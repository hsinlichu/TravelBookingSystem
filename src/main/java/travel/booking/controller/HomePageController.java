/**
 * 
 */
package travel.booking.controller;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import travel.booking.container.*;

@Controller
@SessionAttributes("loginInfo")
public class HomePageController {
	@ModelAttribute("loginInfo")
	public LoginInfo addLoginInfo() {
		System.out.println("LoginInfo @ModelAttribute");
		return new LoginInfo();
	}
	
	@RequestMapping(value={"", "/", "index.html", "index"})
    public String getHomePage(HttpSession session, Model model) {
		System.out.println("Home Page");
		Utility.printSession(session);
		Utility.printModel(model);
    	
    	String msg = (String) model.getAttribute("msg");
    	if(msg != null) {
    		System.out.println("Home Page MSG: " + msg);
    	    model.addAttribute("msg", msg); 
    	}
        return "index";
    }
}