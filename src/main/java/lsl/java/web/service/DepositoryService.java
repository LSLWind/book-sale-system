package lsl.java.web.service;

import lsl.java.web.entity.Depository;

import java.util.List;

public interface DepositoryService {
    Depository getDepositoryByBookId(long bookId);

    int updateDepositoryByBookId(long bookId,int count);

    List<Depository> getDepositoryListByShopId(int shopId);

    List<Depository> getDepositoryListByShopIdAndDepositoryName(int shopId,String depositoryName);


    Depository getDepositoryByDepositoryId(long depositoryId);

    int updateDepositoryCountByDepositoryId(int count,long depositoryId);

    int insertOneDepository(Depository depository);
}
