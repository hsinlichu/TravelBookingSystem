/**
 * 
 */
package travel.booking.controller;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import travel.booking.container.*;

/**
 * @author jameschu
 * This controller is responsible for homepage(index.html)
 */
@Controller
@SessionAttributes("loginInfo")
public class HomePageController {
	/**
	 * This function will create a new LoginInfo class if not exist(Session Singleton).
	 * @return: LoginInfo object
	 */
	@ModelAttribute("loginInfo")
	public LoginInfo addLoginInfo() {
		System.out.println("LoginInfo @ModelAttribute");
		return new LoginInfo();
	}
	
	/**
	 * This function will return homepage view to user
	 * @param session: single session model
	 * @param model: model in MVC framework
	 * @return
	 */
	@RequestMapping(value={"", "/", "index.html", "index"})
    public String getHomePage(HttpSession session, Model model) {
		System.out.println("Home Page");
		Utility.printSession(session);
		Utility.printModel(model);
    	
    	String msg = (String) model.getAttribute("msg"); // check whether have error message
    	if(msg != null) {
    		System.out.println("Home Page MSG: " + msg);
    	    model.addAttribute("msg", msg); 
    	}
        return "index"; // return view
    }
}