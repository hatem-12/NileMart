package org.route;

import org.springframework.stereotype.Component;

@Component
public class SmsNotificationSender implements NotificationSender{

    @Override

    public void send(Customer customer, String message) {
      if (customer!=null && !customer.phone().trim().isEmpty()){
          System.out.println("[SMS] to " + customer.phone() + ": " + message);
      }
    }
}
