package com.nphase.service;

import com.nphase.entity.ShoppingCart;

import javax.naming.OperationNotSupportedException;
import java.math.BigDecimal;

public class ShoppingCartService {

    public BigDecimal calculateTotalPrice(ShoppingCart shoppingCart) {
        return shoppingCart.getProducts()
                .stream()
                .map(product -> product.getPricePerUnit().multiply(BigDecimal.valueOf(product.getQuantity())))
                .reduce(BigDecimal::add)
                .orElse(BigDecimal.ZERO);
    }

    public BigDecimal calculateTotalPriceSameProductDiscount
            (ShoppingCart shoppingCart, int minQuantity, double discountPercent) {
        return shoppingCart.getProducts()
                .stream()
                .map(product ->
                        (product.getQuantity() > minQuantity ?
                                product.getPricePerUnit().multiply(BigDecimal.valueOf(1 - discountPercent / 100)) :
                                product.getPricePerUnit())
                                .multiply(BigDecimal.valueOf(product.getQuantity())))
                .reduce(BigDecimal::add)
                .orElse(BigDecimal.ZERO);
    }
}
