package lsl.java.web.utils;

import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.Getter;
import lombok.Setter;
import lsl.java.web.entity.Channel;
import lsl.java.web.entity.Customer;
import lsl.java.web.service.impl.IChannelService;
import lsl.java.web.service.impl.ICustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Controller;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
@Controller
public class InfoCacheUtil {

    private static List<Channel>channelList;
    /**
     * 让容器注入一个频道Channel服务
     */
    private static IChannelService channelService;
    @Autowired
    public void setChannelService(IChannelService channelService){
        InfoCacheUtil.channelService=channelService;
    }
    /**
     * 让容器注入一个客户customer服务
     */
    private static ICustomerService customerService;
    @Autowired
    public void setCustomerService(ICustomerService customerService){InfoCacheUtil.customerService=customerService;}

    /**
     * 维护频道的缓存
     * @return 频道列表
     */
    public static List<Channel> getChannelList(){
        if(channelList==null){
            channelList=channelService.getChannelList();
        }
        return channelList;
    }

    /**
     * 根据request返回一个Customer，复用代码
     * TODO 维护一个用户列表，进行列表缓存，加快响应速度
     */
     public static Customer getCustomerByRequest(HttpServletRequest request){
         //校验token，传入用户数据
         String token= TokenUtil.loginCheck(request);
         if(token!=null){
             //校验成功，获取token中的数据，注入客户信息
             DecodedJWT jwt= TokenUtil.getJWT(token);
             String name=jwt.getClaim("name").asString();
             String password=jwt.getClaim("password").asString();
             //根据请求数据获取信息
             return customerService.getCustomer(name,password);
         }
         return null;
     }

}
