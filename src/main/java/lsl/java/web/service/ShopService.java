package lsl.java.web.service;

import lsl.java.web.entity.Shop;

public interface ShopService {
    Shop getShopByPhoneNumberAndPassword(String phoneNumber,String password);
    Shop getShopNameByShopId(int shopId);
}
