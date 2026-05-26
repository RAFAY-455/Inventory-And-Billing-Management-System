package com.mycompany.inventorycore;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class Inventorymanager {

    // List to store all products
    private final ArrayList<Product> products = new ArrayList<>();

    // Queue to manage customers (FIFO)
    private final Queue<Customer> customerQueue = new LinkedList<>();

    // Auto-increment product ID
    private int productId = 1;

    // ------------------ PRODUCT OPERATIONS ------------------

    public void addProduct(String name, double price, int qty) {
        Product p = new Product(productId++, name, price, qty);
        products.add(p);
    }

    public boolean updateProduct(int id, double price, int qty) {
        Product p = findProductById(id);
        if (p != null) {
            p.setPrice(price);
            p.setQuantity(qty);
            return true;
        }
        return false;
    }

    private Product findProductById(int id) {
        for (Product p : products) {
            if (p.getId() == id) {
                return p;
            }
        }
        return null;
    }

    public boolean deleteProduct(int id) {
        Product p = findProductById(id);
        if (p != null) {
            products.remove(p);
            return true;
        }
        return false;
    }

    // ⚠️ IMPORTANT: GUI expects this EXACT method
    public boolean isLowStock(Product p) {
        return p.getQuantity() < 5;
    }

   
    public ArrayList<Product> getAllProducts() {
        return products; //
    }

    // ------------------ CUSTOMER QUEUE ------------------

    public void addCustomer(String name) {
        customerQueue.add(new Customer(name));
    }

    public Customer nextCustomer() {
        return customerQueue.poll();
    }

    public boolean hasCustomers() {
        return !customerQueue.isEmpty();
    }
}
