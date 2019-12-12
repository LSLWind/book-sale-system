package lsl.java.web.service.impl;

import lsl.java.web.entity.Depository;
import lsl.java.web.mapper.DepositoryDAO;
import lsl.java.web.service.DepositoryService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

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
}
