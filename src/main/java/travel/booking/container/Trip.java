package travel.booking.container;

import java.io.Serializable;

import com.google.gson.annotations.SerializedName;

public class Trip extends Container implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@SerializedName("trip_id")
	public String id;
	@SerializedName("title")
	public String title;
	@SerializedName("travel_code")
	public int travelCode;
	@SerializedName("travel_code_name")
	public String travelCodeName;
	@SerializedName("product_key")
	public String productKey;
	@SerializedName("price")
	public int price;
	@SerializedName("start_date")
	public String startDate;
	@SerializedName("end_date")
	public String endDate;
	@SerializedName("lower_bound")
	public int lowerBound;
	@SerializedName("upper_bound")
	public int upperBound;
	
}
