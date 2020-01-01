package lsl.java.web.service.impl;

import lsl.java.web.entity.Order;
import lsl.java.web.mapper.OrderDAO;
import lsl.java.web.service.OrderService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

@Component
public class IOrderService implements OrderService {
    @Resource
    private OrderDAO orderDAO;

    @Override
    public int insertOrder(Order order){
        return orderDAO.insertOrder(order);
    }

    /**
     * 获取正在处理或未处理的订单列表
     * @param shopId 商店id
     */
    @Override
    public List<Order> getUnFinishOrderByShopId(int shopId){
        return orderDAO.getUnFinishOrderByShopId(shopId);
    }

    /**
     *获取已经处理完的订单列表
     * @param shopId 商店id
     */
    @Override
    public List<Order> getFinishOrderByShopId(int shopId){
        return orderDAO.getFinishOrderByShopId(shopId);
    }

    /**
     * 获取用户的所有未完成订单，用户结束订单或者超过才算完成订单
     * @param customerId 用户id
     */
    @Override
    public List<Order> getUnFinishedOrderListByCustomerId(long customerId){
        return orderDAO.getUnFinishedOrderListByCustomerId(customerId);
    }

    /**
     * 更新订单状态，结束订单
     * @param id 订单id
     */
    @Override
    public int finishOrder(long id){
        return orderDAO.finishOrder(id);
    }

    /**
     * 更新订单状态信息，这里设置为已发货
     * @param id 订单id
     * TODO 应该设置为可扩展的，参数传递一个状态信息进行设置
     */
    @Override
    public int updateOrderState(long id){
        return orderDAO.updateOrderState(id);
    }
}
