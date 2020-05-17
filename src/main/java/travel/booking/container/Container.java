package travel.booking.container;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class Container {
	private transient Gson gson = new GsonBuilder().setPrettyPrinting().create();
	
	@Override
	public String toString() {
		return gson.toJson(this);
	}
}
