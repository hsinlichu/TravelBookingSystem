package travel.booking.container;

/**
 * @author jameschu
 * store sever sent event content
 */
public class HelloMessage {
	private String name;

	  public HelloMessage() {
	  }

	  public HelloMessage(String name) {
	    this.name = name;
	  }

	  public String getName() {
	    return name;
	  }

	  public void setName(String name) {
	    this.name = name;
	  }

}