package travel.booking.controller;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import travel.booking.Global;
import travel.booking.container.*;

@Controller
public class TripDetailController {  
	@Resource(name = "loginInfoSession")
	LoginInfo loginInfo;
	
	@RequestMapping(value={"trip.html", "trip"}, method=RequestMethod.GET, params = {"id"})
    public String getDetailHotel(@RequestParam String id, Model model) {
		System.out.println("getDetailTrip: " + loginInfo.departure_date+" "+loginInfo.search_location);
    	System.out.println("rooms.html id: " + id);
    	loginInfo.select_trip_id = id;
    	/*
    	Hotel result = Global.db.getHotel(id); 
    	model.addAttribute("hotelInfo", result);
    	System.out.println(result);
    	model.addAttribute("loginInfo", loginInfo);
    	*/
        return "trip";                        
    }
}
