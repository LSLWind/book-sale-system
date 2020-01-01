package lsl.java.web.service;

import lsl.java.web.entity.Order;

import java.util.List;

public interface OrderService {
    int insertOrder(Order order);

    List<Order> getUnFinishOrderByShopId(int shopId);

    List<Order> getFinishOrderByShopId(int shopId);

    List<Order> getUnFinishedOrderListByCustomerId(long customerId);

    int finishOrder(long id);

    int updateOrderState(long id);
}
