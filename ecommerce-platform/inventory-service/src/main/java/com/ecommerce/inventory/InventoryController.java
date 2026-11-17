package com.ecommerce.inventory;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/inventory")
public class InventoryController {

    private final Map<Integer, Item> items = new ConcurrentHashMap<>();

    public InventoryController() {
        items.put(1, new Item(1, "Laptop", 10));
        items.put(2, new Item(2, "Phone", 5));
    }

    @GetMapping
    public Collection<Item> getAll() {
        return items.values();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable int id) {
        Item item = items.get(id);
        if (item == null) return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "Not found"));
        return ResponseEntity.ok(item);
    }

    // Called by shipping-service to reduce stock
    @PutMapping("/{id}/reduce")
    public ResponseEntity<?> reduce(@PathVariable int id, @RequestBody Map<String, Integer> body) {
        Item item = items.get(id);
        if (item == null) return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "Not found"));
        int qty = body.getOrDefault("quantity", 0);
        synchronized (item) {
            if (item.getStock() < qty)
                return ResponseEntity.badRequest().body(Map.of("error", "Insufficient stock"));
            item.setStock(item.getStock() - qty);
        }
        return ResponseEntity.ok(item);
    }
}
