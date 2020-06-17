package travel.booking.controller;

import java.util.*;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import travel.booking.Global;
import travel.booking.container.*;

import java.util.Date;
import java.text.*;
import java.util.concurrent.TimeUnit;

@Controller
public class ResultPageController {  
	@Resource(name = "loginInfoSession")
	LoginInfo loginInfo;
	
	public String departure_date;
	public String location;
	private int price_from = Integer.MAX_VALUE;
	private int price_to = Integer.MIN_VALUE;
	private int show_price_from = Integer.MIN_VALUE;
	private int show_price_to = Integer.MAX_VALUE;
	private SortMethod sortMethod = SortMethod.PriceHigh2Low;
	private List<Trip> tripList = null;
	
	private enum SortMethod{
		PriceHigh2Low, PriceLow2High ,DateRecent2Far, DateFar2Recent
	}

	private static SimpleDateFormat originFormat = new SimpleDateFormat("MM/dd/yyyy");
	private static SimpleDateFormat newFormat = new SimpleDateFormat("yyyy-MM-dd");
	
	@RequestMapping(value={"/result.html", "result"}, method=RequestMethod.POST)
    public String getResult(@RequestParam String location, @RequestParam String departure_date,  Model model) {
		System.out.println("Resultpage " + departure_date + " " + location);

		try {
			this.departure_date = newFormat.format(originFormat.parse(departure_date));
			System.out.println("After formatter: " + this.departure_date);
		} catch (ParseException e) {
			String newurl = "redirect:/";
			newurl += ("?msg=" + 9);
	        return newurl;
		}
		this.location = location;
		
		model.addAttribute("loginInfo", loginInfo);
    	model.addAttribute("sort_method", sortMethod);
    	model.addAttribute("price_from", price_from);
    	model.addAttribute("price_to", price_to);
    	model.addAttribute("show_price_from", show_price_from);
    	model.addAttribute("show_price_to", show_price_to);
        return "result";                       
	}

	@RequestMapping(value="/result", method=RequestMethod.GET, params = {"sortMethod", "amount"})
    public String setFilterSort(@RequestParam String sortMethod, @RequestParam String amount, Model model) {
		System.out.println("setFilterSort");
		
		String[] show_range = amount.replace("$", "").split(" - ");
		this.show_price_from = Integer.parseInt(show_range[0]);
		this.show_price_to = Integer.parseInt(show_range[1]);
		this.sortMethod = SortMethod.valueOf(sortMethod);
		
		if(this.show_price_from > this.show_price_to) {
			this.show_price_from = this.price_from;
			this.show_price_to = this.price_to;
			System.out.println("filter range illegal");
		}
		
		System.out.println(show_price_from + " " + show_price_to + " " + sortMethod);
		
		model.addAttribute("loginInfo", loginInfo);
    	model.addAttribute("sort_method", sortMethod);
    	model.addAttribute("price_from", price_from);
    	model.addAttribute("price_to", price_to);
    	model.addAttribute("show_price_from", show_price_from);
    	model.addAttribute("show_price_to", show_price_to);
    	
        return "result";              
	}
	
	@RequestMapping(path = "/GetAllTrip", produces = "application/json; charset=UTF-8")
    @ResponseBody
    public List<Trip> GetAllHotel(Model model) {
		System.out.println(this.departure_date);
		if(tripList == null) {
			tripList = Global.db.getTrip(this.location, this.departure_date);
			for(Trip t: tripList) {
				System.out.print("1");
				if(t.price < price_from)
					price_from = t.price;
				if(t.price > price_to)
					price_to = t.price;
			}
			
			show_price_from = price_from;
			show_price_to = price_to;
			System.out.println("\n" + show_price_from + " " + show_price_to);
		}
		List<Trip> subTripList = tripList;
		if(show_price_from != price_from || show_price_to != price_to) {
			System.out.println("Show range " + show_price_from + "~" + show_price_to);
			subTripList = new ArrayList<Trip>();
			for(int i = 0; i < tripList.size(); ++i) {
				Trip t = tripList.get(i);
				if(t.price >= show_price_from && t.price <= show_price_to) 
					subTripList.add(t);
			}
		}
		
		switch(sortMethod) {
			case PriceLow2High:
				Collections.sort(subTripList, Trip.byPriceOrder());
				System.out.println("Sort:" + "PriceLow2High");
				break;
			case PriceHigh2Low:
				Collections.sort(subTripList, Trip.byReversePriceOrder());
				System.out.println("Sort:" + "PriceHigh2Low");
				break;
			case DateRecent2Far:
				Collections.sort(subTripList, Trip.byDateOrder());
				System.out.println("Sort:" + "DateRecent2Far");
				break;
			case DateFar2Recent:
				Collections.sort(subTripList, Trip.byReverseDateOrder());
				System.out.println("Sort:" + "DateFar2Recent");
				break;
		}
		
		model.addAttribute("loginInfo", loginInfo);
    	model.addAttribute("sort_method", sortMethod.name());
    	model.addAttribute("price_from", price_from);
    	model.addAttribute("price_to", price_to);
    	model.addAttribute("show_price_from", show_price_from);
    	model.addAttribute("show_price_to", show_price_to);
    	System.out.println(show_price_from + " " + show_price_to);
		System.out.println("successful searching");
		
        return subTripList;
    }
}
