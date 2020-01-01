package lsl.java.web.controller.shop;


import lsl.java.web.entity.Depository;
import lsl.java.web.entity.Shop;
import lsl.java.web.service.impl.IDepositoryService;
import lsl.java.web.utils.ShopCacheUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class ShopRootController {
    @Autowired
    IDepositoryService depositoryService;


    /**
     * 进入店铺管理首页，考虑到对商店操作很重要，因此不设置cookie
     * 每次必须进行登录
     */
    @RequestMapping({"/shop","/shop/index"})
    public String shopIndexPage(HttpServletRequest request, Model model){
        //校验店铺并注入
        Shop shop= ShopCacheUtil.getShopBySession(request);//请求获取一次Shop
        //登录超时，跳转到登录界面
        if(shop==null){
            return "shop/login";
        }
        //否则根据session已经校验成功，获取信息
        //注入商店属性
        model.addAttribute("shop",shop);
        //获取仓库列表，注入仓库属性
        List<Depository> depositoryList=depositoryService.getDepositoryListByShopId(shop.getId());
        model.addAttribute("depositoryList",depositoryList);

        return "shop/index";
    }


}
