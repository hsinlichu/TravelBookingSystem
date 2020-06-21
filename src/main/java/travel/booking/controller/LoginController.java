package travel.booking.controller;

import org.springframework.stereotype.Controller;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import java.util.Enumeration;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import travel.booking.Global;
import travel.booking.container.Account;
import travel.booking.container.LoginInfo;
import travel.booking.container.Utility;

@Controller
@SessionAttributes({"loginInfo"})
public class LoginController {
	@ModelAttribute("loginInfo")
	public LoginInfo addLoginInfo() {
		System.out.println("LoginInfo @ModelAttribute");
		return new LoginInfo();
	}
	
	@RequestMapping(value="/login", method=RequestMethod.POST)
	public String getLogin(@RequestParam String email, @RequestParam String passwd, Model model, RedirectAttributes redir) {
		Utility.printModel(model);
		
		System.out.println("LoginController");
		LoginInfo loginInfo = (LoginInfo) model.getAttribute("loginInfo");
		System.out.println("Original Status: " + loginInfo.islogin);
		
		String msg;
		Account result = Global.db.verifyAccount(email, passwd);
		if (result != null) {
			System.out.println(email + " login succeed");
			loginInfo.update(true, result);
			msg = "Login Success!";
		}
		else
			msg = "Email or Password Incorrect!";

		model.addAttribute("New Status loginInfo: ", loginInfo);
		System.out.println(msg);
	    redir.addFlashAttribute("msg", msg);
	    return "redirect:index";
    }
	
	@RequestMapping(value="/signup", method=RequestMethod.POST)
	public String getLogin(
			@RequestParam String firstname, @RequestParam String lastname, 
			@RequestParam String email, @RequestParam String passwd, 
			Model model, RedirectAttributes redir) {
		
		String name = lastname + firstname;
		
		Account result = Global.db.addAccount(name, email, passwd);
		String msg = (result != null) ? "Sign up success, you can login now!" : "Sign up failed, please try again!";
		System.out.println(msg);
		
	    redir.addFlashAttribute("msg", msg);
	    return "redirect:index";
    }
	
	@RequestMapping(value="/signout", method=RequestMethod.GET)
	public String logout(Model model, RedirectAttributes redir) {
		System.out.println("try logout");
		LoginInfo loginInfo = (LoginInfo) model.getAttribute("loginInfo");
		loginInfo.update(false, null);
		
		model.addAttribute("loginInfo", loginInfo);
		String msg = (!loginInfo.islogin) ? "Logout Success!" : "Logout failed, please try again!";

		System.out.println(msg);
	    redir.addFlashAttribute("msg", msg);
	    return "redirect:index";
    }
}
