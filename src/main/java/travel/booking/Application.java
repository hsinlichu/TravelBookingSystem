package travel.booking;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import travel.booking.database.*;

@SpringBootApplication
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
		
		TravelDatabase dbDatabase = new TravelDatabase("database/TravelDB.db", "database/init_table.sql", "resource");
	}

}
