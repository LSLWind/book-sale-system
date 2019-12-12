package lsl.java.web.controller.mall;

import lsl.java.web.common.Result;
import lsl.java.web.entity.Cart;
import lsl.java.web.entity.Channel;
import lsl.java.web.entity.Customer;
import lsl.java.web.entity.Depository;
import lsl.java.web.service.impl.ICartService;
import lsl.java.web.utils.DepositoryCacheUnit;
import lsl.java.web.utils.InfoCacheUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequestMapping("/cart")
public class CartController {

    @Autowired
    ICartService cartService;
    /**
     * ajax 请求插入一条购物车记录，返回响应，表单数据为cart
     */
    @RequestMapping("/add")
    @ResponseBody
    public Result addCartRecord(@RequestBody Cart cart){
        try {
            //先判断该记录是否存在
            if(cartService.getCartByCustomerIdAndBookId(cart.getCustomerId(),cart.getBookId())!=null){
                return new Result("添加成功",Result.OK);
            }
            //不存在则进行实际插入
            int res=cartService.insertCart(cart);
            if(res<=0)return new Result("服务器错误，请重试",Result.SERVER_ERROR);//是否插入成功
            return new Result("添加成功",Result.OK);
        }catch (Exception e){
            e.printStackTrace();
            return new Result("数据库错误，请重试",Result.SERVER_ERROR);
        }
    }

    /**
     * 进入购物车页面
     * @param customerId 用户id
     * @param request 根据request进行校验，判断用户登录状态
     * @return 用户校验不成功返回登录界面，成功返回用户的购物车界面
     */
    @RequestMapping("/{customerId}")
    public String cartPage(@PathVariable("customerId")int customerId, HttpServletRequest request, Model model){
        Customer customer= InfoCacheUtil.getCustomerByRequest(request);
        //用户token校验失败，跳转到登录界面
        if(customer==null){
            return "mall/login";
        }
        //注入个人信息
        model.addAttribute("customer",customer);

        //从缓存中注入首页频道项目
        List<Channel> channelList= InfoCacheUtil.getChannelList();
        model.addAttribute("channelList",channelList);

        //注入购物车信息
        List<Cart> cartList=cartService.getCartByCustomerId(customerId);
        model.addAttribute("cartList",cartList);

        return "mall/cart";
    }

    @RequestMapping("/remove/{cartId}")
    @ResponseBody
    public Result removeOneCart(@PathVariable("cartId") int cartId,HttpServletRequest request){
        Customer customer= InfoCacheUtil.getCustomerByRequest(request);
        //用户token校验失败
        if(customer==null){
            return new Result("请求非法，请登录",Result.OK);
        }
        try {
            cartService.deleteCartByCartId(cartId);
            return new Result("删除成功，刷新可看",Result.OK);
        }catch (Exception e){
            e.printStackTrace();
            return new Result("数据库异常，请重试",Result.OK);
        }
    }
}
