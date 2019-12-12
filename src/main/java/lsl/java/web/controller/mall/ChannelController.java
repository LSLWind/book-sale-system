package lsl.java.web.controller.mall;

import lsl.java.web.entity.Book;
import lsl.java.web.entity.Channel;
import lsl.java.web.entity.Customer;
import lsl.java.web.service.impl.IBookService;
import lsl.java.web.utils.InfoCacheUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequestMapping("/channel")
public class ChannelController {

    @Autowired
    IBookService bookService;

    /**
     * 根据频道id注入频道图书
     * @param request 请求，验证用户
     * @param channelId 频道id
     * @return 具体的频道页面
     */
    @RequestMapping("/{channelId}")
    public String channelPage(HttpServletRequest request,@PathVariable("channelId") int channelId, Model model){
        //根据请求的token注入Customer，有可能为null
        Customer customer=InfoCacheUtil.getCustomerByRequest(request);
        model.addAttribute("customer",customer);

        //从缓存中注入首页频道项目
        List<Channel> channelList= InfoCacheUtil.getChannelList();
        model.addAttribute("channelList",channelList);

        //注入具体频道的图书列表
        List<Book> bookList=bookService.getBookListByChannelId(channelId);
        model.addAttribute("bookList",bookList);

        return "mall/channel";
    }
}
