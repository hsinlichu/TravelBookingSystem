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
public class HotelDetailController {  
	@Resource(name = "loginInfoSession")
	LoginInfo loginInfo;
	
	@RequestMapping(value={"rooms.html", "rooms"}, method=RequestMethod.GET, params = {"id"})
    public String getDetailHotel(@RequestParam int id, Model model) {
		System.out.println("getDetailHotel: " + loginInfo.search_datein+" "+loginInfo.search_dateout+" "+loginInfo.search_location+" "+loginInfo.search_person+" "+loginInfo.datedifference);
    	System.out.println("rooms.html id: " + id);
    	loginInfo.select_hotel_id = id;
    	/*
    	Hotel result = Global.db.getHotel(id); 
    	model.addAttribute("hotelInfo", result);
    	System.out.println(result);
    	model.addAttribute("loginInfo", loginInfo);
    	*/
        return "rooms";                        
    }
}
