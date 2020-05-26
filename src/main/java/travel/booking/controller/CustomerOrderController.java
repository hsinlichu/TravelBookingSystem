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

import travel.booking.Global;
import travel.booking.container.*;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import javax.validation.Valid;


@Controller
public class CustomerOrderController {         
	@Resource(name = "loginInfoSession")
	LoginInfo loginInfo;
	
	@RequestMapping(value={"ordermanagement", "ordermanagement.html"})
    public String getCustomerOrderPage(Model model) {
    	System.out.println("CustomerOrderPage");
    	model.addAttribute("loginInfo", loginInfo);
        return "ordermanagement";                        
    }
	/*
	@RequestMapping(path = "/GetAllOrder", produces = "application/json; charset=UTF-8")
    @ResponseBody
    public List<Order> getCustomerOrder(){
		System.out.println("GetAllOrder");
    	List<Order> Orderlist=Global.db.getOrder(loginInfo.account.id);
        for(Order order: Orderlist){
            order.room.price *= order.quantity; 
        }
    	//System.out.println(Orderlist);

    	return Orderlist;
    }
    
	
	@RequestMapping(value="/editOrder", method=RequestMethod.POST)
    public ResponseEntity<?> getSearchResultViaAjax(
    		@Valid @RequestBody @RequestParam(required = true) int id, @Valid @RequestBody @RequestParam(required = false) String quantity, 
    		@Valid @RequestBody @RequestParam(required = false) String dateIn, @Valid @RequestBody @RequestParam(required = false) String dateOut,
    		@Valid @RequestBody @RequestParam(required = false) String action) {
		System.out.println("editOrder");
        AjaxResponseBody result = new AjaxResponseBody();
        System.out.println(id + " " + quantity + " " + dateIn + " " + dateOut + " " + action);
        Order modifyOrder = Global.db.getOrder(id);
        boolean execute = false;
        
        if(action.equals("delete")) {
        	execute = deleteCustomerOrder(modifyOrder);
        }
        else if(action.equals("edit")) {
        	String message = reasonable(dateIn, dateOut, quantity);
        	if(message == null) {
        		execute = modifyCustomerOrder(modifyOrder, quantity, dateIn, dateOut);
        	}
        	else {
        		result.setMsg(message);
        	}
        }
        
        if(execute == true) {
        	result.setMsg("edit successfully!");
        }
        else{
        	result.setMsg("edit failed!");
        }
        

        //If error, just return a 400 bad request, along with the error message
        //return ResponseEntity.badRequest().body(result);
        return ResponseEntity.ok(result);
	} 

    public boolean deleteCustomerOrder(Order deleteOrder){
    	return Global.db.cancelOrder(deleteOrder);
    }
    
    public boolean modifyCustomerOrder(Order modifyOrder,String Quantity, String datein, String dateout) {   //modify -> re getCustomer
    	if(Quantity != null) {
    		modifyOrder.quantity = Integer.parseInt(Quantity);
    		System.out.println("change quantity");
    	}
    	if(datein != null) {
    		modifyOrder.dateIn = datein;
    		System.out.println("change datein");
    	}
    	if(dateout != null) {
    		modifyOrder.dateOut = dateout;
    		System.out.println("change dateout");
    	}
    	else {
    		System.out.println("change failed");
    	}
    	System.out.println(modifyOrder);
    	return Global.db.modifyOrder(modifyOrder);
    }
    
    public static String reasonable(String datein, String dateout, String Quantity) {
    	String message = null;
    	int quantity = 0;
    	if(Quantity != null) {
    		quantity = Integer.valueOf(Quantity);
    	}
    	int dateinYear = Integer.valueOf(datein.substring(0, 3));
    	int dateinMonth = Integer.valueOf(datein.substring(5, 6));;
    	int dateinDay = Integer.valueOf(datein.substring(8, 9));;
    	int dateoutYear = Integer.valueOf(datein.substring(0, 3));;
    	int dateoutMonth = Integer.valueOf(datein.substring(5, 6));;
    	int dateoutDay = Integer.valueOf(datein.substring(8, 9));;
    	boolean oddinMonth = true;
    	boolean oddoutMonth = true;
    	
    	if(dateinMonth == 2 || dateinMonth == 4 || dateinMonth == 6 || dateinMonth == 8 || dateinMonth == 10 || dateinMonth == 12) {
    		oddinMonth = false;
    	}
    	if(dateoutMonth == 2 || dateoutMonth == 4 || dateoutMonth == 6 || dateoutMonth == 8 || dateoutMonth == 10 || dateoutMonth == 12) {
    		oddoutMonth = false;
    	}
    	
	  try{
		     java.text.SimpleDateFormat dFormat = new SimpleDateFormat("yyyy/MM/dd");  
		     dFormat.setLenient(false);

		     java.util.Date d = dFormat.parse(datein);
		     System.out.println("datein ok");
		   }catch(ParseException e){
		    message = "please enter correct date";
		   }
	  
	  try{
		     java.text.SimpleDateFormat dFormat = new SimpleDateFormat("yyyy/MM/dd");  
		     dFormat.setLenient(false);
		    
		     java.util.Date d = dFormat.parse(dateout);
		     System.out.println("dateout ok");
		   }catch(ParseException e){
		    message = "please enter correct date";
	   
		   }
    	
    	
    	if(datein.length() > 10 || dateout.length() > 10) {
    		message = "please enter correct date!";
    	}
    	else if(datein.substring(4, 5).equals("/") == false || datein.substring(7, 8).equals("/") == false) {
    		message = "please enter date in correct format!(use /)";
    		System.out.println(datein + datein.substring(7, 8));
    	}
    	else if(quantity <= 0 || Quantity.equals("")) {
    		message = "please enter correct quantity!";
    	}
    	
    	return message;
    }
    */

}


