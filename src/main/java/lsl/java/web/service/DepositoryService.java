package lsl.java.web.service;

import lsl.java.web.entity.Depository;

public interface DepositoryService {
    Depository getDepositoryByBookId(long bookId);

    int updateDepositoryByBookId(long bookId,int count);
}
