package travel.booking.container;

import java.io.Serializable;

import com.google.gson.annotations.SerializedName;

public class Account extends Container implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@SerializedName("account_id")
	public String id;
	@SerializedName("email")
	public String email;
	@SerializedName("name")
	public String name;


}
