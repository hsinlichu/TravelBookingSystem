package travel.booking.container;

import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.context.annotation.SessionScope;
import org.springframework.context.annotation.Configuration;


@Configuration
public class LoginInfo {
	public boolean islogin = false;
	public Account account;
	
	/*
	 * @Bean
	 * 
	 * @SessionScope public LoginInfo loginInfoSession() {
	 * System.out.println("loginInfoSession!!!"); return new LoginInfo(); }
	 */
	public String toString() {
		return islogin + " " + account;
	}
	
	public Boolean getLoginStatus() {
	    return this.islogin;
	}
	
	public Boolean setLoginStatus(Boolean newStatus) {
	    this.islogin = newStatus;
	    return this.getLoginStatus();
	}
	
	public void update(boolean islogin, Account account) {
		this.islogin = islogin;
		this.account = account;
	}

}

