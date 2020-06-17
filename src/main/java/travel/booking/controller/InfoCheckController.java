package travel.booking.controller;

import java.util.List;

import javax.annotation.Resource;

import java.util.ArrayList;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import travel.booking.Global;
import travel.booking.container.*;

@Controller
public class InfoCheckController {     
	//@Resource(name = "loginInfoSession")
	private LoginInfo loginInfo;
	private int numofpeople;
	
	@RequestMapping(value="/confirmation", method=RequestMethod.POST)
    public String infoCheck(@RequestParam int numofpeople, Model model) {
		System.out.println("Confirmation: " + loginInfo.select_trip_id);
		
		//Trip selectTrip = Global.db.getHotel(loginInfo.select_hotel_id); 
		Trip selectTrip = new Trip();
		this.numofpeople = numofpeople;
		model.addAttribute("loginInfo", loginInfo);
		if(loginInfo.getLoginStatus())
			return "index?msg=9";
		if(true){
		//if(bookCheck(selectTrip, numofpeople)){
			model.addAttribute("trip", selectTrip);
			model.addAttribute("numofpeople", numofpeople);
	        return "confirmation";    
		}	   
		else
			return "index?msg=6"; // alert("Selected hotel is unavailable now, please try again!"); 
    }
	
    public Boolean bookCheck(Trip trip, int numofpeople) {
    	System.out.println("bookCheck: " + trip.lowerBound + " " + trip.upperBound);
    	if(trip.lowerBound + numofpeople <= trip.upperBound)
            return true;
    	else
    		return false;
    }

    @RequestMapping(value="/bookcomplete")
    public String confirmOrder() {
    	int msg;
    	if(bookComplete()) {
    		msg = 7;
    		System.out.println("Book Success");
    	}
    	else {
    		msg = 8;
    		System.out.println("Book Fail");
    	}
    	String newurl = "redirect:/";
		newurl += ("?msg=" + msg);
    	return newurl;
    }
    
    private boolean bookComplete() {
        // Place an order. Return true if success, false if failed. 
    	return Global.db.addOrder(loginInfo.account.id, loginInfo.select_trip_id, numofpeople); 
    }

}
