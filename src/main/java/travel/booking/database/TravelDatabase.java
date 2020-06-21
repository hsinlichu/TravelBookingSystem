package travel.booking.database;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import travel.booking.container.Order;
import travel.booking.container.Account;
import travel.booking.container.Trip;
import travel.booking.container.Response;

import java.util.*;

import javax.swing.plaf.synth.Region;


public class TravelDatabase extends Database {
	private String dataDir;
	private Gson gson;

	public TravelDatabase(String dbPath, String initSQLPath, String dataDir) {
		super(dbPath, initSQLPath);
		this.dataDir = dataDir;
		this.gson = new Gson();
		if(len("Trip") == 0) establishDB(dataDir);
	}

	private void establishDB(String dataDir) { 
		// establish the db from the data in dataDir i.e. travel_code.json , trip_data_all.csv
		JsonTravelCode[] travelCodes = read_travel_code(dataDir + "/travel_code.json");
		List<HashMap<String, String>> trips = read_trips(dataDir + "/trip_data_all.csv");
		for(HashMap<String, String> trip: trips) {
			trip.put("trip_id", UUID.randomUUID().toString());
			insert("Trip", trip);
		}
		for(JsonTravelCode travelCode: travelCodes) {
			HashMap<String, String> attr = new HashMap<>();
			attr.put("travel_code", travelCode.travelCode);
			attr.put("travel_code_name", travelCode.travelCodeName);
			insert("TravelCode", attr); 
		}
	}

	private JsonTravelCode[] read_travel_code(String datapath){
		// Read the travel codes from the datapath
		Gson gson = new Gson();
		try {
			File f = new File(datapath);
			InputStreamReader read = new InputStreamReader(new FileInputStream(f),"utf-8"); 
			BufferedReader br = new BufferedReader(read);
			JsonTravelCode[] hotels = gson.fromJson(br, JsonTravelCode[].class);
			return hotels;
			//
		}catch (IOException e) {
			System.out.println("File not exist!");
			return null;
		}
	}

