package com.rubal.lld.vendingmachine.inventory;

/**
 * @author vrubal
 * @created
 * @project rubal-project
 */
public interface InventoryService {
    void addProduct(Product product);
    void remove(Product product);

}
