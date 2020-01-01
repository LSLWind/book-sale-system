package lsl.java.web.utils;

import lsl.java.web.entity.Depository;
import lsl.java.web.entity.Order;
import lsl.java.web.service.impl.IDepositoryService;
import lsl.java.web.service.impl.IOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 仓库缓存单元，保证购买书籍/上架书籍时的数据一致性
 * 单例模式
 */
@Controller
public class DepositoryCacheUnit {
    private static Map<Long,Integer> depositoryMap=new HashMap<>();//仓库映射，书的id映射书的数量


    //注入仓库服务
    private static IDepositoryService depositoryService;
    @Autowired
    public void setDepositoryService(IDepositoryService depositoryService){
        DepositoryCacheUnit.depositoryService=depositoryService;
    }
    //注入订单服务
    private static IOrderService orderService;
    @Autowired
    public void setOrderService(IOrderService orderService){
        DepositoryCacheUnit.orderService=orderService;
    }


    /**
     * 指定bookId，获取该书籍的库存数量，如果该书籍不在内存中则写进内存
     * 该方法相当于读操作
     * @param bookId 书籍id
     */
    public static int getBookCountById(long bookId){
        //内存中不存在则实时获取
        if(!depositoryMap.containsKey(bookId)){
            //获取仓库数据
            Depository depository=depositoryService.getDepositoryByBookId(bookId);
            depositoryMap.put(bookId,depository.getCount());//写入内存
            return depository.getCount();
        }

        return depositoryMap.get(bookId);
    }

    /**
     * 处理订单，更新库存数量，用户要购买图书，减少图书数量,插入订单数据
     * @param order 要处理的订单
     * @return 能够购买返回true，库存不够返回false
     */
    public static boolean handleOrder(Order order){
        Lock lock=new ReentrantLock();
        //进入临界区，保证数据一致性
        lock.lock();
        try{
            int surplus=getBookCountById(order.getBookId());//查询书籍库存
            if(surplus<order.getCount())return false;//库存不够
            //否则处理订单，交给后台处理
            //1.更新库存
            depositoryMap.put(order.getBookId(),getBookCountById(order.getBookId())-order.getCount());
            //3.因为测试时要经常关闭服务器，所以这里还是先进行一次更新操作
            updateDepositoryByBooId(order.getBookId(),depositoryMap.get(order.getBookId()));
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            lock.unlock();
        }
        //2.库存更新完毕，插入订单
        order.setDate(DateUtil.getCurrentDate());//完善订单
        orderService.insertOrder(order);//插入订单

        return true;
    }


    /**
     * 对单个图书信息进行更新操作，将缓存中的数据写入到数据库中
     * Mysql update时会加上行级别的排它锁，但是对于条件查询来说是先select的，因此仍要加锁
     * @param bookId 书籍id
     */
    public static void updateDepositoryByBooId(long bookId,int count){
        Lock lock=new ReentrantLock();
        lock.lock();
        try{
            depositoryService.updateDepositoryByBookId(bookId,count);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            lock.unlock();
        }
    }

    /**
     * 更新仓库缓存数据，因为未使用concurrentHashMap，因此必须显示加锁
     */
    public static void updateDepositoryCacheByBookId(long bookId,int count){
       Lock lock=new ReentrantLock();
       lock.lock();
        try {
            //试图更新一个不存在于缓存中的数据则不用管
            if(!depositoryMap.containsKey(bookId))return ;
            //否则覆盖更新缓存数据
            depositoryMap.put(bookId,count);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            lock.unlock();
        }
    }

    /**
     * 根据缓存更新整个数据库数据，此时迭代，不用加锁
     */
    public static void updateDepository(){
        for(long bookId:depositoryMap.keySet()){
           depositoryService.updateDepositoryByBookId(bookId,depositoryMap.get(bookId));
        }
    }
}
