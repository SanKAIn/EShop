package com.kon.EShop.service;

import com.kon.EShop.MailSender;
import com.kon.EShop.SendHTMLEmail;
import com.kon.EShop.model.Orders;
import com.kon.EShop.model.State;
import com.kon.EShop.repository.impl.OrderImpl;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.util.List;

import static com.kon.EShop.util.EntityUtil.setToSession;

@Service
public class OrderService {
   private final OrderImpl orderImpl;
   private final MailSender mailSender;
   private final SendHTMLEmail htmlEmail;

   public OrderService(OrderImpl orderImpl, MailSender mailSender, SendHTMLEmail htmlEmail) {
      this.orderImpl = orderImpl;
      this.mailSender = mailSender;
      this.htmlEmail = htmlEmail;
   }

   public List<Orders> getAll(State state) {
      return orderImpl.getAll(state);
   }

   public Orders getOrder(Long id) {
      Orders order = orderImpl.get(id);
      if (order.getState() == State.NEW) {
         order.setState(State.PROCESSING);
         orderImpl.save(order);
      }
      return order;
   }

   public void delete(Long id) {
      orderImpl.delete(id);
   }

   public void save(Orders order, Long cartId) {
      order.setCartId(cartId);
      Orders newOrder = orderImpl.save(order);
      setToSession("orderId", newOrder.getId());
      String message = String.format(
          "<div>Добрый день, %s! \n" +
              "Добро пожаловать в наш магазин! Номер вашего заказа № %s , на сумму %s грн</div> \n " +
              "<div style=\"height: 25px; background: green\">fack</div>",
          order.getName(),
          newOrder.getId(),
          new DecimalFormat( "###,###.##" ).format(newOrder.getFullSum() * 1.0 / 100)
      );

      htmlEmail.send(order.getEmail(), "Заказ принят в обработку", message);
      mailSender.send("alfurm2@gmail.com", "Новый заказ", (order.getFullSum() * 1.0 / 100) + "грн");
   }

   public void aUpdate(Orders order) {
      Orders orderB = orderImpl.get(order.getId());
      orderB.setName(order.getName());
      orderB.setAddress(order.getAddress());
      orderB.setPhone(order.getPhone());
      orderB.setDelivery(order.getDelivery());
      orderB.setFullSum(order.getFullSum());
      orderB.setLastName(order.getLastName());
      orderB.setPayMethod(order.getPayMethod());
      orderB.setMiddleName(order.getMiddleName());
      orderB.setState(order.getState());
      orderImpl.update(orderB);
   }

}
