package travel.booking.container;

import java.util.List;

/**
 * @author jameschu
 *	AjaxResponseBody return to view to show
 */
public class AjaxResponseBody {

    public String msg;
    public List<Order> result;
    
    public void setMsg(String msg) {
        this.msg = msg;
    }
}