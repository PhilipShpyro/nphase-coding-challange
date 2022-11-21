package com.nphase.service;


import com.nphase.entity.Category;
import com.nphase.entity.CategoryType;
import com.nphase.entity.Product;
import com.nphase.entity.ShoppingCart;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.naming.OperationNotSupportedException;
import java.math.BigDecimal;
import java.util.Arrays;

public class ShoppingCartServiceTest {
    private final ShoppingCartService service = new ShoppingCartService();

    @Test
    public void calculatesPrice()  {
        ShoppingCart cart = new ShoppingCart(Arrays.asList(
                new Product("Tea", BigDecimal.valueOf(5.0), 2),
                new Product("Coffee", BigDecimal.valueOf(6.5), 1)
        ));

        BigDecimal result = service.calculateTotalPrice(cart);

        Assertions.assertEquals(result, BigDecimal.valueOf(16.5));
    }

    @Test
    public void calculatesPriceSameProductDiscount()  {
        ShoppingCart cart = new ShoppingCart(Arrays.asList(
                new Product("Tea", BigDecimal.valueOf(5.0), 5),
                new Product("Coffee", BigDecimal.valueOf(3.5), 3)
        ));

        BigDecimal result = service.calculateTotalPriceSameProductDiscount(cart, 3, 10);

        Assertions.assertEquals(0, result.compareTo(BigDecimal.valueOf(33.0)));
    }

    @Test
    public void calculatePriceSameCategoryDiscount() {
        Product tea = new Product("Tea", BigDecimal.valueOf(5.3), 2);
        Product coffee = new Product("Coffee", BigDecimal.valueOf(3.5), 2);
        Product cheese = new Product("Cheese", BigDecimal.valueOf(8), 2);
        Category drinks = new Category(CategoryType.DRINKS).addProductNamesToCategory(
                tea.getName(), coffee.getName()
        );
        Category food = new Category(CategoryType.FOOD).addProductNameToCategory(cheese.getName());
        ShoppingCart cart = new ShoppingCart(Arrays.asList(tea, coffee, cheese));
        BigDecimal result = service.calculateTotalPriceSameCategoryDiscount(cart, 3, 10, Arrays.asList(
                new Category(CategoryType.FOOD).addProductNameToCategory("cheese"),
                new Category(CategoryType.DRINKS).addProductNamesToCategory("tea", "coffee")
        ));

        Assertions.assertEquals(BigDecimal.valueOf(31.84), result);
    }
}