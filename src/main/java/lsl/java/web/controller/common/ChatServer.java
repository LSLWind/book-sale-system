package lsl.java.web.controller.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import lsl.java.web.entity.Message;
import lsl.java.web.service.impl.IMessageService;
import lsl.java.web.utils.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

//聊天服务器,WebSocket
@ServerEndpoint(value = "/lsl/chat/{userId}/{shopId}/{isToShop}",configurator = MySpringConfigurator.class)
@Component
public class ChatServer {
    //创建一个线程安全的map，表示用户与该服务器之间的连接，每个客户端都会有一个ChatSer
    private static Map<Integer,Session> users = Collections.synchronizedMap(new HashMap());
    private static Map<Integer,Session> shops=Collections.synchronizedMap(new HashMap());

    //用于对象的序列化
    private ObjectMapper objectMapper=new ObjectMapper();

    //将消息保存到数据库当中
    @Autowired
    IMessageService messageService;

    /**
     * 建立连接成功时调用
     * @param session 用户连接会话
     * @param userId 可以有多个参数，比如从url中获取一些信息
     */
    @OnOpen
    public void onOpen(Session session, @PathParam("userId")int userId,@PathParam("shopId")int shopId,@PathParam("isToShop")int its){
        System.out.println("连接打开"+this.toString());

        if(its==1){
            users.put(userId,session);//用户发起通信，将session加入map中
        }
        else {
            shops.put(shopId,session);//商店发起通信，将session加入map中
        }
    }

    /**
     * 关闭时调用
     */
    @OnClose
    public void onClose(@PathParam("userId")int userId,@PathParam("shopId")int shopId,@PathParam("isToShop")int its){
        System.out.println("连接关闭");
        if(its==1) users.remove(userId);//关闭用户
        else shops.remove(shopId);//关闭商店
    }

    /**
     * 客户端向服务器端发送消息时调用
     */
    @OnMessage
    public void onMessage(@PathParam("userId")int userId,@PathParam("shopId")int shopId,@PathParam("isToShop")int its,String message, Session session) {
        System.out.println("onMessage");
        //根据session发送消息
        if(!users.containsKey(userId)){
            return;
        }
        try {
            //接收消息，存入数据库中，将序列化后的结果返回
            Message receiveMessage=new Message();
            receiveMessage.setContent(message);
            receiveMessage.setDate(DateUtil.getCurrentDate());
            receiveMessage.setCustomerId(userId);
            receiveMessage.setShopId(shopId);
            if(its==1){
                receiveMessage.setIsToShop((byte)1);
            }else {
                receiveMessage.setIsToShop((byte)0);
            }
            receiveMessage.setState((byte)1);

            //插入一条消息记录
            messageService.insertOneMessage(receiveMessage);
            //序列化消息
            String returnMessage=objectMapper.writeValueAsString(receiveMessage);
            //调用session中的方法远程发送信息
            if(users.containsKey(userId))users.get(userId).getBasicRemote().sendText(returnMessage);//返回序列化的消息
            if(shops.containsKey(shopId))shops.get(shopId).getBasicRemote().sendText(returnMessage);

        }catch (IOException e){
            e.printStackTrace();
        }
    }

    /**
     * 发生错误时调用
     */
    @OnError
    public void onError(Session session, Throwable error){
        System.out.println("发生错误");
        error.printStackTrace();
    }
}
