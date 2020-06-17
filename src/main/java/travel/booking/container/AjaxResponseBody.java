package travel.booking.container;

import java.util.List;

public class AjaxResponseBody {

    public String msg;
    public List<Order> result;
    
    public void setMsg(String msg) {
        this.msg = msg;
    }

    //getters and setters

}