package travel.booking.container;

import java.io.Serializable;

import com.google.gson.annotations.SerializedName;

public class Order extends Container implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@SerializedName("order_id")
	public String id;
	@SerializedName("account_id")
	public String accountID;
	@SerializedName("trip_id")
	public String tripID;
	@SerializedName("quantity")
	public int quantity;
	
}
