package lsl.java.web.service.impl;

import lsl.java.web.entity.Depository;
import lsl.java.web.mapper.DepositoryDAO;
import lsl.java.web.service.DepositoryService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

@Component
public class IDepositoryService implements DepositoryService {

    @Resource
    private DepositoryDAO depositoryDAO;

    @Override
    public Depository getDepositoryByBookId(long bookId){
        return depositoryDAO.getDepositoryByBookId(bookId);
    }

    @Override
    public int updateDepositoryByBookId(long bookId,int count){
        return depositoryDAO.updateDepositoryByBookId(bookId,count);
    }

    /**
     * 根据店铺id从仓库记录中获取已创建仓库列表
     */
    @Override
    public List<Depository> getDepositoryListByShopId(int shopId){
        return depositoryDAO.getDepositoryListByShopId(shopId);
    }

    /**
     * 根据商店id与仓库名称获取仓库库存数据
     */
    @Override
    public  List<Depository> getDepositoryListByShopIdAndDepositoryName(int shopId,String depositoryName){
        return depositoryDAO.getDepositoryListByShopIdAndDepositoryName(shopId,depositoryName);
    }

    /**
     * 根据id获取仓库库存数数据
     */
    @Override
    public Depository getDepositoryByDepositoryId(long depositoryId){
        return depositoryDAO.getDepositoryByDepositoryId(depositoryId);
    }

    @Override
    public int updateDepositoryCountByDepositoryId(int count,long depositoryId){
        return depositoryDAO.updateDepositoryCountByDepositoryId(count,depositoryId);
    }

    /**
     * 插入一条仓库数据记录
     * @param depository 仓库实体数据
     */
    @Override
    public int insertOneDepository(Depository depository){
        return depositoryDAO.insertOneDepository(depository);
    }
}
