package lsl.java.web.controller.shop;

import lsl.java.web.common.Result;
import lsl.java.web.entity.Book;
import lsl.java.web.entity.Depository;
import lsl.java.web.entity.LoginForm;
import lsl.java.web.entity.Shop;
import lsl.java.web.service.ShopService;
import lsl.java.web.service.impl.IBookService;
import lsl.java.web.service.impl.IDepositoryService;
import lsl.java.web.service.impl.IOrderService;
import lsl.java.web.service.impl.IShopService;
import lsl.java.web.utils.DepositoryCacheUnit;
import lsl.java.web.utils.ShopCacheUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/shop")
public class ShopController {

    @Autowired
    IShopService shopService;

    @Autowired
    IDepositoryService depositoryService;

    @Autowired
    IBookService bookService;

    /**
     * 商店请求登录校验，传递表单数据，校验成功则设置session
     * @return 返回校验结果Result
     */
    @RequestMapping("/login")
    @ResponseBody
    public Result shopLoginCheck(HttpServletRequest request, @RequestBody LoginForm loginForm){
        //尝试登录
        Shop shop;
        try{
            shop=shopService.getShopByPhoneNumberAndPassword(loginForm.getLoginName(),loginForm.getPassword());
        }catch (Exception e){
            e.printStackTrace();
            return new Result("数据库错误，请重试",Result.SERVER_ERROR);
        }
        //登录失败
        if(shop==null){
            return new Result("登录名或密码错误，请重试",Result.CLIENT_ERROR);
        }
        //创建session，设置属性
        HttpSession session=request.getSession(true);
        session.setMaxInactiveInterval(7200);
        session.setAttribute("phoneNumber",loginForm.getLoginName());
        session.setAttribute("password",loginForm.getPassword());
        return new Result("登录成功",Result.OK);
    }

    /**
     * 根据商店id与具体的仓库名进入仓库页面
     */
    @RequestMapping("/depository/{shopId}/{depositoryName}")
    public String depositoryPage(HttpServletRequest request, @PathVariable("shopId") int shopId, @PathVariable("depositoryName")String depositoryName, Model model){
        Shop shop= ShopCacheUtil.getShopBySession(request);
        //登录超时则跳转到登录界面
        if(shop==null){
            return "shop/login";
        }
        //注入商店数据
        model.addAttribute("shop",shop);
        //注入具体的仓库数据
        List<Depository> depositoryList=depositoryService.getDepositoryListByShopIdAndDepositoryName(shopId,depositoryName);
        model.addAttribute("depositoryList",depositoryList);

        //注入仓库名称
        model.addAttribute("depositoryName",depositoryName);

        //根据仓库数据注入图书数据，仓库记录数据与图书数据映射
        Map<Depository,Book> depositoryStringMap=new HashMap<>();
        for(Depository depository:depositoryList){
            depositoryStringMap.put(depository,bookService.getBookById(depository.getBookId()));
        }
        model.addAttribute("depositoryMap",depositoryStringMap);

        return "shop/depository";
    }

    /**
     * 进入仓库库存编辑界面
     */
    @RequestMapping("/editCount/{shopId}/{depositoryName}/{depositoryId}")
    public String depositoryEditPage(HttpServletRequest request,@PathVariable("shopId")int shopId,@PathVariable("depositoryName")String
                                         depositoryName,@PathVariable("depositoryId")long depositoryId,Model model){
        Shop shop= ShopCacheUtil.getShopBySession(request);
        //登录超时则跳转到登录界面
        if(shop==null){
            return "shop/login";
        }
        //注入商店数据
        model.addAttribute("shop",shop);
        //注入仓库数据
        Depository depository=depositoryService.getDepositoryByDepositoryId(depositoryId);
        model.addAttribute("depository",depository);
        //注入图书数据
        Book book=bookService.getBookById(depository.getBookId());
        model.addAttribute("book",book);
        return "shop/depositoryEdit";
    }

    /**
     * Ajax请求更新仓库库存数据
     */
    @RequestMapping("/depositoryUpdate/{depositoryId}")
    @ResponseBody
    public Result updateDepositoryData(HttpServletRequest request,@PathVariable("depositoryId")long depositoryId,@RequestBody Depository depository){
        Shop shop= ShopCacheUtil.getShopBySession(request);
        //登录超时则跳转到登录界面
        if(shop==null){
            return new Result("登录超时，请登录",Result.CLIENT_ERROR);
        }
        //更新数据，仓库数据上锁更新，调用缓存时的函数对库存量进行并发控制
        try{
            Depository realDepository=depositoryService.getDepositoryByDepositoryId(depositoryId);//仓库数据与图书数据是一对一映射
            Book book=bookService.getBookById(realDepository.getBookId());
            //更新数据库数据
            DepositoryCacheUnit.updateDepositoryByBooId(book.getId(),depository.getCount());
            //更新系统缓存数据
            DepositoryCacheUnit.updateDepositoryCacheByBookId(book.getId(),depository.getCount());
            return new Result("更新成功",Result.OK);
        }catch (Exception e){
            e.printStackTrace();
            return new Result("数据库错误，请重试",Result.SERVER_ERROR);
        }
    }

    /**
     * 进入仓库上架图书页面
     */
    @RequestMapping("/{shopId}/{depositoryName}/depositoryBookAdd")
    public String depositoryBookAddPage(HttpServletRequest request,@PathVariable("shopId")int shopId,@PathVariable("depositoryName")String depositoryName,Model model){
        Shop shop= ShopCacheUtil.getShopBySession(request);
        //登录超时则跳转到登录界面
        if(shop==null){
            return "shop/login";
        }
        //注入商店数据
        model.addAttribute("shop",shop);

        //填写表单之后要上传数据，传递仓库名
        model.addAttribute("depositoryName",depositoryName);
        return "shop/depositoryBookAdd";
    }

    /**
     * Ajax请求增加图书
     * @param shopId 商店id
     * @param depositoryName 商店的仓库名
     * @param book 前台图书表单数据
     */
    @RequestMapping("/{shopId}/{depositoryName}/addBook/{count}")
    @ResponseBody
    public Result addBook(@PathVariable("shopId")int shopId,@PathVariable("depositoryName")String depositoryName,
                          @RequestBody Book book,@PathVariable("count")int count){

        //插入图书数据，同时要根据图书数据插入仓库数据
        try{
            System.out.println(book.toString());
            bookService.insertOneBook(book);
            if(book.getId()<=0){//插入之后应该返回自增id
                return new Result("数据库响应失败，请重试",Result.SERVER_ERROR);
            }
            //插入数据之后要生成仓库记录，插入返回值为主键
            Depository depository=depositoryService.getDepositoryListByShopId(shopId).get(0);//要的只是数据，这里还是数据库设计时遗留下的问题
            depository.setCount(count);
            depository.setBookId(book.getId());
            depositoryService.insertOneDepository(depository);//插入仓库数据记录
            //返回结果
            return new Result("插入成功",Result.OK);
        }catch (Exception e){
            e.printStackTrace();
            return new Result("增加失败，请检查数据是否符合格式",Result.CLIENT_ERROR);
        }
    }

}
