package lsl.java.web.service;

import lsl.java.web.entity.Customer;
import lsl.java.web.entity.LoginForm;

public interface CustomerService {
    Customer getCustomer(String phoneNumber, String password);

    int updateCustomerInfoById(Customer customer);

    Customer getCustomerByCustomerId(long customerId);

    int insertOneCustomerByPhoneNumber(LoginForm loginForm);
}
