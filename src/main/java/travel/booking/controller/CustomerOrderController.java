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


@Controller
@SessionAttributes("loginInfo")
public class CustomerOrderController {         
	@ModelAttribute("loginInfo")
	public LoginInfo addLoginInfo() {
		System.out.println("LoginInfo @ModelAttribute");
		return new LoginInfo();
	}
	private static String errorMessage = "Edit failed!";

	private class DetailedOrder{
		private String id;
		private Trip trip;
		private int quantity;
		private int totalPrice;
	}

	@RequestMapping(value={"ordermanagement", "ordermanagement.html"})
	public String getCustomerOrderPage(Model model, RedirectAttributes redir) {
		LoginInfo loginInfo = (LoginInfo) model.getAttribute("loginInfo");
		model.addAttribute("loginInfo", loginInfo);
		if(loginInfo.islogin) {
			return "ordermanagement";  
		}
		else {
			redir.addFlashAttribute("msg", "Not login");
			return "redirect:index";
		}           
	}

	@RequestMapping(path = "/GetAllOrder", produces = "application/json; charset=UTF-8")
	@ResponseBody
	public List<DetailedOrder> getCustomerOrder(Model model){
		System.out.println("GetAllOrder");
		LoginInfo loginInfo = (LoginInfo) model.getAttribute("loginInfo");
		List<Order> Orderlist = Global.db.getOrder(loginInfo.account.id);
		System.out.println(Orderlist);
		List<DetailedOrder> DetailedOrderlist = new ArrayList<DetailedOrder>();

		for(Order order: Orderlist) {
			DetailedOrder detailedOrder = new DetailedOrder();
			detailedOrder.id = order.id;
			detailedOrder.quantity = order.quantity;
			detailedOrder.trip = Global.db.getTrip(order.tripID);;
			detailedOrder.totalPrice = detailedOrder.trip.price * order.quantity;
			DetailedOrderlist.add(detailedOrder);
		}

		return DetailedOrderlist;
	}

	@RequestMapping(value="/editOrder", method=RequestMethod.POST)
	public ResponseEntity<?> getSearchResultViaAjax(
			@Valid @RequestBody @RequestParam(required = true)  String id,
			@Valid @RequestBody @RequestParam(required = false) String quantity,
			@Valid @RequestBody @RequestParam(required = false) String action,
			Model model) {
		System.out.println("editOrder" + id + " " + quantity + " " + action);
		LoginInfo loginInfo = (LoginInfo) model.getAttribute("loginInfo");
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
				status = deleteCustomerOrder(id, loginInfo.account);
			else if(action.equals("edit"))
				status = modifyCustomerOrder(modifyOrder, quantity, loginInfo.account);
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

	public boolean deleteCustomerOrder(String deleteOrderID, Account account){
		return Global.db.cancelOrder(account.id, deleteOrderID);
	}

	public boolean modifyCustomerOrder(Order modifyOrder, String Quantity, Account account) {   //modify -> re getCustomer

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

		Trip trip = Global.db.getTrip(modifyOrder.id);

		if(trip.lowerBound + inputQuantity <= trip.upperBound) // check inputQuantity range
			return Global.db.modifyOrder(account.id, modifyOrder.id, inputQuantity);
		else {
			errorMessage = "Modify Quantity illegal. Only remain: " + (trip.upperBound - trip.lowerBound);
			System.out.println(errorMessage);
			return false;
		} 	
	}
}


