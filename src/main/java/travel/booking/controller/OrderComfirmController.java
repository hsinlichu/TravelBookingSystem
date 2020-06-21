package travel.booking.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import travel.booking.Global;
import travel.booking.container.*;

@Controller
@SessionAttributes({"loginInfo", "numofpeople", "trip"})
public class OrderComfirmController {
	
	@ModelAttribute("loginInfo")
	public LoginInfo addLoginInfo() {
		System.out.println("LoginInfo @ModelAttribute");
		return new LoginInfo();
	}
	
	@RequestMapping(value="/confirmation", method=RequestMethod.POST)
    public String infoCheck(@RequestParam int numofpeople, @RequestParam String travelid, Model model, RedirectAttributes redir) {
		
		LoginInfo loginInfo = (LoginInfo) model.getAttribute("loginInfo");
		System.out.println("Confirmation: " + travelid + " " + loginInfo + " " + numofpeople);
		
		Trip selectTrip = Global.db.getTrip(travelid);
		if(!loginInfo.getLoginStatus()) {
			redir.addFlashAttribute("msg", "Selected hotel is unavailable now, please try again!");
			return "redirect:index";
		}
		
		if(bookCheck(selectTrip, numofpeople)){
			model.addAttribute("trip", selectTrip);
			model.addAttribute("numofpeople", numofpeople);
	        return "confirmation";    
		}	   
		else {
		    redir.addFlashAttribute("msg", "Selected hotel is unavailable now, please try again!");
			return "redirect:index";
		}
			
    }
	
    public Boolean bookCheck(Trip trip, int numofpeople) {
    	return (numofpeople <= trip.remainSits);
    }

    @RequestMapping(value="/bookcomplete")
    public String confirmOrder(Model model, RedirectAttributes redir) {
		Utility.printModel(model);
		
    	LoginInfo loginInfo = (LoginInfo) model.getAttribute("loginInfo");
    	Trip trip = (Trip) model.getAttribute("trip");
    	Integer numofpeople = (Integer) model.getAttribute("numofpeople");
    	
    	System.out.println("bookcomplete: " + loginInfo + " " + numofpeople + " " + trip);
    	
    	String msg = bookComplete(loginInfo.account, trip.id, numofpeople) ? "Book Success!" : "Book Fail QQ";
    	System.out.println(msg);
    	
	    redir.addFlashAttribute("msg", msg);
	    return "redirect:index";
    }
    
    private boolean bookComplete(Account account, String tripID, int numofpeople) {
        // Place an order. Return true if success, false if failed. 
    	Response response = Global.db.addOrder(account.id, tripID, numofpeople);
    	return response.status.equals("OK");
    }

}
