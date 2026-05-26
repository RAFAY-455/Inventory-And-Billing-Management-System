package com.mycompany.inventorycore;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class InventoryGUI extends JFrame {

    private Inventorymanager manager = new Inventorymanager();
    private JTable table;
    private DefaultTableModel model;

    private CardLayout cardLayout = new CardLayout();
    private JPanel mainPanel = new JPanel(cardLayout);

    private final Color BG = new Color(18,18,28);
    private final Color PANEL = new Color(30,30,45);
    private final Color BTN = new Color(70,120,200);
    private final Color BTN_ALT = new Color(50,160,90);
    private final Color DANGER = new Color(180,50,50);
    private final Color TEXT = new Color(230,230,230);

    public InventoryGUI() {
        setTitle("Inventory and billing management System");
        setSize(1100,700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        mainPanel.add(homeScreen(), "HOME");
        mainPanel.add(inventoryScreen(), "INVENTORY");

        setContentPane(mainPanel);
        cardLayout.show(mainPanel, "HOME");
    }

    private JPanel homeScreen() {
        JPanel home = new JPanel(new GridBagLayout());
        home.setBackground(BG);

        JLabel title = new JLabel("INVENTORY AND BILLING MANAGEMENT SYSTEM");
        title.setFont(new Font("Segoe UI", Font.BOLD, 32));
        title.setForeground(TEXT);

        JButton enterBtn = bigButton("ENTER INVENTORY", BTN);
        JButton exitBtn = bigButton("EXIT", DANGER);

        enterBtn.addActionListener(e -> {
            cardLayout.show(mainPanel, "INVENTORY");
            refreshTable();
        });

        exitBtn.addActionListener(e -> System.exit(0));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(20,20,20,20);

        gbc.gridy = 0; home.add(title, gbc);
        gbc.gridy = 1; home.add(enterBtn, gbc);
        gbc.gridy = 2; home.add(exitBtn, gbc);

        return home;
    }

    private JPanel inventoryScreen() {
        JPanel panel = new JPanel(new BorderLayout(10,10));
        panel.setBackground(BG);
        panel.setBorder(new EmptyBorder(20,20,20,20));

        JLabel header = new JLabel("Inventory Dashboard");
        header.setFont(new Font("Segoe UI", Font.BOLD, 26));
        header.setForeground(TEXT);
        panel.add(header, BorderLayout.NORTH);

        model = new DefaultTableModel(
                new String[]{"ID","Name","Price","Qty","Status"}, 0
        );

        table = new JTable(model);
        table.setRowHeight(40);
        table.setBackground(PANEL);
        table.setForeground(TEXT);
        table.setSelectionBackground(new Color(60,90,140));
        table.getTableHeader().setBackground(PANEL);
        table.getTableHeader().setForeground(TEXT);

        JScrollPane sp = new JScrollPane(table);
        sp.getViewport().setBackground(PANEL);
        panel.add(sp, BorderLayout.CENTER);

        JPanel btnPanel = new JPanel(new GridBagLayout());
        btnPanel.setBackground(BG);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10,10,10,10);

        JButton add = smallButton("ADD PRODUCT", BTN);
        JButton update = smallButton("UPDATE", BTN);
        JButton delete = smallButton("DELETE", DANGER);
        JButton addCust = smallButton("ADD CUSTOMER", BTN_ALT);
        JButton serve = smallButton("SERVE CUSTOMER", BTN_ALT);
        JButton back = smallButton("BACK", new Color(90,90,90));
        JButton billing = smallButton("BILLING", new Color(160,120,40));

        add.addActionListener(e -> addProduct());
        update.addActionListener(e -> updateProduct());
        delete.addActionListener(e -> deleteProduct());
        addCust.addActionListener(e -> addCustomer());
        serve.addActionListener(e -> serveCustomer());
        back.addActionListener(e -> cardLayout.show(mainPanel, "HOME"));
        billing.addActionListener(e -> openBilling());

        gbc.gridx=0; gbc.gridy=0; btnPanel.add(add, gbc);
        gbc.gridx=1; btnPanel.add(update, gbc);
        gbc.gridx=2; btnPanel.add(delete, gbc);
        gbc.gridx=3; btnPanel.add(billing, gbc);
        gbc.gridx=0; gbc.gridy=1; btnPanel.add(addCust, gbc);
        gbc.gridx=1; btnPanel.add(serve, gbc);
        gbc.gridx=2; btnPanel.add(back, gbc);

        panel.add(btnPanel, BorderLayout.SOUTH);
        return panel;
    }

    private JButton bigButton(String text, Color c) {
        JButton b = new JButton(text);
        b.setPreferredSize(new Dimension(320,65));
        b.setBackground(c);
        b.setForeground(Color.WHITE);
        b.setFont(new Font("Segoe UI", Font.BOLD, 18));
        b.setFocusPainted(false);
        return b;
    }

    private JButton smallButton(String text, Color c) {
        JButton b = new JButton(text);
        b.setBackground(c);
        b.setForeground(Color.WHITE);
        b.setFont(new Font("Segoe UI", Font.BOLD, 14));
        b.setFocusPainted(false);
        return b;
    }

    private void refreshTable() {
        model.setRowCount(0);
        for (Product p : manager.getAllProducts()) {
            String status = manager.isLowStock(p) ? "LOW STOCK" : "STABLE";
            model.addRow(new Object[]{
                    p.getId(),
                    p.getName(),
                    p.getPrice(),
                    p.getQuantity(),
                    status
            });
        }
    }

    private void addProduct() {
        JTextField name = new JTextField();
        JTextField price = new JTextField();
        JTextField qty = new JTextField();

        Object[] msg = {"Name:", name, "Price:", price, "Quantity:", qty};

        if (JOptionPane.showConfirmDialog(this, msg,
                "Add Product", JOptionPane.OK_CANCEL_OPTION)
                == JOptionPane.OK_OPTION) {
            try {
                manager.addProduct(
                        name.getText(),
                        Double.parseDouble(price.getText()),
                        Integer.parseInt(qty.getText())
                );
                refreshTable();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Invalid input!");
            }
        }
    }

    private void updateProduct() {
        int row = table.getSelectedRow();
        if (row == -1) return;

        try {
            int id = (int) model.getValueAt(row, 0);
            double price = Double.parseDouble(
                    JOptionPane.showInputDialog("New Price:")
            );
            int qty = Integer.parseInt(
                    JOptionPane.showInputDialog("New Quantity:")
            );

            manager.updateProduct(id, price, qty);
            refreshTable();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Invalid input!");
        }
    }

    private void deleteProduct() {
        int row = table.getSelectedRow();
        if (row != -1) {
            manager.deleteProduct((int) model.getValueAt(row, 0));
            refreshTable();
        }
    }

    private void addCustomer() {
        String name = JOptionPane.showInputDialog(this, "Customer Name:");
        if (name != null && !name.trim().isEmpty())
            manager.addCustomer(name);
    }

    private void serveCustomer() {
        Customer c = manager.nextCustomer();
        JOptionPane.showMessageDialog(this,
                c != null ? "Serving: " + c.getName() : "Queue Empty");
    }

    private void openBilling() {

        JDialog dialog = new JDialog(this, "Billing", true);
        dialog.setSize(550,450);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout(10,10));

        DefaultTableModel billModel = new DefaultTableModel(
                new String[]{"Product","Price","Qty","Subtotal"},0
        );

        JTable billTable = new JTable(billModel);
        JScrollPane sp = new JScrollPane(billTable);

        JLabel totalLabel = new JLabel("Total: 0.0");
        totalLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));

        JButton addItem = new JButton("Add Item");
        JButton checkout = new JButton("Checkout");

        java.util.ArrayList<Billitem> billItems = new java.util.ArrayList<>();

        addItem.addActionListener(e -> {
            try {
                Product p = (Product) JOptionPane.showInputDialog(
                        dialog,
                        "Select Product",
                        "Product",
                        JOptionPane.PLAIN_MESSAGE,
                        null,
                        manager.getAllProducts().toArray(),
                        null
                );

                if (p == null) return;

                int qty = Integer.parseInt(
                        JOptionPane.showInputDialog(dialog, "Quantity")
                );

                Billitem item = new Billitem(p, qty);
                billItems.add(item);

                billModel.addRow(new Object[]{
                        p.getName(),
                        p.getPrice(),
                        qty,
                        item.getSubtotal()
                });

                double total = 0;
                for (Billitem bi : billItems)
                    total += bi.getSubtotal();

                totalLabel.setText("Total: " + total);

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, "Invalid input");
            }
        });

        checkout.addActionListener(e -> {
            JOptionPane.showMessageDialog(dialog,
                    "Bill Generated\n" + totalLabel.getText());
            dialog.dispose();
        });

        JPanel bottom = new JPanel(new BorderLayout());
        bottom.add(totalLabel, BorderLayout.WEST);

        JPanel btns = new JPanel();
        btns.add(addItem);
        btns.add(checkout);
        bottom.add(btns, BorderLayout.EAST);

        dialog.add(sp, BorderLayout.CENTER);
        dialog.add(bottom, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() ->
                new InventoryGUI().setVisible(true));
    }
}
