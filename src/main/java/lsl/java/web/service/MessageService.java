package lsl.java.web.service;

import lsl.java.web.entity.Message;

import java.util.List;

public interface MessageService {
    List<Message> getAboutShopMessageByCustomerId(int customerId);
    Message getNewMessageByCustomerIdAndShopId(int customerId,int shopId);
    List<Message> getOrderMessageListByCustomerIdAndShopId(int customerId,int shopId);
    int updateMessageState(int customerId,int shopId);
    int insertOneMessage(Message message);
    List<Message> getMessageListByShopId(int shopId);
}
