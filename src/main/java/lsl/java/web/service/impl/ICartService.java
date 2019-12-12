package lsl.java.web.service.impl;

import lsl.java.web.entity.Cart;
import lsl.java.web.mapper.CartDAO;
import lsl.java.web.service.CartService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

@Component
public class ICartService implements CartService {
    @Resource
    private CartDAO cartDAO;

    @Override
    public int insertCart(Cart cart){
        return cartDAO.insertCart(cart);
    }

    @Override
    public Cart getCartByCustomerIdAndBookId(int customerId,long bookId){
        return cartDAO.getCartByCustomerIdAndBookId(customerId,bookId);
    }

    @Override
    public List<Cart> getCartByCustomerId(int customerId){
        return cartDAO.getCartByCustomerId(customerId);
    }

    @Override
    public int deleteCartByCartId(int cartId){
        return cartDAO.deleteCartByCartId(cartId);
    }
}
