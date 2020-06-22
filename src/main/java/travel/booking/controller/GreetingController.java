package travel.booking.controller;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import travel.booking.container.*;

/**
 * @author 	jameschu
 * class remain our people also searching for this trip
 */
@Controller
public class GreetingController {
  @MessageMapping("/{message.getName()}")
  @SendTo("/topic/{message.getName()}")
  public Greeting greeting(HelloMessage message) throws Exception {
    System.out.println(message.getName());
    return new Greeting("有人正在查看同一個行程，要買要快！");
  }

}
