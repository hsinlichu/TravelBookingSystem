package travel.booking.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.annotations.SerializedName;

import travel.booking.Global;
import travel.booking.container.*;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import javax.validation.Valid;


@Controller
public class CustomerOrderController {         
	//@Resource(name = "loginInfoSession")
	LoginInfo loginInfo;
	String errorMessage = "Edit failed!";
	
	private class DetailedOrder{
		public String id;
		public Trip trip;
		public int quantity;
		public int totalPrice;
	}
	
	@RequestMapping(value={"ordermanagement", "ordermanagement.html"})
    public String getCustomerOrderPage(Model model) {
		model.addAttribute("loginInfo", loginInfo);
		if(loginInfo.islogin) {
			System.out.println("CustomerOrderPage");
	        return "ordermanagement";  
		}
		else
			return "index";                    
    }
	
	@RequestMapping(path = "/GetAllOrder", produces = "application/json; charset=UTF-8")
    @ResponseBody
    public List<DetailedOrder> getCustomerOrder(){
		System.out.println("GetAllOrder");
    	List<Order> Orderlist = Global.db.getOrder(loginInfo.account.id);
    	System.out.println(Orderlist);
    	List<DetailedOrder> DetailedOrderlist = new ArrayList<DetailedOrder>();
    	
    	Trip tmp = new Trip();
    	tmp.title = "《玩美加族》加勒比海展望號遊輪牙買加11日";
    	tmp.travelCode = 43;
    	tmp.price = 100;
    	tmp.startDate = "2020-09-04";
    	tmp.endDate = "2020-09-13";
    	
    	
		
		for(Order order: Orderlist) {
		//for(int i = 0; i < Orderlist.size(); ++i) {
			DetailedOrder detailedOrder = new DetailedOrder();
			detailedOrder.id = order.id;
			detailedOrder.quantity = order.quantity;
			detailedOrder.trip = tmp;
			detailedOrder.totalPrice = tmp.price * order.quantity;
			DetailedOrderlist.add(detailedOrder);
		}

    	return DetailedOrderlist;
    }
	
	@RequestMapping(value="/editOrder", method=RequestMethod.POST)
    public ResponseEntity<?> getSearchResultViaAjax(
    		@Valid @RequestBody @RequestParam(required = true)  String id,
    		@Valid @RequestBody @RequestParam(required = false) String quantity,
    		@Valid @RequestBody @RequestParam(required = false) String action) {
		System.out.println("editOrder");
        
        System.out.println(id + " " + quantity + " " + action);
        List<Order> Orderlist = Global.db.getOrder(loginInfo.account.id);
        Order modifyOrder = null;
        for(Order order: Orderlist) {
        	System.out.println(order);
        	if(order.id.equals(id)) {
        		modifyOrder = order;
        		break;
        	}
        }
        boolean status = false;
        AjaxResponseBody result = new AjaxResponseBody();
        if(modifyOrder != null) {
            if(action.equals("delete"))
            	status = deleteCustomerOrder(id);
            else if(action.equals("edit"))
            	status = modifyCustomerOrder(modifyOrder, quantity);
        }
        else
        	errorMessage = "Can not find corresponding order.";
        
        if(status) 
        	result.setMsg("Edit successfully!");
        else
        	result.setMsg(errorMessage);
        	
        //If error, just return a 400 bad request, along with the error message
        //return ResponseEntity.badRequest().body(result);
        return ResponseEntity.ok(result);
	} 

    public boolean deleteCustomerOrder(String deleteOrderID){
    	return Global.db.cancelOrder(loginInfo.account.id, deleteOrderID);
    }
    
    public boolean modifyCustomerOrder(Order modifyOrder, String Quantity) {   //modify -> re getCustomer
    	
		System.out.println(Quantity);
		int inputQuantity;
		try {
		inputQuantity = Integer.parseInt(Quantity);
		}
		catch(Exception e) {
    		errorMessage = "Can not parse input quantity to int";
    		return false;
    	}
		System.out.println("parse success");
		
		Trip tmp = new Trip();
		tmp.lowerBound = 10;
    	tmp.upperBound = 30;
    	System.out.println(tmp.lowerBound + " " + tmp.upperBound + " " + inputQuantity);
    	System.out.println(loginInfo.account.id);
    	System.out.println(modifyOrder.id);
		
		if(tmp.lowerBound + inputQuantity <= tmp.upperBound) // check inputQuantity range
			return Global.db.modifyOrder(loginInfo.account.id, modifyOrder.id, inputQuantity);
		else {
			errorMessage = "Quantity range illegal. Legal quantity range: 0 ~ " + (tmp.upperBound - tmp.lowerBound);
			System.out.println(errorMessage);
			return false;
		} 	
    }
}


