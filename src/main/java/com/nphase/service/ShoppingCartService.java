package com.nphase.service;

import com.nphase.entity.Category;
import com.nphase.entity.CategoryType;
import com.nphase.entity.Product;
import com.nphase.entity.ShoppingCart;

import javax.naming.OperationNotSupportedException;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

public class ShoppingCartService {

    public BigDecimal calculateTotalPrice(ShoppingCart shoppingCart) {
        return shoppingCart.getProducts()
                .stream()
                .map(product -> product.getPricePerUnit().multiply(BigDecimal.valueOf(product.getQuantity())))
                .reduce(BigDecimal::add)
                .orElse(BigDecimal.ZERO);
    }

    public BigDecimal calculateTotalPriceSameProductDiscount(
            ShoppingCart shoppingCart, int minQuantity, double discountPercent) {
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

    public BigDecimal calculateTotalPriceSameCategoryDiscount(
            ShoppingCart shoppingCart, int minQuality, double discountPercent, List<Category> categoryList) {
        List<Product> products = shoppingCart.getProducts();
        Map<CategoryType, List<Product>> categoryTypeProductsMap =
                products.stream().collect(Collectors.groupingBy(product -> {
                    Optional<Category> optionalCategory =
                            categoryList.stream().filter(
                                    c -> c.containsProductName(product.getName()))
                                    .findFirst();
                    return optionalCategory.isEmpty() ? CategoryType.NOT_IN_CATEGORY : optionalCategory.get().getType();
                }));

        BigDecimal result = BigDecimal.ZERO;
        for (Map.Entry<CategoryType, List<Product>> categoryTypeProduct : categoryTypeProductsMap.entrySet()) {
            List<Product> productList = categoryTypeProduct.getValue();
            int quantityPerCategory = productList.stream().map(Product::getQuantity).reduce(Integer::sum).orElse(0);
            for (Product product : productList) {
                    boolean discountable =
                            quantityPerCategory > minQuality
                                    && categoryTypeProduct.getKey() != CategoryType.NOT_IN_CATEGORY;
                    result = result.add(
                            (discountable ?
                                    product.getPricePerUnit().multiply(BigDecimal.valueOf(1 - discountPercent / 100)) :
                                    product.getPricePerUnit())
                                    .multiply(BigDecimal.valueOf(product.getQuantity())));
            }
        }

        return result;
    }

}
