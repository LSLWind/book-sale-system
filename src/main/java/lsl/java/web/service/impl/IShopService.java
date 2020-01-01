package lsl.java.web.service.impl;

import lsl.java.web.entity.Shop;
import lsl.java.web.mapper.ShopDAO;
import lsl.java.web.service.ShopService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class IShopService implements ShopService {
    @Resource
    private ShopDAO shopDAO;

    @Override
    public Shop getShopByPhoneNumberAndPassword(String phoneNumber, String password){
        return shopDAO.getShopByPhoneNumberAndPassword(phoneNumber,password);
    }

    @Override
    public Shop getShopNameByShopId(int shopId){
        return shopDAO.getShopNameByShopId(shopId);
    }
}
