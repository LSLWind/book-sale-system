package lsl.java.web.service;

import lsl.java.web.entity.Customer;

public interface CustomerService {
    Customer getCustomer(String phoneNumber, String password);

    int updateCustomerInfoById(Customer customer);
}
