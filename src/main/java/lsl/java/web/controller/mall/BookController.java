package lsl.java.web.controller.mall;

import lsl.java.web.common.Constant;
import lsl.java.web.common.Result;
import lsl.java.web.entity.*;
import lsl.java.web.service.impl.IBookService;
import lsl.java.web.service.impl.ICommentService;
import lsl.java.web.service.impl.IDepositoryService;
import lsl.java.web.utils.DepositoryCacheUnit;
import lsl.java.web.utils.InfoCacheUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequestMapping("/book")
public class BookController {

    @Autowired
    IBookService bookService;

    @Autowired
    IDepositoryService depositoryService;

    @Autowired
    ICommentService commentService;

    /**
     * 书籍详情页面
     * @param request 用户请求，获取token
     * @param bookId 书籍id
     */
    @RequestMapping("/{bookId}")
    public String bookDetailPage(HttpServletRequest request,@PathVariable("bookId")long bookId, Model model){
        //根据请求的token注入Customer，有可能为null
        Customer customer=InfoCacheUtil.getCustomerByRequest(request);
        model.addAttribute("customer",customer);

        //从缓存中注入首页频道项目
        List<Channel> channelList= InfoCacheUtil.getChannelList();
        model.addAttribute("channelList",channelList);

        //根据图书id注入图书信息
        Book book=bookService.getBookById(bookId);
        model.addAttribute("book",book);

        //根据图书id注入仓库信息
        //TODO 多商店，根据商店与bookID注入仓库
        Depository depository=depositoryService.getDepositoryByBookId(bookId);
        //因为图书数量要并发处理，所有仓库中图书的数量从缓存中取出
        depository.setCount(DepositoryCacheUnit.getBookCountById(bookId));
        model.addAttribute("depository",depository);

        //根据图书id注入评论消息
        List<Comment> commentList=commentService.getCommentListByBookId(bookId);
        model.addAttribute("commentList",commentList);
        return "mall/book";
    }

    /**
     * 用户请求购买书籍，进入订单页面
     * @param customerId 用户id
     * @param bookId 用户将要购买的数据id
     * @return 进入订单页面
     */
    @RequestMapping("/order/{customerId}/{bookId}")
    public String buyBookPage(HttpServletRequest request,@PathVariable("customerId")long customerId,@PathVariable("bookId")long bookId,Model model){
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

        //根据图书id注入图书信息
        Book book=bookService.getBookById(bookId);
        model.addAttribute("book",book);

        //根据图书id注入仓库信息
        //TODO 多商店，根据商店与bookID注入仓库
        //注入缓存中的图书数量
        Depository depository=new Depository();
        depository.setCount(DepositoryCacheUnit.getBookCountById(bookId));
        model.addAttribute("depository",depository);

        return "mall/order";
    }

    /**
     * 用户Ajax，请求购买书籍，生成订单，后台进行并发处理
     * @return 订单请求结果
     */
    @RequestMapping("/buy")
    @ResponseBody
    public Result buyBook(@RequestBody Order order, HttpServletRequest request){
        Customer customer= InfoCacheUtil.getCustomerByRequest(request);
        //用户token校验失败，跳转到登录界面
        if(customer==null){
            return new Result("未登录，请登录后进行购买",Result.CLIENT_ERROR);
        }
        //尝试处理订单
        boolean handleResult= DepositoryCacheUnit.handleOrder(order);

        if(!handleResult)return new Result("对不起，商品已卖完",Result.OK);
        return new Result("购买成功，已生成订单，正在处理订单",Result.OK);
    }
}
