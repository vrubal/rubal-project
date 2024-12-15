package com.rubal.lld.vendingmachine.inventory;

/**
 * @author vrubal
 * @created
 * @project rubal-project
 */
public class InventoryServiceImpl implements InventoryService{
    private InventoryRepository repository;
    private InventoryConfig config;
    @Override
    public void addProduct(Product product) {
        String slotId = getSlotByProduct(product);
        repository.addProduct(slotId, product);
    }

    private String getSlotByProduct(Product product) {
        String slotId = config.getSlotId(product);
        return slotId;
    }

    @Override
    public void remove(Product product) {
        String slotByProduct = getSlotByProduct(product);
        repository.removeProduct(slotByProduct, product);
    }
}
