package travel.booking.container;

import java.util.Enumeration;
import java.util.Map;
import org.springframework.ui.Model;
import javax.servlet.http.HttpSession;

public class Utility {
	public static void printSession(HttpSession session) {
		System.out.println("--- Session data ---");
		Enumeration<String> f = session.getAttributeNames();
		while (f.hasMoreElements()) {
			String s = f.nextElement();
			System.out.println(s + " -> " + session.getAttribute(s));
		}
		System.out.println("--- Session data end ---");
	}
	public static void printModel(Model model) {
		System.out.println("--- Model data ---");
		Map modelMap = model.asMap();
		for (Object modelKey : modelMap.keySet()) {
			Object modelValue = modelMap.get(modelKey);
			System.out.println(modelKey + " -- " + modelValue);
		}
		System.out.println("--- Model data end ---");
	}

}
