package travel.booking.database;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import java.util.*;

public class TravelDatabase extends Database {
	private String dataDir;

	public TravelDatabase(String dbPath, String initSQLPath, String dataDir) {
		super(dbPath, initSQLPath);
		this.dataDir = dataDir;
		if(len("Trip") == 0) establishDB(dataDir);
	}

	private void establishDB(String dataDir) { 
		// establish the db from the data in dataDir i.e. travel_code.json , trip_data_all.csv
		JsonTravelCode[] travelCodes = read_travel_code(dataDir + "/travel_code.json");
		List<HashMap<String, String>> trips = read_trips(dataDir + "/trip_data_all.csv");
		for(HashMap<String, String> trip: trips) insert("Trip", trip);
		for(JsonTravelCode travelCode: travelCodes) {
			HashMap<String, String> attr = new HashMap<>();
			attr.put("travel_code", travelCode.travelCode);
			attr.put("travel_code_name", travelCode.travelCodeName);
			insert("TravelCode", attr);
		}
		System.out.println(travelCodes[0].travelCodeName);
		System.out.println(trips.get(0).get("title"));
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
}

class JsonTravelCode{

	@SerializedName("travel_code")
	public String travelCode; 

	@SerializedName("travel_code_name")
	public String travelCodeName;
}

