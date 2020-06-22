package travel.booking.container;


/**
 * @author root
 *	exception when modify order in DB
 */
public class Edit_Exception extends Exception {
	private String message;

	public Edit_Exception(String message) {
		this.message = message; 
    }
	public String getMessage() {
		return message;
	}

}