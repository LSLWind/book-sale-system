package lsl.java.web.utils;


import lsl.java.web.entity.Shop;
import lsl.java.web.service.impl.IShopService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.server.Session;
import org.springframework.stereotype.Controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
public class ShopCacheUtil {

    private static IShopService shopService;
    @Autowired
    public void setShopService(IShopService shopService){
        ShopCacheUtil.shopService=shopService;
    }
    /**
     * 根据Session获取Shop
     */
    public static Shop getShopBySession(HttpServletRequest request){
        HttpSession session=request.getSession();
        if(session==null)return null;//session不存在则让其登录

        //根据Session获取信息，重置最大登录时间
        String phoneNumber=(String)session.getAttribute("phoneNumber");
        String password=(String)session.getAttribute("password");
        session.setMaxInactiveInterval(7200);//重置session时间
        return shopService.getShopByPhoneNumberAndPassword(phoneNumber,password);
    }

}
