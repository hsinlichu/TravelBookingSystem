package travel.booking.container;

import java.io.Serializable;

import com.google.gson.annotations.SerializedName;

public class Response extends Container implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@SerializedName("status")
	public String status;
	@SerializedName("message")
	public String message;
	
	public Response(String status) {
		this.status = status;
	}
	
	public Response(String status, String message) {
		this.status = status;
		this.message = message;
	}
}
