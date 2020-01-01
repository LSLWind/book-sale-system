package lsl.java.web.controller.mall;

import com.sun.org.apache.xpath.internal.operations.Mod;
import lsl.java.web.controller.common.ChatServer;
import lsl.java.web.entity.Customer;
import lsl.java.web.entity.Message;
import lsl.java.web.entity.Shop;
import lsl.java.web.service.impl.IMessageService;
import lsl.java.web.service.impl.IShopService;
import lsl.java.web.utils.InfoCacheUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Controller
@RequestMapping("/chat")
public class ChatController {
    @Autowired
    ChatServer chatServer;

    @Autowired
    IMessageService messageService;

    @Autowired
    IShopService shopService;

    /**
     * 进入个人消息页面
     * @param customerId 用户id
     * @return 消息通知页面
     */
    @RequestMapping("/{customerId}")
    public String messagePage(HttpServletRequest request, @PathVariable("customerId")int customerId, Model model){
        Customer customer= InfoCacheUtil.getCustomerByRequest(request);
        if(customer==null){
            return "mall/login";
        }
        //注入用户属性
        model.addAttribute("customer",customer);
        //注入频道属性

        model.addAttribute("channelList",InfoCacheUtil.getChannelList());

        //获取通信过的商店的id，通过sql保证id唯一，使用map将消息与商店映射，按日期排序
        List<Message> messageShop=messageService.getAboutShopMessageByCustomerId(customerId);//该方法调用的message只有用户id与商店id
        Map<Message,Shop>messageShopMap=new TreeMap<>((Message a,Message b)->{
            //将日期转换为long类型进行比较
            long ad= Long.valueOf(a.getDate().replaceAll("[-\\s:]",""));
            long bd=Long.valueOf(b.getDate().replaceAll("[-\\s:]",""));
            if(ad>bd)return 1;
            else if (ad==bd)return 0;
            return -1;
        });//按消息的最新时间排列
        for (Message message:messageShop){
            Message newMessage=messageService.getNewMessageByCustomerIdAndShopId(customerId,message.getShopId());//最新消息
            Shop shop=shopService.getShopNameByShopId(message.getShopId());//对应的商店
            messageShopMap.put(newMessage,shop);
        }
        //注入消息属性
        model.addAttribute("messageShopMap",messageShopMap);

        return "mall/message";
    }

    /**
     * 进入用户与商店的聊天室页面，内部将使用webSocket做通信
     */
    @RequestMapping("/{customerId}/{shopId}")
    public String chatRoomPage(HttpServletRequest request, @PathVariable("customerId")int customerId,
                               @PathVariable("shopId")int shopId, Model model){
        Customer customer= InfoCacheUtil.getCustomerByRequest(request);
        if(customer==null){
            return "mall/login";
        }
        //注入用户属性
        model.addAttribute("customer",customer);
        //注入频道属性
        model.addAttribute("channelList",InfoCacheUtil.getChannelList());
        //注入聊天的商店信息
        Shop shop=shopService.getShopNameByShopId(shopId);
        model.addAttribute("shop",shop);
        //注入初始聊天信息，在sql中已经按照时间排序
        List<Message> messageList=messageService.getOrderMessageListByCustomerIdAndShopId(customerId,shopId);
        model.addAttribute("messageList",messageList);
        messageService.updateMessageState(customerId,shopId);//更新所有消息为已读

        return "mall/chat";
    }

}
