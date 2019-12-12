package lsl.java.web.service.impl;

import lsl.java.web.entity.Channel;
import lsl.java.web.mapper.ChannelDAO;
import lsl.java.web.service.ChannelService;
import org.apache.ibatis.annotations.Result;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

@Component
public class IChannelService implements ChannelService {
    @Resource
    private ChannelDAO channelDAO;

    @Override
    public List<Channel> getChannelList(){
        return channelDAO.getChannelList();
    }
}
