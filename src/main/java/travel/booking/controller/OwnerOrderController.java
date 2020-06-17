package travel.booking.controller;

import java.util.List;

import javax.annotation.Resource;
import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import travel.booking.Global;
import travel.booking.container.*;

@Controller
public class OwnerOrderController {         
    @Resource(name = "loginInfoSession")
	LoginInfo loginInfo;
	
	@RequestMapping(value={"hotelordermanagement", "hotelordermanagement.html"})
    public String getOwnerOrderPage(Model model) {
    	System.out.println("ownerOrderPage");
    	model.addAttribute("loginInfo", loginInfo);
        return "hotelordermanagement";                        
    }
	/*
	@RequestMapping(path = "/getOwnerOrder", produces = "application/json; charset=UTF-8")
    @ResponseBody
    public List<Order> getOwnerOrder(){
		System.out.println("getOwnerOrder");
    	List<Order> Orderlist=Global.db.getOwnerOrder(loginInfo.account.id);
    	for(Order order: Orderlist){
            order.room.price *= order.quantity; 
        }
    	//System.out.println(Orderlist);
    	return Orderlist;
    }

    public boolean deleteOwnerOrder(Order deleteOrder){
    	return Global.db.cancelOrder(deleteOrder);
    }
    
	@RequestMapping(value="/editOwnerOrder", method=RequestMethod.POST)
    public ResponseEntity<?> getSearchResultViaAjax(
    		@Valid @RequestBody @RequestParam(required = true) int id,
    		@Valid @RequestBody @RequestParam(required = false) String action) {
		System.out.println("editOwnerOrder");
        AjaxResponseBody result = new AjaxResponseBody();
        System.out.println(id+action);
        Order modifyOrder = Global.db.getOrder(id);
        boolean execute = false;
        
        if(action.equals("delete")) {
        	execute = deleteOwnerOrder(modifyOrder);
        }
        
        if(execute = false) {
        	result.setMsg("delete failed!");
        }
        else {
        	result.setMsg("delete successfully!");
        }

        //If error, just return a 400 bad request, along with the error message
        //return ResponseEntity.badRequest().body(result);
        return ResponseEntity.ok(result);
	}
	*/

}
