package travel.booking.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

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
@SessionAttributes({"loginInfo"})
public class TripDetailController {
	@ModelAttribute("loginInfo")
	public LoginInfo addLoginInfo() {
		System.out.println("LoginInfo @ModelAttribute");
		return new LoginInfo();
	}
	
	@RequestMapping(value={"trip.html", "trip"}, method=RequestMethod.GET, params = {"id"})
    public String getDetailHotel(@RequestParam String id, Model model, HttpSession session) {
    	System.out.println("trip.html id: " + id);
    	Trip trip = Global.db.getTrip(id);
    	model.addAttribute("trip", trip);
    	System.out.println(trip);
        return "trip";                        
    }
}
