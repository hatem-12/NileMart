package org.route;

import jakarta.annotation.PreDestroy;
import lombok.AccessLevel;
import lombok.Getter;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Component
@Getter
public class OrderRegistry {
    @Getter(AccessLevel.NONE)
  private final Map<String,Order> orders=new HashMap<>();
    private BigDecimal totalRevenue=BigDecimal.valueOf(0);

    public String createOrderNumber(){
    String date = LocalDate.now()
            .format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        return "NM-" + date + "-" + String.format("%05d", orders.size() + 1);
}
public void addOrder(Order order){
        orders.put(order.orderNumber(),order);
}
private BigDecimal totalRevenue(){
        BigDecimal revenue=BigDecimal.ZERO;
        for (Map.Entry<String,Order> o:orders.entrySet()){
            revenue=revenue.add(o.getValue().total());
        }
        totalRevenue=revenue;
        return totalRevenue;
}
@PreDestroy
    public void printOrdersInfo(){
    DecimalFormat df = new DecimalFormat(
            "#,##0.00",
            DecimalFormatSymbols.getInstance(Locale.US)
    );

    System.out.println("End of day: " + orders.size()
            + " orders, revenue " + df.format(totalRevenue()) + " EGP");
}
}
