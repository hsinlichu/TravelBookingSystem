package travel.booking.controller;

import java.util.*;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import travel.booking.Global;
import travel.booking.container.*;

import java.text.*;

@Controller
@SessionAttributes({"resultSetting", "loginInfo"})
public class ResultPageController {
	@ModelAttribute("loginInfo")
	public LoginInfo addLoginInfo() {
		System.out.println("LoginInfo @ModelAttribute");
		return new LoginInfo();
	}
	@ModelAttribute("resultSetting")
	public ResultSetting addResultSetting() {
		System.out.println("result @ModelAttribute");
		return new ResultSetting();
	}

	private class ResultSetting {
		public String departure_date;
		public String location;
		public int price_from = Integer.MAX_VALUE;
		public int price_to = Integer.MIN_VALUE;
		public int show_price_from = Integer.MIN_VALUE;
		public int show_price_to = Integer.MAX_VALUE;
		public SortMethod sortMethod = SortMethod.PriceHigh2Low;
		public List<Trip> tripList = null;

		public ResultSetting() {
		}

		public String toString() {
			return departure_date + " " + location + " " +  price_from + " " +  price_to + " " + show_price_from + " " + show_price_to + " " +  sortMethod;
		}

	}

	private enum SortMethod {
		PriceHigh2Low, PriceLow2High, DateRecent2Far, DateFar2Recent
	}

	private static SimpleDateFormat FormatChecker = new SimpleDateFormat("yyyy-MM-dd");

	@RequestMapping(value = { "/result.html", "result" }, method = RequestMethod.GET)
	public String getResult(
			@RequestParam String location, @RequestParam String departure_date,
			@RequestParam(required = false)  String sortMethod,
			@RequestParam(required = false)  String amount,
			Model model, HttpSession session, RedirectAttributes redir) {
		System.out.println("============== Resultpage " + departure_date + " " + location + " " + sortMethod + " " + amount + " ==============");
		//Utility.printSession(session);
		//Utility.printModel(model);

		ResultSetting resultSetting = (ResultSetting) model.getAttribute("resultSetting");
		System.out.println("resultSetting" + resultSetting);
		try {
			FormatChecker.setLenient(false);
			Date select = FormatChecker.parse(departure_date);
			Date now = new Date();
			if(!select.after(now)) {
				redir.addFlashAttribute("msg", "Departure date should at least after today.");
				return "redirect:index";
			}
			resultSetting.departure_date = departure_date;
			System.out.println("departure_date: " + departure_date);
		} catch (ParseException e) {
			redir.addFlashAttribute("msg", "Input departure date invalid");
			return "redirect:index";
		}
		resultSetting.location = location;

		System.out.println("GetTrip");
		resultSetting.tripList = Global.db.getTrip(resultSetting.location, resultSetting.departure_date);
		for (Trip t : resultSetting.tripList) {
			if (t.price < resultSetting.price_from)
				resultSetting.price_from = t.price;
			if (t.price > resultSetting.price_to)
				resultSetting.price_to = t.price;
		}

		if(sortMethod != null) {
			try {
				System.out.println("Change sortMethod");
				resultSetting.sortMethod = SortMethod.valueOf(sortMethod);
			} catch (Exception e) {
				redir.addFlashAttribute("msg", "sortMethod Invalid");
				return "redirect:index";
			}
		}
		if(amount != null) {
			String[] show_range = amount.replace("$", "").split(" - ");

			try {
				int show_price_from = Integer.parseInt(show_range[0]);
				int show_price_to = Integer.parseInt(show_range[1]);
				
				if (resultSetting.show_price_from > show_price_to) {
					resultSetting.show_price_from = resultSetting.price_from;
					resultSetting.show_price_to = resultSetting.price_to;
					System.out.println("filter range illegal");
				}
				else {
					resultSetting.show_price_from = show_price_from;
					resultSetting.show_price_to = show_price_to;
				}	
				System.out.println("Change amount");
			} catch (Exception e) {
				redir.addFlashAttribute("msg", "amount Invalid");
				return "redirect:index";
			}
		}
		else {
			resultSetting.show_price_from = resultSetting.price_from;
			resultSetting.show_price_to = resultSetting.price_to;
			System.out.println("Amount not set" + resultSetting.show_price_from + " " + resultSetting.show_price_to);
		}
		System.out.println("resultSetting" + resultSetting);
		return "result";
	}

	@RequestMapping(path = "/GetAllTrip", produces = "application/json; charset=UTF-8")
	@ResponseBody
	public List<Trip> GetAllHotel(Model model, HttpSession session) {
		ResultSetting resultSetting = (ResultSetting) session.getAttribute("resultSetting");
		System.out.println("resultSetting" + resultSetting);

		List<Trip> subTripList = resultSetting.tripList;
		if (resultSetting.show_price_from != resultSetting.price_from || resultSetting.show_price_to != resultSetting.price_to) {
			System.out.println("Show range " + resultSetting.show_price_from + "~" + resultSetting.show_price_to);
			subTripList = new ArrayList<Trip>();
			for (int i = 0; i < resultSetting.tripList.size(); ++i) {
				Trip t = resultSetting.tripList.get(i);
				if (t.price >= resultSetting.show_price_from && t.price <= resultSetting.show_price_to)
					subTripList.add(t);
			}
		}

		switch (resultSetting.sortMethod) {
		case PriceLow2High:
			Collections.sort(subTripList, Trip.byPriceOrder());
			break;
		case PriceHigh2Low:
			Collections.sort(subTripList, Trip.byReversePriceOrder());
			break;
		case DateRecent2Far:
			Collections.sort(subTripList, Trip.byDateOrder());
			break;
		case DateFar2Recent:
			Collections.sort(subTripList, Trip.byReverseDateOrder());
			break;
		}

		session.setAttribute("resultSetting", resultSetting);
		return subTripList;
	}
}
