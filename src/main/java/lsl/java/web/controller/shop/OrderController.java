package lsl.java.web.controller.shop;


import lsl.java.web.common.Result;
import lsl.java.web.entity.Customer;
import lsl.java.web.entity.Order;
import lsl.java.web.entity.Shop;
import lsl.java.web.service.impl.ICustomerService;
import lsl.java.web.service.impl.IOrderService;
import lsl.java.web.utils.ShopCacheUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/shop")
public class OrderController {

    @Autowired
    IOrderService orderService;

    @Autowired
    ICustomerService customerService;
    /**
     * 请求访问商店的订单管理页面
     */
    @RequestMapping("/{shopId}/orderManage")
    public String orderManagePage(HttpServletRequest request, @PathVariable("shopId")int shopId, Model model){
        Shop shop= ShopCacheUtil.getShopBySession(request);
        //登录超时则跳转到登录界面
        if(shop==null){
            return "shop/login";
        }
        //注入商店数据
        model.addAttribute("shop",shop);
        //注入订单数据，MAP---Order与Customer
        Map<Order, Customer> unFinishOrderMap=new HashMap<>();
        Map<Order, Customer> finishOrderMap=new HashMap<>();
        List<Order> unFinishOrderList=new ArrayList<>();
        List<Order> finishOrderList=new ArrayList<>();

        //尝试获取未处理订单页面
        try{
            //获取未结束订单页面，由isFinish标识
            unFinishOrderList=orderService.getUnFinishOrderByShopId(shopId);
            for(Order order:unFinishOrderList){
                Customer customer=customerService.getCustomerByCustomerId(order.getCustomerId());//尝试获取用户信息
                unFinishOrderMap.put(order,customer);
            }
            //获取已经处理完的订单列表
            finishOrderList=orderService.getFinishOrderByShopId(shopId);
            for(Order order:finishOrderList){
                Customer customer=customerService.getCustomerByCustomerId(order.getCustomerId());//尝试获取用户信息
                finishOrderMap.put(order,customer);
            }
            //注入订单列表属性
            model.addAttribute("unFinishOrderMap",unFinishOrderMap);
            model.addAttribute("finishOrderMap",finishOrderMap);
        }catch (Exception e){
            e.printStackTrace();
        }

        return "shop/orderManage";
    }

    /**
     * Ajax请求更新订单状态信息
     * @param request 请求
     * @param orderId 订单ID
     * TODO 订单状态信息应该是可扩展的，传递订单状态参数信息，但这里是在SQl中写死了，直接设置为已发货
     */
    @RequestMapping("/order/stateUpdate/{orderId}")
    @ResponseBody
    public Result shopUpdateOrderState(HttpServletRequest request,@PathVariable("orderId")long orderId){
        Shop shop= ShopCacheUtil.getShopBySession(request);
        //登录超时则跳转到登录界面
        if(shop==null){
            return new Result("登录超时，请登录",Result.CLIENT_ERROR);
        }
        //简单的订单状态更新
        try {
            orderService.updateOrderState(orderId);
            return new Result("已更新",Result.OK);
        }catch (Exception e){
            e.printStackTrace();
            return new Result("数据库响应失败，请重试",Result.SERVER_ERROR);
        }
    }
}
