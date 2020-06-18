package travel.booking.controller;

import java.util.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;

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

	private static SimpleDateFormat originFormat = new SimpleDateFormat("MM/dd/yyyy");
	private static SimpleDateFormat newFormat = new SimpleDateFormat("yyyy-MM-dd");

	@RequestMapping(value = { "/result.html", "result" }, method = RequestMethod.POST)
	public String getResult(@RequestParam String location, @RequestParam String departure_date, Model model,
			HttpSession session) {
		System.out.println("Resultpage " + departure_date + " " + location);
		System.out.println("--- Session data ---");
		Enumeration<String> f = session.getAttributeNames();
		while (f.hasMoreElements()) {
			String s = f.nextElement();
			System.out.println(s + " -> " + session.getAttribute(s));
		}

		System.out.println("--- Model data ---");
		Map modelMap = model.asMap();
		for (Object modelKey : modelMap.keySet()) {
			Object modelValue = modelMap.get(modelKey);
			System.out.println(modelKey + " -- " + modelValue);
		}
		
		ResultSetting resultSetting = (ResultSetting) model.getAttribute("resultSetting");
		System.out.println(resultSetting);
		try {
			departure_date = newFormat.format(originFormat.parse(departure_date));
			resultSetting.departure_date = departure_date;
			System.out.println("After formatter: " + departure_date);
		} catch (ParseException e) {
			String newurl = "redirect:/";
			newurl += ("?msg=" + 9);
			return newurl;
		}
		resultSetting.location = location;
		
		System.out.println("GetTrip");
		resultSetting.tripList = Global.db.getTrip(resultSetting.location, resultSetting.departure_date);
		for (Trip t : resultSetting.tripList) {
			System.out.print("1");
			if (t.price < resultSetting.price_from)
				resultSetting.price_from = t.price;
			if (t.price > resultSetting.price_to)
				resultSetting.price_to = t.price;
		}
		resultSetting.show_price_from = resultSetting.price_from;
		resultSetting.show_price_to = resultSetting.price_to;
		System.out.println("\n" + resultSetting.show_price_from + " " + resultSetting.show_price_to);
		

		System.out.println("done");
		return "result";
	}

	@RequestMapping(value = "/result", method = RequestMethod.GET, params = { "sortMethod", "amount"})
	public String setFilterSort(@RequestParam String sortMethod, @RequestParam String amount, Model model, HttpSession session) {
		System.out.println("setFilterSort");
		
		ResultSetting resultSetting = (ResultSetting) session.getAttribute("resultSetting");
		System.out.println("Origin:" + resultSetting);

		String[] show_range = amount.replace("$", "").split(" - ");
		int show_price_from = Integer.parseInt(show_range[0]);
		int show_price_to = Integer.parseInt(show_range[1]);
		resultSetting.sortMethod = SortMethod.valueOf(sortMethod);

		if (resultSetting.show_price_from > show_price_to) {
			resultSetting.show_price_from = resultSetting.price_from;
			resultSetting.show_price_to = resultSetting.price_to;
			System.out.println("filter range illegal");
		}
		else {
			resultSetting.show_price_from = show_price_from;
			resultSetting.show_price_to = show_price_to;
		}

		System.out.println(show_price_from + " " + show_price_to + " " + sortMethod);
		System.out.println("After:" + resultSetting);
		session.setAttribute("resultSetting", resultSetting);
		return "result";
	}

	@RequestMapping(path = "/GetAllTrip", produces = "application/json; charset=UTF-8")
	@ResponseBody
	public List<Trip> GetAllHotel(Model model, HttpSession session) {
		ResultSetting resultSetting = (ResultSetting) session.getAttribute("resultSetting");
		System.out.println(resultSetting);
		
		
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

		session.setAttribute("resultSetting", resultSetting);;
		System.out.println("successful searching");

		return subTripList;
	}
}
