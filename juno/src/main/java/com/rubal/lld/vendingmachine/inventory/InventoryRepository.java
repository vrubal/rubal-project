package com.rubal.lld.vendingmachine.inventory;

import java.util.HashMap;
import java.util.Map;

/**
 * @author vrubal
 * @created
 * @project rubal-project
 */
public class InventoryRepository {
    Map<String, ProductInventory> store = new HashMap<>();

    void addProduct(String slotId, Product product){

    }
    void removeProduct(String slotId, Product product){

    }

    double getPriceBySlot(String slot){
       return 1.0;
    }
}
