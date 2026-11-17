package com.ecommerce.shipping;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/shipping")
public class ShippingController {

    private final RestTemplate restTemplate;
    private final String inventoryUrl;
    private final List<Shipment> shipments = new CopyOnWriteArrayList<>();

    public ShippingController(RestTemplate restTemplate,
                              @Value("${inventory.service.url}") String inventoryUrl) {
        this.restTemplate = restTemplate;
        this.inventoryUrl = inventoryUrl;
    }

    @GetMapping
    public List<Shipment> getAll() {
        return shipments;
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody Shipment request) {
        try {
            // Service-to-service communication: shipping -> inventory
            restTemplate.exchange(
                    inventoryUrl + "/inventory/" + request.getProductId() + "/reduce",
                    HttpMethod.PUT,
                    new HttpEntity<>(Map.of("quantity", request.getQuantity())),
                    String.class);
        } catch (HttpClientErrorException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getResponseBodyAsString());
        } catch (ResourceAccessException e) {
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                    .body(Map.of("error", "Inventory service unavailable"));
        }
        Shipment shipment = new Shipment(shipments.size() + 1, request.getProductId(),
                request.getQuantity(), request.getAddress(), "SHIPPED");
        shipments.add(shipment);
        return ResponseEntity.status(HttpStatus.CREATED).body(shipment);
    }
}
