package lsl.java.web.controller.mall;

import com.auth0.jwt.interfaces.DecodedJWT;
import lsl.java.web.entity.Book;
import lsl.java.web.entity.Channel;
import lsl.java.web.entity.Customer;
import lsl.java.web.service.ChannelService;
import lsl.java.web.service.impl.IBookService;
import lsl.java.web.service.impl.IChannelService;
import lsl.java.web.service.impl.ICustomerService;
import lsl.java.web.utils.InfoCacheUtil;
import lsl.java.web.utils.TokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class RootController {

    @Autowired
    IChannelService channelService;

    @Autowired
    IBookService bookService;

    @Autowired
    ICustomerService customerService;
    /**
     * 进入首页，先判断是否有token，有则注入customer，没有则不注入
     * @return 客户浏览的首页页面
     */
    @GetMapping({"/","/index"})
    public String indexPage(HttpServletRequest request,Model model){

        //根据请求的token注入Customer，有可能为null
        Customer customer=InfoCacheUtil.getCustomerByRequest(request);
        model.addAttribute("customer",customer);

        //从缓存中注入首页频道项目
        List<Channel> channelList= InfoCacheUtil.getChannelList();
        model.addAttribute("channelList",channelList);

        //注入随机图书列表
        List<Book> bookList=bookService.getRandomBookList();
        model.addAttribute("bookList",bookList);
        return "mall/index";
    }
}
