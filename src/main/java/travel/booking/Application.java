package travel.booking;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.google.gson.Gson;


import travel.booking.database.*;
import travel.booking.container.*;
import java.util.*;

@SpringBootApplication
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
		Global.db = new TravelDatabase("database/TravelDB.db", "database/init_table.sql", "resource");
		
		Gson gson = new Gson();
		Account userA = Global.db.addAccount("UserA", "UserA@gmail.com", "aaa"); 
		userA  = Global.db.verifyAccount("UserA@gmail.com", "aaa"); 
		System.out.println(userA);
		List<Trip> trips = Global.db.getTrip("美國", "2020-07-31");	
		System.out.println(trips);
		Global.db.addOrder(userA.id, trips.get(0).id, 10);
		List<Order> orders = Global.db.getOrder(userA.id);
		System.out.println(orders); 
		System.out.println(Global.db.modifyOrder(userA.id, orders.get(0).id, 12));
		System.out.println(Global.db.cancelOrder(userA.id, orders.get(0).id));
		Trip trip = Global.db.getTrip("7561118d-54f6-4cb7-843d-500c3f82fcdb");
		System.out.println(trip);
	}

}
