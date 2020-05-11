/**
 * 
 */
package travel.booking.controller;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import travel.booking.container.LoginInfo;


/**
 * @author root
 *
 */
@Controller
public class HomePageController {
	@Resource(name = "loginInfoSession")
	LoginInfo loginInfo;
	
	@RequestMapping(value={"", "/", "index.html", "index"}, method=RequestMethod.GET, params = {})
    public String getHomePage(Model model) {
		
		model.addAttribute("loginInfo", loginInfo);
		
    	System.out.println("Home Page");
    	System.out.println("New Status: " + loginInfo.islogin);
        return "index";
    }
	
	@RequestMapping(value={"", "/", "index.html", "index"}, method=RequestMethod.GET, params = {"msg"})
    public String getHomePage(@RequestParam String msg, Model model) {
		System.out.println(msg);
		model.addAttribute("loginInfo", loginInfo);
		model.addAttribute("msg", msg);
    	System.out.println("Home Page MSG");
    	System.out.println("New Status: " + loginInfo.islogin);
    	
    	System.out.println(loginInfo.account);
        return "index";
    }
}