package lsl.java.web.controller.mall;

import lsl.java.web.common.Result;
import lsl.java.web.entity.*;
import lsl.java.web.service.impl.ICommentService;
import lsl.java.web.service.impl.ICustomerService;
import lsl.java.web.service.impl.IOrderService;
import lsl.java.web.utils.DateUtil;
import lsl.java.web.utils.InfoCacheUtil;
import lsl.java.web.utils.TokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequestMapping("/customer")
public class CustomerController {

    @Autowired
    ICustomerService customerService;

    @Autowired
    ICommentService commentService;

    @Autowired
    IOrderService orderService;
    /**
     * ajax请求登录
     * @return 后台验证结果
     */
    @RequestMapping(value = "/login",method = RequestMethod.POST)
    @ResponseBody
    public Result postLogin(HttpServletRequest request, @RequestBody LoginForm loginForm){
        //首先校验验证码
        String verifyCode=(String) request.getSession().getAttribute("lslVerifyCode");
        if(!verifyCode.trim().equals(loginForm.getVerifyCode().trim())){
            return new Result("验证码错误，请重试",Result.CLIENT_ERROR);
        }

        //校验用户
        Customer customer=customerService.getCustomer(loginForm.getLoginName(),loginForm.getPassword());
        //校验失败
        if(customer==null){
            return new Result("手机号或密码错误", Result.CLIENT_ERROR);
        }

        //校验成功，签署token并返回
        String token=TokenUtil.sign(loginForm.getLoginName(),customer.getPassword());
        System.out.println(token);
        return new Result(token,Result.OK);
    }

    /**
     * 请求访问登录页面
     */
    @RequestMapping(value = "/loginPage",method = RequestMethod.GET)
    public String loginPage(){
        return "mall/login";
    }

    /**
     * 请求访问注册页面
     */
    @RequestMapping(value = "/registerPage",method = RequestMethod.GET)
    public String registerPage(){
        return "mall/register";
    }

    @RequestMapping("/register/lsl")
    @ResponseBody
    public Result postRegister(HttpServletRequest request, @RequestBody LoginForm loginForm){
        //首先校验验证码
        String verifyCode=(String) request.getSession().getAttribute("lslVerifyCode");
        if(!verifyCode.trim().equals(loginForm.getVerifyCode().trim())){
            return new Result("验证码错误，请重试",Result.CLIENT_ERROR);
        }
        //插入用户数据
        try {
            Customer customer=customerService.getCustomer(loginForm.getLoginName(),loginForm.getPassword());
            if(customer!=null)return new Result("该账号已注册！",Result.CLIENT_ERROR);
            customerService.insertOneCustomerByPhoneNumber(loginForm);
            customer=customerService.getCustomer(loginForm.getLoginName(),loginForm.getPassword());
            customer.setName("用户"+customer.getPhoneNumber());
            customerService.updateCustomerInfoById(customer);
            return new Result("注册成功",Result.OK);
        }catch (Exception e){
            e.printStackTrace();
            return new Result("数据错误，请检查",Result.SERVER_ERROR);
        }
    }

    /**
     * 根据用户id访问个人中心页面，需要根据请求中的token进行一次校验
     * @param customerId 用户id
     */
    @RequestMapping("/{customerId}")
    public String customerPage(HttpServletRequest request, @PathVariable("customerId") int customerId, Model model){
         Customer customer= InfoCacheUtil.getCustomerByRequest(request);
         //用户token校验失败，跳转到登录界面
         if(customer==null){
             return "mall/login";
         }

        //从缓存中注入首页频道项目
        List<Channel> channelList= InfoCacheUtil.getChannelList();
        model.addAttribute("channelList",channelList);

         //注入用户信息
         model.addAttribute("customer",customer);

         //注入个人订单信息，都是未完成订单
        List<Order> orderList=orderService.getUnFinishedOrderListByCustomerId(customerId);
        model.addAttribute("unFinishedOrderList",orderList);

        return "mall/customer";
    }

    /**
     * 根据用户id访问个人中心页面，需要根据请求中的token进行一次校验
     * @param customerId 用户id
     */
    @RequestMapping("/edit/{customerId}")
    public String customerEditPage(HttpServletRequest request, @PathVariable("customerId") int customerId, Model model){
        Customer customer= InfoCacheUtil.getCustomerByRequest(request);
        //用户token校验失败，跳转到登录界面
        if(customer==null){
            return "mall/login";
        }

        //从缓存中注入首页频道项目
        List<Channel> channelList= InfoCacheUtil.getChannelList();
        model.addAttribute("channelList",channelList);

        //注入用户信息
        model.addAttribute("customer",customer);
        return "mall/customerEdit";
    }

    /**
     * Ajax请求更新用户信息
     * @param customerId 用户id
     * @param request 请求,用于校验
     * @param customer 用户表单数据
     */
    @RequestMapping("/update/{customerId}")
    @ResponseBody
    public Result updateCustomerById(@PathVariable("customerId") int customerId,HttpServletRequest request,@RequestBody Customer customer, Model model){
        Customer customerCheck= InfoCacheUtil.getCustomerByRequest(request);
        //用户token校验失败，返回错误信息
        if(customerCheck==null){
            return new Result("用户未登录，请登录",Result.CLIENT_ERROR);
        }
        if(customer==null)return new Result("用户提交表单格式错误",Result.CLIENT_ERROR);
        try{
            customerService.updateCustomerInfoById(customer);
            return new Result("更新信息成功",Result.OK);
        }catch (Exception e){
            e.printStackTrace();
            return new Result("数据库访问失败，请检查信息格式,请重试",Result.SERVER_ERROR);
        }
    }

    /**
     * 提交用户评论
     * @param comment 评论的表单数据
     */
    @RequestMapping("/comment/add/{customerId}")
    @ResponseBody
    public Result submitComment(@RequestBody Comment comment,@PathVariable("customerId")int customerId ){
        //插入系统当前时间
        comment.setDate(DateUtil.getCurrentDate());
        try{
            if(commentService.insertComment(comment)>=0){
                return new Result("评论成功，刷新可看",Result.OK);
            }else {
                return new Result("插入失败，数据库错误，请重试",Result.SERVER_ERROR);
            }
        }catch (Exception e){
            e.printStackTrace();
            return new Result("插入失败，数据库错误，请重试",Result.SERVER_ERROR);
        }
    }

    /**
     * Ajax请求更新订单状态
     * @param request 请求，用于权鉴
     * @param orderId 订单id
     */
    @RequestMapping("/order/stateUpdate/{orderId}")
    @ResponseBody
    public Result updateCustomerOrderState(HttpServletRequest request,@PathVariable("orderId")long orderId){
        Customer customerCheck= InfoCacheUtil.getCustomerByRequest(request);
        //用户token校验失败，返回错误信息
        if(customerCheck==null){
            return new Result("用户未登录，请登录",Result.CLIENT_ERROR);
        }
        try {
            orderService.finishOrder(orderId);
            return new Result("更新成功",Result.OK);
        }catch (Exception e){
            e.printStackTrace();
            return new Result("数据库响应失败，请重试",Result.SERVER_ERROR);
        }
    }

}
