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
	
	public String checkin_date;
	public String checkout_date;
	public String location;
	public int person; 
	private int star = 0;
	private int price_from = 0;
	private int price_to = 10000000;
	private int sortmethod = -1;     //0->star HtoL, 1->star LtoH, 2->price HtoL, 3->price LtoH

	
	@RequestMapping(value={"/result.html", "result"}, method=RequestMethod.POST)
    public String getResult(@RequestParam String location, @RequestParam String departure_date,  Model model) {
		
		System.out.println("Resultpage " + departure_date + " " + location + " " + person);
		/*
    	//this.checkin_date = dateChangeType(checkin_date);
    	//this.checkout_date = dateChangeType(checkout_date);
    	this.location = location;
    	this.person = person;
    	
    	loginInfo.search_datein = this.checkin_date;
    	loginInfo.search_dateout = this.checkout_date;
    	loginInfo.search_location = this.location;
    	loginInfo.search_person = this.person;
    	loginInfo.datedifference = dateDifference(this.checkin_date, this.checkout_date);
    	model.addAttribute("loginInfo", loginInfo);
    	model.addAttribute("sort_method", this.sortmethod);
    	model.addAttribute("star", this.star);
    	model.addAttribute("price_from", this.price_from);
    	model.addAttribute("price_to", this.price_to);
    	*/
        return "result";                       
	}


	@RequestMapping(value="/result", method=RequestMethod.GET)
    public String getfilter(@RequestParam int star, @RequestParam int pricefrom, @RequestParam int priceto, Model model) {
		model.addAttribute("loginInfo", loginInfo);
		model.addAttribute("sort_method", this.sortmethod);
		
    	System.out.println("Resultpage");
    	System.out.println(star+"\n"+pricefrom+"\n"+priceto+"\n");
    	
    	this.star = star;
    	this.price_from = pricefrom;
    	this.price_to = priceto;
    	model.addAttribute("star", this.star);
    	model.addAttribute("price_from", this.price_from);
    	model.addAttribute("price_to", this.price_to);
        return "result";              
	}

	@RequestMapping(value="/result", method=RequestMethod.GET, params = {"sort_method"})
    public String sort(@RequestParam String sort_method, Model model) {
		model.addAttribute("loginInfo", loginInfo);
		model.addAttribute("star", this.star);
		model.addAttribute("price_from", this.price_from);
    	model.addAttribute("price_to", this.price_to);
    	System.out.println("Resultpage");
    	System.out.println(sort_method);
    	switch(sort_method){
    		case "Star_HtoL": 
    			this.sortmethod = 0;
    			break;
    		case "Star_LtoH": 
    			this.sortmethod = 1;
    			break;
    		case "Price_HtoL": 
    			this.sortmethod = 2;
    			break;
    		case "Price_LtoH": 
    			this.sortmethod = 3;
    			break;
    		default:
    			this.sortmethod = -1;
    			break;

    	}
    	model.addAttribute("sort_method", this.sortmethod);
    	
        return "result";              
	}
	
	/*
	public List<ResultHotel> FilteredHotel(List<ResultHotel> search,int star,int downfloor,int upfloor){//create new list to store totalprice within downfloor price and upfloor price
		if(upfloor == -1) upfloor = 100000000;
		List<ResultHotel> filteredtotal=new ArrayList<>();
		for(int i=0;i<search.size();i++) {
			if(star == 0 || search.get(i).star==star) {
				if(search.get(i).avgprice>=downfloor&&search.get(i).avgprice<=upfloor) filteredtotal.add(search.get(i));
			}
		}
		for(int j=0;j<filteredtotal.size();j++) {
			System.out.println("-------Hotel " + filteredtotal.get(j).id + "-------");
			System.out.println("star: " + filteredtotal.get(j).star);
			System.out.println("locality: " + filteredtotal.get(j).locality);
			System.out.println("street: " + filteredtotal.get(j).street);
			System.out.println("Single room[price: " + filteredtotal.get(j).singleRoomPrice + ", quantity: " + filteredtotal.get(j).singleRoomNum + "]");
			System.out.println("Double room[price: " + filteredtotal.get(j).doubleRoomPrice + ", quantity: " + filteredtotal.get(j).doubleRoomNum + "]");
			System.out.println("Quad room[price: " + filteredtotal.get(j).quadRoomPrice + ", quantity: " + filteredtotal.get(j).quadRoomNum + "]");
			System.out.println("average price: " + filteredtotal.get(j).avgprice);
			System.out.println(" ");
		}
				
		return filteredtotal;
	}
	public static List<Hotel> sort_star_LtoH(List<Hotel> search) {
		List<Hotel> tmp = search;
		Hotel swap =new Hotel();
		boolean check = true;
		while (check) {
			check = false;
			for (int i = 0; i < tmp.size()-1; i++) {
				if (tmp.get(i).star > tmp.get(i+1).star) {
					swap = tmp.get(i);
					tmp.set(i, tmp.get(i+1));
					tmp.set(i+1, swap);
					check = true;
				}
			}
		}
		return tmp;
		
	}
	
	public static List<Hotel> sort_star_HtoL(List<Hotel> search) {
		List<Hotel> tmp = search;
		Hotel swap =new Hotel();
		boolean check = true;
		while (check) {
			check = false;
			for (int i = 0; i < tmp.size()-1; i++) {
				if (tmp.get(i).star < tmp.get(i+1).star) {
					swap = tmp.get(i);
					tmp.set(i, tmp.get(i+1));
					tmp.set(i+1, swap);
					check = true;
				}
			}
		}
		return tmp;
		
	}
	public static List<ResultHotel> sort_price_LtoH(List<ResultHotel> search) {
		List<ResultHotel> tmp = search;
		ResultHotel swap =new ResultHotel();
		boolean check = true;
		while (check) {
			check = false;
			for (int i = 0; i < tmp.size()-1; i++) {
				if (tmp.get(i).avgprice > tmp.get(i+1).avgprice) {
					swap = tmp.get(i);
					tmp.set(i, tmp.get(i+1));
					tmp.set(i+1, swap);
					check = true;
				}
			}
		}
		return tmp;
		
	}
	public static List<ResultHotel> sort_price_HtoL(List<ResultHotel> search) {
		List<ResultHotel> tmp = search;
		ResultHotel swap =new ResultHotel();
		boolean check = true;
		while (check) {
			check = false;
			for (int i = 0; i < tmp.size()-1; i++) {
				if (tmp.get(i).avgprice < tmp.get(i+1).avgprice) {
					swap = tmp.get(i);
					tmp.set(i, tmp.get(i+1));
					tmp.set(i+1, swap);
					check = true;
				}
			}
		}
		return tmp;
		
	}
	

	
	@RequestMapping(path = "/GetAllHotel", produces = "application/json; charset=UTF-8")
    @ResponseBody
    public List<ResultHotel> GetAllHotel() {
		List<Hotel> hotel =Global.db.getAllHotel(this.checkin_date, this.checkout_date, this.location, this.person);
		
		List<ResultHotel> resultHotel = new ArrayList<>();
		
		if(sortmethod == 0)                         //sort method
			hotel = sort_star_HtoL(hotel);
		else if(sortmethod == 1)
			hotel = sort_star_LtoH(hotel);
		
		for (int i = 0; i < hotel.size(); i++) {    //turn Hotel to ResultHotel
			int numofSingle = this.person % 4 % 2;
			int numofDouble = this.person % 4 / 2;
			int numofQuad = this.person / 4;
			int priceofSingle = 0;
			int priceofDouble = 0;
			int priceofQuad = 0;
			int leftofSingle = 0;
			int leftofDouble = 0;
			int leftofQuad = 0;
			List<Room> roomslist = hotel.get(i).rooms;
			int hotelId = hotel.get(i).id;
			
			for(int j = 0; j < roomslist.size(); j++) {          //take Room list
				if(roomslist.get(j).type.equals("Single")) {
					priceofSingle = roomslist.get(j).price;
					leftofSingle = Global.db.roomLeft(roomslist.get(j), this.checkin_date, this.checkout_date);
				}
				if(roomslist.get(j).type.equals("Double")) {
					priceofDouble = roomslist.get(j).price;
					leftofDouble = Global.db.roomLeft(roomslist.get(j), this.checkin_date, this.checkout_date);
				}
				if(roomslist.get(j).type.equals("Quad")){
					priceofQuad = roomslist.get(j).price;
					leftofQuad = Global.db.roomLeft(roomslist.get(j), this.checkin_date, this.checkout_date);
				}
			}
			
			ResultHotel addHotel = new ResultHotel();
			addHotel.id = hotelId;
			addHotel.locality = hotel.get(i).locality;
			addHotel.star = hotel.get(i).star;
			addHotel.street = hotel.get(i).street;
			addHotel.singleRoomNum = leftofSingle;
			addHotel.singleRoomPrice = priceofSingle;
			addHotel.doubleRoomNum = leftofDouble;
			addHotel.doubleRoomPrice = priceofDouble;
			addHotel.quadRoomNum = leftofQuad;
			addHotel.quadRoomPrice = priceofQuad;
			if(leftofSingle < numofSingle && leftofDouble < numofDouble && leftofQuad < numofQuad){
				numofDouble = 0;
			    numofQuad = 0;
				numofSingle = 0;
			}
			else if(leftofSingle < numofSingle && leftofDouble < numofDouble) {
				numofDouble = 0;
			    numofSingle = 0;
				if(this.person % 4 == 0) 
					numofQuad = this.person / 4;
				else 
					numofQuad = this.person / 4 + 1;	
			}			
			else if(leftofSingle < numofSingle && leftofQuad < numofQuad){
				numofQuad = 0;
			    numofSingle = 0;
				if(this.person % 2 ==0)
					numofDouble = this.person / 2;
				else
					numofDouble = this.person / 2 + 1;
			}
			else if(leftofDouble < numofDouble && leftofQuad < numofQuad) {
				numofDouble = 0;
			    numofQuad = 0;
				numofSingle = this.person;
			}
			else if(leftofSingle < numofSingle) {
				numofQuad = this.person / 4;
			    numofSingle = 0;
				if(this.person % 4 % 2 == 0)
					numofDouble = this.person % 4 / 2;
				else {
					numofDouble = this.person % 4 / 2 + 1;
				}
			}
			else if(leftofDouble < numofDouble) {
				numofDouble = 0;
				numofQuad = this.person / 4;
				numofSingle = this.person % 4;
			}
			else if(leftofQuad < numofQuad) {
				numofDouble = this.person / 2;
				numofSingle = this.person % 2;
				numofQuad = 0;
			}
			else {
				numofQuad = this.person / 4;
				numofDouble = this.person % 4 / 2;
				numofSingle = this.person % 4 % 2;
			}
			addHotel.avgprice = numofSingle * priceofSingle + numofDouble * priceofDouble + numofQuad * priceofQuad;
			resultHotel.add(addHotel);
			
			
			  System.out.println("-------Hotel " + resultHotel.get(i).id + "-------");
			  System.out.println("star: " + resultHotel.get(i).star);
			  System.out.println("locality: " + resultHotel.get(i).locality);
			  System.out.println("street: " + resultHotel.get(i).street);
			  System.out.println("Single room[price: " + resultHotel.get(i).singleRoomPrice
			  + ", quantity: " + resultHotel.get(i).singleRoomNum + "]");
			  System.out.println("Double room[price: " + resultHotel.get(i).doubleRoomPrice
			  + ", quantity: " + resultHotel.get(i).doubleRoomNum + "]");
			  System.out.println("Quad room[price: " + resultHotel.get(i).quadRoomPrice +
			  ", quantity: " + resultHotel.get(i).quadRoomNum + "]");
			  System.out.println("average price: " + resultHotel.get(i).avgprice);
			  System.out.println(" ");
			 
			
		}	
		System.out.println("successful searching" );
		if(sortmethod == 2)
			resultHotel = sort_price_HtoL(resultHotel);
		else if(sortmethod == 3)
			resultHotel = sort_price_LtoH(resultHotel);
		resultHotel = FilteredHotel(resultHotel, star, price_from, price_to);
        return resultHotel;
    }
	*/
	
}
