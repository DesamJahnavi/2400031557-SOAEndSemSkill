package com.ecommerce.shipping;

public class Shipment {
    private int id;
    private int productId;
    private int quantity;
    private String address;
    private String status;

    public Shipment() {}
    public Shipment(int id, int productId, int quantity, String address, String status) {
        this.id = id; this.productId = productId; this.quantity = quantity;
        this.address = address; this.status = status;
    }
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getProductId() { return productId; }
    public void setProductId(int productId) { this.productId = productId; }
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
