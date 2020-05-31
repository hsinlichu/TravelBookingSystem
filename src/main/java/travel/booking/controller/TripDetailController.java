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
    	System.out.println("rooms.html id: " + id);
    	loginInfo.select_trip_id = id;
    	Trip trip = new Trip();
    	trip.title = "《玩美加族》加勒比海展望號遊輪牙買加11日";
    	trip.travelCode = 43;
    	//Trip result = Global.db.getHotel(id); 
    	model.addAttribute("loginInfo", loginInfo);
    	model.addAttribute("trip", trip);
        return "trip";                        
    }
}
