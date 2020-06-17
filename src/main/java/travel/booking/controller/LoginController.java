package travel.booking.controller;

import org.springframework.stereotype.Controller;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;

import travel.booking.Global;
import travel.booking.container.Account;
import travel.booking.container.LoginInfo;

@Controller
public class LoginController {
	@Resource(name = "loginInfoSession")
	LoginInfo loginInfo;
	
	@RequestMapping(value="/login", method=RequestMethod.POST)
	public String getLogin(@RequestParam String email, @RequestParam String passwd, Model model) {
		System.out.println("LoginController");
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
		
		String newurl = "redirect:/";
		newurl += ("?msg=" + msg);
        return newurl;
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
		
		String newurl = "redirect:/";
		newurl += ("?msg=" + msg);
        return newurl;
    }
	@RequestMapping(value="/signout", method=RequestMethod.GET)
	public String logout(Model model) {
		System.out.println("try logout");
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
		
		String newurl = "redirect:/";
		newurl += ("?msg=" + msg);
        return newurl;
    }
}
