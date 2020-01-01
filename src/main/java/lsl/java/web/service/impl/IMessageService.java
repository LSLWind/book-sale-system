package lsl.java.web.service.impl;

import lsl.java.web.entity.Message;
import lsl.java.web.mapper.MessageDAO;
import lsl.java.web.service.MessageService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;

@Component
public class IMessageService implements MessageService {

    @Resource
    private MessageDAO messageDAO;

    /**
     * 查询与指定用户通信过的商店的id信息列表，因为使用group by 所以只能注入商店的id信息
     * 只用用户id与商店id
     * @param customerId 用户id
     */
    @Override
    public List<Message> getAboutShopMessageByCustomerId(int customerId){
        return messageDAO.getAboutShopMessageByCustomerId(customerId);
    }

    /**
     * 根据指定的用户id与商店id获取一条最新的通信消息
     */
    @Override
    public  Message getNewMessageByCustomerIdAndShopId(int customerId,int shopId){
        return messageDAO.getNewMessageByCustomerIdAndShopId(customerId,shopId);
    }

    /**
     * 返回按时间排序后的指定用户与商店的聊天信息
     */
    @Override
    public List<Message> getOrderMessageListByCustomerIdAndShopId(int customerId,int shopId){
        return messageDAO.getOrderMessageListByCustomerIdAndShopId(customerId,shopId);
    }

    /**
     * 更新所有消息为已读
     */
    @Override
    public int updateMessageState(int customerId,int shopId){
        return messageDAO.updateMessageState(customerId,shopId);
    }

    /**
     * 插入一条消息
     */
    @Override
    public int insertOneMessage(Message message){
        return messageDAO.insertOneMessage(message);
    }


    /**
     * 根据商店id返回与之通信过的所有用户消息列表，已经分组过一次，只有用户id信息
     */
    @Override
    public List<Message> getMessageListByShopId(int shopId){
        return messageDAO.getMessageListByShopId(shopId);
    }
}
