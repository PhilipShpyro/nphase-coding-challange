package com.nphase.entity;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class Category {
    private final CategoryType type;
    private final Set<String> productNames = new HashSet<>();

    public Category(CategoryType type) {
        this.type = type;
    }

    public CategoryType getType() {
        return type;
    }

    public Category addProductNameToCategory(String productName) {
        productNames.add(productName.trim().toLowerCase());
        return this;
    }

    public Category addProductNamesToCategory(String ... productNames) {
        Arrays.asList(productNames).forEach(this::addProductNameToCategory);
        return this;
    }

    public void removeProductNameFromCategoryIfExists(String productName) {
        productNames.remove(productName);
    }

    public boolean containsProductName(String productName) {
        return productNames.contains(productName.toLowerCase());
    }
}
