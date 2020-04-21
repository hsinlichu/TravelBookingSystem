/**
 * 
 */
package travel.booking.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;


/**
 * @author root
 *
 */
@Controller
public class HomePageController {
	
	@RequestMapping(value={"", "/", "index.html", "index"}, method=RequestMethod.GET, params = {})
    public String getHomePage(Model model) {
		
    	System.out.println("Home Page");
        return "index";
    }
}