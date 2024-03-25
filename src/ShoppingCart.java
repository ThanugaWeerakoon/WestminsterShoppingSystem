import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import java.util.Map;
import java.util.Vector;

public class ShoppingCart extends JFrame {
    private final DefaultTableModel cartTableModel;
    private final JTable cartTable;
    private final WestminsterShoppingManager shoppingManager;
    private final JLabel finalPriceLabel;

    private double originalPrice;
    private Map<String, Integer> categoryCount;

    public ShoppingCart(WestminsterShoppingManager shoppingManager) {
        this.shoppingManager = shoppingManager;
        this.originalPrice = 0;

        // Set up the main frame
        setTitle("Shopping Cart");
        setSize(800, 400);
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        setLocationRelativeTo(null);

        // Create a table model
        Vector<String> columnNames = new Vector<>();
        columnNames.add("Product");
        columnNames.add("Quantity");
        columnNames.add("Price");
        Vector<Vector<String>> data = new Vector<>();
        cartTableModel = new DefaultTableModel(data, columnNames);
        cartTable = new JTable(cartTableModel);

        // Add a scroll pane for the cart table
        JScrollPane scrollPane = new JScrollPane(cartTable);

        // Create a panel for the bottom layout
        JPanel bottomPanel = new JPanel(new BorderLayout());

        // Create a label to display the final price
        finalPriceLabel = new JLabel("Final Price: Rs 0.00"); // Initialize the label
        finalPriceLabel.setHorizontalAlignment(JLabel.CENTER);

        // Add the label to the bottom panel
        bottomPanel.add(finalPriceLabel, BorderLayout.SOUTH);

        // Set up the layout
        setLayout(new BorderLayout());
        add(scrollPane, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        // Update the cart table and final price
        updateCartTable();
        displayFinalPrice();
    }

    public void updateCartTable() {
        cartTableModel.setRowCount(0);
        originalPrice = 0; // Initialize originalPrice to 0 when updating the cart table

        for (Product product : shoppingManager.getShoppingCart()) {
            Vector<String> rowData = new Vector<>();
            rowData.add(getProductDetails(product));
            int quantity = shoppingManager.getProductQuantity(product);
            double productPrice = product.getPrice() * quantity;
            rowData.add(String.valueOf(quantity));
            rowData.add(String.format("%.2f", productPrice));
            originalPrice += productPrice;

            cartTableModel.addRow(rowData);
        }

        // Update the category count and display the final price after updating the cart table
        categoryCount = getProductCategoryCount(shoppingManager.getShoppingCart());
        displayFinalPrice();
    }


    private String getProductDetails(Product product) {
        if (product instanceof Electronics) {
            Electronics electronics = (Electronics) product;
            return String.format("ID: %s, Name: %s, Warranty: %s",
                    product.getProductID(), product.getProductName(), electronics.getWarrantyPeriod());
        } else if (product instanceof Clothing) {
            Clothing clothing = (Clothing) product;
            return String.format("ID: %s, Name: %s, Color: %s",
                    product.getProductID(), product.getProductName(), clothing.getColor());
        } else {
            return "";
        }
    }

    // display the final price and reduced price
    public void displayFinalPrice() {
        double finalPrice = calculateFinalPrice();
        double discount = originalPrice - finalPrice;

        if (finalPriceLabel != null) {
            finalPriceLabel.setText("<html>Original Price: Rs" + String.format("%.2f", originalPrice) +
                    "<br>Final Price: Rs" + String.format("%.2f", finalPrice) +
                    "<br>Discount: Rs" + String.format("%.2f", discount) + "</html>");
        }
    }

    // calculate the final price with discounts
    private double calculateFinalPrice() {
        double discountedPrice = originalPrice;

        for (Product product : shoppingManager.getShoppingCart()) {
            int quantity = shoppingManager.getProductQuantity(product);
            double discount = calculateDiscount(categoryCount.get(getCategoryName(product)));

            // Update the final price based on the quantity and discount
            discountedPrice -= product.getPrice() * quantity * discount;
        }

        return discountedPrice;
    }

    // calculate the discount based on the category count
    private double calculateDiscount(Integer categoryCount) {
        if (categoryCount != null && categoryCount >= 3) {
            return 0.20; // 20% discount for buying at least three products of the same category
        }
        return 0.0;
    }


    // category name
    private String getCategoryName(Product product) {
        if (product instanceof Electronics) {
            return "Electronics";
        } else if (product instanceof Clothing) {
            return "Clothing";
        }
        return "";
    }

    // get the count of products in each category
    private Map<String, Integer> getProductCategoryCount(List<Product> products) {
        int electronicsCount = 0;
        int clothingCount = 0;

        for (Product product : products) {
            if (product instanceof Electronics) {
                electronicsCount++;
            } else if (product instanceof Clothing) {
                clothingCount++;
            }
        }

        return Map.of(
                "Electronics", electronicsCount,
                "Clothing", clothingCount
        );
    }
}