	private List<HashMap<String, String>> read_trips(String dataPath){
		try {
			InputStreamReader isr = new InputStreamReader(new FileInputStream(dataPath)); 
			BufferedReader reader = new BufferedReader(isr);
			String line = null;
			String[] columnNames = null;
			List<HashMap<String, String>> rows = new ArrayList<HashMap<String, String>>();
			line = reader.readLine();
			columnNames = line.split(",");
			while((line=reader.readLine()) !=null){
				String item[] = line.split(",");
				if(item.length != columnNames.length) continue;
				HashMap<String, String> row = new HashMap<>();
				for(int i = 0; i < columnNames.length; i++) {
					row.put(columnNames[i], item[i].trim());
				}
				rows.add(row);
			} 
			reader.close();
			return rows;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/************* Account ***************/
	public Account addAccount(String name, String email, String password){
		HashMap<String, String> attr = new HashMap<>();
		String uuid = UUID.randomUUID().toString();
		attr.put("account_id", uuid);
		attr.put("name", name);
		attr.put("email", email);
		attr.put("password", hashPassword(password));
		if(this.insert("Account", attr)) return getAccount(uuid);
		else return null;
	}
	
	public Account updateAccount(Account account, String name, String email, String password){
		// addAccount will fail if account with same email has existed.
		// todo: return Account 
		HashMap<String, String> attr = new HashMap<>();
		attr.put("name", name);
		attr.put("email", email);
		attr.put("password", hashPassword(password));
		HashMap<String, String> cond_attr = new HashMap<>();
		cond_attr.put("email", account.email);
		if(this.update("Account", attr, cond_attr)) return getAccount(account.id);
		else return null;
	}

	public Account getAccount(String account_id){
		List<Account> accounts = new ArrayList<Account>();
		HashMap<String, String> attr = new HashMap<>();
		attr.put("account_id", account_id);
		List<HashMap<String, String>> results = this.select("Account", attr);
		for(HashMap<String, String> result: results){
			Account account = gson.fromJson(gson.toJson(result), Account.class);
			accounts.add(account);
		}
		if(accounts.size() == 0){
			return null;
		}else{
			return accounts.get(0);
		}
	}

	public Account verifyAccount(String email, String password){
		HashMap<String, String> attr = new HashMap<>();
		attr.put("email", email);
		attr.put("password", hashPassword(password));
		List<HashMap<String, String>> account = this.select("Account", attr);
		if(account.size() > 0) return gson.fromJson(gson.toJson(account.get(0)), Account.class);
		else return null;
	}
	
	/************* Trip ***************/
	public List<Trip> getTrip(String region, String startDate) {
		HashMap<String, String> attr;
		List<HashMap<String, String>> traveCodes = new ArrayList<>();
		if(region != null) {
			attr = new HashMap<String, String>();
			attr.put("travel_code_name", region);
			traveCodes = selectContains("TravelCode", attr);
		}
		List<HashMap<String, String>> tripsMap = new ArrayList<>();
		if(region != null) {
			for(HashMap<String, String> travelCode: traveCodes) {
				attr = new HashMap<String, String>();
				attr.put("travel_code=", travelCode.get("travel_code"));
				if(startDate != null) attr.put("start_date>", startDate);
				tripsMap.addAll((selectCondition("Trip", attr)));
			}
		}else {
			attr = new HashMap<String, String>();
			if(startDate != null) {
				attr.put("start_date>", startDate);
				tripsMap.addAll((selectCondition("Trip", attr)));
			}else {
				tripsMap.addAll(selectAll("Trip"));
			}
		}
		
		Gson gson = new Gson();
		List<Trip> trips = new ArrayList<>();
		for(HashMap<String, String> tripMap : tripsMap) {
			String travelCodeName = getTripCodeName(tripMap.get("travel_code"));
			//System.out.println(travelCodeName);
			tripMap.put("travel_code_name", travelCodeName);
			tripMap.put("remain_sits", String.valueOf(getRemainTripQuantity(tripMap.get("trip_id"))));
			String jsonFormat = gson.toJson(tripMap);
			Trip trip = gson.fromJson(jsonFormat, Trip.class);
			trips.add(trip);
		}
		
		return trips;
	}
	
	public Trip getTrip(String tripID){
		HashMap<String, String> attr = new HashMap<>();
		attr.put("trip_id", tripID);
		List<HashMap<String, String>> results = select("Trip", attr);
		if(results.size() == 0) return null;
		HashMap<String, String> result = results.get(0);
		String travelCodeName = getTripCodeName(result.get("travel_code"));
		result.put("travel_code_name", travelCodeName);
		result.put("remain_sits", String.valueOf(getRemainTripQuantity(tripID)));
		String jsonFormat = gson.toJson(result);
		Trip trip = gson.fromJson(jsonFormat, Trip.class);
		return trip;
	}
	
	public String getTripCodeName(String travelCode) {
		HashMap<String, String> attr = new HashMap<>();
		attr.put("travel_code", travelCode);
		List<HashMap<String, String>> results = select("TravelCode", attr);
		if(results.size() == 0) return null;
		else return results.get(0).get("travel_code_name");
	}
	
	public int getTripQuantity(String tripID) {
		HashMap<String, String> attr = new HashMap<>();
		attr.put("trip_id", tripID);
		List<HashMap<String, String>> results = select("Order", attr);
		if(results.size() == 0) return 0;
		int total = 0;
		for(HashMap<String, String> result: results) {
			total += Integer.parseInt(result.get("quantity"));
		}
		return total;
	}
	
	public int getRemainTripQuantity(String tripID) {
		return getUpperBound(tripID) - getTripQuantity(tripID);
	}
	
	public int getUpperBound(String tripID) {
		HashMap<String, String> attr = new HashMap<>();
		attr.put("trip_id", tripID);
		List<HashMap<String, String>> results = select("Trip", attr);
		if(results.size() == 0) return -1;
		int upperBound = Integer.parseInt(results.get(0).get("upper_bound"));
		return upperBound;
	}
	
	public int getLowerBound(String tripID) {
		HashMap<String, String> attr = new HashMap<>();
		attr.put("trip_id", tripID);
		List<HashMap<String, String>> results = select("Trip", attr);
		if(results.size() == 0) return -1;
		int lowerBound = Integer.parseInt(results.get(0).get("lower_bound"));
		return lowerBound;
	}
	
	/*********** Order ***************/
	
	public Response addOrder(String accountID, String tripID, int quantity){
		// Add a new order 
		HashMap<String, String> attr = new HashMap<>();
		attr.put("order_id", UUID.randomUUID().toString());
		attr.put("account_id", accountID);
		attr.put("trip_id", tripID);
		attr.put("quantity", Integer.toString(quantity));
		int remainingSits = getRemainTripQuantity(tripID);
		if(remainingSits < quantity) {
			return new Response("Failed", "No remaining sits.");
		}else {
			insert("Order", attr);
			return new Response("OK");
		}
		
	}

	public List<Order> getOrder(String account_id){
		// Get all the orders associate the the account
		HashMap<String, String> attr = new HashMap<>();
		attr.put("account_id", account_id);
		List<HashMap<String, String>> results = this.select("Order", attr);
		List<Order> orders = new ArrayList<Order>();
		for(HashMap<String, String> result: results){
			orders.add(gson.fromJson(gson.toJson(result), Order.class));
		}
		return orders;
	}
	
	public boolean modifyOrder(String accountID, String orderID, int quantity) {
		HashMap<String, String> attr, cond_attr;
		List<HashMap<String, String>> results;
		attr = new HashMap<String, String>();
		attr.put("order_id", orderID);
		results = select("Order", attr);
		// Failed if there is no order associate with the orderID
		if(results.size() == 0) return false;
		cond_attr = new HashMap<String, String>();
		cond_attr.put("quantity", Integer.toString(quantity));
		if(update("Order", cond_attr, attr)) return true;
		else return false;
	}
	
	public boolean cancelOrder(String accountID, String orderID) {
		HashMap<String, String> attr;
		List<HashMap<String, String>> results;
		attr = new HashMap<String, String>();
		attr.put("order_id", orderID);
		results = select("Order", attr);
		// Failed if there is no order associate with the orderID
		if(results.size() == 0) return false;
		if(delete("Order", attr)) return true;
		else return false;
	}
	
}
 
class JsonTravelCode{

	@SerializedName("travel_code")
	public String travelCode; 

	@SerializedName("travel_code_name")
	public String travelCodeName;
}

