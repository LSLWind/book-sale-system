package lsl.java.web.controller.shop;


import lsl.java.web.entity.Customer;
import lsl.java.web.entity.Message;
import lsl.java.web.entity.Shop;
import lsl.java.web.service.impl.ICustomerService;
import lsl.java.web.service.impl.IMessageService;
import lsl.java.web.utils.ShopCacheUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.websocket.server.PathParam;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

@Controller
@RequestMapping("/shop/chat")
public class ShopChatController {
    @Autowired
    IMessageService messageService;
    @Autowired
    ICustomerService customerService;

    @RequestMapping("/message")
    public String messageManagePage(HttpServletRequest request, Model model){
        Shop shop= ShopCacheUtil.getShopBySession(request);
        //登录超时则跳转到登录界面
        if(shop==null){
            return "shop/login";
        }
        //注入商店数据
        model.addAttribute("shop",shop);
        //注入用户消息，message与customer对应
        List<Message> messageCustomerList=messageService.getMessageListByShopId(shop.getId());
        Map<Message,Customer> messageCustomerMap=new TreeMap<>((Message a,Message b)->{//每条消息对应一个用户，按照消息时间排序
            //将日期转换为long类型进行比较
            long ad= Long.valueOf(a.getDate().replaceAll("[-\\s:]",""));
            long bd=Long.valueOf(b.getDate().replaceAll("[-\\s:]",""));
            if(ad>bd)return 1;
            else if (ad==bd)return 0;
            return -1;
        });
        for(Message message:messageCustomerList){
            Customer customer=customerService.getCustomerByCustomerId(message.getCustomerId());
            Message realMessage=messageService.getNewMessageByCustomerIdAndShopId(customer.getId(),shop.getId());//获取通信到的最新消息
            messageCustomerMap.put(realMessage,customer);
        }
        model.addAttribute("messageCustomerMap",messageCustomerMap);
        return "shop/message";
    }

    @RequestMapping("/{shopId}/{customerId}")
    public String customerChatServer(HttpServletRequest request, @PathVariable("shopId")int shopId, @PathVariable("customerId")int customerId,Model model){
        Shop shop= ShopCacheUtil.getShopBySession(request);
        //登录超时则跳转到登录界面
        if(shop==null){
            return "shop/login";
        }
        //注入商店数据
        model.addAttribute("shop",shop);
        //注入聊天的用户数据
        Customer customer=customerService.getCustomerByCustomerId(customerId);
        model.addAttribute("customer",customer);
        //注入初始聊天信息，在sql中已经按照时间排序
        List<Message> messageList=messageService.getOrderMessageListByCustomerIdAndShopId(customerId,shopId);
        model.addAttribute("messageList",messageList);
        messageService.updateMessageState(customerId,shopId);//更新所有消息为已读

        return "shop/chat";
    }
}
