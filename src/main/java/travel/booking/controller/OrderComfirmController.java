package travel.booking.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import travel.booking.Global;
import travel.booking.container.*;

/**
 * @author jameschu
 * This controller is responsible for order final confirmation page
 */
@Controller
@SessionAttributes({"loginInfo", "numofpeople", "trip"})
public class OrderComfirmController {
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
	 * @param numofpeople
	 * @param travelid
	 * @param model
	 * @param redir
	 * @return
	 * This function will return oder confirmation page if trip still valid
	 */
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
	
    /**
     * @param trip
     * @param numofpeople
     * @return
     * Check if the trip still have enough remain seats
     */
    public Boolean bookCheck(Trip trip, int numofpeople) {
    	return (numofpeople <= trip.remainSits);
    }

    /**
     * @param model
     * @param redir
     * @return
     * This function will sent out the order to DB
     */
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
    
    /**
     * @param account
     * @param tripID
     * @param numofpeople
     * @return
     * modified the DB
     */
    private boolean bookComplete(Account account, String tripID, int numofpeople) {
        // Place an order. Return true if success, false if failed. 
    	Response response = Global.db.addOrder(account.id, tripID, numofpeople);
    	return response.status.equals("OK");
    }

}
