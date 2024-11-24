package ca.gbc.inventoryservice.service;

import ca.gbc.inventoryservice.model.Inventory;
import ca.gbc.inventoryservice.repository.InventoryRepository;
import org.springframework.stereotype.Service;

@Service
public class InventoryService {

    private final InventoryRepository inventoryRepository;

    public InventoryService(InventoryRepository inventoryRepository) {
        this.inventoryRepository = inventoryRepository;
    }

    public boolean isInStock(String skuCode, int quantity) {
        Inventory item = inventoryRepository.findBySkuCode(skuCode);
        return item != null && item.getQuantity() >= quantity;
    }
}
