package travel.booking.container;

import org.springframework.context.annotation.Bean;

import org.springframework.web.context.annotation.SessionScope;
import org.springframework.context.annotation.Configuration;


@Configuration
public class LoginInfo {
	public boolean islogin = false;
	public Account account;
	public String departure_date;
	public String search_location;
	public int search_person;
	public String select_trip_id;
	
	@Bean
	@SessionScope
	public LoginInfo loginInfoSession() {
	    return new LoginInfo();
	}
	
	public Boolean getLoginStatus() {
	    return this.islogin;
	}
	public Boolean setLoginStatus(Boolean newStatus) {
	    this.islogin = newStatus;
	    return this.getLoginStatus();
	}

}

