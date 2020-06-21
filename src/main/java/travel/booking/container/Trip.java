package travel.booking.container;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Comparator;

import com.google.gson.annotations.SerializedName;

import java.util.Date; 

public class Trip extends Container implements Serializable, Comparator<Trip>{
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
	@SerializedName("remain_sits")
	public int remainSits;
	
	public String toString() {
		return id + " " + title + " " +  travelCode + " " +  travelCodeName + " " + productKey + " " + price + " " +  startDate + " " +  endDate + " " +  lowerBound + " " + upperBound;
	}
	
	
	public static Comparator<Trip> byPriceOrder(){
    	return new ByPriceOrder();	
    }
    
    private static class ByPriceOrder implements Comparator<Trip>{
    	@Override
        public int compare(Trip tripA, Trip tripB) { 
            return ((Integer)tripA.price).compareTo((Integer)tripB.price); // compare price in ascending order
        }
    }
    public static Comparator<Trip> byReversePriceOrder(){
    	return new ByReversePriceOrder();	
    }
    
    private static class ByReversePriceOrder implements Comparator<Trip>{
    	@Override
        public int compare(Trip tripA, Trip tripB) { 
            return - ((Integer)tripA.price).compareTo((Integer)tripB.price); // compare price in ascending order
        }
    }
    public static Comparator<Trip> byDateOrder(){
    	return new ByDateOrder();	
    }
    
    private static class ByDateOrder implements Comparator<Trip>{
    	@Override
        public int compare(Trip tripA, Trip tripB) {
    		try {
    			Date tripA_date=new SimpleDateFormat("yyyy-MM-dd").parse(tripA.startDate);
        		Date tripB_date=new SimpleDateFormat("yyyy-MM-dd").parse(tripB.startDate);
        		return (tripA_date).compareTo(tripB_date); // compare date in ascending order
    		}
    		catch(Exception e){
    			throw new RuntimeException("Can not pharse date");
    		}
        }
    }
    
    public static Comparator<Trip> byReverseDateOrder(){
    	return new ByReverseDateOrder();	
    }
    
    private static class ByReverseDateOrder implements Comparator<Trip>{
    	@Override
        public int compare(Trip tripA, Trip tripB) {
    		try {
    			Date tripA_date=new SimpleDateFormat("yyyy-MM-dd").parse(tripA.startDate);
        		Date tripB_date=new SimpleDateFormat("yyyy-MM-dd").parse(tripB.startDate);
        		return - (tripA_date).compareTo(tripB_date); // compare date in ascending order
    		}
    		catch(Exception e){
    			throw new RuntimeException("Can not pharse date");
    		}
        }
    }

	@Override
	public int compare(Trip tripA, Trip tripB) {
		return ((Integer)tripA.price).compareTo((Integer)tripB.price);
	}
	
	

    
}
