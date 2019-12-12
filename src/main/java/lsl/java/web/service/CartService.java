package lsl.java.web.service;

import lsl.java.web.entity.Cart;

import java.util.List;

public interface CartService {
    int insertCart(Cart cart);
    Cart getCartByCustomerIdAndBookId(int customerId,long bookId);
    List<Cart> getCartByCustomerId(int customerId);
    int deleteCartByCartId(int cartId);
}
