package lsl.java.web.service.impl;

import lsl.java.web.entity.Customer;
import lsl.java.web.entity.LoginForm;
import lsl.java.web.mapper.CustomerDAO;
import lsl.java.web.service.CustomerService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class ICustomerService implements CustomerService {

    @Resource
    private CustomerDAO customerDAO;

    @Override
    public Customer getCustomer(String phoneNumber, String password){
        return customerDAO.getCustomer(phoneNumber,password);
    }

    @Override
    public int updateCustomerInfoById(Customer customer){
        return customerDAO.updateCustomerInfoById(customer);
    }

    @Override
    public Customer getCustomerByCustomerId(long customerId){
        return customerDAO.getCustomerByCustomerId(customerId);
    }

    @Override
    public int insertOneCustomerByPhoneNumber(LoginForm loginForm){
        return customerDAO.insertOneCustomerByPhoneNumber(loginForm);
    }
}
