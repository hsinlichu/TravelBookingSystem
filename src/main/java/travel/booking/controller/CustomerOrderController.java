package travel.booking.controller;

import java.util.*;

import org.springframework.stereotype.Controller;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import travel.booking.Global;
import travel.booking.container.*;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

/**
 * @author jameschu
 * This controller is responsible for customer order management page
 */
@Controller
@SessionAttributes("loginInfo")
public class CustomerOrderController {  
	/**
	 * This function will create a new LoginInfo class if not exist(Session Singleton).
	 * @return: LoginInfo object
	 */
	@ModelAttribute("loginInfo")
	public LoginInfo addLoginInfo() {
		System.out.println("LoginInfo @ModelAttribute");
		return new LoginInfo();
	}

	/**
	 * @author jameschu
	 * This inner-class store order information after calculation
	 */
	private class DetailedOrder {
		public String id;      // order ID
		public Trip trip;      // trip in this order
		public int quantity;   // order quantity in this order 
		public int totalPrice; // quantity * trip price

		/**
		 * @param order
		 * Conver order class object information to DetailedOrder class object
		 */
		public DetailedOrder(Order order) {
			this.id = order.id;
			this.quantity = order.quantity;
			this.trip = Global.db.getTrip(order.tripID);
			this.totalPrice = this.trip.price * order.quantity;
		}
	}
	
	/**
	 * @author jameschu
	 * This inner-class will return status and message from modifying DB
	 */
	private class Response{
		public boolean status;
		public String message;
		
		/**
		 * @param status: true->success; false->fail
		 * @param message
		 */
		public Response(boolean status, String message) {
			this.status = status;
			this.message = message;
		}
	}

	/**
	 * @param model
	 * @param redir
	 * @return
	 * This function will return customer order management page
	 */
	@RequestMapping(value={"ordermanagement", "ordermanagement.html"})
	public String getCustomerOrderPage(Model model, RedirectAttributes redir) {
		LoginInfo loginInfo = (LoginInfo) model.getAttribute("loginInfo");
		model.addAttribute("loginInfo", loginInfo);
		if(loginInfo.islogin)
			return "ordermanagement";
		else { // if not login, return to homepage and show error message
			redir.addFlashAttribute("msg", "Not login");
			return "redirect:index";
		}           
	}

	/**
	 * @param model
	 * @return
	 * This function will return all the order object corresponging to this user
	 */
	@RequestMapping(path = "/GetAllOrder", produces = "application/json; charset=UTF-8")
	@ResponseBody
	public List<DetailedOrder> getCustomerOrder(Model model){
		System.out.println("GetAllOrder");
		LoginInfo loginInfo = (LoginInfo) model.getAttribute("loginInfo");
		List<Order> Orderlist = Global.db.getOrder(loginInfo.account.id);
		System.out.println(Orderlist);
		List<DetailedOrder> DetailedOrderlist = new ArrayList<DetailedOrder>();

		for(Order order: Orderlist) { // convert normal oder object to detailed order
			DetailedOrder detailedOrder = new DetailedOrder(order);
			DetailedOrderlist.add(detailedOrder);
		}

		return DetailedOrderlist;
	}

	/**
	 * @param id
	 * @param quantity
	 * @param action
	 * @param model
	 * @return
	 * This function will modify DB according to user command
	 */
	@RequestMapping(value="/editOrder", method=RequestMethod.POST)
	public ResponseEntity<?> getSearchResultViaAjax(
			@Valid @RequestBody @RequestParam(required = true)  String id,
			@Valid @RequestBody @RequestParam(required = false) String quantity,
			@Valid @RequestBody @RequestParam(required = false) String action,
			Model model) {
		System.out.println("editOrder" + id + " " + quantity + " " + action);
		LoginInfo loginInfo = (LoginInfo) model.getAttribute("loginInfo");
		List<Order> Orderlist = Global.db.getOrder(loginInfo.account.id);
		
		Order modifyOrder = null; // find the order that was modified
		for(Order order: Orderlist)
			if(order.id.equals(id)) {
				modifyOrder = order;
				break;
			}
		
		AjaxResponseBody result = new AjaxResponseBody();
		Response response = null;
		if(modifyOrder != null) {
			if(action.equals("delete"))
				response = deleteCustomerOrder(id, loginInfo.account);
			else if(action.equals("edit")) {
				try {
					response = modifyCustomerOrder(modifyOrder, quantity, loginInfo.account);
				} catch(Edit_Exception e) {
					response = new Response(false, e.getMessage());					
				}
			}
		}
		else
			response = new Response(false, "Can not find corresponding order.");

		if(response.status) 
			result.setMsg("Edit successfully!");
		else
			result.setMsg(response.message);

		//If error, just return a 400 bad request, along with the error message
		//return ResponseEntity.badRequest().body(result);
		return ResponseEntity.ok(result);
	} 

	/**
	 * @param deleteOrderID
	 * @param account
	 * @return
	 * This function will delete customer order
	 */
	public Response deleteCustomerOrder(String deleteOrderID, Account account){
		return new Response(Global.db.cancelOrder(account.id, deleteOrderID), null);
	}

	/**
	 * @param modifyOrder
	 * @param Quantity
	 * @param account
	 * @return
	 * @throws Edit_Exception
	 * This function will modified customer order according to legal user input 
	 */
	public Response modifyCustomerOrder(Order modifyOrder, String Quantity, Account account) throws Edit_Exception {   //modify -> re getCustomer

		System.out.println(modifyOrder + Quantity);
		int inputQuantity;
		try {
			inputQuantity = Integer.parseInt(Quantity);
		}
		catch(Exception e) {
			return new Response(false, "Quantity format invalid");
		}
		System.out.println("parse success");

		Trip trip = Global.db.getTrip(modifyOrder.tripID);

		if(inputQuantity <= trip.remainSits + modifyOrder.quantity && inputQuantity > 0) // check inputQuantity range
			return new Response(Global.db.modifyOrder(account.id, modifyOrder.id, inputQuantity), null);
		else
			throw new Edit_Exception("Modify Quantity illegal. You can other up to: " + (modifyOrder.quantity + trip.remainSits));
		
	}
}


