package com.rubal.lld.vendingmachine.inventory;

/**
 * @author vrubal
 * @created
 * @project rubal-project
 */
public class ProductInventory {
    Product product;
    int quantity;

    int price;
    ProductInventory(Product product, int quantity){
        this.product = product;
        this.quantity = quantity;
    }
}
