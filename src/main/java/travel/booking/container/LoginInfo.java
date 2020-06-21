package travel.booking.container;

import org.springframework.context.annotation.Configuration;


@Configuration
public class LoginInfo {
	public boolean islogin = false;
	public Account account;
	
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

