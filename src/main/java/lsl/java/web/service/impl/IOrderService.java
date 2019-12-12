package lsl.java.web.service.impl;

import lsl.java.web.entity.Order;
import lsl.java.web.mapper.OrderDAO;
import lsl.java.web.service.OrderService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class IOrderService implements OrderService {
    @Resource
    private OrderDAO orderDAO;

    @Override
    public int insertOrder(Order order){
        return orderDAO.insertOrder(order);
    }
}
