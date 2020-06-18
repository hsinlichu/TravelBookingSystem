package travel.booking.controller;

import org.springframework.stereotype.Controller;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;

import java.util.Enumeration;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import travel.booking.Global;
import travel.booking.container.Account;
import travel.booking.container.LoginInfo;

@Controller
@SessionAttributes({"loginInfo"})
public class LoginController {
	@ModelAttribute("loginInfo")
	public LoginInfo addLoginInfo() {
		System.out.println("LoginInfo @ModelAttribute");
		return new LoginInfo();
	}
	
	@RequestMapping(value="/login", method=RequestMethod.POST)
	public String getLogin(@RequestParam String email, @RequestParam String passwd, Model model, HttpSession session) {
		Enumeration<String> e = session.getAttributeNames();
		  while (e.hasMoreElements()){
			String s = e.nextElement();
			System.out.println(s);
			System.out.println("**" + session.getAttribute(s));
		  }
		System.out.println("LoginController");
		LoginInfo loginInfo = (LoginInfo) model.getAttribute("loginInfo");
		System.out.println("Original Status: " + loginInfo.islogin);
		
		int msg = 0;
		Account result = Global.db.verifyAccount(email, passwd);
		if (result != null) {
			System.out.println(email + " login succeed");
			loginInfo.islogin = true;
			loginInfo.account = result;
			System.out.println(loginInfo);
			msg = 0;
		}
		else {
			System.out.println(email + " login failed");
			msg = 1;
		}

		model.addAttribute("loginInfo", loginInfo);
		System.out.println("New Status: " + loginInfo.islogin);
		model.addAttribute("msg", msg);
        return "index";
    }
	
	@RequestMapping(value="/signup", method=RequestMethod.POST)
	public String getLogin(@RequestParam String firstname, @RequestParam String lastname, @RequestParam String email, @RequestParam String passwd, Model model) {
		int msg = 0;
		String name = lastname + firstname;
		
		Account result = Global.db.addAccount(name, email, passwd);
		if (result != null) {
			System.out.println(name + " has registered successfully.");
			msg = 2;
		}
		else
			msg = 3;
		
		model.addAttribute("msg", msg);
        return "index";
    }
	@RequestMapping(value="/signout", method=RequestMethod.GET)
	public String logout(Model model) {
		System.out.println("try logout");
		LoginInfo loginInfo = (LoginInfo) model.getAttribute("loginInfo");
		loginInfo.islogin = false;
		loginInfo.account = null;
		int msg = 0;
		model.addAttribute("loginInfo", loginInfo);
		
		
		if (loginInfo.islogin == false) {
			System.out.println("logout successfully.");
			msg = 4;
		}
		else
			msg = 5;
		model.addAttribute("msg", msg);
        return "index";
    }
}
