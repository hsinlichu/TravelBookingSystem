package travel.booking.controller;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import java.util.ArrayList;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;

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
    public String infoCheck(@RequestParam int numofpeople, @RequestParam String travelid, Model model) {
		
		LoginInfo loginInfo = (LoginInfo) model.getAttribute("loginInfo");
		System.out.println("Confirmation: " + travelid);
		System.out.println("Confirmation: " + loginInfo);
		System.out.println("Confirmation: " + numofpeople);
		
		Trip selectTrip = Global.db.getTrip(travelid);
		System.out.println("Confirmation: " + travelid);
		if(!loginInfo.getLoginStatus()) {
			model.addAttribute("msg", 9);
			return "index";
		}
		
		if(bookCheck(selectTrip, numofpeople)){
			model.addAttribute("trip", selectTrip);
			model.addAttribute("numofpeople", numofpeople);
			System.out.println("bookCheck yes");
	        return "confirmation";    
		}	   
		else {
			model.addAttribute("msg", 6);
			System.out.println("bookCheck no");
			return "index"; // alert("Selected hotel is unavailable now, please try again!"); 
		}
			
    }
	
    public Boolean bookCheck(Trip trip, int numofpeople) {
    	System.out.println("bookCheck: " + trip.lowerBound + " " + trip.upperBound);
    	if(trip.lowerBound + numofpeople <= trip.upperBound)
            return true;
    	else
    		return false;
    }

    @RequestMapping(value="/bookcomplete")
    public String confirmOrder(Model model) {
    	System.out.println("--- Model data ---");
		Map modelMap = model.asMap();
		for (Object modelKey : modelMap.keySet()) {
			Object modelValue = modelMap.get(modelKey);
			System.out.println(modelKey + " -- " + modelValue);
		}
    	LoginInfo loginInfo = (LoginInfo) model.getAttribute("loginInfo");
    	Trip trip = (Trip) model.getAttribute("trip");
    	Integer numofpeople = (Integer) model.getAttribute("numofpeople");
    	
    	System.out.println("bookcomplete: " + loginInfo);
    	System.out.println("bookcomplete: " + numofpeople);
    	System.out.println("bookcomplete: " + trip);
    	
    	int msg;
    	if(bookComplete(loginInfo.account, trip.id, numofpeople)) {
    		msg = 7;
    		System.out.println("Book Success");
    	}
    	else {
    		msg = 8;
    		System.out.println("Book Fail");
    	}
    	model.addAttribute("msg", msg);
    	return "index";
    }
    
    private boolean bookComplete(Account account, String tripID, int numofpeople) {
        // Place an order. Return true if success, false if failed. 
    	Response response = Global.db.addOrder(account.id, tripID, numofpeople);
    	return response.status.equals("OK");
    }

}
